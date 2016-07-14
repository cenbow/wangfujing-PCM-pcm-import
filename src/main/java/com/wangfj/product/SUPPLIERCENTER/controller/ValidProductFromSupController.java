/**
 * @Probject Name: pcm-inner
 * @Path: com.wangfj.product.SUPPLIERCENTER.controllerValidProductController.java
 * @Create By wangc
 * @Create In 2016-3-30 上午9:50:18
 */
package com.wangfj.product.SUPPLIERCENTER.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

import com.wangfj.core.cache.RedisVo;
import com.wangfj.core.constants.ComErrorCodeConstants.ErrorCode;
import com.wangfj.core.constants.ErrorCodeConstants;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.CacheUtils;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.core.utils.ThrowExcetpionUtil;
import com.wangfj.product.PIS.controller.ValidProductController;
import com.wangfj.product.SUPPLIERCENTER.support.PullDataPara;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.impl.PcmExceptionLogService;
import com.wangfj.product.constants.DomainName;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.core.service.intf.IPcmMqService;
import com.wangfj.product.maindata.domain.entity.PcmShoppeProduct;
import com.wangfj.product.maindata.domain.vo.PullDataDto;
import com.wangfj.product.maindata.domain.vo.ResultPullDataForSupllierDto;
import com.wangfj.product.maindata.service.intf.IPcmShoppeProductService;
import com.wangfj.product.maindata.service.intf.IValidProductService;
import com.wangfj.util.Constants;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.MqUtil;
import com.wangfj.util.mq.PublishDTO;
import com.wangfj.util.mq.RequestHeader;
import com.wfj.exception.client.util.StringUtils;

/**
 * 供应商商品接口
 * @Class Name ValidProductController
 * @Author  wangc
 * @Create In 2016-3-30
 */
@Controller
@RequestMapping("/validProductFromSup")
public class ValidProductFromSupController {

	private static final Logger logger = LoggerFactory.getLogger(ValidProductController.class);
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private IValidProductService validProductService;
	@Autowired
	private IPcmShoppeProductService proService;
	@Autowired
	private IPcmMqService mqService;
	@Autowired
	private PcmExceptionLogService pcmExceptionLogService;
	
	List<PublishDTO> sidList = null;
	List<PublishDTO> skusidList = null;
	List<PublishDTO> spusidList = null;
	//电商商品下发LIST
	List<PublishDTO> sapSidList = null;
	//电商不带要约的下发
	List<PublishDTO> sapSidListOffernumIsNull = null;
	/**
	 * 供应商商品上传
	 * @Methods Name pullProductFromSupllier
	 * @Create In 2016-3-30 By wangc
	 * @param para1
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/pullProductFromSupllier", method = RequestMethod.POST)
	public String pullProductFromSupllier(@RequestBody MqRequestDataListPara<PullDataPara> para1) {
		final MqRequestDataListPara<PullDataPara> para = para1;
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				RequestHeader header = para.getHeader();
				List<PullDataPara> data = para.getData();
				List<PullDataDto> listDataDto = new ArrayList<PullDataDto>();
				for (int i = 0; i < data.size(); i++) {
					PullDataDto dataDto = new PullDataDto();
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
				List<ResultPullDataForSupllierDto> resList = new ArrayList<ResultPullDataForSupllierDto>();
				// 异常信息LIST
				List<ResultPullDataForSupllierDto> excepList = new ArrayList<ResultPullDataForSupllierDto>();
				// 下发LIST
				sidList = new ArrayList<PublishDTO>();
				skusidList = new ArrayList<PublishDTO>();
				spusidList = new ArrayList<PublishDTO>();
				// 电商下发LIST
				sapSidList = new ArrayList<PublishDTO>();
				// 不带要约下发
				sapSidListOffernumIsNull = new ArrayList<PublishDTO>();
				for (PullDataDto dataDto : listDataDto) {
					ResultPullDataForSupllierDto resDto = new ResultPullDataForSupllierDto();
					resDto.setLineNumber(dataDto.getLineNumber());
					try {
						PcmShoppeProduct result = validProductService
								.savePullProductFromSupllier(dataDto);
						if (result != null) {
							if(!"2".equals(dataDto.getType())){//非电商商品按之前下发
								resDto.setMessageCode(Constants.PUBLIC_0);
								resDto.setMessageName("商品添加成功");
								resDto.setProductCode(result.getShoppeProSid());// 专柜商品编码
								// 下发专柜商品
								PublishDTO publishDto = new PublishDTO();
								publishDto.setSid(result.getSid());
								publishDto.setType(Constants.PUBLIC_0);
								sidList.add(publishDto);
							}else{//电商商品下发电商和富基
								if(StringUtils.isBlank(dataDto.getOfferNumber())){
									//如果电商商品没有要约 , 则只下发到SAP
									resDto.setMessageCode(Constants.PUBLIC_0);
									resDto.setMessageName("商品添加成功");
									resDto.setProductCode(result.getShoppeProSid());// 专柜商品编码
									// 下发专柜商品
									PublishDTO publishDto = new PublishDTO();
									publishDto.setSid(result.getSid());
									publishDto.setType(Constants.PUBLIC_0);
									sapSidListOffernumIsNull.add(publishDto);
								}else{//如果有合同,则下发sap 搜索和future
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
						resDto.setImportNo(dataDto.getImportNo());//批次号
						excepList.add(resDto);
					} catch (Exception e) {
						resDto.setMessageCode(Constants.PUBLIC_1);
						resDto.setMessageName("数据库操作异常,商品已存在");
						resDto.setImportNo(dataDto.getImportNo());//批次号
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
								if(sapSidList != null && sapSidList.size() != 0){//电商商品下发
									Map<String, Object> pushMap = new HashMap<String, Object>();
									pushMap.put("paraList", sapSidList);
									pushMap.put("PcmSapErpSourcePis", "1");
									pushMap.put("PcmEfuturePromotionSourcePis", "1");
									pushMap.put("PcmProSearch", "1");
									HttpUtil.doPost(
											PropertyUtil.getSystemUrl("product.pushShoppeProduct"),
											JsonUtil.getJSONString(pushMap));
									/*logger.info("调用SYN服务下发电商商品条码");
									HttpUtil.doPost(
											PropertyUtil.getSystemUrl("product.pushBarcode"),
											JsonUtil.getJSONString(sapSidList));*/
								}
								//无合同商品 只下发sap
								if(sapSidListOffernumIsNull != null && sapSidListOffernumIsNull.size() != 0){
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
}
