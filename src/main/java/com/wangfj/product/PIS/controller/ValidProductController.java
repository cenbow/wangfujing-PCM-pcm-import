package com.wangfj.product.PIS.controller;

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
import com.wangfj.core.framework.base.controller.BaseController;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.CacheUtils;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.core.utils.ThrowExcetpionUtil;
import com.wangfj.product.PIS.controller.support.PullDataPara;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.impl.PcmExceptionLogService;
import com.wangfj.product.constants.DomainName;
import com.wangfj.product.constants.FlagType;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.core.service.intf.IPcmMqService;
import com.wangfj.product.maindata.domain.entity.PcmShoppeProduct;
import com.wangfj.product.maindata.domain.vo.PullDataDto;
import com.wangfj.product.maindata.domain.vo.ResultPullDataDto;
import com.wangfj.product.maindata.service.intf.IPcmShoppeProductService;
import com.wangfj.product.maindata.service.intf.IValidProductService;
import com.wangfj.util.Constants;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.MqUtil;
import com.wangfj.util.mq.PublishDTO;
import com.wangfj.util.mq.RequestHeader;

/**
 * 商品和库存准入导入的校验服务
 * 
 * @Class Name ValidProductController
 * @Author wangsy
 * @Create In 2015年7月14日
 */
@Controller
@RequestMapping("/validProduct")
public class ValidProductController extends BaseController {
	//
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

	//
	// /**
	// * 商品和库存准入导入前的校验服务接口
	// * <p>
	// * 层级验证SPU ↓ SKU ↓ 专柜商品
	// *
	// * @Methods Name validCounterVariantProduct
	// * @Create In 2015年7月14日 By wangsy
	// * @param validProductPara
	// * 需要验证的数据
	// * @param request
	// * @return String
	// */
	// @ResponseBody
	// @RequestMapping(value = "/getPISValidProductFromEfuture", method =
	// RequestMethod.POST, consumes = "application/json")
	// public String getPISValidProductFromEfuture(
	// @RequestBody @Valid ValidProductPara validProductPara, HttpServletRequest
	// request) {
	//
	// ValidProductDto validProductDto = new ValidProductDto();
	// List<ProductListPara> listProductPara =
	// validProductPara.getListProductPara();
	// List<ProductListDto> listProductDto = new ArrayList<ProductListDto>();
	// try {
	// BeanUtils.copyProperties(validProductDto, validProductPara);
	// for (ProductListPara productPara : listProductPara) {
	// ProductListDto productListDto = new ProductListDto();
	// BeanUtils.copyProperties(productListDto, productPara);
	// listProductDto.add(productListDto);
	// }
	// validProductDto.setListProductDto(listProductDto);
	// } catch (IllegalAccessException e) {
	// logger.error(e.getMessage());
	// return e.toString();
	// } catch (InvocationTargetException e) {
	// logger.error(e.getMessage());
	// return e.toString();
	// }
	// /* productCount满足200以内向下执行 */
	// if (Integer.valueOf(validProductDto.getProductCount()) <
	// Constants.VALIDPRODUCTDTO_PRODUCTCOUNT) {
	// try {
	// return JsonUtil.getJSONString(validProductService
	// .getPISValidProductFromEfuture(validProductDto));
	// } catch (Exception e) {
	// return JsonUtil.getJSONString(e.toString());
	// }
	// } else {
	// ValidResultDto vrd = new ValidResultDto();
	// vrd.setResCode(String.valueOf(Constants.PUBLIC_2));
	// vrd.setResMessage("productCount > " +
	// Constants.VALIDPRODUCTDTO_PRODUCTCOUNT);
	// vrd.setResCount(String.valueOf(Constants.PUBLIC_0));
	// vrd.setResList(null);
	// return JsonUtil.getJSONString(vrd);
	// }
	// }
	//
    //超市百货商品下发LIST
	List<PublishDTO> sidList = null;
	List<PublishDTO> skusidList = null;
	List<PublishDTO> spusidList = null;
	//电商商品下发LIST
	List<PublishDTO> sapSidList = null;
	/*List<PublishDTO> sapSkuSidList = null;
	List<PublishDTO> sapSpuSidList = null;*/

	/**
	 * 商品准入导入终端上传商品到主数据ERP
	 *
	 * @Methods Name pullProductFromEFuture
	 * @Create In 2015年7月15日 By wangsy
	 * @param @RequestBody MqRequestDataListPara<PullDataPara> para,
	 * @param request
	 * @return String
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping(value = "/pullProductFromYongliPIS", method = RequestMethod.POST)
	public String pullProductFromYongliPIS(@RequestBody MqRequestDataListPara<PullDataPara> para1) {
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
				List<ResultPullDataDto> resList = new ArrayList<ResultPullDataDto>();
				// 异常信息LIST
				List<ResultPullDataDto> excepList = new ArrayList<ResultPullDataDto>();
				// 超市百货下发LIST
				sidList = new ArrayList<PublishDTO>();
				skusidList = new ArrayList<PublishDTO>();
				spusidList = new ArrayList<PublishDTO>();
				// 电商下发LIST
				sapSidList = new ArrayList<PublishDTO>();
				/*sapSkuSidList = new ArrayList<PublishDTO>();
				sapSpuSidList = new ArrayList<PublishDTO>();*/
				for (PullDataDto dataDto : listDataDto) {
					ResultPullDataDto resDto = new ResultPullDataDto();
					resDto.setLineNumber(dataDto.getLineNumber());
					String type = dataDto.getType();//商品业态
					try {
						PcmShoppeProduct result = validProductService
								.savePullProductFromEFuture(dataDto);
						if(result != null){
							if (!"2".equals(type)) {//非电商商品按之前下发
								resDto.setMessageCode(Constants.PUBLIC_0);
								resDto.setMessageName("商品添加成功");
								resDto.setProductCode(result.getShoppeProSid());// 专柜商品编码
								// 下发专柜商品
								PublishDTO publishDto = new PublishDTO();
								publishDto.setSid(result.getSid());
								publishDto.setType(Constants.PUBLIC_0);
								sidList.add(publishDto);
							}else{//电商商品下发电商和富基
								resDto.setMessageCode(Constants.PUBLIC_0);
								resDto.setMessageName("商品添加成功");
								resDto.setProductCode(result.getShoppeProSid());// 专柜商品编码
								// 下发专柜商品
								PublishDTO publishDto = new PublishDTO();
								publishDto.setSid(result.getSid());
								publishDto.setType(Constants.PUBLIC_0);
								sapSidList.add(publishDto);
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
								if(sapSidList != null && sapSidList.size() != 0){//电商商品下发
									Map<String, Object> pushMap = new HashMap<String, Object>();
									pushMap.put("paraList", sapSidList);
									pushMap.put("PcmSapErpSourcePis", "1");
									pushMap.put("PcmEfuturePromotionSourcePis", "1");
									HttpUtil.doPost(
											PropertyUtil.getSystemUrl("product.pushShoppeProduct"),
											JsonUtil.getJSONString(pushMap));
									/*logger.info("调用SYN服务下发电商商品条码");
									HttpUtil.doPost(
											PropertyUtil.getSystemUrl("product.pushBarcode"),
											JsonUtil.getJSONString(sapSidList));*/
								}
								if (sidList != null && sidList.size() != 0) {//非电商商品下发
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
}
