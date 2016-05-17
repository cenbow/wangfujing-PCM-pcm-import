package com.wangfj.product.PIS.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangfj.core.constants.ComErrorCodeConstants.ErrorCode;
import com.wangfj.core.framework.base.controller.BaseController;
import com.wangfj.core.framework.base.page.Page;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.core.utils.ResultUtil;
import com.wangfj.core.utils.ThrowExcetpionUtil;
import com.wangfj.product.PIS.controller.support.PcmPricePISPara;
import com.wangfj.product.PIS.controller.support.PcmPricePara;
import com.wangfj.product.PIS.controller.support.PcmPriceToPISPara;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.intf.IPcmExceptionLogService;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.price.domain.vo.PcmChangePriceLimitDto;
import com.wangfj.product.price.domain.vo.PcmPriceERPDto;
import com.wangfj.product.price.domain.vo.PcmPricePISDto;
import com.wangfj.product.price.domain.vo.QueryShoppeProSidDto;
import com.wangfj.product.price.service.intf.IPcmChangePriceLimitService;
import com.wangfj.product.price.service.intf.IPcmPriceService;
import com.wangfj.util.Constants;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.MqUtil;

/**
 * 导入终端价格管理
 * 
 * @Class Name PriceChangePISController
 * @Author kongqf
 * @Create In 2015年8月21日
 */
@Controller
@RequestMapping("/changeprice/pis")
public class PriceChangePISController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(PriceChangePISController.class);

	@Autowired
	private IPcmPriceService pcmPriceService;
	@Autowired
	private IPcmExceptionLogService pcmExceptionLogService;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private IPcmChangePriceLimitService limitService;

	/**
	 * 单一变价
	 * 
	 * @Methods Name changePriceInfoFromERP
	 * @Create In 2015年8月21日 By kongqf
	 * @param request
	 * @param priceParalist
	 * @return Map<String,Object>
	 * @throws IOException
	 */
	@RequestMapping(value = "/importProPriceInfo", method = RequestMethod.POST)
	@ResponseBody
	public String changePriceInfoFromERP(HttpServletRequest request,
			@RequestBody @Valid MqRequestDataListPara<PcmPricePara> mqpara1) {

		final MqRequestDataListPara<PcmPricePara> mqpara = new MqRequestDataListPara<PcmPricePara>();

		org.springframework.beans.BeanUtils.copyProperties(mqpara1, mqpara);

		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					List<PcmPricePara> priceParalist = new ArrayList<PcmPricePara>();
					priceParalist = mqpara.getData();
					String callBackUrl = mqpara.getHeader().getCallbackUrl();
					String RequestMsg = "";
					if (priceParalist.size() < Constants.PRICE_LINE_COUNT) {
						List<PcmPriceToPISPara> priceToPISParas_failed = new ArrayList<PcmPriceToPISPara>();
						List<PcmPricePara> errorList = new ArrayList<PcmPricePara>();
						final List<PcmPricePara> pcmPriceParaPushList = new ArrayList<PcmPricePara>();
						List<String> pushSearch = new ArrayList<String>();
						PcmPriceERPDto pcmPriceDto = null;
						boolean flag = false;

						BigDecimal upperLimit = new BigDecimal(0);
						BigDecimal lowerLimit = new BigDecimal(0);
						for (int i = 0; i < priceParalist.size(); i++) {
							flag = false;
							PcmPricePara para = new PcmPricePara();
							pcmPriceDto = new PcmPriceERPDto();
							try {
								try {
									BeanUtils.copyProperties(para, priceParalist.get(i));
									BeanUtils.copyProperties(pcmPriceDto, para);
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
								if (i == 0) {
									PcmChangePriceLimitDto limitDto = limitService
											.selectPriceLimitByShopCode(pcmPriceDto.getStorecode());
									upperLimit = limitService.getPriceLimit(limitDto, true);
									lowerLimit = limitService.getPriceLimit(limitDto, false);
								}
								pcmPriceService.saveChangePriceRecord(pcmPriceDto, Constants.PIS);
								flag = pcmPriceService.saveOrUpdatePrice(pcmPriceDto, Constants.PIS,
										upperLimit, lowerLimit);
								if (flag) {
									pcmPriceParaPushList.add(para);
									pushSearch.add(para.getSupplierprodcode());
								}
							} catch (BleException e) {
								errorList.add(para);
								priceToPISParas_failed.add(
										getPcmPricePISEntity(para, e.getCode(), e.getMessage()));
								if (e.getCode()
										.equals(ErrorCode.ADD_CHANGE_PRICE_ERROR.getErrorCode())) {
									ThrowExcetpionUtil.splitExcetpion(new BleException(
											ErrorCode.ADD_CHANGE_PRICE_ERROR.getErrorCode(),
											ErrorCode.ADD_CHANGE_PRICE_ERROR.getMemo()));
								}
							}
						}

						List<PcmPriceToPISPara> pcmPriceToPISParaList = new ArrayList<PcmPriceToPISPara>();
						pcmPriceToPISParaList = getPcmPricePISEntityList(pcmPriceParaPushList);

						if (pcmPriceParaPushList != null && pcmPriceParaPushList.size() > 0) {
							// 下发门店ERP和营销中心
							final String pushToERP = PropertyUtil
									.getSystemUrl("price.synPushToERP");
							taskExecutor.execute(new Runnable() {
								@Override
								public void run() {
									try {
										logger.info(
												"API,importProPriceInfo.htm,synPushToERP,request:"
														+ pcmPriceParaPushList.toString());
										String response = HttpUtil.doPost(pushToERP,
												JsonUtil.getJSONString(pcmPriceParaPushList));
										logger.info(
												"API,importProPriceInfo.htm,synPushToERP,response:"
														+ response);
									} catch (Exception e) {
										logger.error(
												"API,importProPriceInfo.htm,synPushToERP,Error:"
														+ e.getMessage());
									}
								}
							});
						}

						/** 价格下发搜索 **/
						List<Map<String, Object>> pushSearchList = new ArrayList<Map<String, Object>>();
						pushSearchList = pcmPriceService.selectShopProSidByShopProCode(pushSearch);
						if (pushSearchList != null && pushSearchList.size() > 0) {
							final Map<String, Object> pushSearchMap = new HashMap<String, Object>();
							pushSearchMap.put("PcmSearcherOnline", "1");
							pushSearchMap.put("paraList", pushSearchList);
							final String pushToSearch = PropertyUtil
									.getSystemUrl("product.pushShoppeProduct");
							taskExecutor.execute(new Runnable() {
								@Override
								public void run() {
									try {
										String response = HttpUtil.doPost(pushToSearch,
												JsonUtil.getJSONString(pushSearchMap));
										logger.info("importProPriceInfo.htm,synPushToERP,response:"
												+ response);
									} catch (Exception e) {
										logger.error(
												"importProPriceInfo.htm,pushShoppeProduct,Error:"
														+ e.getMessage());
									}
								}
							});
						}

						try {
							if (errorList != null && errorList.size() > 0) {
								SavaErrorMessage2(JsonUtil.getJSONString(priceToPISParas_failed),
										JsonUtil.getJSONString(errorList));
							}
						} catch (Exception e2) {
						}

						RequestMsg = JsonUtil.getJSONString(
								creListResult(pcmPriceToPISParaList, priceToPISParas_failed));
					} else {
						RequestMsg = JsonUtil.getJSONString(ResultUtil.creComErrorResult(
								ErrorCode.PRICE_IMPORT_ERROR.getErrorCode(),
								ErrorCode.PRICE_IMPORT_ERROR.getMemo()));
					}

					try {
						logger.info("API,importProPriceInfo.htm,callBackUrl:" + callBackUrl
								+ ",request:" + RequestMsg);
						String response = HttpUtil.doPost(callBackUrl, RequestMsg);
						logger.info("API,importProPriceInfo.htm,callBackUrl,response:" + response);
						if (response == null) {
							ThrowExcetpionUtil.splitExcetpion(new BleException(
									ErrorCode.CALLBACK_PRICE_PIS_FAILED.getErrorCode(),
									ErrorCode.CALLBACK_PRICE_PIS_FAILED.getMemo()));
							SavaErrorMessage2(
									JsonUtil.getJSONString(
											ErrorCode.CALLBACK_PRICE_PIS_FAILED.getMemo()),
									RequestMsg);
						}
					} catch (Exception e) {
						ThrowExcetpionUtil.splitExcetpion(
								new BleException(ErrorCode.CALLBACK_PRICE_PIS_FAILED.getErrorCode(),
										ErrorCode.CALLBACK_PRICE_PIS_FAILED.getMemo()));
						SavaErrorMessage2(e.getMessage(), RequestMsg);
					}

				} catch (Exception e) {
					logger.error("API,importProPriceInfo.htm,synPushToERP,Error:" + e.getMessage());
				}
			}
		});
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(mqpara.getHeader()));

	}

	/**
	 * 批量变价
	 * 
	 * @Methods Name batchChangePriceFromPIS
	 * @Create In 2015年8月21日 By kongqf
	 * @param request
	 * @param pcmPricePISParaList
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "/batchimportproprice", method = RequestMethod.POST)
	@ResponseBody
	public String batchChangePriceFromPIS(HttpServletRequest request,
			@RequestBody @Valid MqRequestDataListPara<PcmPricePISPara> mqpara1) {
		logger.info("API,Start batchChangePriceFromPIS.htm");
		final MqRequestDataListPara<PcmPricePISPara> mqpara = new MqRequestDataListPara<PcmPricePISPara>();
		org.springframework.beans.BeanUtils.copyProperties(mqpara1, mqpara);

		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					List<PcmPricePISPara> pcmPricePISParaList = new ArrayList<PcmPricePISPara>();
					pcmPricePISParaList = mqpara.getData();
					String callBackUrl = "";
					if (mqpara.getHeader() != null)
						callBackUrl = mqpara.getHeader().getCallbackUrl();
					List<PcmPriceToPISPara> pcmPricePISPara_failed = new ArrayList<PcmPriceToPISPara>();
					List<PcmPriceToPISPara> pcmPricePISPara_success = new ArrayList<PcmPriceToPISPara>();
					List<PcmPricePISDto> errorList = null;
					final List<PcmPricePISPara> pcmPriceParaPISPushList = new ArrayList<PcmPricePISPara>();
					PcmPricePISDto pcmPricePISDto = null;
					PcmPricePISPara pcmPricePISPara = null;
					BigDecimal upperLimit = new BigDecimal(0);
					BigDecimal lowerLimit = new BigDecimal(0);
					boolean isValidSuccess = true;
					for (int i = 0; i < pcmPricePISParaList.size(); i++) {
						String RequestMsg = "", errorCode = "", errorMsg = "", guid = "";
						errorList = new ArrayList<PcmPricePISDto>();
						pcmPricePISDto = new PcmPricePISDto();
						pcmPricePISPara = new PcmPricePISPara();
						try {
							BeanUtils.copyProperties(pcmPricePISPara, pcmPricePISParaList.get(i));
							BeanUtils.copyProperties(pcmPricePISDto, pcmPricePISPara);
							pcmPriceParaPISPushList.add(pcmPricePISPara);
							guid = pcmPricePISPara.getGuid();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}

						// 保存变价记录
						try {
							pcmPriceService.saveBatchChangePriceRecord(pcmPricePISDto);
						} catch (BleException e) {
							isValidSuccess = false;
							errorCode = e.getCode();
							errorMsg = e.getMessage();
							SavaErrorMessage(e.getCode() + ":" + e.getMessage(),
									JsonUtil.getJSONString(pcmPricePISDto));
						}
						if (isValidSuccess) {
							if (i == 0) {
								PcmChangePriceLimitDto limitDto = limitService
										.selectPriceLimitByShopCode(pcmPricePISDto.getStorecode());
								upperLimit = limitService.getPriceLimit(limitDto, true);
								lowerLimit = limitService.getPriceLimit(limitDto, false);
							}
							pcmPricePISDto.setPageSize(Constants.PRICE_LINE_COUNT100);
							int pages = pcmPriceService
									.queryBatchChangePriceInfoPages(pcmPricePISDto);
							if (0 != pages) {
								for (int p = 1; p <= pages; p++) {
									List<String> pushSearch = new ArrayList<String>();
									RequestMsg = "";
									errorList.clear();
									pcmPricePISPara_success.clear();
									pcmPricePISPara_failed.clear();
									pcmPricePISDto.setCurrentPage(p);

									Page<QueryShoppeProSidDto> pageDto = new Page<QueryShoppeProSidDto>();
									pageDto = pcmPriceService
											.queryBatchChangePriceInfo(pcmPricePISDto);
									List<QueryShoppeProSidDto> queryShoppeProSidDtos = new ArrayList<QueryShoppeProSidDto>();
									queryShoppeProSidDtos = pageDto.getList();

									for (QueryShoppeProSidDto spsto : queryShoppeProSidDtos) {
										try {
											pcmPricePISDto
													.setSupplierprodcode(spsto.getShoppeProSid());
											boolean flag = pcmPriceService.saveOrUpdateBatchPrice(
													pcmPricePISDto, upperLimit, lowerLimit);
											if (!flag) {
												errorList.add(pcmPricePISDto);
												pcmPricePISPara_failed.add(
														getPcmPricePISEntityBatch(pcmPricePISPara,
																spsto.getShoppeProSid(),
																ErrorCode.ADD_CHANGE_PRICE_ERROR
																		.getErrorCode(),
																ErrorCode.ADD_CHANGE_PRICE_ERROR
																		.getMemo()));
											} else {
												pcmPricePISPara_success.add(
														getPcmPricePISEntityBatch(pcmPricePISPara,
																spsto.getShoppeProSid(), null,
																null));
												pushSearch.add(spsto.getShoppeProSid());
											}
										} catch (BleException e) {
											errorList.add(pcmPricePISDto);
											pcmPricePISPara_failed.add(getPcmPricePISEntityBatch(
													pcmPricePISPara, spsto.getShoppeProSid(),
													e.getCode(), e.getMessage()));
											logger.info("API,saveOrUpdateBatchPrice failed:"
													+ e.getMessage());
											if (e.getCode().equals(ErrorCode.ADD_CHANGE_PRICE_ERROR
													.getErrorCode())) {
												ThrowExcetpionUtil.splitExcetpion(new BleException(
														ErrorCode.ADD_CHANGE_PRICE_ERROR
																.getErrorCode(),
														ErrorCode.ADD_CHANGE_PRICE_ERROR
																.getMemo()));
											}
										}
									}

									/** 价格下发搜索 **/
									List<Map<String, Object>> pushSearchList = new ArrayList<Map<String, Object>>();
									pushSearchList = pcmPriceService
											.selectShopProSidByShopProCode(pushSearch);
									if (pushSearchList != null && pushSearchList.size() > 0) {
										final Map<String, Object> pushSearchMap = new HashMap<String, Object>();
										pushSearchMap.put("PcmSearcherOnline", "1");
										pushSearchMap.put("paraList", pushSearchList);
										final String pushToSearch = PropertyUtil
												.getSystemUrl("product.pushShoppeProduct");
										taskExecutor.execute(new Runnable() {
											@Override
											public void run() {
												try {
													String response = HttpUtil.doPost(pushToSearch,
															JsonUtil.getJSONString(pushSearchMap));
													logger.info(
															"importProPriceInfo.htm,synPushToERP,response:"
																	+ response);
												} catch (Exception e) {
													logger.error(
															"API,importProPriceInfo.htm,pushShoppeProduct,Error:"
																	+ e.getMessage());
												}
											}
										});
									}

									try {
										RequestMsg = JsonUtil.getJSONString(creListResultBatch(
												pcmPricePISPara_success, pcmPricePISPara_failed,
												"true", "", "", guid));
										logger.info("API,callBackUrl,request:" + callBackUrl
												+ ",request:" + RequestMsg);
										String response = HttpUtil.doPost(callBackUrl, RequestMsg);
										logger.info("API,callBackUrl,response:" + response);
										if (response == null) {
											ThrowExcetpionUtil.splitExcetpion(new BleException(
													ErrorCode.CALLBACK_PRICE_PIS_FAILED
															.getErrorCode(),
													ErrorCode.CALLBACK_PRICE_PIS_FAILED.getMemo()));
											SavaErrorMessage(
													ErrorCode.CALLBACK_PRICE_PIS_FAILED.getMemo(),
													RequestMsg);
										}
									} catch (Exception e) {
										ThrowExcetpionUtil.splitExcetpion(new BleException(
												ErrorCode.CALLBACK_PRICE_PIS_FAILED.getErrorCode(),
												ErrorCode.CALLBACK_PRICE_PIS_FAILED.getMemo()));
										SavaErrorMessage(e.getMessage(), RequestMsg);
									}
								}
							}
							if (errorList != null && errorList.size() > 0) {
								SavaErrorMessage(JsonUtil.getJSONString(pcmPricePISPara_failed),
										JsonUtil.getJSONString(errorList));
							}
						} else {
							try {
								RequestMsg = JsonUtil.getJSONString(creListResultBatch(
										pcmPricePISPara_success, pcmPricePISPara_failed, "false",
										errorCode, errorMsg, guid));
								logger.info("API,callBackUrl,request:" + callBackUrl + ",request:"
										+ RequestMsg);
								String response = HttpUtil.doPost(callBackUrl, RequestMsg);
								logger.info("API,callBackUrl,response:" + response);
								if (response == null) {
									ThrowExcetpionUtil.splitExcetpion(new BleException(
											ErrorCode.CALLBACK_PRICE_PIS_FAILED.getErrorCode(),
											ErrorCode.CALLBACK_PRICE_PIS_FAILED.getMemo()));
									SavaErrorMessage(ErrorCode.CALLBACK_PRICE_PIS_FAILED.getMemo(),
											RequestMsg);
								}
							} catch (Exception e) {
								ThrowExcetpionUtil.splitExcetpion(new BleException(
										ErrorCode.CALLBACK_PRICE_PIS_FAILED.getErrorCode(),
										ErrorCode.CALLBACK_PRICE_PIS_FAILED.getMemo()));
								SavaErrorMessage(e.getMessage(), RequestMsg);
							}
							break;
						}
					}

					if (isValidSuccess && pcmPriceParaPISPushList != null
							&& pcmPriceParaPISPushList.size() > 0) {
						// 下发营销中心
						// final String pushBatchToeFuture = PropertyUtil
						// .getSystemUrl("price.synPushBatchToEFUTURE");
						final String pushBatchToeFuture = PropertyUtil
								.getSystemUrl("price.synPushBatchToERP");
						taskExecutor.execute(new Runnable() {
							@Override
							public void run() {
								try {
									logger.info("API,synPushBatchToEFUTURE,request:"
											+ pcmPriceParaPISPushList.toString());
									String response = HttpUtil.doPost(pushBatchToeFuture,
											JsonUtil.getJSONString(pcmPriceParaPISPushList));
									logger.info("API,synPushBatchToEFUTURE,response:" + response);
								} catch (Exception e) {
									logger.error(
											"API,synPushBatchToEFUTURE,Error:" + e.getMessage());
									SavaErrorMessage(e.getMessage(),
											JsonUtil.getJSONString(pcmPriceParaPISPushList));
								}
							}
						});
					}
				} catch (Exception e) {
					logger.error("API,importProPriceInfo.htm,synPushToERP,Error:" + e.getMessage());
				}
			}
		});
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(mqpara.getHeader()));
	}

	/**
	 * 返回数据
	 * 
	 * @Methods Name creListResult
	 * @Create In 2015年8月20日 By kongqf
	 * @param successList
	 * @param failedList
	 * @return Map<String,Object>
	 */
	private Map<String, Object> creListResult(List<PcmPriceToPISPara> successList,
			List<PcmPriceToPISPara> failedList) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("data_success", successList);
		resultMap.put("data_failed", failedList);
		resultMap.put("Data_total", successList.size() + failedList.size());
		resultMap.put("failed_total", failedList.size());
		return resultMap;
	}

	/**
	 * 批量变价返回PIS结果
	 * 
	 * @Methods Name creListResultBatch
	 * @Create In 2015年10月30日 By kongqf
	 * @param successList
	 * @param failedList
	 * @param errorCode
	 * @param message
	 * @return Map<String,Object>
	 */
	private Map<String, Object> creListResultBatch(List<PcmPriceToPISPara> successList,
			List<PcmPriceToPISPara> failedList, String success, String errorCode, String message,
			String guid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("data_success", successList);
		resultMap.put("data_failed", failedList);
		resultMap.put("Data_total", successList.size() + failedList.size());
		resultMap.put("failed_total", failedList.size());
		Map<String, Object> errMap = new HashMap<String, Object>();
		resultMap.put("success", success);
		errMap.put("errorCode", errorCode);
		errMap.put("errorMsg", message);
		resultMap.put("data", errMap);
		resultMap.put("guid", guid);
		return resultMap;
	}

	/**
	 * 获取返回信息
	 * 
	 * @Methods Name getPcmPricePISEntityList
	 * @Create In 2015年8月19日 By kongqf
	 * @param priceParalist
	 * @return List<PcmPriceToPISPara>
	 */
	private List<PcmPriceToPISPara> getPcmPricePISEntityList(List<PcmPricePara> priceParalist) {
		List<PcmPriceToPISPara> pcmPriceToPISList = new ArrayList<PcmPriceToPISPara>();
		PcmPriceToPISPara pcmPriceToPISPara = null;
		for (PcmPricePara pcmPricePara : priceParalist) {
			pcmPriceToPISPara = new PcmPriceToPISPara();
			pcmPriceToPISPara.setStorecode(pcmPricePara.getStorecode());
			pcmPriceToPISPara.setSupplierprodcode(pcmPricePara.getSupplierprodcode());
			pcmPriceToPISPara.setGuid(pcmPricePara.getGuid());
			pcmPriceToPISList.add(pcmPriceToPISPara);
		}
		return pcmPriceToPISList;
	}

	/**
	 * 获取错误返回信息
	 * 
	 * @Methods Name getPcmPricePISEntity
	 * @Create In 2015年8月19日 By kongqf
	 * @param pcmPricePara
	 * @param errorCode
	 * @param errorMsg
	 * @return PcmPriceToPISPara
	 */
	private PcmPriceToPISPara getPcmPricePISEntity(PcmPricePara pcmPricePara, String errorCode,
			String errorMsg) {
		PcmPriceToPISPara pcmPriceToPISPara = new PcmPriceToPISPara();
		pcmPriceToPISPara.setStorecode(pcmPricePara.getStorecode());
		pcmPriceToPISPara.setSupplierprodcode(pcmPricePara.getSupplierprodcode());
		pcmPriceToPISPara.setGuid(pcmPricePara.getGuid());
		pcmPriceToPISPara.setResultcode(errorCode);
		pcmPriceToPISPara.setMessage(errorMsg);
		return pcmPriceToPISPara;
	}

	/**
	 * 批量变更下发实体
	 * 
	 * @Methods Name getPcmPricePISEntityBatch
	 * @Create In 2015年8月24日 By kongqf
	 * @param pcmPricePISPara
	 * @param ShoppeProSid
	 * @param errorCode
	 * @param errorMsg
	 * @return PcmPriceToPISPara
	 */
	private PcmPriceToPISPara getPcmPricePISEntityBatch(PcmPricePISPara pcmPricePISPara,
			String ShoppeProSid, String errorCode, String errorMsg) {
		PcmPriceToPISPara pcmPriceToPISPara = new PcmPriceToPISPara();
		pcmPriceToPISPara.setStorecode(pcmPricePISPara.getStorecode());
		pcmPriceToPISPara.setSupplierprodcode(ShoppeProSid);
		pcmPriceToPISPara.setGuid(pcmPricePISPara.getGuid());
		pcmPriceToPISPara.setResultcode(errorCode);
		pcmPriceToPISPara.setMessage(errorMsg);
		return pcmPriceToPISPara;
	}

	/**
	 * 保存错误信息
	 * 
	 * @Methods Name SavaErrorMessage
	 * @Create In 2015年12月21日 By kongqf
	 * @param errorMessage
	 * @param dataContent
	 *            void
	 */
	private void SavaErrorMessage(String errorMessage, String dataContent) {
		try {
			PcmExceptionLogDto dto = new PcmExceptionLogDto();
			dto.setInterfaceName("pcm-import/changeprice/pis/batchimportproprice");
			dto.setExceptionType(StatusCode.EXCEPTION_PRICE.getStatus());
			dto.setErrorMessage(errorMessage);
			dto.setDataContent(dataContent);
			dto.setUuid(UUID.randomUUID().toString());
			pcmExceptionLogService.saveExceptionLogInfo(dto);
		} catch (Exception e) {
			logger.info("API,Save PcmExceptionLogDto failed:" + e.getMessage());
		}
	}

	private void SavaErrorMessage2(String errorMessage, String dataContent) {
		try {
			PcmExceptionLogDto dto = new PcmExceptionLogDto();
			dto.setInterfaceName("pcm-import/changeprice/pis/importProPriceInfo");
			dto.setExceptionType(StatusCode.EXCEPTION_PRICE.getStatus());
			dto.setErrorMessage(errorMessage);
			dto.setDataContent(dataContent);
			dto.setUuid(UUID.randomUUID().toString());
			pcmExceptionLogService.saveExceptionLogInfo(dto);
		} catch (Exception e) {
			logger.info("API,Save PcmExceptionLogDto failed:" + e.getMessage());
		}
	}
}