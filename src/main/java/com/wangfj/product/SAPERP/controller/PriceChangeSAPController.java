package com.wangfj.product.SAPERP.controller;

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
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.core.utils.ThrowExcetpionUtil;
import com.wangfj.product.SAPERP.controller.support.PcmPricePara;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.intf.IPcmExceptionLogService;
import com.wangfj.product.constants.JcoSAPUtils;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.price.domain.vo.PcmChangePriceLimitDto;
import com.wangfj.product.price.domain.vo.PcmPriceERPDto;
import com.wangfj.product.price.service.intf.IPcmChangePriceLimitService;
import com.wangfj.product.price.service.intf.IPcmPriceService;
import com.wangfj.util.Constants;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.MqUtil;

@Controller
@RequestMapping("/changeprice/erp")
public class PriceChangeSAPController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(PriceChangeSAPController.class);

	@Autowired
	private IPcmPriceService pcmPriceService;
	@Autowired
	private IPcmExceptionLogService pcmExceptionLogService;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private IPcmChangePriceLimitService pcmChangePriceLimitService;

	/**
	 * 单一变价
	 * 
	 * @Methods Name changePriceInfoFromERP
	 * @Create In 2015年11月18日 By kongqf
	 * @param request
	 * @param mqpara1
	 * @return String
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
					final List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
					List<String> pushSearch = new ArrayList<String>();
					if (priceParalist.size() < Constants.PRICE_LINE_COUNT) {
						List<PcmPricePara> errorList = new ArrayList<PcmPricePara>();
						final List<PcmPriceERPDto> pcmPriceParaPushList = new ArrayList<PcmPriceERPDto>();
						PcmPriceERPDto pcmPriceDto = null;
						boolean flag = false;

						BigDecimal upperLimit = new BigDecimal(0);
						BigDecimal lowerLimit = new BigDecimal(0);
						for (int i = 0; i < priceParalist.size(); i++) {
							flag = false;
							PcmPricePara para = new PcmPricePara();
							pcmPriceDto = new PcmPriceERPDto();

							try {
								BeanUtils.copyProperties(para, priceParalist.get(i));
								pcmPriceDto = getPcmPriceDto(para);
								if (i == 0) {
									PcmChangePriceLimitDto limitDto = pcmChangePriceLimitService
											.selectPriceLimitByShopCode(pcmPriceDto.getStorecode());
									upperLimit = pcmChangePriceLimitService.getPriceLimit(limitDto,
											true);
									lowerLimit = pcmChangePriceLimitService.getPriceLimit(limitDto,
											false);
								}
								pcmPriceService.saveChangePriceRecord(pcmPriceDto, Constants.PIS);
								flag = pcmPriceService.saveOrUpdatePrice(pcmPriceDto, Constants.PIS,
										upperLimit, lowerLimit);
								if (flag) {
									pcmPriceParaPushList.add(pcmPriceDto);
									pushSearch.add(pcmPriceDto.getSupplierprodcode());
								}
							} catch (BleException e) {
								errorList.add(para);
								resultMapList.add(getResultMap(para.getSUPPLIERPRODCODE(),
										e.getCode(), e.getMessage()));
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
								SavaErrorMessage(JsonUtil.getJSONString(resultMapList),
										JsonUtil.getJSONString(errorList));
							}
						} catch (Exception e2) {
						}
					} else {
						resultMapList
								.add(getResultMap("", ErrorCode.PRICE_IMPORT_ERROR.getErrorCode(),
										ErrorCode.PRICE_IMPORT_ERROR.getMemo()));
					}
					if (resultMapList != null && resultMapList.size() > 0) {
						taskExecutor.execute(new Runnable() {
							@Override
							public void run() {
								try {
									logger.info("API,importProPriceInfo.htm,result Error:"
											+ JsonUtil.getJSONString(resultMapList));
									JcoSAPUtils.functionExecute("ZFM_MD_PCM2SAP_ERROR_IN", "INPUT",
											resultMapList);
								} catch (Exception e) {
									logger.error(
											"API,importProPriceInfo.htm,Error:" + e.getMessage());
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
				} catch (Exception e) {
					logger.error("API,importProPriceInfo.htm,Error:" + e.getMessage());
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
	private Map<String, Object> getResultMap(String matnr, String errorCode, String errorMsg) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("KEY_FIELD", matnr);
		map.put("FLAG", "4");
		map.put("MESSAGE", errorCode + ";" + errorMsg);
		return map;
	}

	/**
	 * 获取PcmPricePara对象
	 * 
	 * @Methods Name getPcmPricePara
	 * @Create In 2015年9月23日 By kongqf
	 * @param object
	 * @return PcmPricePara
	 */
	private PcmPriceERPDto getPcmPriceDto(PcmPricePara pricePara) {
		PcmPriceERPDto dto = new PcmPriceERPDto();
		dto.setStorecode(pricePara.getSTORECODE());
		dto.setMatnr(pricePara.getMATNR());
		dto.setSupplierprodcode(pricePara.getSUPPLIERPRODCODE());
		dto.setZsprice(pricePara.getZSPRICE());
		dto.setSitecode(pricePara.getSITECODE());
		dto.setWaers(pricePara.getWAERS());
		dto.setBdate(pricePara.getBDATE());
		dto.setEdate(pricePara.getEDATE());
		dto.setChangecode(pricePara.getCHANGECODE());
		dto.setAction_code(pricePara.getACTION_CODE());
		dto.setAction_date(pricePara.getACTION_DATE());
		dto.setAction_persion(pricePara.getACTION_PERSION());

		return dto;
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
			dto.setInterfaceName("pcm-import/changeprice/erp/importProPriceInfo");
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
