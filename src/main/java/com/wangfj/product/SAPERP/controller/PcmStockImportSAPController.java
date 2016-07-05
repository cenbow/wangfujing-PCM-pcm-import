package com.wangfj.product.SAPERP.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangfj.core.constants.ComErrorCodeConstants.ErrorCode;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.core.utils.ThrowExcetpionUtil;
import com.wangfj.product.PAD.controller.support.PcmEdiProductStockPara;
import com.wangfj.product.PAD.controller.support.PcmStockWcsPara;
import com.wangfj.product.SAPERP.controller.support.PcmStockPara;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.intf.IPcmExceptionLogService;
import com.wangfj.product.constants.FlagType;
import com.wangfj.product.constants.JcoSAPUtils;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.stocks.domain.vo.PcmStockDto;
import com.wangfj.product.stocks.service.intf.IPcmStockService;
import com.wangfj.util.Constants;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.MqUtil;

@Controller
@RequestMapping("/stockImport/SAPErp")
public class PcmStockImportSAPController {
	private static final Logger logger = LoggerFactory.getLogger(PcmStockImportSAPController.class);
	@Autowired
	private IPcmStockService pcmStockService;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private IPcmExceptionLogService pcmExceptionLogService;

	/**
	 * 导入
	 * 
	 * @Methods Name findStockImportFromPcm
	 * @Create In 2015年7月28日 By yedong
	 * @param paraList
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/findStockImportFromPcm", produces = "application/json; charset=utf-8")
	public String findStockImportFromPcm(HttpServletRequest request,
			@RequestBody @Valid MqRequestDataListPara<PcmStockPara> mqpara1) {
		final List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
		final MqRequestDataListPara<PcmStockPara> mqpara = new MqRequestDataListPara<PcmStockPara>();
		org.springframework.beans.BeanUtils.copyProperties(mqpara1, mqpara);

		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					List<PcmStockPara> paraList = new ArrayList<PcmStockPara>();
					paraList = mqpara.getData();
					List<PcmStockDto> list = new ArrayList<PcmStockDto>();

					for (int i = Constants.PUBLIC_0; i < paraList.size(); i++) {
						PcmStockDto dto = new PcmStockDto();
						PcmStockPara para = new PcmStockPara();
						try {
							BeanUtils.copyProperties(para, paraList.get(i));
							dto = getPcmStockDto(para);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
						/* 专柜商品编码 */
						dto.setShoppeProSid(dto.getSupplyProductId());
						if (StringUtils.isBlank(dto.getChannelSid())) {
							dto.setChannelSid(Constants.DEFAULT_CHANNEL_SID);
						}
						/* 库类型 */
						if (dto.getInventory() != null) {
							PcmStockDto InventoryDto = new PcmStockDto();
							try {
								BeanUtils.copyProperties(InventoryDto, dto);
								InventoryDto.setProSum((long) dto.getInventory());
								InventoryDto.setStockTypeSid(Constants.PCMSTOCK_TYPE_SALE);/* 可售 */
								list.add(InventoryDto);
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
						if (dto.getBorrowInventory() != null && dto.getBorrowInventory() != 0) {
							PcmStockDto borIDto = new PcmStockDto();
							try {
								BeanUtils.copyProperties(borIDto, dto);
								borIDto.setProSum((long) dto.getBorrowInventory());
								borIDto.setStockTypeSid(Constants.PCMSTOCK_TYPE_BORROW);/* 借出 */
								list.add(borIDto);
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
						if (dto.getDefectiveInventory() != null && dto.getDefectiveInventory() != 0) {
							PcmStockDto DefIDto = new PcmStockDto();
							try {
								BeanUtils.copyProperties(DefIDto, dto);
								DefIDto.setProSum((long) dto.getDefectiveInventory());
								DefIDto.setStockTypeSid(Constants.PCMSTOCK_TYPE_DEFECTIVE);/* 残次品 */
								list.add(DefIDto);
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}
					final List<String> proList = new ArrayList<String>();
					if (list.size() < Constants.STOCK_IN_COUNT) {
						for (PcmStockDto pcmStockDto : list) {
							try {
								PcmStockDto dto = pcmStockService
										.findStockImportFromPcm(pcmStockDto);
								if (dto.getSuccess() == null) {
									dto.setSuccess(Constants.SUCCESS);
									proList.add(dto.getShoppeProSid());
									pcmStockService.updateImportStockCache(dto.getShoppeProSid(),
											dto.getChannelSid());
								}
							} catch (BleException e) {
								resultMapList.add(getResultMap(pcmStockDto.getShoppeProSid(),
										e.getCode(), e.getMessage()));
								SavaErrorMessage(e.getMessage(), JsonUtil.getJSONString(pcmStockDto));
							}
						}
					}

					// 库存下发
					stockPushEdi(proList);
					if (proList != null && proList.size() > 0
							&& FlagType.getPublish_info() == Constants.PUBLIC_0) {
						List<PcmStockWcsPara> wcsList2 = new ArrayList<PcmStockWcsPara>();
						for (PcmStockDto para : list) {
							PcmStockWcsPara wcs = new PcmStockWcsPara();
							wcs.setFlag("1");
							wcs.setMatnr(para.getSupplyProductId());
							wcs.setNum(para.getInventory());
							if (para.getType().equals(Constants.PCMSTOCK_TYPE_ALL)) {
								wcs.setType("1");
							} else {
								wcs.setType("2");
							}
							wcsList2.add(wcs);
						}
						stockPushWcs(wcsList2);
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
									logger.error("API,importProPriceInfo.htm,Error:"
											+ e.getMessage());
									SavaErrorMessage(e.getMessage(), JsonUtil.getJSONString(resultMapList));
								}
							}
						});
					}
				} catch (Exception e) {
					logger.error("API,findStockImportFromPcm.htm,Error:" + e.getMessage());
				}
			}
		});
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(mqpara.getHeader()));
	}

	/**
	 * 库存数据转换
	 * 
	 * @Methods Name getPcmStockDto
	 * @Create In 2015年11月23日 By kongqf
	 * @param para
	 * @return PcmStockDto
	 */
	private PcmStockDto getPcmStockDto(PcmStockPara para) {
		PcmStockDto dto = new PcmStockDto();
		dto.setSku(para.getSKU());
		dto.setSupplyProductId(para.getSUPPLYPRODUCTID());
		dto.setLocation(para.getLOCATION());
		dto.setLocationOwnerId(para.getLOCATIONOWNERID());
		dto.setInventory(para.getINVENTORY());
		dto.setBorrowInventory(para.getBORROWINVENTORY());
		dto.setDefectiveInventory(para.getDEFECTIVEINVENTORY());
		dto.setStopsalesInventory(para.getSTOPSALESINVENTORY());
		dto.setWarningInventory(para.getWARNINGINVENTORY());
		dto.setType(para.getTYPE());
		dto.setSource(para.getSOURCE());
		dto.setOperator(para.getOPERATOR());
		dto.setGuid(para.getGUID());
		return dto;
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
			dto.setInterfaceName("pcm-import/stockImport/SAPErp/findStockImportFromPcm");
			dto.setExceptionType(StatusCode.EXCEPTION_STOCK.getStatus());
			dto.setErrorMessage(errorMessage);
			dto.setDataContent(dataContent);
			dto.setUuid(UUID.randomUUID().toString());
			pcmExceptionLogService.saveExceptionLogInfo(dto);
		} catch (Exception e) {
			logger.info("API,Save PcmExceptionLogDto failed:" + e.getMessage());
		}
	}

	/**
	 * 库存下发
	 * 
	 * @Methods Name stockPushWcs
	 * @Create In 2016年6月20日 By yedong
	 * @param proList
	 *            void
	 */
	public void stockPushWcs(List<PcmStockWcsPara> paraList) {
		try {
			String wcsStockUrl = PropertyUtil.getSystemUrl("wcs.stock");
			logger.info("API,synPushStockToWCS,request:" + paraList.toString());
			String response = HttpUtil.doPost(wcsStockUrl, JsonUtil.getJSONString(paraList));
			logger.info("API,synPushStockToWCS,response:" + response);
		} catch (Exception e) {
			logger.error("API,synPushStockToWCS,Error:" + e.getMessage());
			ThrowExcetpionUtil.splitExcetpion(new BleException(ErrorCode.STOCK_IMPORT_PUSH_ERROR
					.getErrorCode(), ErrorCode.STOCK_IMPORT_PUSH_ERROR.getMemo() + e.getMessage()));
			SavaErrorMessage(e.getMessage(), JsonUtil.getJSONString(paraList));
		}
	}

	/**
	 * 库存下发
	 * 
	 * @Methods Name stockPushEdi
	 * @Create In 2016年3月10日 By kongqf
	 * @param proList
	 *            void
	 */
	public void stockPushEdi(final List<String> proList) {
		if (proList != null && proList.size() > 0) {
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					PcmEdiProductStockPara pushList = new PcmEdiProductStockPara();
					try {
						pushList.setShoppeProSids(proList);
						String ediStockUrl = PropertyUtil.getSystemUrl("edi.stock");
						logger.info("API,synPushStockToEDI,request:" + pushList.toString());
						String response = HttpUtil.doPost(ediStockUrl,
								JsonUtil.getJSONString(pushList));
						logger.info("API,synPushStockToEDI,response:" + response);
					} catch (Exception e) {
						logger.error("API,synPushStockToEDI,Error:" + e.getMessage());
						ThrowExcetpionUtil.splitExcetpion(new BleException(
								ErrorCode.STOCK_IMPORT_PUSH_ERROR.getErrorCode(),
								ErrorCode.STOCK_IMPORT_PUSH_ERROR.getMemo() + e.getMessage()));
						SavaErrorMessage(e.getMessage(), JsonUtil.getJSONString(pushList));
					}
				}
			});
		}
	}
}
