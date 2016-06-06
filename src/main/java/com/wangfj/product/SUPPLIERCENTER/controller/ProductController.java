/**
 * @Probject Name: pcm-import
 * @Path: com.wangfj.product.SUPPLIERCENTER.controllerProductController.java
 * @Create By wangc
 * @Create In 2016-2-25 下午7:07:41
 * TODO
 */
package com.wangfj.product.SUPPLIERCENTER.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.wangfj.product.PIS.controller.support.PullDataPara;
import com.wangfj.product.SUPPLIERCENTER.controller.support.SupplierBarCodeFromSupPara;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.impl.PcmExceptionLogService;
import com.wangfj.product.constants.DomainName;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.maindata.domain.entity.PcmShoppeProduct;
import com.wangfj.product.maindata.domain.vo.PullDataDto;
import com.wangfj.product.maindata.domain.vo.ResultPullDataDto;
import com.wangfj.product.supplier.domain.vo.SupplierBarCodeFromSupDto;
import com.wangfj.product.supplier.service.intf.IPcmBarcodeSupService;
import com.wangfj.product.supplier.service.intf.IPcmUploadProductService;
import com.wangfj.util.Constants;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.MqUtil;
import com.wangfj.util.mq.PublishDTO;
import com.wangfj.util.mq.RequestHeader;

/**
 * 供应商商品接口
 * 
 * @Class Name ProductController
 * @Author wangc
 * @Create In 2016-2-25
 */
@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	List<PublishDTO> sidList = null;
	List<PublishDTO> skusidList = null;
	List<PublishDTO> spusidList = null;

	@Autowired
	private IPcmBarcodeSupService pcmBarcodeSupService;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private IPcmUploadProductService pcmUploadProductService;

	@Autowired
	private PcmExceptionLogService pcmExceptionLogService;

	/**
	 * 供应商商品上传（批量&单一）
	 * 
	 * @Methods Name uploadShoProductFromSup
	 * @Create In 2016-2-25 By wangc
	 * @param para
	 * @return String
	 */
	@RequestMapping(value = "/uploadShoProductFromSup", method = RequestMethod.POST)
	@ResponseBody
	public String uploadShoProductFromSup(@RequestBody MqRequestDataListPara<PullDataPara> paras) {
		final MqRequestDataListPara<PullDataPara> para = paras;
		// 创建进程
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
				List<ResultPullDataDto> resList = new ArrayList<ResultPullDataDto>();
				// 异常信息LIST
				List<ResultPullDataDto> excepList = new ArrayList<ResultPullDataDto>();
				// 下发LIST
				sidList = new ArrayList<PublishDTO>();
				skusidList = new ArrayList<PublishDTO>();
				spusidList = new ArrayList<PublishDTO>();
				for (PullDataDto dataDto : listDataDto) {
					ResultPullDataDto resDto = new ResultPullDataDto();
					resDto.setLineNumber(dataDto.getLineNumber());
					try {
						if (StringUtils.isBlank(dataDto.getUnitCode())) {
							dataDto.setUnitCode(dataDto.getBaseUnitCode());// 供应商基本计量单位= 销售单位
						}
						if (StringUtils.isBlank(dataDto.getType())) {
							dataDto.setType(dataDto.getBusinessType());// 业态
						}
						if (StringUtils.isBlank(dataDto.getSupplyInnerCode())) {
							dataDto.setSupplyInnerCode(dataDto.getSupProCode());// 供应商商品编码
						}
						if (StringUtils.isBlank(dataDto.getRate_price())) {
							dataDto.setRate_price(dataDto.getDiscount());// 扣率
						}
						if (StringUtils.isBlank(dataDto.getErpProductCode())) {
							dataDto.setErpProductCode(dataDto.getDiscountCode());// 大码=扣率码
						}
						PcmShoppeProduct result = pcmUploadProductService
								.uploadProductFromSup(dataDto);
						if (result != null) {
							resDto.setMessageCode(Constants.PUBLIC_0);
							resDto.setMessageName("商品添加成功");
							resDto.setProductCode(result.getShoppeProSid());// 专柜商品编码
							// 下发非电商专柜商品
							if (!"2".equals(dataDto.getType())
									|| !"2".equals(dataDto.getBusinessType())) {
								PublishDTO publishDto = new PublishDTO();
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
								if (sidList != null && sidList.size() != 0) {
									Map<String, Object> pushMap = new HashMap<String, Object>();
									pushMap.put("paraList", sidList);
									pushMap.put("PcmEfutureERP", "1");
									pushMap.put("PcmProSearch", "1");
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
	 * 供应商条码上传
	 * 
	 * @Methods Name uploadSupplierBarCodeFromEfuture
	 * @Create In 2016-3-1 By wangc
	 * @param para1
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = "/uploadSupplierBarCodeFromSup", method = RequestMethod.POST)
	@ResponseBody
	public String uploadSupplierBarCodeFromEfuture(
			@RequestBody MqRequestDataListPara<SupplierBarCodeFromSupPara> para1,
			HttpServletRequest request) {
		final MqRequestDataListPara<SupplierBarCodeFromSupPara> para = para1;
		// JSONObject jsono = JSONObject.fromObject(para.getData());
		List<SupplierBarCodeFromSupPara> listPara = para.getData();
		List<SupplierBarCodeFromSupPara> listparas = new ArrayList<SupplierBarCodeFromSupPara>();
		for (int i = 0; i < listPara.size(); i++) {
			SupplierBarCodeFromSupPara paras = new SupplierBarCodeFromSupPara();
			try {
				BeanUtils.copyProperties(paras, listPara.get(i));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			listparas.add(paras);
		}
		List<String> excepList = new ArrayList<String>();
		Collections.sort(listparas, new Comparator<SupplierBarCodeFromSupPara>() {
			public int compare(SupplierBarCodeFromSupPara m1, SupplierBarCodeFromSupPara m2) {
				if ("D".equals(m1.getActionCode()) && "A".equals(m2.getActionCode())) {
					return -1;
				} else if ("A".equals(m1.getActionCode()) && "D".equals(m2.getActionCode())) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		for (int i = 0; i < listPara.size(); i++) {
			SupplierBarCodeFromSupPara paras = new SupplierBarCodeFromSupPara();
			try {
				BeanUtils.copyProperties(paras, listPara.get(i));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			SupplierBarCodeFromSupDto pslDto = new SupplierBarCodeFromSupDto();
			pslDto.setActionCode(paras.getActionCode());// 操作类型
			pslDto.setActionDate(paras.getActionDate());// 操作时间
			pslDto.setActionPerson(paras.getActionPerson());// 操作人
			pslDto.setCounterCode(paras.getCOUNTERCODE());// 专柜编码
			pslDto.setLifnr(paras.getLIFNR());// 供应商编码
			pslDto.setMatnr(paras.getMATNR());// 商品erp编码
			pslDto.setSaleAmount(paras.getSALEAMOUNT());// 多包装的含量
			pslDto.setSalePrice(paras.getSALEPRICE());// 售价
			pslDto.setSaleUnit(paras.getSALEUNIT());// 销售单位
			pslDto.setSbarcode(paras.getSBARCODE());// 供应商商品条码
			pslDto.setSbarcodeName(paras.getSBARCODENAME());// 条码名称
			pslDto.setSbarcodeType(paras.getSBARCODETYPE());// 条码类型
			pslDto.setStoreCode(paras.getSTORECODE());// 门店编码
			pslDto.setOriginLand(paras.getORIGINLAND());// 产地
			if (pslDto.getActionCode() != null) {
				try {
					pcmBarcodeSupService.getSupplierBarCodeFromSup(pslDto);
				} catch (BleException e) {
					if (ErrorCodeConstants.ErrorCode.vaildErrorCode(e.getCode())) {
						ThrowExcetpionUtil.splitExcetpion(new BleException(e.getCode(), e
								.getMessage()));
					}
					excepList.add(pslDto.getStoreCode() + "-" + pslDto.getSbarcode() + ":"
							+ e.toString());
				}
			} else {
				excepList.add(pslDto.getStoreCode() + "-" + pslDto.getSbarcode()
						+ "--ActionCode不能为空");
			}
		}
		// 写入异常表
		if (excepList != null && excepList.size() > 0) {
			PcmExceptionLogDto pcmExceptionLogDto = new PcmExceptionLogDto();
			pcmExceptionLogDto.setInterfaceName("uploadSupplierBarCodeFromEfuture");
			pcmExceptionLogDto.setExceptionType(StatusCode.EXCEPTION_PRODUCT.getStatus());
			pcmExceptionLogDto.setErrorMessage(JsonUtil.getJSONString(excepList));
			pcmExceptionLogDto.setDataContent(JsonUtil.getJSONString(para));
			pcmExceptionLogService.saveExceptionLogInfo(pcmExceptionLogDto);
		}
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(para.getHeader()));
	}
}
