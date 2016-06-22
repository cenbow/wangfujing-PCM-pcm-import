package com.wangfj.product.SAPERP.controller.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.wangfj.core.cache.RedisVo;
import com.wangfj.core.constants.ComErrorCodeConstants.ErrorCode;
import com.wangfj.core.constants.ErrorCodeConstants;
import com.wangfj.core.framework.base.controller.BaseController;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.CacheUtils;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.core.utils.ThrowExcetpionUtil;
import com.wangfj.product.SAPERP.controller.support.ProductsSAPERP;
import com.wangfj.product.SAPERP.controller.support.SapContractPara;
import com.wangfj.product.SAPERP.controller.support.SapProListPara;
import com.wangfj.product.SAPERP.controller.support.SaveContractParaSAPERP;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.impl.PcmExceptionLogService;
import com.wangfj.product.common.service.intf.IJcoSAPUtil;
import com.wangfj.product.constants.DomainName;
import com.wangfj.product.constants.FlagType;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.maindata.domain.entity.PcmShoppeProduct;
import com.wangfj.product.maindata.domain.entity.PcmShoppeProductExtend;
import com.wangfj.product.maindata.domain.vo.PcmContractLogDto;
import com.wangfj.product.maindata.domain.vo.PullDataDto;
import com.wangfj.product.maindata.domain.vo.SapContractDto;
import com.wangfj.product.maindata.domain.vo.SapProListDto;
import com.wangfj.product.maindata.service.intf.IPcmContractLogService;
import com.wangfj.product.maindata.service.intf.IPcmCreateProductService;
import com.wangfj.product.maindata.service.intf.IPcmShoppeProductService;
import com.wangfj.product.maindata.service.intf.IValidProductService;
import com.wangfj.util.Constants;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.MqUtil;
import com.wangfj.util.mq.PublishDTO;
import com.wangfj.util.mq.RequestHeader;

@Controller
@RequestMapping("/saveProduct")
public class SaveProductController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(SaveProductController.class);
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private IValidProductService validProductService;
	@Autowired
	private PcmExceptionLogService pcmExceptionLogService;
	@Autowired
	private IPcmContractLogService pcmContractLogService;
	@Autowired
	private IPcmCreateProductService pcmCreateProductService;
	@Autowired
	private IJcoSAPUtil jcoUtils;
	@Autowired
	private IPcmShoppeProductService proService;

	/**
	 * 新电商合同导入
	 * 
	 * @Methods Name saveContractFromNewSAPERP
	 * @Create In 2016年6月6日 By yedong
	 * @param para2
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/saveContractFromNewSAPERP", method = RequestMethod.POST)
	public String saveContractFromNewSAPERP(
			@RequestBody MqRequestDataListPara<SapContractPara> para2) {
		final MqRequestDataListPara<SapContractPara> paraf = para2;
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				List<SapContractPara> data2 = paraf.getData();

				// 返回信息LIST
				List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
				// 异常信息LIST
				List<Map<String, Object>> excepList = new ArrayList<Map<String, Object>>();
				// 下发LIST
				for (int l = 0; l < data2.size(); l++) {
					List<SapProListDto> sapProList = new ArrayList<SapProListDto>();
					SapContractDto dto = new SapContractDto();
					try {
						BeanUtils.copyProperties(dto, data2.get(l));
						List<SapProListPara> prodtolist = data2.get(l).getPRODTOLIST();
						for (SapProListPara sapProListPara : prodtolist) {
							SapProListDto sapDto = new SapProListDto();
							BeanUtils.copyProperties(sapDto, sapProListPara);
							sapProList.add(sapDto);
						}
						dto.setPRODTOLIST(sapProList);
					} catch (Exception e1) {
						logger.error("BeanUtils.copyProperties-Error" + dto.toString());
					}
					// 写入/校验合同----------------------------------
					List<PcmContractLogDto> contractLogDtos = new ArrayList<PcmContractLogDto>();
					PcmContractLogDto contractDto = null;
					try {
						contractDto = createContractNewDto(dto);
						contractLogDtos.add(contractDto);
						pcmContractLogService.uploadContractLogBatch(contractLogDtos);// 电商合同上传，第二个参数是SAP
						if (dto.getPRODTOLIST() != null && dto.getPRODTOLIST().size() > 0) {
							List<PublishDTO> sidList = new ArrayList<PublishDTO>();
							List<Map<String, Object>> reMap = pcmContractLogService
									.proAndContractLogInfoManager(dto.getPRODTOLIST(), contractDto);
							for (Map<String, Object> map : reMap) {
								resList.add(map);
							}
							List<SapProListDto> prodtolist = dto.getPRODTOLIST();
							List<Long> proSidList = proService.getSidListBySapProCode(prodtolist);
							for (Long sid : proSidList) {
								PublishDTO dto1 = new PublishDTO();
								dto1.setSid(sid);
								dto1.setType(0);
								sidList.add(dto1);
							}
							// 专柜商品信息下发 - 搜索OPS前台展示 - 电商SAPERP - 富集
							final Map<String, Object> paramMap = new HashMap<String, Object>();
							paramMap.put("paraList", proSidList);
							// paramMap.put("PcmEfutureERP", "1"); // 门店
							// paramMap.put("PcmSapErp", "1"); // SAP
							paramMap.put("PcmEfuturePromotion", "1"); // 富基
							// paramMap.put("PcmSearcherOffline", "1"); // 搜索线下
							// paramMap.put("PcmSearcherOnline", "1"); // 搜索线上上架
							// paramMap.put("PcmSearcherOnline2", "1"); //
							// 搜索线上下架
							paramMap.put("PcmProSearch", "1");
							HttpUtil.doPost(PropertyUtil.getSystemUrl("pcm-syn")
									+ "/pcmShoppeProduct/publishShoppeProductFromPcm.htm",
									JsonUtil.getJSONString(paramMap));
						}
					} catch (BleException e1) {
						logger.error(e1.getMessage() + dto.toString());
						Map<String, Object> resMap = new HashMap<String, Object>();
						resMap.put("KEY_FIELD", contractDto.getContractCode());
						resMap.put("FLAG", 6);
						resMap.put("MESSAGE", e1.getMessage());
						resList.add(resMap);
						excepList.add(resMap);
						continue;
					} catch (Exception e2) {
						logger.error(ErrorCode.PARA_NORULE_ERROR.getMemo() + dto.toString());
						Map<String, Object> resMap = new HashMap<String, Object>();
						resMap.put("KEY_FIELD", contractDto.getContractCode());
						resMap.put("FLAG", 6);
						resMap.put("MESSAGE", ErrorCode.PARA_NORULE_ERROR.getMemo());
						resList.add(resMap);
						excepList.add(resMap);
						continue;
					}
				}
				// 写入异常表
				if (excepList.size() != 0) {
					PcmExceptionLogDto pcmExceptionLogDto = new PcmExceptionLogDto();
					pcmExceptionLogDto.setInterfaceName("saveProductFromSAPERP");
					pcmExceptionLogDto.setExceptionType(StatusCode.EXCEPTION_PRODUCT.getStatus());
					pcmExceptionLogDto.setErrorMessage(JsonUtil.getJSONString(excepList));
					pcmExceptionLogDto.setDataContent(JsonUtil.getJSONString(paraf));
					pcmExceptionLogDto.setUuid(UUID.randomUUID().toString());
					pcmExceptionLogService.saveExceptionLogInfo(pcmExceptionLogDto);
					jcoUtils.functionExecute("ZFM_MD_PCM2SAP_ERROR_IN", "INPUT", resList);
				}
			}
		});
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(paraf.getHeader()));
	}

	/**
	 * 生成合同表DTO
	 * 
	 * @Methods Name createContractDto
	 * @Create In 2015年11月18日 By zhangxy
	 * @param para
	 * @return PcmContractLogDto
	 */
	private PcmContractLogDto createContractNewDto(SapContractDto dto) {
		PcmContractLogDto pcmContractLogDto = new PcmContractLogDto();
		pcmContractLogDto.setStoreCode(dto.getSTORECODE());
		pcmContractLogDto.setContractCode(dto.getCONTRACTCODE());
		pcmContractLogDto.setBuyType(Integer.valueOf(dto.getBUYTYPE()));
		pcmContractLogDto.setOperMode(Integer.valueOf(dto.getOPERMODE()));
		pcmContractLogDto.setSupplyCode(dto.getSUPPLIERCODE());
		pcmContractLogDto.setOptTime(new Date());
		pcmContractLogDto.setCol1(dto.getGLFL());// 管理分类
		if ("E".equals(dto.getBUSINESSTYPE())) {
			pcmContractLogDto.setBusinessType(0);
		}
		if ("C".equals(dto.getBUSINESSTYPE())) {
			pcmContractLogDto.setBusinessType(2);
		}
		if (StringUtils.isNotBlank(dto.getINPUTTAX())) {
			pcmContractLogDto.setInputTax(new BigDecimal(dto.getINPUTTAX()));
		}
		if (StringUtils.isNotBlank(dto.getOUTPUTTAX())) {
			pcmContractLogDto.setOutputTax(new BigDecimal(dto.getOUTPUTTAX()));
		}
		if (StringUtils.isNotBlank(dto.getCOMMISSIONRATE())) {
			pcmContractLogDto.setCommissionRate(new BigDecimal(dto.getCOMMISSIONRATE()));
		}

		if ("A".equals(dto.getACTION_CODE())) {
			pcmContractLogDto.setFlag(0);
		}
		if ("U".equals(dto.getACTION_CODE())) {
			pcmContractLogDto.setFlag(1);
		}

		// 经营方式

		// 经销
		pcmContractLogDto.setManageType(Integer.valueOf(dto.getMANAGETYPE()));
		return pcmContractLogDto;
	}

	/**
	 * 电商商品导入
	 * 
	 * @Methods Name saveProductFromSAPERP
	 * @Create In 2015年11月17日 By zhangxy
	 * @param para1
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/saveProductFromSAPERP", method = RequestMethod.POST)
	public String saveProductFromSAPERP(
			@RequestBody @Valid MqRequestDataListPara<ProductsSAPERP> para2) {
		final MqRequestDataListPara<ProductsSAPERP> para = para2;
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				RequestHeader header = para.getHeader();
				List<ProductsSAPERP> data2 = para.getData();
				List<ProductsSAPERP> data = JSON.parseArray(JsonUtil.getJSONString(data2)
						.toString(), ProductsSAPERP.class);
				// 返回信息LIST
				List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
				// 异常信息LIST
				List<Map<String, Object>> excepList = new ArrayList<Map<String, Object>>();
				// 下发LIST
				final List<PublishDTO> sidList = new ArrayList<PublishDTO>();
				final List<PublishDTO> skusidList = new ArrayList<PublishDTO>();
				final List<PublishDTO> spusidList = new ArrayList<PublishDTO>();
				PublishDTO publishDto = null;
				// sidList = new ArrayList<PublishDTO>();
				// skusidList = new ArrayList<PublishDTO>();
				// spusidList = new ArrayList<PublishDTO>();
				for (int i = 0; i < data.size(); i++) {
					ProductsSAPERP sapPara = new ProductsSAPERP();
					try {
						BeanUtils.copyProperties(sapPara, data.get(i));
					} catch (Exception e1) {
						logger.error("BeanUtils.copyProperties-Error" + para.toString());
					}
					/*
					 * String smatnr = sapPara.getSUPPLIERPRODUCTCODE();
					 * sapPara.setS_MATNR(smatnr);
					 */
					if (StringUtils.isNotBlank(sapPara.getS_MATNR())) {
						// 中台商品编码字段不为空时 修改商品所挂合同号
						try {
							if (StringUtils.isBlank(sapPara.getS_MATNR())
									|| StringUtils.isBlank(sapPara.getMATNR())
									|| StringUtils.isBlank(sapPara.getOFFERNUMBER())) {
								Map<String, Object> resMap = new HashMap<String, Object>();
								resMap.put("KEY_FIELD", sapPara.getMATNR());
								// resMap.put("SUCCESS", "false");
								resMap.put("FLAG", 0);
								resMap.put("MESSAGE", ErrorCode.PRO_PARA_NORULE_ERROR.getMemo());
								resList.add(resMap);
								continue;
							}
							validProductService.updateProductContract(sapPara.getS_MATNR(),
									sapPara.getMATNR(), sapPara.getOFFERNUMBER());

							// 修改专柜商品属性
							PullDataDto dataDto = createProductDto(sapPara); // 专柜商品信息
							PcmShoppeProductExtend extendDto = createExtendDto(sapPara); // 扩展表信息
							String shoppeProSid = sapPara.getS_MATNR(); // 已有专柜商品编码
							dataDto.setType("2");// 业态表示 0百货 1超市 2电商
							dataDto.setOfferNumber(sapPara.getOFFERNUMBER());
							PcmShoppeProduct result = pcmCreateProductService
									.updateSProductBySProductCode(dataDto, extendDto, shoppeProSid);
							if (result != null) {
								// 下发专柜商品
								publishDto = new PublishDTO();
								publishDto.setSid(result.getSid());
								publishDto.setType(Constants.PUBLIC_0);
								sidList.add(publishDto);
							}

							RedisVo vo2 = new RedisVo();
							vo2.setKey(sapPara.getS_MATNR());
							vo2.setField(DomainName.getShoppeInfo);
							vo2.setType(CacheUtils.HDEL);
							CacheUtils.setRedisData(vo2);
							// Map<String, Object> resMap = new HashMap<String,
							// Object>();
							// resMap.put("MATNR", sapPara.getMATNR());
							// resMap.put("SUCCESS", "true");
							// resMap.put("ERRORCODE", "");
							// resMap.put("ERRORMSG", "");
							// resList.add(resMap);
						} catch (BleException e) {
							Map<String, Object> resMap = new HashMap<String, Object>();
							resMap.put("KEY_FIELD", sapPara.getMATNR());
							// resMap.put("SUCCESS", "false");
							resMap.put("FLAG", 0);
							resMap.put("MESSAGE", e.getMessage());
							resList.add(resMap);
							excepList.add(resMap);
						} catch (Exception e) {
							Map<String, Object> resMap = new HashMap<String, Object>();
							resMap.put("KEY_FIELD", sapPara.getMATNR());
							resMap.put("FLAG", 0);
							resMap.put("MESSAGE", "数据库操作异常,商品已存在");
							resList.add(resMap);
							excepList.add(resMap);
						}
					} else {// 为空时添加商品
						PullDataDto dataDto = createProductDto(sapPara);
						PcmShoppeProductExtend extendDto = createExtendDto(sapPara);
						dataDto.setType("2");// 业态表示 0百货 1超市 2电商
						dataDto.setOfferNumber(sapPara.getOFFERNUMBER());
						try {
							PcmShoppeProduct result = validProductService.saveProductFromSAPERP(
									dataDto, extendDto);
							// Map<String, Object> resMap = new HashMap<String,
							// Object>();
							// resMap.put("MATNR", sapPara.getMATNR());
							// resMap.put("SUCCESS", "true");
							// resMap.put("ERRORCODE", "");
							// resMap.put("ERRORMSG", "");
							// resList.add(resMap);
							if (result != null) {
								// 下发专柜商品
								publishDto = new PublishDTO();
								publishDto.setSid(result.getSid());
								publishDto.setType(Constants.PUBLIC_0);
								sidList.add(publishDto);
								if (result.getPackUnitDictSid() != 0l) {
									// 下发SPU
									PublishDTO publishDtoSpu = new PublishDTO();
									publishDtoSpu.setSid(result.getPackUnitDictSid());
									publishDtoSpu.setType(Constants.PUBLIC_0);
									spusidList.add(publishDtoSpu);
								}
								if (result.getMeasureUnitDictSid() != 0l) {
									// 下发SKU
									PublishDTO publishDtoSku = new PublishDTO();
									publishDtoSku.setSid(result.getMeasureUnitDictSid());
									publishDtoSku.setType(Constants.PUBLIC_0);
									skusidList.add(publishDtoSku);
								}
								// 缓存处理
								RedisVo vo2 = new RedisVo();
								vo2.setKey("skuPage");
								vo2.setField(DomainName.getShoppeInfo);
								vo2.setType(CacheUtils.HDEL);
								CacheUtils.setRedisData(vo2);
							}
						} catch (BleException e) {
							if (ErrorCodeConstants.ErrorCode.vaildErrorCode(e.getCode())) {
								ThrowExcetpionUtil.splitExcetpion(new BleException(e.getCode(), e
										.getMessage()));
							}
							Map<String, Object> resMap = new HashMap<String, Object>();
							resMap.put("KEY_FIELD", sapPara.getMATNR());
							resMap.put("FLAG", 0);
							resMap.put("MESSAGE", e.getMessage());
							resList.add(resMap);
							excepList.add(resMap);
						} catch (Exception e) {
							Map<String, Object> resMap = new HashMap<String, Object>();
							resMap.put("KEY_FIELD", sapPara.getMATNR());
							resMap.put("FLAG", 0);
							resMap.put("MESSAGE", "数据库操作异常,商品已存在");
							resList.add(resMap);
							excepList.add(resMap);
						}
					}
				}
				// 下发专柜商品
				if (sidList != null && sidList.size() != 0
						&& FlagType.getPublish_info() == Constants.PUBLIC_0) {
					taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								logger.info("调用SYN服务下发专柜商品");
								// HttpUtil.doPost(
								// PropertyUtil.getSystemUrl("product.pushShoppeProduct"),
								// JsonUtil.getJSONString(sidList));
								Map<String, Object> paramMap = new HashMap<String, Object>();
								paramMap.put("paraList", sidList);
								paramMap.put("PcmSapErp", "1");// 电商
								paramMap.put("PcmEfuturePromotion", "1");// 促销
								paramMap.put("PcmProSearch", "1");
								HttpUtil.doPost(
										PropertyUtil.getSystemUrl("product.pushShoppeProduct"),
										JsonUtil.getJSONString(paramMap));
								if (skusidList != null && skusidList.size() != 0) {
									// 下发sku商品
									logger.info("调用SYN服务下发SKU");
									HttpUtil.doPost(
											PropertyUtil.getSystemUrl("product.pushSkuProduct"),
											JsonUtil.getJSONString(skusidList));
								}
								if (spusidList != null && spusidList.size() != 0) {
									// 下发spu商品
									logger.info("调用SYN服务下发SPU");
									HttpUtil.doPost(
											PropertyUtil.getSystemUrl("product.pushSpuProduct"),
											JsonUtil.getJSONString(spusidList));
								}
							} catch (Exception e) {
								ThrowExcetpionUtil.splitExcetpion(new BleException(
										ErrorCode.DOPOST_SYN_FAILED.getErrorCode(),
										ErrorCode.DOPOST_SYN_FAILED.getMemo()));
							}
						}
					});
				}
				// 写入异常表
				if (excepList.size() != 0) {
					PcmExceptionLogDto pcmExceptionLogDto = new PcmExceptionLogDto();
					pcmExceptionLogDto.setInterfaceName("saveProductFromSAPERP");
					pcmExceptionLogDto.setExceptionType(StatusCode.EXCEPTION_PRODUCT.getStatus());
					pcmExceptionLogDto.setErrorMessage(JsonUtil.getJSONString(excepList));
					pcmExceptionLogDto.setDataContent(JsonUtil.getJSONString(para));
					pcmExceptionLogDto.setUuid(UUID.randomUUID().toString());
					pcmExceptionLogService.saveExceptionLogInfo(pcmExceptionLogDto);
					// callBack
					// System.out.println(JsonUtil.getJSONString(resList));
					jcoUtils.functionExecute("ZFM_MD_PCM2SAP_ERROR_IN", "INPUT", resList);
				}
			}
		});
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(para.getHeader()));
	}

	/**
	 * 电商合同导入
	 * 
	 * @Methods Name saveProductFromSAPERP
	 * @Create In 2015年11月17日 By zhangxy
	 * @param para1
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/saveContractFromSAPERP", method = RequestMethod.POST)
	public String saveContractFromSAPERP(
			@RequestBody MqRequestDataListPara<SaveContractParaSAPERP> para2) {
		final MqRequestDataListPara<SaveContractParaSAPERP> paraf = para2;
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				List<SaveContractParaSAPERP> data2 = paraf.getData();

				// 返回信息LIST
				List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
				// 异常信息LIST
				List<Map<String, Object>> excepList = new ArrayList<Map<String, Object>>();
				// 下发LIST
				// sidList = new ArrayList<PublishDTO>();
				// skusidList = new ArrayList<PublishDTO>();
				// spusidList = new ArrayList<PublishDTO>();
				for (int l = 0; l < data2.size(); l++) {
					SaveContractParaSAPERP para = new SaveContractParaSAPERP();
					try {
						BeanUtils.copyProperties(para, data2.get(l));
					} catch (Exception e1) {
						logger.error("BeanUtils.copyProperties-Error" + para.toString());
					}
					// 写入/校验合同----------------------------------
					List<PcmContractLogDto> contractLogDtos = new ArrayList<PcmContractLogDto>();
					PcmContractLogDto contractDto = null;
					try {
						contractDto = createContractDto(para);
						/*
						 * if(StringUtils.isBlank(contractDto.getCol1())){ throw
						 * new BleException(ErrorCode.
						 * SAPERP_PCM_ERROR_MANAGECATEGORY.getErrorCode(),
						 * ErrorCode.SAPERP_PCM_ERROR_MANAGECATEGORY.getMemo());
						 * }
						 */
						contractLogDtos.add(contractDto);
						pcmContractLogService.uploadContractLogBatch(contractLogDtos);// 电商合同上传，第二个参数是SAP
						// Map<String, Object> resMap = new HashMap<String,
						// Object>();
						// resMap.put("CONTRACTCODE",
						// contractDto.getContractCode());
						// resMap.put("ERRORCODE", "");
						// resMap.put("ERRORMSG", "");
						// resList.add(resMap);
					} catch (BleException e1) {
						logger.error(e1.getMessage() + para.toString());
						Map<String, Object> resMap = new HashMap<String, Object>();
						resMap.put("KEY_FIELD", contractDto.getContractCode());
						resMap.put("FLAG", 6);
						resMap.put("MESSAGE", e1.getMessage());
						resList.add(resMap);
						excepList.add(resMap);
						continue;
					} catch (Exception e2) {
						logger.error(ErrorCode.PARA_NORULE_ERROR.getMemo() + para.toString());
						Map<String, Object> resMap = new HashMap<String, Object>();
						resMap.put("KEY_FIELD", contractDto.getContractCode());
						resMap.put("FLAG", 6);
						resMap.put("MESSAGE", ErrorCode.PARA_NORULE_ERROR.getMemo());
						resList.add(resMap);
						excepList.add(resMap);
						continue;
					}
				}
				// 写入异常表
				if (excepList.size() != 0) {
					PcmExceptionLogDto pcmExceptionLogDto = new PcmExceptionLogDto();
					pcmExceptionLogDto.setInterfaceName("saveProductFromSAPERP");
					pcmExceptionLogDto.setExceptionType(StatusCode.EXCEPTION_PRODUCT.getStatus());
					pcmExceptionLogDto.setErrorMessage(JsonUtil.getJSONString(excepList));
					pcmExceptionLogDto.setDataContent(JsonUtil.getJSONString(paraf));
					pcmExceptionLogDto.setUuid(UUID.randomUUID().toString());
					pcmExceptionLogService.saveExceptionLogInfo(pcmExceptionLogDto);
					// callBack
					// System.out.println(JsonUtil.getJSONString(resList));
					jcoUtils.functionExecute("ZFM_MD_PCM2SAP_ERROR_IN", "INPUT", resList);
				}
			}
		});
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(paraf.getHeader()));
	}

	/**
	 * 生成合同表DTO
	 * 
	 * @Methods Name createContractDto
	 * @Create In 2015年11月18日 By zhangxy
	 * @param para
	 * @return PcmContractLogDto
	 */
	private PcmContractLogDto createContractDto(SaveContractParaSAPERP para) {
		PcmContractLogDto pcmContractLogDto = new PcmContractLogDto();
		pcmContractLogDto.setStoreCode(para.getSTORECODE());
		pcmContractLogDto.setContractCode(para.getCONTRACTCODE());
		pcmContractLogDto.setBuyType(Integer.valueOf(para.getBUYTYPE()));
		pcmContractLogDto.setOperMode(Integer.valueOf(para.getOPERMODE()));
		pcmContractLogDto.setSupplyCode(para.getSUPPLIERCODE());
		pcmContractLogDto.setOptTime(new Date());
		pcmContractLogDto.setCol1(para.getGLFL());// 管理分类

		if ("E".equals(para.getBUSINESSTYPE())) {
			pcmContractLogDto.setBusinessType(0);
		}
		if ("C".equals(para.getBUSINESSTYPE())) {
			pcmContractLogDto.setBusinessType(2);
		}
		if (StringUtils.isNotBlank(para.getINPUTTAX())) {
			pcmContractLogDto.setInputTax(new BigDecimal(para.getINPUTTAX()));
		}
		if (StringUtils.isNotBlank(para.getOUTPUTTAX())) {
			pcmContractLogDto.setOutputTax(new BigDecimal(para.getOUTPUTTAX()));
		}
		if (StringUtils.isNotBlank(para.getCOMMISSIONRATE())) {
			pcmContractLogDto.setCommissionRate(new BigDecimal(para.getCOMMISSIONRATE()));
		}

		if ("A".equals(para.getACTION_CODE())) {
			pcmContractLogDto.setFlag(0);
		}
		if ("U".equals(para.getACTION_CODE())) {
			pcmContractLogDto.setFlag(1);
		}

		// 经营方式

		// 经销
		pcmContractLogDto.setManageType(Integer.valueOf(para.getMANAGETYPE()));
		return pcmContractLogDto;
	}

	/**
	 * 生成商品表DTO
	 * 
	 * @Methods Name createProductDto
	 * @Create In 2015年11月18日 By zhangxy
	 * @param para
	 * @return PcmContractLogDto
	 */
	private PullDataDto createProductDto(ProductsSAPERP para) {
		PullDataDto dto = new PullDataDto();
		dto.setManageCateGory(para.getGLFL());// 管理分类（9位）
		dto.setOperateMode(para.getMTART());// 经营方式
		dto.setProdCategoryCode(para.getMATKL());// 工业分类
		dto.setProductCode(para.getMATNR());// 商品编码
		dto.setProductDesc(para.getMAKTX());// 商品描述（短文本）
		dto.setSupplierCode(para.getLIFNR());// 供应商编码
		dto.setCounterCode(para.getZGID());// 专柜编码
		dto.setSupplyInnerCode(para.getZZVDMAT());// 供应商商品编码
		dto.setTmsParam(para.getZZWLLX());// 物流类型
		dto.setBrandCode(para.getZZBRAND_ID());// 集团品牌编码
		dto.setModelNum(para.getZZDKNO());// 供应商货号sss
		dto.setMarketPrice(para.getZZPRICE());// 市场价
		dto.setColorCode(para.getZZCOLORCODE());// 颜色码
		dto.setSizeCode(para.getZZSIZECODE());// 尺寸码
		if ("Y".equals(para.getZZCOD())) {
			dto.setIsCOD("0");// 可COD（Y/N）
		} else {
			dto.setIsCOD("1");// 可COD（Y/N）
		}
		if ("Y".equals(para.getZZPACK())) {
			dto.setIsPacking("0");// 可包装 (Y/N)
		} else {
			dto.setIsPacking("1");// 可包装 (Y/N)
		}
		if ("Y".equals(para.getMSTAV())) {
			dto.setIsSale("0");// 跨分销链商品状态(停售标记)（Y/N）
		} else {
			dto.setIsSale("1");// 跨分销链商品状态(停售标记)（Y/N）
		}
		dto.setCountryOfOrigin(para.getZLOCAL());// 原产地
		if ("Y".equals(para.getZLY_FLAG())) {
			dto.setStockMode("2");// 虚库标志（Y/N）
		} else if ("N".equals(para.getZLY_FLAG())) {
			dto.setStockMode("1");// 虚库标志（Y/N）
		}
		dto.setYearToMarket(para.getZSSDATE());// 上市日期（yyyymmdd）
		dto.setProductNum(para.getGOODCLASS());// 商品款号
		dto.setCrowdUser(para.getZZGENDER());// 适用性别
		if (StringUtils.isNotBlank(para.getTAXKM1()) && para.getTAXKM1().trim().indexOf("%") == -1) {
			dto.setOutputTax(para.getTAXKM1().trim() + "%");// 销项税
		}
		if (StringUtils.isNotBlank(para.getTAXKM2()) && para.getTAXKM2().trim().indexOf("%") == -1) {
			dto.setConsumptionTax(para.getTAXKM2().trim() + "%");// 消费税
		}
		if (StringUtils.isNotBlank(para.getTAXKM3()) && para.getTAXKM3().trim().indexOf("%") == -1) {
			dto.setInputTax(para.getTAXKM3().trim() + "%");// 进项税
		}
		dto.setSeasonCode(para.getSAISO());// 季节
		dto.setIsGift(para.getIS_GIFT());// 赠品范围
		dto.setStandardBarCode(para.getZZVDBC());// 条码
		dto.setSalePrice(para.getZSPRICE());// 售价
		dto.setProColor(para.getCOLORSID());// 色系
		dto.setUnitCode(para.getUNIT());// 销售单位
		dto.setFinalClassiFicationCode(para.getSCATE());
		// dto.setRate_price(para.getRATE_PRICE());// 扣率
		// dto.setEntryNumber(para.getACTIONPERSON());// 操作人-录入人员编号?
		// para.getZZXXHC_FLAG();// 先销后采(Y/N)
		// para.getZCOLOR();// 特性-颜色
		// para.getZSIZE();// 特性-尺码/规格
		// para.getMEINS();// 基本计量单位
		// para.getZZCARD();// 可贺卡 (Y/N)
		// para.getZLAND();// 原产国
		// para.getZZYCBZ();// 是否有原厂包装
		// para.getWERKS();// 工厂（门店）====================?
		// para.getZZPICCODE();// 照片编码====================?
		return dto;
	}

	/**
	 * 生成附加表DTO
	 * 
	 * @Methods Name createProductDto
	 * @Create In 2015年11月18日 By zhangxy
	 * @param para
	 * @return PcmContractLogDto
	 */
	private PcmShoppeProductExtend createExtendDto(ProductsSAPERP para) {
		PcmShoppeProductExtend dto = new PcmShoppeProductExtend();
		if ("Y".equals(para.getZZXXHC_FLAG())) {
			dto.setXxhcFlag("0");// 先销后采(Y/N)
		} else {
			dto.setXxhcFlag("1");// 先销后采(Y/N)
		}
		if ("Y".equals(para.getZZCARD())) {
			dto.setIsCard("0");// 可贺卡 (Y/N)
		} else {
			dto.setIsCard("1");// 可贺卡 (Y/N)
		}
		if ("Y".equals(para.getZZYCBZ())) {
			dto.setIsOriginPackage("0");// 是否有原厂包装
		} else {
			dto.setIsOriginPackage("1");// 是否有原厂包装
		}
		dto.setZcolor(para.getZCOLOR());// 特性-颜色
		dto.setZsize(para.getZSIZE());// 特性-尺码/规格
		dto.setBaseUnitCode(para.getMEINS());// 基本计量单位
		dto.setOriginCountry(para.getZLAND());// 原产国
		if ("0".equals(para.getMTART()) || "1".equals(para.getMTART())) {// 经销或代销时，扩展表商品类别为1自营单品
			dto.setField1("1");
		}
		if ("2".equals(para.getMTART())) {// 联营时，扩展表商品类别为8联营单品
			dto.setField1("8");
		}
		dto.setField2(para.getZZPRICE());// 原价
		return dto;
	}

}
