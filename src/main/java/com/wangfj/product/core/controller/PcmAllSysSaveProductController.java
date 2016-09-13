/**
 * @Probject Name: pcm-import
 * @Path: com.wangfj.product.core.controllerPcmSaveProductController.java
 * @Create By wangc
 * @Create In 2016年6月15日 下午5:04:25
 * TODO
 */
package com.wangfj.product.core.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.wangfj.product.PIS.controller.ValidProductController;
import com.wangfj.product.SAPERP.controller.support.ProductsSAPERP;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.impl.PcmExceptionLogService;
import com.wangfj.product.common.service.intf.IJcoSAPUtil;
import com.wangfj.product.constants.DomainName;
import com.wangfj.product.constants.FlagType;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.core.controller.support.PcmAllSysPullDataPara;
import com.wangfj.product.maindata.domain.entity.PcmShoppeProduct;
import com.wangfj.product.maindata.domain.entity.PcmShoppeProductExtend;
import com.wangfj.product.maindata.domain.vo.PcmAllSysPullDataDto;
import com.wangfj.product.maindata.domain.vo.PullDataDto;
import com.wangfj.product.maindata.domain.vo.ResultPullDataDto;
import com.wangfj.product.maindata.domain.vo.ResultPullDataForSupllierDto;
import com.wangfj.product.maindata.service.intf.IPcmAllSysPullProductSevice;
import com.wangfj.product.maindata.service.intf.IPcmCreateProductService;
import com.wangfj.product.maindata.service.intf.IValidProductService;
import com.wangfj.util.Constants;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.MqUtil;
import com.wangfj.util.mq.PublishDTO;
import com.wangfj.util.mq.RequestHeader;
import com.wfj.exception.client.util.StringUtils;

/**
 * @Class Name PcmSaveProductController
 * @Author wangc
 * @Create In 2016年6月15日
 */
@Controller
@RequestMapping("/allSysPullProData")
public class PcmAllSysSaveProductController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ValidProductController.class);
	@Autowired
	private IPcmAllSysPullProductSevice allSysPullProductService;
	/**
	 * 导入终端 +供应商平台
	 */
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private IValidProductService validProductService;
	@Autowired
	private PcmExceptionLogService pcmExceptionLogService;
	/**
	 * 电商ERP
	 */
	@Autowired
	private IPcmCreateProductService pcmCreateProductService;
	@Autowired
	private IJcoSAPUtil jcoUtils;

	/**
	 * 导入终端上传商品
	 * 
	 * @Methods Name pullProductFromYongliPIS
	 * @Create In 2016年6月15日 By wangc
	 * @param para1
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/pullProductFromYongliPIS", method = RequestMethod.POST)
	public String pullProductFromYongliPIS(
			@RequestBody MqRequestDataListPara<PcmAllSysPullDataPara> para1) {
		final MqRequestDataListPara<PcmAllSysPullDataPara> para = para1;
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				RequestHeader header = para.getHeader();
				List<PcmAllSysPullDataPara> data = para.getData();
				List<PcmAllSysPullDataDto> listDataDto = new ArrayList<PcmAllSysPullDataDto>();
				for (int i = 0; i < data.size(); i++) {
					PcmAllSysPullDataDto dataDto = new PcmAllSysPullDataDto();
					try {
						BeanUtils.copyProperties(dataDto, data.get(i));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					listDataDto.add(dataDto);
				}

				// 超市百货商品下发LIST
				final List<PublishDTO> sidList = new ArrayList<PublishDTO>();
				final List<PublishDTO> skusidList = new ArrayList<PublishDTO>();
				final List<PublishDTO> spusidList = new ArrayList<PublishDTO>();
				// 电商商品下发LIST
				final List<PublishDTO> sapSidList = new ArrayList<PublishDTO>();
				// 不带要约的电商商品下发
				final List<PublishDTO> sapSidListOffernumIsNull = new ArrayList<PublishDTO>();
				// 回调信息LIST
				final List<ResultPullDataDto> resList = new ArrayList<ResultPullDataDto>();
				// 异常信息LIST
				final List<ResultPullDataDto> excepList = new ArrayList<ResultPullDataDto>();
				System.out
						.println("------------------------------------------------导入终端---------------------------------------------------");
				System.out.println(sapSidList.toString());
				System.out
						.println("-----------------------------------------------------------------------------------------------------------");
				for (PcmAllSysPullDataDto dataDto : listDataDto) {
					ResultPullDataDto resDto = new ResultPullDataDto();
					resDto.setLineNumber(dataDto.getLineNumber());
					String type = dataDto.getType();// 商品业态
					try {
						dataDto.setSource("PIS");
						PcmShoppeProduct result = allSysPullProductService.allSysSaveProduct(
								dataDto, null);
						if (result != null) {
							if (!"2".equals(type)) {// 非电商商品按之前下发
								resDto.setMessageCode(Constants.PUBLIC_0);
								resDto.setMessageName("商品添加成功");
								resDto.setProductCode(result.getShoppeProSid());// 专柜商品编码
								// 下发专柜商品
								PublishDTO publishDto = new PublishDTO();
								publishDto.setSid(result.getSid());
								publishDto.setType(Constants.PUBLIC_0);
								sidList.add(publishDto);
							} else {// 电商商品下发电商和富基
								if (StringUtils.isBlank(dataDto.getOfferNumber())) {
									// 如果电商商品没有要约 , 则只下发到SAP
									resDto.setMessageCode(Constants.PUBLIC_0);
									resDto.setMessageName("商品添加成功");
									resDto.setProductCode(result.getShoppeProSid());// 专柜商品编码
									// 下发专柜商品
									PublishDTO publishDto = new PublishDTO();
									publishDto.setSid(result.getSid());
									publishDto.setType(Constants.PUBLIC_0);
									sapSidListOffernumIsNull.add(publishDto);
								} else {// 如果有合同,则下发sap 搜索和future
									resDto.setMessageCode(Constants.PUBLIC_0);
									resDto.setMessageName("商品添加成功");
									resDto.setProductCode(result.getShoppeProSid());// 专柜商品编码
									// 下发专柜商品
									PublishDTO publishDto = new PublishDTO();
									publishDto.setSid(result.getSid());
									publishDto.setType(Constants.PUBLIC_0);
									sapSidList.add(publishDto);
								}

							}
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
						resDto.setMessageCode(Constants.PUBLIC_1);
						resDto.setMessageName(e.getCode() + " " + e.getMessage());
						excepList.add(resDto);
					} catch (Exception e) {
						resDto.setMessageCode(Constants.PUBLIC_1);
						resDto.setMessageName("商品已存在");
						excepList.add(resDto);
					}
					resList.add(resDto);
				}

				// 下发专柜商品
				if (sidList != null && sidList.size() != 0
						&& FlagType.getPublish_info() == Constants.PUBLIC_0) {
					taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								logger.info("调用SYN服务下发专柜商品");
								if (sapSidList != null && sapSidList.size() != 0) {// 电商商品下发
									Map<String, Object> pushMap = new HashMap<String, Object>();
									pushMap.put("paraList", sapSidList);
									pushMap.put("PcmSapErpSourcePis", "1");
									pushMap.put("PcmEfuturePromotionSourcePis", "1");
									pushMap.put("PcmProSearch", "1");
									HttpUtil.doPost(
											PropertyUtil.getSystemUrl("product.pushShoppeProduct"),
											JsonUtil.getJSONString(pushMap));
								}
								// 无合同商品 只下发sap
								if (sapSidListOffernumIsNull != null
										&& sapSidListOffernumIsNull.size() != 0) {
									Map<String, Object> pushMap = new HashMap<String, Object>();
									pushMap.put("paraList", sapSidListOffernumIsNull);
									pushMap.put("PcmSapErpSourcePis", "1");
									pushMap.put("PcmProSearch", "1");
									HttpUtil.doPost(
											PropertyUtil.getSystemUrl("product.pushShoppeProduct"),
											JsonUtil.getJSONString(pushMap));
								}
								if (sidList != null && sidList.size() != 0) {// 非电商商品下发
									Map<String, Object> pushMap = new HashMap<String, Object>();
									pushMap.put("paraList", sidList);
									pushMap.put("PcmEfutureERP", "1");
									pushMap.put("PcmEfuturePromotion", "1");
									pushMap.put("PcmSearcherOffline", "1");
									pushMap.put("PcmProSearch", "1");
									HttpUtil.doPost(
											PropertyUtil.getSystemUrl("product.pushShoppeProduct"),
											JsonUtil.getJSONString(pushMap));
								}
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
					pcmExceptionLogDto.setInterfaceName("pullProductFromYongliPIS");
					pcmExceptionLogDto.setExceptionType(StatusCode.EXCEPTION_PRODUCT.getStatus());
					pcmExceptionLogDto.setErrorMessage(JsonUtil.getJSONString(excepList));
					pcmExceptionLogDto.setDataContent(JsonUtil.getJSONString(para));
					pcmExceptionLogDto.setUuid(UUID.randomUUID().toString());
					pcmExceptionLogService.saveExceptionLogInfo(pcmExceptionLogDto);
				}
				// callBack
				try {
					String response = HttpUtil.doPost(header.getCallbackUrl(),
							JsonUtil.getJSONString(resList));
					logger.info(response);
				} catch (Exception e) {
					PcmExceptionLogDto pcmExceptionLogDto = new PcmExceptionLogDto();
					pcmExceptionLogDto.setInterfaceName("商品导入回调导入终端:" + header.getCallbackUrl());
					pcmExceptionLogDto.setExceptionType(StatusCode.EXCEPTION_PRODUCT.getStatus());
					pcmExceptionLogDto.setErrorMessage(e.getMessage());
					pcmExceptionLogDto.setDataContent(JsonUtil.getJSONString(resList));
					pcmExceptionLogDto.setUuid(UUID.randomUUID().toString());
					pcmExceptionLogService.saveExceptionLogInfo(pcmExceptionLogDto);
				}
			}
		});
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(para.getHeader()));
	}

	/**
	 * 供应商商品上传
	 * 
	 * @Methods Name pullProductFromSupllier
	 * @Create In 2016年6月15日 By wangc
	 * @param para1
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/pullProductFromSupllier", method = RequestMethod.POST)
	public String pullProductFromSupllier(
			@RequestBody MqRequestDataListPara<PcmAllSysPullDataPara> para1) {
		final MqRequestDataListPara<PcmAllSysPullDataPara> para = para1;
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				RequestHeader header = para.getHeader();
				List<PcmAllSysPullDataPara> data = para.getData();
				List<PcmAllSysPullDataDto> listDataDto = new ArrayList<PcmAllSysPullDataDto>();
				for (int i = 0; i < data.size(); i++) {
					PcmAllSysPullDataDto dataDto = new PcmAllSysPullDataDto();
					try {
						BeanUtils.copyProperties(dataDto, data.get(i));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					listDataDto.add(dataDto);
				}

				// 回调信息LIST
				final List<ResultPullDataForSupllierDto> resList = new ArrayList<ResultPullDataForSupllierDto>();
				// 异常信息LIST
				final List<ResultPullDataForSupllierDto> excepList = new ArrayList<ResultPullDataForSupllierDto>();
				// 超市百货商品下发LIST
				final List<PublishDTO> sidList = new ArrayList<PublishDTO>();
				final List<PublishDTO> skusidList = new ArrayList<PublishDTO>();
				final List<PublishDTO> spusidList = new ArrayList<PublishDTO>();
				// 电商商品下发LIST
				final List<PublishDTO> sapSidList = new ArrayList<PublishDTO>();
				// 不带要约的电商商品下发
				final List<PublishDTO> sapSidListOffernumIsNull = new ArrayList<PublishDTO>();
				for (int i = 1; i < 10; i++) {
					PublishDTO dt = new PublishDTO();
					dt.setSid(Long.valueOf(i));
					sapSidList.add(dt);
				}
				System.out
						.println("------------------------------------------------供应商平台---------------------------------------------------");
				System.out.println(sapSidList.toString());
				System.out
						.println("-----------------------------------------------------------------------------------------------------------");
				for (PcmAllSysPullDataDto dataDto : listDataDto) {
					ResultPullDataForSupllierDto resDto = new ResultPullDataForSupllierDto();
					resDto.setLineNumber(dataDto.getLineNumber());
					try {
						dataDto.setSource("SUP");
						PcmShoppeProduct result = allSysPullProductService.allSysSaveProduct(
								dataDto, null);
						if (result != null) {
							if (!"2".equals(dataDto.getType())) {// 非电商商品按之前下发
								resDto.setMessageCode(Constants.PUBLIC_0);
								resDto.setMessageName("商品添加成功");
								resDto.setProductCode(result.getShoppeProSid());// 专柜商品编码
								// 下发专柜商品
								PublishDTO publishDto = new PublishDTO();
								publishDto.setSid(result.getSid());
								publishDto.setType(Constants.PUBLIC_0);
								sidList.add(publishDto);
							} else {// 电商商品下发电商和富基
								if (StringUtils.isBlank(dataDto.getOfferNumber())) {
									// 如果电商商品没有要约 , 则只下发到SAP
									resDto.setMessageCode(Constants.PUBLIC_0);
									resDto.setMessageName("商品添加成功");
									resDto.setProductCode(result.getShoppeProSid());// 专柜商品编码
									// 下发专柜商品
									PublishDTO publishDto = new PublishDTO();
									publishDto.setSid(result.getSid());
									publishDto.setType(Constants.PUBLIC_0);
									sapSidListOffernumIsNull.add(publishDto);
								} else {// 如果有合同,则下发sap 搜索和future
									resDto.setMessageCode(Constants.PUBLIC_0);
									resDto.setMessageName("商品添加成功");
									resDto.setProductCode(result.getShoppeProSid());// 专柜商品编码
									// 下发专柜商品
									PublishDTO publishDto = new PublishDTO();
									publishDto.setSid(result.getSid());
									publishDto.setType(Constants.PUBLIC_0);
									sapSidList.add(publishDto);
								}
							}
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
						resDto.setMessageCode(Constants.PUBLIC_1);
						resDto.setMessageName(e.getCode() + " " + e.getMessage());
						resDto.setImportNo(dataDto.getImportNo());// 批次号
						excepList.add(resDto);
					}
					resList.add(resDto);
				}

				// 下发专柜商品
				if (sidList != null && sidList.size() != 0) {
					taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								logger.info("调用SYN服务下发专柜商品");
								if (sapSidList != null && sapSidList.size() != 0) {// 电商商品下发
									Map<String, Object> pushMap = new HashMap<String, Object>();
									pushMap.put("paraList", sapSidList);
									pushMap.put("PcmSapErpSourcePis", "1");
									pushMap.put("PcmEfuturePromotionSourcePis", "1");
									pushMap.put("PcmProSearch", "1");
									HttpUtil.doPost(
											PropertyUtil.getSystemUrl("product.pushShoppeProduct"),
											JsonUtil.getJSONString(pushMap));
								}
								// 无合同商品 只下发sap
								if (sapSidListOffernumIsNull != null
										&& sapSidListOffernumIsNull.size() != 0) {
									Map<String, Object> pushMap = new HashMap<String, Object>();
									pushMap.put("paraList", sapSidListOffernumIsNull);
									pushMap.put("PcmSapErpSourcePis", "1");
									pushMap.put("PcmProSearch", "1");
									HttpUtil.doPost(
											PropertyUtil.getSystemUrl("product.pushShoppeProduct"),
											JsonUtil.getJSONString(pushMap));
								}
								if (sidList != null && sidList.size() != 0) {
									Map<String, Object> pushMap = new HashMap<String, Object>();
									pushMap.put("paraList", sidList);
									pushMap.put("PcmEfutureERP", "1");
									pushMap.put("PcmEfuturePromotion", "1");
									pushMap.put("PcmSearcherOffline", "1");
									HttpUtil.doPost(
											PropertyUtil.getSystemUrl("product.pushShoppeProduct"),
											JsonUtil.getJSONString(pushMap));
								}
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
					pcmExceptionLogDto.setInterfaceName("pullProductFromSupllier");
					pcmExceptionLogDto.setExceptionType(StatusCode.EXCEPTION_PRODUCT.getStatus());
					pcmExceptionLogDto.setErrorMessage(JsonUtil.getJSONString(excepList));
					pcmExceptionLogDto.setDataContent(JsonUtil.getJSONString(para));
					pcmExceptionLogDto.setUuid(UUID.randomUUID().toString());
					pcmExceptionLogService.saveExceptionLogInfo(pcmExceptionLogDto);
				}
				// callBack
				try {
					String response = HttpUtil.doPost(header.getCallbackUrl(),
							JsonUtil.getJSONString(resList));
					logger.info(response);
				} catch (Exception e) {
					PcmExceptionLogDto pcmExceptionLogDto = new PcmExceptionLogDto();
					pcmExceptionLogDto.setInterfaceName("商品导入回调供应商平台:" + header.getCallbackUrl());
					pcmExceptionLogDto.setExceptionType(StatusCode.EXCEPTION_PRODUCT.getStatus());
					pcmExceptionLogDto.setErrorMessage(e.getMessage());
					pcmExceptionLogDto.setDataContent(JsonUtil.getJSONString(resList));
					pcmExceptionLogDto.setUuid(UUID.randomUUID().toString());
					pcmExceptionLogService.saveExceptionLogInfo(pcmExceptionLogDto);
				}
			}
		});
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(para.getHeader()));
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
				for (int i = 0; i < data.size(); i++) {
					ProductsSAPERP sapPara = new ProductsSAPERP();
					try {
						BeanUtils.copyProperties(sapPara, data.get(i));
					} catch (Exception e1) {
						logger.error("BeanUtils.copyProperties-Error" + para.toString());
					}
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
							PullDataDto dataDto = createProductDto1(sapPara); // 专柜商品信息
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
							resMap.put("MESSAGE", "数据库操作异常,商品重复");
							resList.add(resMap);
							excepList.add(resMap);
						}
					} else {// 为空时添加商品
						PcmAllSysPullDataDto dataDto = createProductDto(sapPara);
						PcmShoppeProductExtend extendDto = createExtendDto(sapPara);
						dataDto.setType("2");// 业态表示 0百货 1超市 2电商
						dataDto.setOfferNumber(sapPara.getOFFERNUMBER());
						try {
							PcmShoppeProduct result = allSysPullProductService.allSysSaveProduct(
									dataDto, extendDto);
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
							resMap.put("MESSAGE", "数据库操作异常,商品重复");
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
					jcoUtils.functionExecute("ZFM_MD_PCM2SAP_ERROR_IN", "INPUT", resList);
				}
			}
		});
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(para.getHeader()));
	}

	/**
	 * 生成商品表DTO
	 * 
	 * @Methods Name createProductDto
	 * @Create In 2015年11月18日 By zhangxy
	 * @param para
	 * @return PcmContractLogDto
	 */
	private PcmAllSysPullDataDto createProductDto(ProductsSAPERP para) {
		PcmAllSysPullDataDto dto = new PcmAllSysPullDataDto();
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
		dto.setYearToMarket(para.getZZSSDATE());// 上市日期（yyyymmdd）
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
		dto.setFinalClassiFicationCode(para.getSCATE());// 统计分类
		dto.setSource("SAP");
		return dto;
	}

	private PullDataDto createProductDto1(ProductsSAPERP para) {
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
		dto.setYearToMarket(para.getZZSSDATE());// 上市日期（yyyymmdd）
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
