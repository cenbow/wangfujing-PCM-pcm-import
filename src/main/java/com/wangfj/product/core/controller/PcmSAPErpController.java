package com.wangfj.product.core.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangfj.core.constants.ErrorCodeConstants;
import com.wangfj.core.framework.base.controller.BaseController;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.core.utils.ThrowExcetpionUtil;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.impl.PcmExceptionLogService;
import com.wangfj.product.constants.JcoSAPUtils;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.core.controller.support.PcmSapErpChangePKPara;
import com.wangfj.product.core.controller.support.PcmSapErpChangeSkuPara;
import com.wangfj.product.maindata.domain.vo.ChangeProductDto;
import com.wangfj.product.maindata.domain.vo.ProSkuSpuPublishDto;
import com.wangfj.product.maindata.domain.vo.ProductCondDto;
import com.wangfj.product.maindata.domain.vo.ResultPullDataDto;
import com.wangfj.product.maindata.service.intf.IPcmChangeProductService;
import com.wangfj.product.maindata.service.intf.IPcmProductService;
import com.wangfj.product.maindata.service.intf.IPcmShoppeProductService;
import com.wangfj.util.Constants;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.MqUtil;
import com.wangfj.util.mq.PublishDTO;
import com.wangfj.util.mq.RequestHeader;

@Controller
@RequestMapping(value = "/SAPErp")
public class PcmSAPErpController extends BaseController {
	@Autowired
	private IPcmProductService spuService;
	@Autowired
	private IPcmShoppeProductService proService;
	@Autowired
	private IPcmChangeProductService changeService;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private PcmExceptionLogService pcmExceptionLogService;
	List<PublishDTO> sidList = null;
	List<PublishDTO> spuList = null;
	List<PublishDTO> skuList = null;
	List<PublishDTO> proList = null;

	/**
	 * SKU换色码（特性）/尺寸码（规格）
	 * 
	 * @Methods Name validUpdateSkuColorStan
	 * @Create In 2015年9月9日 By yedong
	 * @param para
	 * @param request
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping(value = "/updateProProp", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public String updateProProp(@RequestBody MqRequestDataListPara<PcmSapErpChangeSkuPara> para1,
			HttpServletRequest request) {
		final MqRequestDataListPara<PcmSapErpChangeSkuPara> para = para1;
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				RequestHeader header = para.getHeader();
				List<PcmSapErpChangeSkuPara> data = para.getData();
				List<PcmSapErpChangeSkuPara> listDataDto = new ArrayList<PcmSapErpChangeSkuPara>();
				for (int i = 0; i < data.size(); i++) {
					PcmSapErpChangeSkuPara dataDto = new PcmSapErpChangeSkuPara();
					try {
						BeanUtils.copyProperties(dataDto, data.get(i));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					listDataDto.add(dataDto);
				}
				ResultPullDataDto resDto = new ResultPullDataDto();
				// 回调信息LIST
				List<ResultPullDataDto> resList = new ArrayList<ResultPullDataDto>();
				// JCO返回
				List<Map<String, Object>> pushList1 = new ArrayList<Map<String, Object>>();
				// 异常信息LIST
				List<ResultPullDataDto> excepList = new ArrayList<ResultPullDataDto>();
				for (PcmSapErpChangeSkuPara pullDataDto : listDataDto) {
					try {
						ProductCondDto dto = new ProductCondDto();
						// dto.setShoppeProSid(paramMap.get("COUNTER_PROD_CODE").toString());
						// dto.setProColorName(paramMap.get("STYLE_CODE").toString());
						// dto.setProStanSid(paramMap.get("SIZE_CODE").toString());
						// dto.setProColorAlias(paramMap.get("COLOR_NAME").toString());
						dto.setShoppeProSid(pullDataDto.getCOUNTER_PROD_CODE());
						dto.setProColorName(pullDataDto.getSTYLE_CODE());
						dto.setProStanSid(pullDataDto.getSIZE_CODE());
						dto.setProColorAlias(pullDataDto.getCOLOR_NAME());
						ProSkuSpuPublishDto publishDto = spuService.updateSapColorStan(dto);
						proSkuSpuPublish(publishDto);
					} catch (BleException e) {
						if (ErrorCodeConstants.ErrorCode.vaildErrorCode(e.getCode())) {
							ThrowExcetpionUtil.splitExcetpion(new BleException(e.getCode(), e
									.getMessage()));
						}
						resDto.setMessageCode(Constants.PUBLIC_1);
						resDto.setMessageName(e.getCode() + " " + e.getMessage());
						excepList.add(resDto);
						resList.add(resDto);

						Map<String, Object> map = new HashMap<String, Object>();
						map.put("KEY_FIELD", "SAP_SKU");
						map.put("FLAG", "1");
						map.put("MESSAGE", e.getMessage());
						pushList1.add(map);
						// return ResultUtil.creComErrorResult(e.getCode(),
						// e.getMessage());
					}
				}
				try {
					String response = HttpUtil.doPost(header.getCallbackUrl(),
							JsonUtil.getJSONString(resList));
					JcoSAPUtils.functionExecute("ZFM_MD_PCM2SAP_ERROR_IN", "INPUT", pushList1);
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
	 * 换款/品牌-电商
	 * 
	 * @Methods Name updateProBrandKuan
	 * @Create In 2015年11月18日 By yedong
	 * @param paramMap
	 * @param request
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping(value = "/updateProBrandKuan", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public String updateProBrandKuan(
			@RequestBody MqRequestDataListPara<PcmSapErpChangePKPara> para1,
			HttpServletRequest request) {
		final MqRequestDataListPara<PcmSapErpChangePKPara> para = para1;
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				RequestHeader header = para.getHeader();
				List<PcmSapErpChangePKPara> data = para.getData();
				List<PcmSapErpChangePKPara> listDataDto = new ArrayList<PcmSapErpChangePKPara>();
				for (int i = 0; i < data.size(); i++) {
					PcmSapErpChangePKPara dataDto = new PcmSapErpChangePKPara();
					try {
						BeanUtils.copyProperties(dataDto, data.get(i));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					listDataDto.add(dataDto);
				}
				ResultPullDataDto resDto = new ResultPullDataDto();
				// 回调信息LIST
				List<ResultPullDataDto> resList = new ArrayList<ResultPullDataDto>();
				// JCO返回
				List<Map<String, Object>> pushList1 = new ArrayList<Map<String, Object>>();
				// 异常信息LIST
				List<ResultPullDataDto> excepList = new ArrayList<ResultPullDataDto>();
				// String billType = paramMap.get("XGLB").toString();
				// String billType = listDataDto.get(0).getXGLB();
				// 6、品牌/7、款号
				for (PcmSapErpChangePKPara pullDataDto : listDataDto) {
					String billType = pullDataDto.getXGLB();
					if (billType.equals("6")) {
						Map<String, Object> codeMap = new HashMap<String, Object>();
						// codeMap.put("proCode",
						// paramMap.get("PRODUCT").toString());
						// codeMap.put("brandCode",
						// paramMap.get("VALUE").toString());
						codeMap.put("proCode", pullDataDto.getPRODUCT());
						codeMap.put("brandCode", pullDataDto.getVALUE());
						Map<String, Object> sidMap = proService.getProAndBrandSidByCode(codeMap);
						ChangeProductDto changeProductDto = new ChangeProductDto();
						changeProductDto.setSid((Long) sidMap.get("proSid"));
						changeProductDto.setBrandSid((Long) sidMap.get("brandSid"));
						try {
							ProSkuSpuPublishDto publishDto = changeService
									.changeGroupBrand(changeProductDto);
							proSkuSpuPublish(publishDto);
						} catch (BleException e) {
							if (ErrorCodeConstants.ErrorCode.vaildErrorCode(e.getCode())) {
								ThrowExcetpionUtil.splitExcetpion(new BleException(e.getCode(), e
										.getMessage()));
							}
							resDto.setMessageCode(Constants.PUBLIC_1);
							resDto.setMessageName(e.getCode() + " " + e.getMessage());
							excepList.add(resDto);
							resList.add(resDto);

							Map<String, Object> map = new HashMap<String, Object>();
							map.put("KEY_FIELD", "SAP_SKU");
							map.put("FLAG", "2");
							map.put("MESSAGE", pullDataDto.getPRODUCT() + e.getMessage());
							pushList1.add(map);

						}
					} else {
						ProductCondDto condDto = new ProductCondDto();
						// condDto.setShoppeProSid(paramMap.get("PRODUCT").toString());
						// condDto.setProductSku(paramMap.get("VALUE").toString());
						condDto.setShoppeProSid(pullDataDto.getPRODUCT());
						condDto.setProductSku(pullDataDto.getVALUE());
						try {
							ProSkuSpuPublishDto publishDto = spuService.changeProductSku(condDto);
							proSkuSpuPublish(publishDto);
						} catch (BleException e) {
							if (ErrorCodeConstants.ErrorCode.vaildErrorCode(e.getCode())) {
								ThrowExcetpionUtil.splitExcetpion(new BleException(e.getCode(), e
										.getMessage()));
							}
							resDto.setMessageCode(Constants.PUBLIC_1);
							resDto.setMessageName(e.getCode() + " " + e.getMessage());
							excepList.add(resDto);
							resList.add(resDto);

							Map<String, Object> map = new HashMap<String, Object>();
							map.put("KEY_FIELD", "SAP_SKU");
							map.put("FLAG", "2");
							map.put("MESSAGE", pullDataDto.getPRODUCT() + e.getMessage());
							pushList1.add(map);
						}
					}
				}
				if (resList != null && resList.size() > 0) {
					PcmExceptionLogDto pcmExceptionLogDto = new PcmExceptionLogDto();
					pcmExceptionLogDto.setInterfaceName("SAP换款或品牌:" + header.getCallbackUrl());
					pcmExceptionLogDto.setExceptionType(StatusCode.EXCEPTION_PRODUCT.getStatus());
					pcmExceptionLogDto.setDataContent(JsonUtil.getJSONString(resList));
					pcmExceptionLogDto.setUuid(UUID.randomUUID().toString());
					pcmExceptionLogService.saveExceptionLogInfo(pcmExceptionLogDto);
				}
				try {
					JcoSAPUtils.functionExecute("ZFM_MD_PCM2SAP_ERROR_IN", "INPUT", pushList1);
					String response = HttpUtil.doPost(header.getCallbackUrl(),
							JsonUtil.getJSONString(resList));
				} catch (Exception e) {

				}
			}
		});
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(para.getHeader()));
	}

	public void proSkuSpuPublish(ProSkuSpuPublishDto publishDto) {
		spuList = new ArrayList<PublishDTO>();
		skuList = new ArrayList<PublishDTO>();
		proList = new ArrayList<PublishDTO>();

		spuList = publishDto.getSpuList();
		skuList = publishDto.getSkuList();
		proList = publishDto.getProList();
		if (spuList != null && spuList.size() != 0) {
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(JsonUtil.getJSONString(spuList) + "spu");
					HttpUtil.doPost(PropertyUtil.getSystemUrl("product.pushSpuProduct"),
							JsonUtil.getJSONString(spuList));
				}
			});
		}
		if (skuList != null && skuList.size() != 0) {
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(JsonUtil.getJSONString(skuList) + "sku");
					HttpUtil.doPost(PropertyUtil.getSystemUrl("product.pushSkuProduct"),
							JsonUtil.getJSONString(skuList));
				}
			});
		}
		if (proList != null && proList.size() != 0) {
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("paraList", proList);
					// paramMap.put("PcmEfutureERP", "1");// 门店
					paramMap.put("PcmProSearch", "1");// 搜索
					paramMap.put("PcmEfuturePromotion", "1");// 促销
					paramMap.put("PcmSapErp", "1");// SAP
					System.out.println(JsonUtil.getJSONString(paramMap) + "shoppeProduct");
					HttpUtil.doPost(PropertyUtil.getSystemUrl("product.pushShoppeProduct"),
							JsonUtil.getJSONString(paramMap));
				}
			});
		}

	}
}
