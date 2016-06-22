package com.wangfj.product.PAD.controller.stock;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.wangfj.core.framework.base.controller.BaseController;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.core.utils.ResultUtil;
import com.wangfj.core.utils.ThrowExcetpionUtil;
import com.wangfj.product.PAD.controller.support.PcmEdiProductStockPara;
import com.wangfj.product.PAD.controller.support.PcmStockPara;
import com.wangfj.product.PAD.controller.support.PcmStockResultPara;
import com.wangfj.product.PAD.controller.support.PcmStockWcsPara;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.intf.IPcmExceptionLogService;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.stocks.domain.vo.PcmStockDto;
import com.wangfj.product.stocks.service.intf.IPcmStockService;
import com.wangfj.util.Constants;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.MqRequestDataPara;
import com.wangfj.util.mq.MqUtil;

/**
 * 库存管理
 * 
 * @Class Name PcmStockController
 * @Author yedong
 * @Create In 2015年7月20日
 */
@Controller
@RequestMapping("/stockImport")
public class PcmStockImportController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(PcmStockImportController.class);
	@Autowired
	private IPcmStockService pcmStockService;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private IPcmExceptionLogService pcmExceptionLogService;

	/**
	 * 供应商实时库存导入
	 * 
	 * @Methods Name findStockImportFromPcmRealTime
	 * @Create In 2016-3-8 By wangc
	 * @param request
	 * @param mqpara1
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/findStockImportFromPcmRealTime", produces = "application/json; charset=utf-8")
	public Map<String, Object> findStockImportFromPcmRealTime(
			@RequestBody @Valid Map<String, Object> paras) {
		// org.springframework.beans.BeanUtils.copyProperties(mqpara1, mqpara);
		final Map<String, Object> paraMap = paras;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			List<PcmStockPara> paraList = new ArrayList<PcmStockPara>();
			paraList = (List<PcmStockPara>) paraMap.get("data");
			List<PcmStockDto> list = new ArrayList<PcmStockDto>();
			for (int i = Constants.PUBLIC_0; i < paraList.size(); i++) {
				PcmStockDto dto = new PcmStockDto();
				PcmStockPara para = new PcmStockPara();
				try {
					BeanUtils.copyProperties(para, paraList.get(i));
					BeanUtils.copyProperties(dto, para);
					dto.setOperator(para.getOperators());
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				/* 专柜商品编码 */
				dto.setShoppeProSid(para.getSupplyProductId());
				if (StringUtils.isBlank(dto.getChannelSid())) {
					dto.setChannelSid(Constants.DEFAULT_CHANNEL_SID);
				}
				/* 库类型 */
				if (para.getInventory() != null) {
					PcmStockDto InventoryDto = new PcmStockDto();
					try {
						BeanUtils.copyProperties(InventoryDto, dto);
						InventoryDto.setProSum((long) para.getInventory());
						InventoryDto.setStockTypeSid(Constants.PCMSTOCK_TYPE_SALE);/* 可售 */
						list.add(InventoryDto);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				if (para.getBorrowInventory() != null && para.getBorrowInventory() != 0) {
					PcmStockDto borIDto = new PcmStockDto();
					try {
						BeanUtils.copyProperties(borIDto, dto);
						borIDto.setProSum((long) para.getBorrowInventory());
						borIDto.setStockTypeSid(Constants.PCMSTOCK_TYPE_BORROW);/* 借出 */
						list.add(borIDto);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				if (para.getDefectiveInventory() != null && para.getDefectiveInventory() != 0) {
					PcmStockDto DefIDto = new PcmStockDto();
					try {
						BeanUtils.copyProperties(DefIDto, dto);
						DefIDto.setProSum((long) para.getDefectiveInventory());
						DefIDto.setStockTypeSid(Constants.PCMSTOCK_TYPE_DEFECTIVE);/* 残次品 */
						list.add(DefIDto);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}

			List<PcmStockDto> list1 = new ArrayList<PcmStockDto>();
			final List<String> proList = new ArrayList<String>();
			if (list.size() <= Constants.STOCK_IN_COUNT) {
				for (PcmStockDto pcmStockDto : list) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					try {
						PcmStockDto dto = pcmStockService.findStockImportFromPcm(pcmStockDto);
						if (dto.getSuccess() == null) {
							dto.setSuccess(Constants.SUCCESS);
							proList.add(dto.getShoppeProSid());
							resultMap.put("success", Constants.SUCCESS);
							resultMap.put("supplyProductId", dto.getShoppeProSid());
							resultMap.put("proSum", dto.getProSum());
							pcmStockService.updateImportStockCache(dto.getShoppeProSid(),
									dto.getChannelSid());
						} else {
							resultMap.put("success", dto.getSuccess());
							resultMap.put("errorCode", dto.getErrorCode());
							resultMap.put("errorMsg", dto.getException());
						}
					} catch (BleException e) {
						pcmStockDto.setErrorCode(e.getCode());
						pcmStockDto.setException(e.getMessage());
						pcmStockDto.setSuccess(Constants.FAILURE);
						resultMap.put("success", "false");
						resultMap.put("supplyProductId", pcmStockDto.getShoppeProSid());
						resultMap.put("errorCode", e.getCode());
						resultMap.put("errorMsg", e.getMessage());
					}
					list1.add(pcmStockDto);
					result.add(resultMap);
				}
			}

			// 库存下发
			stockPushEdi(proList);
			if (proList != null && proList.size() > 0) {
				List<PcmStockWcsPara> wcsList2 = new ArrayList<PcmStockWcsPara>();
				for (PcmStockDto para : list) {
					PcmStockWcsPara wcs = new PcmStockWcsPara();
					wcs.setFlag("2");
					wcs.setMatnr(para.getSupplyProductId());
					wcs.setNum(para.getInventory().toString());
					if (para.getType().equals(Constants.PCMSTOCK_TYPE_ALL)) {
						wcs.setType("1");
					} else {
						wcs.setType("2");
					}
					wcsList2.add(wcs);
				}
				stockPushWcs(wcsList2);
			}
		} catch (Exception e) {
			logger.error("API,findStockImportFromPcm.htm,Error:" + e.getMessage());
			return ResultUtil.creComErrorResult(ErrorCode.DATA_OPER_ERROR.getErrorCode(),
					ErrorCode.DATA_OPER_ERROR.getMemo());
		}
		return ResultUtil.creComSucResult(result);
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
		final MqRequestDataListPara<PcmStockPara> mqpara = new MqRequestDataListPara<PcmStockPara>();
		org.springframework.beans.BeanUtils.copyProperties(mqpara1, mqpara);

		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					List<PcmStockPara> paraList = new ArrayList<PcmStockPara>();
					paraList = mqpara.getData();
					String callBackUrl = StringUtils.EMPTY;
					if (mqpara.getHeader() != null) {
						callBackUrl = mqpara.getHeader().getCallbackUrl();
					}
					String RequestMsg = "";
					List<PcmStockDto> list = new ArrayList<PcmStockDto>();
					list = getImpPcmStockDto(paraList);
					List<PcmStockDto> list1 = new ArrayList<PcmStockDto>();
					final List<String> proList = new ArrayList<String>();
					if (list.size() <= Constants.STOCK_IN_COUNT) {
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
								pcmStockDto.setErrorCode(e.getCode());
								pcmStockDto.setException(e.getMessage());
								pcmStockDto.setSuccess(Constants.FAILURE);
							}
							list1.add(pcmStockDto);
						}
					}

					// 库存下发
					stockPushEdi(proList);
					RequestMsg = JsonUtil.getJSONString(list1);
					logger.info("API,findStockImportFromPcm.htm,callBackUrl:" + callBackUrl
							+ ",request:" + RequestMsg);
					String response = HttpUtil.doPost(callBackUrl, RequestMsg);
					logger.info("API,findStockImportFromPcm.htm,callBackUrl,response:" + response);
				} catch (Exception e) {
					logger.error("API,findStockImportFromPcm.htm,Error:" + e.getMessage());
				}
			}
		});

		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(mqpara.getHeader()));
	}

	/**
	 * 门店ERP导入
	 * 
	 * @Methods Name findStockImportFromPcm
	 * @Create In 2015年7月28日 By yedong
	 * @param paraList
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/findStockImportFromEFERP", produces = "application/json; charset=utf-8")
	public String findStockImportFromEFERP(HttpServletRequest request,
			@RequestBody MqRequestDataPara para) {
		logger.info("API,findStockImportFromEFERP.htm,para:" + para.toString());
		final MqRequestDataPara mqpara = para;
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject jsono = JSONObject.fromObject(mqpara.getData());
					JSONArray jsona = JSONArray.fromObject(jsono.get("data"));
					List<PcmStockDto> list = new ArrayList<PcmStockDto>();
					for (int i = 0; i < jsona.size(); i++) {
						PcmStockDto dto = new PcmStockDto();
						dto = getPcmStockDto(jsona.getJSONObject(i));
						if (StringUtils.isBlank(dto.getChannelSid())) {
							dto.setChannelSid(Constants.DEFAULT_CHANNEL_SID);
						}
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
					List<PcmStockDto> list1 = new ArrayList<PcmStockDto>();
					final List<String> proList = new ArrayList<String>();
					boolean isSuccess = true;
					if (list.size() <= Constants.STOCK_IN_COUNT) {
						for (PcmStockDto pcmStockDto : list) {
							try {
								PcmStockDto dto = pcmStockService
										.findStockImportFromPcm(pcmStockDto);
								if (dto.getSuccess() == null) {
									dto.setSuccess(Constants.SUCCESS);
									proList.add(dto.getShoppeProSid());
									pcmStockService.updateImportStockCache(dto.getShoppeProSid(),
											dto.getChannelSid());
								} else {
									isSuccess = false;
								}
							} catch (BleException e) {
								pcmStockDto.setErrorCode(e.getCode());
								pcmStockDto.setException(e.getMessage());
								pcmStockDto.setSuccess(Constants.FAILURE);
								isSuccess = false;
							}
							list1.add(pcmStockDto);
						}
					}

					// 库存下发
					stockPushEdi(proList);
					if (!isSuccess) {
						SavaErrorMessage(JsonUtil.getJSONString(list1),
								JsonUtil.getJSONString(list));
					}

				} catch (Exception e) {
					logger.error("API,findStockImportFromPcm.htm,Error:" + e.getMessage());
				}
			}
		});

		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(mqpara.getHeader()));
	}

	/**
	 * PAD 库存导入
	 * 
	 * @Methods Name findStockImportFromPAD
	 * @Create In 2016年1月26日 By kongqf
	 * @param request
	 * @param paraList
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping(value = "/findStockImportFromPAD", produces = "application/json; charset=utf-8")
	public Map<String, Object> findStockImportFromPAD(HttpServletRequest request,
			@RequestBody @Valid List<PcmStockPara> paraList) {
		String RequestMsg = "";
		List<PcmStockDto> list = new ArrayList<PcmStockDto>();
		list = getImpPcmStockDto(paraList);
		final List<String> proList = new ArrayList<String>();
		List<PcmStockResultPara> resultList = new ArrayList<PcmStockResultPara>();
		PcmStockResultPara resultPara = null;
		for (PcmStockDto pcmStockDto : list) {
			try {
				PcmStockDto dto = pcmStockService.findStockImportFromPcm(pcmStockDto);
				if (dto.getSuccess() == null) {
					dto.setSuccess(Constants.SUCCESS);
					proList.add(dto.getShoppeProSid());
					pcmStockService.updateImportStockCache(dto.getShoppeProSid(),
							dto.getChannelSid());
				}
			} catch (BleException e) {
				pcmStockDto.setErrorCode(e.getCode());
				pcmStockDto.setException(e.getMessage());
				pcmStockDto.setSuccess(Constants.FAILURE);
			}
			resultPara = new PcmStockResultPara();
			resultPara.setSupplyProductId(pcmStockDto.getShoppeProSid());
			resultPara.setSuccess(pcmStockDto.getSuccess());
			resultPara.setErrorMsg(pcmStockDto.getException());
			resultPara.setErrorCode(pcmStockDto.getErrorCode());
			resultList.add(resultPara);
		}

		// 库存下发
		stockPushEdi(proList);

		RequestMsg = JsonUtil.getJSONString(resultList);
		return ResultUtil.creComSucResult(RequestMsg);
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
			dto.setInterfaceName("pcm-import/stockImport/findStockImportFromPcm");
			dto.setExceptionType(StatusCode.EXCEPTION_STOCK.getStatus());
			dto.setErrorMessage(errorMessage);
			dto.setDataContent(dataContent);
			dto.setUuid(UUID.randomUUID().toString());
			pcmExceptionLogService.saveExceptionLogInfo(dto);
		} catch (Exception e) {
			logger.info("API,Save PcmExceptionLogDto failed:" + e.getMessage());
		}
	}

	private PcmStockDto getPcmStockDto(JSONObject object) {
		PcmStockDto dto = new PcmStockDto();
		dto.setSupplyProductId(object.getString("supplyProductId"));
		dto.setShoppeProSid(object.getString("supplyProductId"));
		dto.setLocation(object.getString("locationId"));
		dto.setLocationOwnerId(object.getString("locationOwnerId"));
		if (StringUtils.isNotBlank(object.getString("inventory"))) {
			dto.setInventory(Integer.parseInt(object.getString("inventory")));
		}
		if (StringUtils.isNotBlank(object.getString("borrowInventory"))) {
			dto.setBorrowInventory(Integer.parseInt(object.getString("borrowInventory")));
		}
		if (StringUtils.isNotBlank(object.getString("defectiveInventory"))) {
			dto.setDefectiveInventory(Integer.parseInt(object.getString("defectiveInventory")));
		}
		if (StringUtils.isNotBlank(object.getString("stopsalesInventory"))) {
			dto.setStopsalesInventory(Integer.parseInt(object.getString("stopsalesInventory")));
		}
		if (StringUtils.isNotBlank(object.getString("warningInventory"))) {
			dto.setWarningInventory(Integer.parseInt(object.getString("warningInventory")));
		}
		dto.setType(object.getString("type"));
		dto.setSource(object.getString("source"));
		dto.setOperator(object.getString("operators"));

		return dto;
	}

	/**
	 * 库存上传List
	 * 
	 * @Methods Name getImpPcmStockDto
	 * @Create In 2016年3月10日 By kongqf
	 * @param paraList
	 * @return List<PcmStockDto>
	 */
	public List<PcmStockDto> getImpPcmStockDto(List<PcmStockPara> paraList) {
		List<PcmStockDto> list = new ArrayList<PcmStockDto>();
		for (int i = 0; i < paraList.size(); i++) {
			PcmStockDto dto = new PcmStockDto();
			PcmStockPara para = new PcmStockPara();
			try {
				BeanUtils.copyProperties(para, paraList.get(i));
				BeanUtils.copyProperties(dto, para);
				dto.setOperator(para.getOperators());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			/* 专柜商品编码 */
			dto.setShoppeProSid(para.getSupplyProductId());
			if (StringUtils.isBlank(dto.getChannelSid())) {
				dto.setChannelSid(Constants.DEFAULT_CHANNEL_SID);
			}
			/* 库类型 */
			if (para.getInventory() != null) {
				PcmStockDto InventoryDto = new PcmStockDto();
				try {
					BeanUtils.copyProperties(InventoryDto, dto);
					InventoryDto.setProSum((long) para.getInventory());
					InventoryDto.setStockTypeSid(Constants.PCMSTOCK_TYPE_SALE);/* 可售 */
					list.add(InventoryDto);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			if (para.getBorrowInventory() != null && para.getBorrowInventory() != 0) {
				PcmStockDto borIDto = new PcmStockDto();
				try {
					BeanUtils.copyProperties(borIDto, dto);
					borIDto.setProSum((long) para.getBorrowInventory());
					borIDto.setStockTypeSid(Constants.PCMSTOCK_TYPE_BORROW);/* 借出 */
					list.add(borIDto);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			if (para.getDefectiveInventory() != null && para.getDefectiveInventory() != 0) {
				PcmStockDto DefIDto = new PcmStockDto();
				try {
					BeanUtils.copyProperties(DefIDto, dto);
					DefIDto.setProSum((long) para.getDefectiveInventory());
					DefIDto.setStockTypeSid(Constants.PCMSTOCK_TYPE_DEFECTIVE);/* 残次品 */
					list.add(DefIDto);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
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
