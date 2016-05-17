package com.wangfj.product.EfutureERP.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangfj.core.constants.ComErrorCodeConstants.ErrorCode;
import com.wangfj.core.constants.ErrorCodeConstants;
import com.wangfj.core.framework.base.controller.BaseController;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.core.utils.ThrowExcetpionUtil;
import com.wangfj.product.EfutureERP.controller.support.ErpChangePara;
import com.wangfj.product.EfutureERP.controller.support.PcmLimitPara;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.intf.IPcmErpTestService;
import com.wangfj.product.common.service.intf.IPcmExceptionLogService;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.maindata.domain.entity.PcmShoppeProduct;
import com.wangfj.product.maindata.domain.vo.ErpChangeDto;
import com.wangfj.product.maindata.domain.vo.GetErpProductFromEfutureDto;
import com.wangfj.product.maindata.domain.vo.ModifyErpProductDto;
import com.wangfj.product.maindata.domain.vo.ProSkuSpuPublishDto;
import com.wangfj.product.maindata.domain.vo.UpdateProductInfoDto;
import com.wangfj.product.maindata.service.intf.IPcmErpProductService;
import com.wangfj.product.maindata.service.intf.IPcmShoppeProductService;
import com.wangfj.util.mq.MqRequestDataPara;
import com.wangfj.util.mq.MqUtil;
import com.wangfj.util.mq.PublishDTO;

/**
 * ERP商品信息Controller——MQ
 * 
 * @Class Name ErpProductController
 * @Author zhangxy
 * @Create In 2015年7月17日
 */
@Controller
@RequestMapping("/erpProductEfuture")
public class ErpProductController extends BaseController {

	@Autowired
	private IPcmErpProductService erpProductService;
	@Autowired
	private IPcmShoppeProductService spService;
	@Autowired
	private IPcmExceptionLogService pcmExceptionLogService;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	List<PublishDTO> sidList = null;
	List<PublishDTO> sidList2 = null;
	List<PublishDTO> spuList = null;
	List<PublishDTO> skuList = null;
	List<PublishDTO> proList = null;

	@Autowired
	private IPcmErpTestService erpTestService;

	@RequestMapping(value = "/erpDataMigration", method = RequestMethod.POST)
	@ResponseBody
	public String erpDataMigration(@RequestBody PcmLimitPara para) {
		PcmLimitPara para1 = new PcmLimitPara();
		try {
			BeanUtils.copyProperties(para1, para);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = para1.getStart(); i < para1.getMaxNum() / para1.getLimit(); i++) {
			erpTestService.erpDataMigration(i * para1.getLimit(), para1.getLimit());
		}
		return "success";
	}

	/**
	 * 从门店上传到PCM
	 * 
	 * @Methods Name uploadErpProductFromEFuture
	 * @Create In 2015年7月13日 By zhangxy
	 * @param para1
	 * @param request
	 * @return
	 * @return Map<String,Object>
	 * @throws Exception
	 */
	@RequestMapping(value = "/uploadErpProductFromEFuture", method = RequestMethod.POST)
	@ResponseBody
	public String uploadErpProductFromEFuture(@RequestBody MqRequestDataPara para1,
			HttpServletRequest request) {
		final MqRequestDataPara para = para1;
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				JSONObject jsono = JSONObject.fromObject(para.getData());
				JSONArray jsona = JSONArray.fromObject(jsono.get("data"));
				List<GetErpProductFromEfutureDto> listPara = JSONArray.toList(jsona);
				List<String> excepList = new ArrayList<String>();
				sidList = new ArrayList<PublishDTO>();
				for (int i = 0; i < listPara.size(); i++) {
					GetErpProductFromEfutureDto pcmDto = new GetErpProductFromEfutureDto();
					try {
						BeanUtils.copyProperties(pcmDto, listPara.get(i));
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (pcmDto.getACTION_CODE() != null) {
						try {
							PublishDTO publish = erpProductService
									.saveErpProductFromEFuture(pcmDto);
							// 下发
							if (publish.getSid() != null) {
								if (publish.getType() == -1) {// 下发专柜商品(可售状态修改)
									PcmShoppeProduct pageDto = new PcmShoppeProduct();
									pageDto.setShoppeProSid(publish.getSid().toString());
									List<PcmShoppeProduct> dtoList = spService
											.selectShoppeProductInfo(pageDto);
									PublishDTO pbDto = new PublishDTO();
									pbDto.setSid(dtoList.get(0).getSid());
									pbDto.setType(1);
									sidList2 = new ArrayList<PublishDTO>();
									sidList2.add(pbDto);
									if (sidList2 != null && sidList2.size() != 0) {
										Map<String, Object> map = new HashMap<String, Object>();
										map.put("paraList", sidList2);
										map.put("PcmEfutureERP", "1");// 门店
										map.put("PcmProSearch", "1");
										map.put("PcmEfuturePromotion", "1");// 促销
										map.put("PcmSearcherOffline", "1");// 线下搜索
										HttpUtil.doPost(PropertyUtil
												.getSystemUrl("product.pushShoppeProduct"),
												JsonUtil.getJSONString(map));
									}
									System.out.println("下发促销" + JsonUtil.getJSONString(publish));
									HttpUtil.doPost(
											PropertyUtil.getSystemUrl("product.pushErpProduct"),
											JsonUtil.getJSONString(publish));
								} else if (publish.getType() == -2) {
									PcmShoppeProduct proDto = new PcmShoppeProduct();
									proDto.setRateCode(publish.getSid().toString());
									List<PcmShoppeProduct> shoppeProList = spService
											.selectShoppeProductInfo(proDto);
									List<UpdateProductInfoDto> list = new ArrayList<UpdateProductInfoDto>();
									for (PcmShoppeProduct pcmShoppeProduct : shoppeProList) {
										UpdateProductInfoDto entity = new UpdateProductInfoDto();
										entity.setProductCode(pcmShoppeProduct.getSid().toString());
										;
										entity.setStatus(Integer.parseInt(pcmDto.getStatus()));
										list.add(entity);
									}
									proList = new ArrayList<PublishDTO>();
									proList = spService.updateProductStatusInfo(list);
									ProSkuSpuPublishDto publishDto = new ProSkuSpuPublishDto();
									publishDto.setProList(proList);
									proSkuSpuPublish(publishDto);
									sidList.add(publish);
								} else {// 下发大码商品
									sidList.add(publish);
								}
							}
						} catch (BleException e) {
							if (ErrorCodeConstants.ErrorCode.vaildErrorCode(e.getCode())) {
								ThrowExcetpionUtil.splitExcetpion(new BleException(e.getCode(), e
										.getMessage()));
							}
							excepList.add(pcmDto.getStoreCode() + "-" + pcmDto.getProductCode()
									+ "--导入失败,错误:" + e.toString());
						}
					} else {
						excepList.add(pcmDto.getStoreCode() + "-" + pcmDto.getProductCode()
								+ "--导入失败,错误:ActionCode不能为空");
					}
				}
				if (excepList != null && excepList.size() > 0) {
					// 写入异常表
					PcmExceptionLogDto pcmExceptionLogDto = new PcmExceptionLogDto();
					pcmExceptionLogDto.setInterfaceName("uploadErpProductFromEFuture");
					pcmExceptionLogDto.setExceptionType(StatusCode.EXCEPTION_PRODUCT.getStatus());
					pcmExceptionLogDto.setErrorMessage(JsonUtil.getJSONString(excepList));
					pcmExceptionLogDto.setDataContent(JsonUtil.getJSONString(para));
					pcmExceptionLogService.saveExceptionLogInfo(pcmExceptionLogDto);
				}
				if (sidList != null && sidList.size() > 0) {
					taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								HttpUtil.doPost(
										PropertyUtil.getSystemUrl("product.pushErpProduct"),
										JsonUtil.getJSONString(sidList));
								HttpUtil.doPost(PropertyUtil.getSystemUrl("product.pushSearch"),
										JsonUtil.getJSONString(sidList));
							} catch (Exception e2) {
								ThrowExcetpionUtil.splitExcetpion(new BleException(
										ErrorCode.DOPOST_SYN_FAILED.getErrorCode(),
										ErrorCode.DOPOST_SYN_FAILED.getMemo()));
							}
						}
					});
				}
			}
		});
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(para.getHeader()));
	}

	/**
	 * 从门店上传到PCM
	 * 
	 * @Methods Name uploadErpProductFromEFuture
	 * @Create In 2015年7月13日 By zhangxy
	 * @param para1
	 * @param request
	 * @return
	 * @return Map<String,Object>
	 * @throws Exception
	 */
	@RequestMapping(value = "/uploadErpProductFromEFuture2", method = RequestMethod.POST)
	@ResponseBody
	public String uploadErpProductFromEFuture2(@RequestBody MqRequestDataPara para1,
			HttpServletRequest request) {
		final MqRequestDataPara para = para1;
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				JSONObject jsono = JSONObject.fromObject(para.getData());
				JSONArray jsona = JSONArray.fromObject(jsono.get("data"));
				List<GetErpProductFromEfutureDto> listPara = JSONArray.toList(jsona);
				List<String> excepList = new ArrayList<String>();
				sidList = new ArrayList<PublishDTO>();
				for (int i = 0; i < listPara.size(); i++) {
					GetErpProductFromEfutureDto pcmDto = new GetErpProductFromEfutureDto();
					try {
						BeanUtils.copyProperties(pcmDto, listPara.get(i));
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (pcmDto.getACTION_CODE() != null) {
						try {
							PublishDTO publish = erpProductService
									.saveErpProductFromEFuture2(pcmDto);
							// 下发
							if (publish.getSid() != null) {
								if (publish.getType() == -1) {// 下发专柜商品(可售状态修改)
									PcmShoppeProduct pageDto = new PcmShoppeProduct();
									pageDto.setShoppeProSid(publish.getSid().toString());
									List<PcmShoppeProduct> dtoList = spService
											.selectShoppeProductInfo(pageDto);
									PublishDTO pbDto = new PublishDTO();
									pbDto.setSid(dtoList.get(0).getSid());
									pbDto.setType(1);
									sidList2 = new ArrayList<PublishDTO>();
									sidList2.add(pbDto);
									if (sidList2 != null && sidList2.size() != 0) {
										Map<String, Object> map = new HashMap<String, Object>();
										map.put("paraList", sidList2);
										map.put("PcmEfutureERP", "1");// 门店
										map.put("PcmProSearch", "1");
										map.put("PcmEfuturePromotion", "1");// 促销
										map.put("PcmSearcherOffline", "1");// 线下搜索
										HttpUtil.doPost(PropertyUtil
												.getSystemUrl("product.pushShoppeProduct"),
												JsonUtil.getJSONString(map));
									}
									System.out.println("下发促销" + JsonUtil.getJSONString(publish));
									HttpUtil.doPost(
											PropertyUtil.getSystemUrl("product.pushErpProduct"),
											JsonUtil.getJSONString(publish));
								} else if (publish.getType() == -2) {
									PcmShoppeProduct proDto = new PcmShoppeProduct();
									proDto.setRateCode(publish.getSid().toString());
									List<PcmShoppeProduct> shoppeProList = spService
											.selectShoppeProductInfo(proDto);
									List<UpdateProductInfoDto> list = new ArrayList<UpdateProductInfoDto>();
									for (PcmShoppeProduct pcmShoppeProduct : shoppeProList) {
										UpdateProductInfoDto entity = new UpdateProductInfoDto();
										entity.setProductCode(pcmShoppeProduct.getSid().toString());
										;
										entity.setStatus(Integer.parseInt(pcmDto.getStatus()));
										list.add(entity);
									}
									proList = new ArrayList<PublishDTO>();
									proList = spService.updateProductStatusInfo(list);
									ProSkuSpuPublishDto publishDto = new ProSkuSpuPublishDto();
									publishDto.setProList(proList);
									proSkuSpuPublish(publishDto);
									sidList.add(publish);
								} else {// 下发大码商品
									sidList.add(publish);
								}
							}
						} catch (BleException e) {
							if (ErrorCodeConstants.ErrorCode.vaildErrorCode(e.getCode())) {
								ThrowExcetpionUtil.splitExcetpion(new BleException(e.getCode(), e
										.getMessage()));
							}
							excepList.add(pcmDto.getStoreCode() + "-" + pcmDto.getProductCode()
									+ "--导入失败,错误:" + e.toString());
						}
					} else {
						excepList.add(pcmDto.getStoreCode() + "-" + pcmDto.getProductCode()
								+ "--导入失败,错误:ActionCode不能为空");
					}
				}
				if (excepList != null && excepList.size() > 0) {
					// 写入异常表
					PcmExceptionLogDto pcmExceptionLogDto = new PcmExceptionLogDto();
					pcmExceptionLogDto.setInterfaceName("uploadErpProductFromEFuture");
					pcmExceptionLogDto.setExceptionType(StatusCode.EXCEPTION_PRODUCT.getStatus());
					pcmExceptionLogDto.setErrorMessage(JsonUtil.getJSONString(excepList));
					pcmExceptionLogDto.setDataContent(JsonUtil.getJSONString(para));
					pcmExceptionLogService.saveExceptionLogInfo(pcmExceptionLogDto);
				}
				// if (sidList != null && sidList.size() > 0) {
				// taskExecutor.execute(new Runnable() {
				// @Override
				// public void run() {
				// try {
				// HttpUtil.doPost(
				// PropertyUtil.getSystemUrl("product.pushErpProduct"),
				// JsonUtil.getJSONString(sidList));
				// HttpUtil.doPost(PropertyUtil.getSystemUrl("product.pushSearch"),
				// JsonUtil.getJSONString(sidList));
				// } catch (Exception e2) {
				// ThrowExcetpionUtil.splitExcetpion(new BleException(
				// ErrorCode.DOPOST_SYN_FAILED.getErrorCode(),
				// ErrorCode.DOPOST_SYN_FAILED.getMemo()));
				// }
				// }
				// });
				// }
			}
		});
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(para.getHeader()));
	}

	/**
	 * ERP商品换品牌、专柜、供应商（添加单据）
	 *
	 * @Methods Name uploadErpChangeFromEfuture
	 * @Create In 2015年7月16日 By zhangxy
	 * @param para1
	 * @return String
	 */
	@RequestMapping(value = "/uploadErpChangeFromEfuture")
	@ResponseBody
	public String uploadErpChangeFromEfuture(@RequestBody @Valid MqRequestDataPara para1) {
		final MqRequestDataPara para = para1;
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				JSONObject jsono = JSONObject.fromObject(para.getData());
				JSONArray jsona = JSONArray.fromObject(jsono.get("data"));
				List<ErpChangePara> data = JSONArray.toList(jsona);
//				List<Map<String, Object>> excepList = new ArrayList<Map<String, Object>>();
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < data.size(); i++) {
					ErpChangeDto dto = new ErpChangeDto();
					try {
						BeanUtils.copyProperties(dto, data.get(i));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					// if (dto.getActionCode() != null) {
					try {
						Integer result = erpProductService.modifyErpChangeFromEFutureForBill(dto);
					} catch (BleException e) {
						if (ErrorCodeConstants.ErrorCode.vaildErrorCode(e.getCode())) {
							ThrowExcetpionUtil.splitExcetpion(new BleException(e.getCode(), e
									.getMessage()));
						}
						map.put("单据号:" + dto.getSEQNO() + ",行号:" + dto.getROWNO(), e.getMessage());
//						excepList.add(map);
					}
					// } else {
					// map.put("单据号:" + dto.getSEQNO() + ",行号:" +
					// dto.getROWNO(), "ActionCode不能为空");
					// excepList.add(map);
					// }
				}
				// 写入异常表
				if (map != null && map.size() > 0) {
					PcmExceptionLogDto pcmExceptionLogDto = new PcmExceptionLogDto();
					pcmExceptionLogDto.setInterfaceName("uploadErpChangeFromEfuture");
					pcmExceptionLogDto.setExceptionType(StatusCode.EXCEPTION_PRODUCT.getStatus());
					pcmExceptionLogDto.setErrorMessage(JsonUtil.getJSONString(map));
					pcmExceptionLogDto.setDataContent(JsonUtil.getJSONString(para));
					pcmExceptionLogService.saveExceptionLogInfo(pcmExceptionLogDto);
				}
			}
		});
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(para.getHeader()));
	}

	/**
	 * ERP商品换品牌、专柜、供应商(废弃)
	 * 
	 * @Methods Name uploadErpChangeFromEfuture
	 * @Create In 2015年7月16日 By zhangxy
	 * @param para1
	 * @return String
	 */
	@Deprecated
	@RequestMapping(value = "/uploadErpChangeFromEfuture1")
	@ResponseBody
	public String uploadErpChangeFromEfuture1(@RequestBody @Valid MqRequestDataPara para1) {
		final MqRequestDataPara para = para1;
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				JSONObject jsono = JSONObject.fromObject(para.getData());
				JSONArray jsona = JSONArray.fromObject(jsono.get("data"));
				List<ErpChangePara> data = JSONArray.toList(jsona);
				List<Map<String, Object>> excepList = new ArrayList<Map<String, Object>>();
				Map<String, Object> map = new HashMap<String, Object>();
				sidList = new ArrayList<PublishDTO>();
				for (int i = 0; i < data.size(); i++) {
					ErpChangeDto dto = new ErpChangeDto();
					try {
						BeanUtils.copyProperties(dto, data.get(i));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					// if (dto.getActionCode() != null) {
					try {
						ModifyErpProductDto mDto = erpProductService
								.modifyErpChangeFromEFuture(dto);
						if (mDto.getSid() != null) {
							PublishDTO pDto = new PublishDTO();
							pDto.setSid(mDto.getSid());
							pDto.setType(1);
							sidList.add(pDto);
						}
						List<PcmShoppeProduct> resList = mDto.getList();
						List<Map<String, Object>> excepList2 = new ArrayList<Map<String, Object>>();
						if (resList != null && resList.size() > 0) {
							Map<String, Object> excepMap = new HashMap<String, Object>();
							sidList2 = new ArrayList<PublishDTO>();
							for (PcmShoppeProduct entity : resList) {
								try {
									Boolean b = erpProductService.modifyShoppeProduct(entity, dto);
									if (b) {
										PublishDTO e = new PublishDTO();
										e.setSid(entity.getSid());
										e.setType(1);
										sidList2.add(e);
									}
								} catch (BleException e) {
									if (ErrorCodeConstants.ErrorCode.vaildErrorCode(e.getCode())) {
										ThrowExcetpionUtil.splitExcetpion(new BleException(e
												.getCode(), e.getMessage()));
									}
									excepMap.clear();
									excepMap.put("专柜商品编码:" + entity.getShoppeProSid(),
											e.getMessage());
									excepList2.add(excepMap);
								}
							}
							if (excepList2.size() != 0 && excepList2.size() == resList.size()) {
								map.put("单据号:" + dto.getSEQNO() + ",行号:" + dto.getROWNO(),
										"扣率码下所有专柜商品更换失败:" + excepList2);
							} else {
								map.put("单据号:" + dto.getSEQNO() + ",行号:" + dto.getROWNO(),
										excepList2);
							}
						}
						if (excepList2 != null && excepList2.size() > 0) {
							excepList.add(map);
						}
					} catch (BleException e) {
						if (ErrorCodeConstants.ErrorCode.vaildErrorCode(e.getCode())) {
							ThrowExcetpionUtil.splitExcetpion(new BleException(e.getCode(), e
									.getMessage()));
						}
						map.put("单据号:" + dto.getSEQNO() + ",行号:" + dto.getROWNO(), e.getMessage());
						excepList.add(map);
					}
					// } else {
					// map.put("单据号:" + dto.getSEQNO() + ",行号:" +
					// dto.getROWNO(), "ActionCode不能为空");
					// excepList.add(map);
					// }
				}
				// 下发
				if (sidList != null && sidList.size() > 0) {
					taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								// 大码下发
								HttpUtil.doPost(
										PropertyUtil.getSystemUrl("product.pushErpProduct"),
										JsonUtil.getJSONString(sidList));
								// 专柜商品下发
								if (sidList2 != null && sidList2.size() != 0) {
									HttpUtil.doPost(
											PropertyUtil.getSystemUrl("product.pushShoppeProduct"),
											JsonUtil.getJSONString(sidList2));
								}
							} catch (Exception e2) {
								ThrowExcetpionUtil.splitExcetpion(new BleException(
										ErrorCode.DOPOST_SYN_FAILED.getErrorCode(),
										ErrorCode.DOPOST_SYN_FAILED.getMemo()));
							}
						}
					});
				}
				// 写入异常表
				if (excepList != null && excepList.size() > 0) {
					PcmExceptionLogDto pcmExceptionLogDto = new PcmExceptionLogDto();
					pcmExceptionLogDto.setInterfaceName("uploadErpChangeFromEfuture");
					pcmExceptionLogDto.setExceptionType(StatusCode.EXCEPTION_PRODUCT.getStatus());
					pcmExceptionLogDto.setErrorMessage(JsonUtil.getJSONString(excepList));
					pcmExceptionLogDto.setDataContent(JsonUtil.getJSONString(para));
					pcmExceptionLogService.saveExceptionLogInfo(pcmExceptionLogDto);
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
		final String proType = publishDto.getProType();
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
					paramMap.put("PcmEfuturePromotion", "1");// 促销
					paramMap.put("PcmProSearch", "1");
					paramMap.put("PcmEfutureERP", "1");// 门店
					paramMap.put("PcmSearcherOffline", "1");// 线下搜索
					HttpUtil.doPost(PropertyUtil.getSystemUrl("product.pushShoppeProduct"),
							JsonUtil.getJSONString(paramMap));
				}
			});
		}

	}
}
