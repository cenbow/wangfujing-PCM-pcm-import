package com.wangfj.product.EfutureERP.controller;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
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
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.DateUtil;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.core.utils.ThrowExcetpionUtil;
import com.wangfj.product.EfutureERP.controller.support.PcmPriceImportEFPara;
import com.wangfj.product.EfutureERP.controller.support.PcmPricePara;
import com.wangfj.product.EfutureERP.controller.support.PcmPriceToEFPara;
import com.wangfj.product.PIS.controller.support.PcmPriceToPISPara;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.domain.vo.PcmSequenceDto;
import com.wangfj.product.common.service.intf.IPcmExceptionLogService;
import com.wangfj.product.common.service.intf.IPcmSequenceService;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.maindata.domain.entity.PcmErpProduct;
import com.wangfj.product.maindata.service.intf.IPcmErpProductService;
import com.wangfj.product.price.domain.vo.PcmChangePriceLimitDto;
import com.wangfj.product.price.domain.vo.PcmPriceERPDto;
import com.wangfj.product.price.service.intf.IPcmChangePriceLimitService;
import com.wangfj.product.price.service.intf.IPcmPriceService;
import com.wangfj.util.Constants;
import com.wangfj.util.mq.MqRequestDataPara;
import com.wangfj.util.mq.MqRequestEntityDataPara;
import com.wangfj.util.mq.MqUtil;
import com.wangfj.util.mq.PublishDTO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 门店ERP价格信息管理
 * 
 * @Class Name PriceChangeEFERPController
 * @Author kongqf
 * @Create In 2015年8月21日
 */
@Controller
@RequestMapping("/changeprice/eferp")
public class PriceChangeEFERPController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(PriceChangeEFERPController.class);

	@Autowired
	private IPcmPriceService pcmPriceService;
	@Autowired
	private IPcmExceptionLogService pcmExceptionLogService;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private IPcmChangePriceLimitService limitService;
	@Autowired
	private IPcmSequenceService pcmSequenceService;
	@Autowired
	private IPcmErpProductService erpProductSerwvice;

	/**
	 * ERP商品变价
	 * 
	 * @Methods Name changePriceInfoFromERP
	 * @Create In 2015年8月19日 By kongqf
	 * @param request
	 * @param priceParalist
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "/importProPriceInfo", method = RequestMethod.POST)
	@ResponseBody
	public String changePriceInfoFromERP(HttpServletRequest request,
			@RequestBody MqRequestDataPara para) {
		logger.info("API,importProPriceInfo.htm,para:" + para.toString());
		final MqRequestDataPara mqpara = para;

		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				// List<JSONObject> priceParalist = new ArrayList<JSONObject>();
				JSONObject jsono = JSONObject.fromObject(mqpara.getData());
				JSONArray jsona = JSONArray.fromObject(jsono.get("data"));
				// priceParalist = JSONArray.toList(jsona);

				// priceParalist = mqpara.getData();
				List<PcmPriceToEFPara> priceToERPParas_failed = new ArrayList<PcmPriceToEFPara>();
				List<PcmPricePara> errorList = new ArrayList<PcmPricePara>();
				final List<PcmPricePara> pcmPriceParaPushList = new ArrayList<PcmPricePara>();
				List<String> pushSearch = new ArrayList<String>();
				PcmPriceERPDto pcmPriceDto = null;
				boolean flag = false;
				PcmErpProduct erpProduct = null;
				final List<PublishDTO> sidList = new ArrayList<PublishDTO>();
				PublishDTO publishDTO = null;
				BigDecimal upperLimit = new BigDecimal(0);
				BigDecimal lowerLimit = new BigDecimal(0);
				for (int i = 0; i < jsona.size(); i++) {
					flag = false;
					PcmPricePara para = new PcmPricePara();
					pcmPriceDto = new PcmPriceERPDto();

					try {
						try {
							para = getPcmPricePara(jsona.getJSONObject(i));
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
						flag = pcmPriceService.saveChangePriceRecord(pcmPriceDto,
								Constants.EFUTUREERP);
						flag = pcmPriceService.saveOrUpdatePrice(pcmPriceDto, Constants.EFUTUREERP,
								upperLimit, lowerLimit);
						if (flag) {
							pcmPriceParaPushList.add(para);
							if (StringUtils.isBlank(pcmPriceDto.getSupplierprodcode())
									&& StringUtils.isNotBlank(pcmPriceDto.getMatnr())) {
								erpProduct = new PcmErpProduct();
								publishDTO = new PublishDTO();
								erpProduct = erpProductSerwvice.selectErpProductByProCode(
										pcmPriceDto.getMatnr(), pcmPriceDto.getStorecode());
								publishDTO.setSid(erpProduct.getSid());
								publishDTO.setType(1);
								sidList.add(publishDTO);
							} else {
								pushSearch.add(pcmPriceDto.getSupplierprodcode());
							}
						} else {
							errorList.add(para);
							priceToERPParas_failed.add(getPcmPriceToERPEntity(para,
									ErrorCode.PRICE_CHANGERECORD_ERROR.getErrorCode(),
									ErrorCode.PRICE_CHANGERECORD_ERROR.getMemo()));
							ThrowExcetpionUtil.splitExcetpion(new BleException(
									ErrorCode.ADD_CHANGE_PRICE_ERROR.getErrorCode(),
									ErrorCode.ADD_CHANGE_PRICE_ERROR.getMemo()));
						}
					} catch (BleException e) {
						// pcmPriceParaPushList.add(para);// 临时解决方案
						errorList.add(para);
						priceToERPParas_failed
								.add(getPcmPriceToERPEntity(para, e.getCode(), e.getMessage()));
						if (e.getCode().equals(ErrorCode.ADD_CHANGE_PRICE_ERROR.getErrorCode())) {
							ThrowExcetpionUtil.splitExcetpion(new BleException(
									ErrorCode.ADD_CHANGE_PRICE_ERROR.getErrorCode(),
									ErrorCode.ADD_CHANGE_PRICE_ERROR.getMemo()));
						}
					}
				}
				if (pcmPriceParaPushList != null && pcmPriceParaPushList.size() > 0) {
					final String pushToeFuture = PropertyUtil
							.getSystemUrl("price.synPushToEFUTURE");
					taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								logger.info("API,importProPriceInfo.htm,synPushToERP,request:"
										+ pcmPriceParaPushList.toString());
								String response = HttpUtil.doPost(pushToeFuture,
										JsonUtil.getJSONString(pcmPriceParaPushList));
								logger.info("API,importProPriceInfo.htm,synPushToERP,response:"
										+ response);
							} catch (Exception e) {
								SavaErrorMessage("importProPriceInfo",
										"syn 下发营销失败" + e.getMessage(),
										JsonUtil.getJSONString(pcmPriceParaPushList));
							}
						}
					});
				}
				// 大码价格下发
				if (sidList != null && sidList.size() > 0) {
					final String pushToSearch = PropertyUtil.getSystemUrl("product.pushSearch");
					taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								logger.info("API,importProPriceInfo.htm,synPushToERP,request:"
										+ pcmPriceParaPushList.toString());
								String response = HttpUtil.doPost(pushToSearch,
										JsonUtil.getJSONString(sidList));
								logger.info("API,importProPriceInfo.htm,synPushToERP,response:"
										+ response);
							} catch (Exception e) {
								SavaErrorMessage("importProPriceInfo",
										"syn 下发搜索失败" + e.getMessage(),
										JsonUtil.getJSONString(pcmPriceParaPushList));
							}
						}
					});
				}

				/** 单品价格下发搜索 **/
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
								logger.info(
										"importProPriceInfo.htm,synPushToERP,response:" + response);
							} catch (Exception e) {
								logger.error("importProPriceInfo.htm,pushShoppeProduct,Error:"
										+ e.getMessage());
							}
						}
					});
				}

				try {
					if (errorList != null && errorList.size() > 0) {
						SavaErrorMessage("importProPriceInfo",
								JsonUtil.getJSONString(priceToERPParas_failed),
								JsonUtil.getJSONString(errorList));
					}
				} catch (Exception e2) {
				}

				// return creListResult(pcmPriceToEFParaList,
				// priceToERPParas_failed);

			}
		});
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(para.getHeader()));
	}

	/**
	 * 变更促销价格信息
	 * 
	 * @Methods Name changePriceInfoFromEFERP
	 * @Create In 2015年11月25日 By kongqf
	 * @param request
	 * @param para
	 * @return String
	 */
	@RequestMapping(value = "/importPromotionProPriceInfo", method = RequestMethod.POST)
	@ResponseBody
	public String changePriceInfoFromEFERP(HttpServletRequest request,
			@RequestBody MqRequestEntityDataPara<PcmPriceImportEFPara> para) {
		final MqRequestEntityDataPara<PcmPriceImportEFPara> mqpara = para;

		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject jsono = JSONObject
							.fromObject(JSONObject.fromObject(mqpara.getData()).get("data"));
					JSONArray jsona = JSONArray.fromObject(jsono.get("eventItemPrice"));

					/*
					 * String callBackUrl = mqpara.getHeader().getCallbackUrl();
					 * String RequestMsg = "";
					 */
					List<PcmPriceToPISPara> priceToPISParas_failed = new ArrayList<PcmPriceToPISPara>();
					List<PcmPricePara> errorList = new ArrayList<PcmPricePara>();
					final List<PcmPriceERPDto> pcmPriceParaPushList = new ArrayList<PcmPriceERPDto>();
					List<String> pushSearch = new ArrayList<String>();
					PcmPriceERPDto pcmPriceDto = null;
					boolean flag = false;

					BigDecimal upperLimit = new BigDecimal(0);
					BigDecimal lowerLimit = new BigDecimal(0);
					for (int i = 0; i < jsona.size(); i++) {
						flag = false;
						PcmPricePara para = new PcmPricePara();
						pcmPriceDto = new PcmPriceERPDto();
						try {
							para = getPcmPriceParaByEfture(jsona.getJSONObject(i));
							BeanUtils.copyProperties(pcmPriceDto, para);
							if (i == 0) {
								PcmChangePriceLimitDto limitDto = limitService
										.selectPriceLimitByShopCode(pcmPriceDto.getStorecode());
								upperLimit = limitService.getPriceLimit(limitDto, true);
								lowerLimit = limitService.getPriceLimit(limitDto, false);
							}
							String fromSystem = "EFUTUREPROMOTION";
							if (StringUtils.isBlank(pcmPriceDto.getSupplierprodcode())
									&& StringUtils.isNotBlank(pcmPriceDto.getMatnr())) {
								fromSystem = "EFUTUREERP";
							}
							pcmPriceService.saveChangePriceRecord(pcmPriceDto, fromSystem);
							flag = pcmPriceService.saveOrUpdatePrice(pcmPriceDto, fromSystem,
									upperLimit, lowerLimit);
							if (flag) {
								pcmPriceParaPushList.add(pcmPriceDto);
								pushSearch.add(pcmPriceDto.getSupplierprodcode());
							}
						} catch (BleException e) {
							errorList.add(para);
							if (e.getCode()
									.equals(ErrorCode.ADD_CHANGE_PRICE_ERROR.getErrorCode())) {
								ThrowExcetpionUtil.splitExcetpion(new BleException(
										ErrorCode.ADD_CHANGE_PRICE_ERROR.getErrorCode(),
										ErrorCode.ADD_CHANGE_PRICE_ERROR.getMemo()));
							}
						}
					}

					try {
						if (errorList != null && errorList.size() > 0) {
							SavaErrorMessage("importPromotionProPriceInfo",
									JsonUtil.getJSONString(priceToPISParas_failed),
									JsonUtil.getJSONString(errorList));
						}
					} catch (Exception e2) {
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
									logger.error("importProPriceInfo.htm,pushShoppeProduct,Error:"
											+ e.getMessage());
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

	@RequestMapping(value = "/importPromotionProPriceInfo2", method = RequestMethod.POST)
	@ResponseBody
	public String changePriceInfoFromEFERP2(HttpServletRequest request,
			@RequestBody MqRequestDataPara para) {
		final MqRequestDataPara mqpara = para;

		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject jsono = JSONObject
							.fromObject(JSONObject.fromObject(mqpara.getData()).get("data"));
					JSONArray jsona = JSONArray.fromObject(jsono.get("eventItemPrice"));

					/*
					 * String callBackUrl = mqpara.getHeader().getCallbackUrl();
					 * String RequestMsg = "";
					 */
					if (jsona.size() < Constants.PRICE_LINE_COUNT) {
						List<PcmPriceToPISPara> priceToPISParas_failed = new ArrayList<PcmPriceToPISPara>();
						List<PcmPricePara> errorList = new ArrayList<PcmPricePara>();
						final List<PcmPriceERPDto> pcmPriceParaPushList = new ArrayList<PcmPriceERPDto>();
						PcmPriceERPDto pcmPriceDto = null;
						boolean flag = false;

						BigDecimal upperLimit = new BigDecimal(0);
						BigDecimal lowerLimit = new BigDecimal(0);
						for (int i = 0; i < jsona.size(); i++) {
							flag = false;
							PcmPricePara para = new PcmPricePara();
							pcmPriceDto = new PcmPriceERPDto();
							try {
								para = getPcmPriceParaByEfture(jsona.getJSONObject(i));
								BeanUtils.copyProperties(pcmPriceDto, para);
								if (i == 0) {
									PcmChangePriceLimitDto limitDto = limitService
											.selectPriceLimitByShopCode(pcmPriceDto.getStorecode());
									upperLimit = limitService.getPriceLimit(limitDto, true);
									lowerLimit = limitService.getPriceLimit(limitDto, false);
								}
								String fromSystem = "EFUTUREPROMOTION";
								if (StringUtils.isBlank(pcmPriceDto.getSupplierprodcode())
										&& StringUtils.isNotBlank(pcmPriceDto.getMatnr())) {
									fromSystem = "EFUTUREERP";
								}
								pcmPriceService.saveChangePriceRecord(pcmPriceDto, fromSystem);
								flag = pcmPriceService.saveOrUpdatePrice(pcmPriceDto, fromSystem,
										upperLimit, lowerLimit);
								if (flag) {
									pcmPriceParaPushList.add(pcmPriceDto);
								}
							} catch (BleException e) {
								errorList.add(para);
								if (e.getCode()
										.equals(ErrorCode.ADD_CHANGE_PRICE_ERROR.getErrorCode())) {
									ThrowExcetpionUtil.splitExcetpion(new BleException(
											ErrorCode.ADD_CHANGE_PRICE_ERROR.getErrorCode(),
											ErrorCode.ADD_CHANGE_PRICE_ERROR.getMemo()));
								}
							}
						}

						try {
							if (errorList != null && errorList.size() > 0) {
								SavaErrorMessage("importPromotionProPriceInfo",
										JsonUtil.getJSONString(priceToPISParas_failed),
										JsonUtil.getJSONString(errorList));
							}
						} catch (Exception e2) {
						}
					} else {
						// RequestMsg =
						// JsonUtil.getJSONString(ResultUtil.creComErrorResult(
						// ErrorCode.PRICE_IMPORT_ERROR.getErrorCode(),
						// ErrorCode.PRICE_IMPORT_ERROR.getMemo()));
					}

				} catch (Exception e) {
					logger.error("API,importProPriceInfo.htm,synPushToERP,Error:" + e.getMessage());
				}
			}
		});
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(mqpara.getHeader()));
	}

	/**
	 * 获取门店下发数据
	 * 
	 * @Methods Name getPcmPriceToERPEntity
	 * @Create In 2015年8月19日 By kongqf
	 * @param pricePara
	 * @param errorCode
	 * @param errorMsg
	 * @return PcmPriceToEFPara
	 */
	private PcmPriceToEFPara getPcmPriceToERPEntity(PcmPricePara pricePara, String errorCode,
			String errorMsg) {
		PcmPriceToEFPara pcmPriceToERP = new PcmPriceToEFPara();

		try {
			BeanUtils.copyProperties(pcmPriceToERP, pricePara);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		pcmPriceToERP.setResultcode(errorCode);
		pcmPriceToERP.setMessage(errorMsg);

		return pcmPriceToERP;
	}

	/**
	 * 获取PcmPricePara对象
	 * 
	 * @Methods Name getPcmPricePara
	 * @Create In 2015年9月23日 By kongqf
	 * @param object
	 * @return PcmPricePara
	 */
	private PcmPricePara getPcmPricePara(JSONObject object) {
		PcmPricePara para = new PcmPricePara();
		para.setStorecode(object.getString("STORECODE"));
		para.setMatnr(object.getString("MATNR"));
		para.setSupplierprodcode(object.getString("SupplierProdCode"));
		para.setZsprice(object.getString("ZSPRICE"));
		para.setSitecode(object.getString("SITECODE"));
		para.setWaers(object.getString("WAERS"));
		para.setBdate(object.getString("BDATE"));
		para.setEdate(object.getString("EDATE"));
		para.setChangecode(object.getString("CHANGECODE"));
		para.setAction_code(object.getString("ACTION_CODE"));
		para.setAction_date(object.getString("ACTION_DATE"));
		para.setAction_persion(object.getString("ACTION_PERSION"));

		return para;
	}

	/**
	 * 获取促销中心变价信息
	 * 
	 * @Methods Name getPcmPriceParaByEfture
	 * @Create In 2015年12月17日 By kongqf
	 * @param object
	 * @return PcmPricePara
	 */
	private PcmPricePara getPcmPriceParaByEfture(JSONObject object) {
		PcmPricePara para = new PcmPricePara();
		para.setStorecode(object.getString("COCODE"));
		if (object.containsKey("MATNR"))
			para.setMatnr(object.getString("MATNR"));
		para.setSupplierprodcode(object.getString("CRPSCODE"));
		para.setZsprice(object.getString("NSPRICE"));
		para.setSitecode(object.getString("CSLCODE"));
		para.setWaers("RMB");
		para.setBdate(DateUtil.formatDateStr(object.getString("DTHIS"), "yyyyMMdd.HHmmss"));
		para.setEdate(DateUtil.formatToStr(
				addDate(DateUtil.formatDate(object.getString("DEND"), "yyyyMMdd.HHmmss"),
						Calendar.SECOND, 59),
				"yyyyMMdd.HHmmss"));
		PcmSequenceDto seqDto = new PcmSequenceDto();
		seqDto.setPrefix("Z");
		seqDto.setName("efuture");
		String seq = pcmSequenceService.GenerateSeq(seqDto);
		if (StringUtils.isNotBlank(seq)) {
			para.setChangecode(seq);
		} else {
			throw new BleException(ErrorCode.PRICE_EFUTURE_SEQ_FAILED.getErrorCode(),
					ErrorCode.PRICE_EFUTURE_SEQ_FAILED.getMemo());
		}
		para.setAction_code("A");
		para.setAction_date(DateUtil.formatToStr(new Date(), "yyyyMMdd.HHmmssZ"));
		para.setAction_persion("EFUTURE");

		return para;
	}

	public Date addDate(Date date, int filed, int value) {
		java.util.Calendar now = java.util.Calendar.getInstance();
		now.setTime(date);
		now.set(filed, value);
		return now.getTime();
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
	private void SavaErrorMessage(String controllerName, String errorMessage, String dataContent) {
		try {
			PcmExceptionLogDto dto = new PcmExceptionLogDto();
			dto.setInterfaceName("changeprice/eferp/" + controllerName);
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
