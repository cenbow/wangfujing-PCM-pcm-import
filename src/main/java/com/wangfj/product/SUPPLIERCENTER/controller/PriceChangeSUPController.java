package com.wangfj.product.SUPPLIERCENTER.controller;

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
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.core.utils.ThrowExcetpionUtil;
import com.wangfj.product.SUPPLIERCENTER.controller.support.PcmPricePara;
import com.wangfj.product.SUPPLIERCENTER.controller.support.PcmPriceToPISPara;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.intf.IPcmExceptionLogService;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.core.controller.support.base.constants.CommonParamValidate;
import com.wangfj.product.price.domain.vo.PcmChangePriceLimitDto;
import com.wangfj.product.price.domain.vo.PcmPriceERPDto;
import com.wangfj.product.price.service.intf.IPcmChangePriceLimitService;
import com.wangfj.product.price.service.intf.IPcmPriceService;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.MqUtil;

/**
 * 供应商价格管理
 * 
 * @Class Name PriceChangePISController
 * @Author kongqf
 * @Create In 2016年1月21日
 */
@Controller
@RequestMapping("/changeprice/Suppliers")
public class PriceChangeSUPController {
	private static final Logger logger = LoggerFactory.getLogger(PriceChangeSUPController.class);

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
							pcmPriceService.saveChangePriceRecord(pcmPriceDto,
									CommonParamValidate.SUPPLIERCENTER);
							flag = pcmPriceService.saveOrUpdatePrice(pcmPriceDto,
									CommonParamValidate.SUPPLIERCENTER, upperLimit, lowerLimit);
							if (flag) {
								pcmPriceParaPushList.add(para);
								pushSearch.add(para.getSupplierprodcode());
							}
						} catch (BleException e) {
							errorList.add(para);
							priceToPISParas_failed
									.add(getPcmPricePISEntity(para, e.getCode(), e.getMessage()));
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
						// 下发SAPERP和营销中心
						final String pushToERP = PropertyUtil.getSystemUrl("price.synPushToSAPERP");
						taskExecutor.execute(new Runnable() {
							@Override
							public void run() {
								try {
									logger.info("API,importProPriceInfo.htm,synPushToERP,request:"
											+ pcmPriceParaPushList.toString());
									String response = HttpUtil.doPost(pushToERP,
											JsonUtil.getJSONString(pcmPriceParaPushList));
									logger.info("API,importProPriceInfo.htm,synPushToERP,response:"
											+ response);
								} catch (Exception e) {
									logger.error("API,importProPriceInfo.htm,synPushToERP,Error:"
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
									logger.error("importProPriceInfo.htm,pushShoppeProduct,Error:"
											+ e.getMessage());
								}
							}
						});
					}
					try {
						if (errorList != null && errorList.size() > 0) {
							SavaErrorMessage(JsonUtil.getJSONString(priceToPISParas_failed),
									JsonUtil.getJSONString(errorList));
						}
					} catch (Exception e2) {
					}

					RequestMsg = JsonUtil.getJSONString(
							creListResult(pcmPriceToPISParaList, priceToPISParas_failed));

					try {
						logger.info("API,importProPriceInfo.htm,callBackUrl:" + callBackUrl
								+ ",request:" + RequestMsg);
						String response = HttpUtil.doPost(callBackUrl, RequestMsg);
						logger.info("API,importProPriceInfo.htm,callBackUrl,response:" + response);
						if (response == null) {
							ThrowExcetpionUtil.splitExcetpion(new BleException(
									ErrorCode.CALLBACK_PRICE_PIS_FAILED.getErrorCode(),
									ErrorCode.CALLBACK_PRICE_PIS_FAILED.getMemo()));
							SavaErrorMessage(ErrorCode.CALLBACK_PRICE_PIS_FAILED.getMemo(),
									RequestMsg);
						}
					} catch (Exception e) {
						ThrowExcetpionUtil.splitExcetpion(
								new BleException(ErrorCode.CALLBACK_PRICE_PIS_FAILED.getErrorCode(),
										ErrorCode.CALLBACK_PRICE_PIS_FAILED.getMemo()));
						SavaErrorMessage(ErrorCode.CALLBACK_PRICE_PIS_FAILED.getMemo() + ":"
								+ e.getMessage(), RequestMsg);
					}

				} catch (Exception e) {
					logger.error("API,importProPriceInfo.htm,synPushToERP,Error:" + e.getMessage());
				}
			}
		});
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(mqpara.getHeader()));

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
			dto.setInterfaceName("pcm-import/changeprice/Suppliers/batchimportproprice");
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
