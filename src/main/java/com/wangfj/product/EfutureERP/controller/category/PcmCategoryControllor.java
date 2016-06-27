/**
 * @Probject Name: pcm-core
 * @Path: com.wangfj.product.EfutureERP.controller.categoryCategoryManageControllor.java
 * @Create By duanzhaole
 * @Create In 2015年7月9日 下午3:09:39
 * TODO
 */
package com.wangfj.product.EfutureERP.controller.category;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.wangfj.core.constants.ComErrorCodeConstants;
import com.wangfj.core.constants.ComErrorCodeConstants.ErrorCode;
import com.wangfj.core.framework.base.controller.BaseController;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.core.utils.ResultUtil;
import com.wangfj.product.EfutureERP.controller.support.CopyToCategoryPara;
import com.wangfj.product.EfutureERP.controller.support.PcmTJCategoryPara;
import com.wangfj.product.category.domain.entity.PcmCategory;
import com.wangfj.product.category.domain.vo.PcmAddCategoryDto;
import com.wangfj.product.category.service.intf.ICategoryService;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.intf.IPcmExceptionLogService;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.organization.domain.vo.PcmOrganizationResultDto;
import com.wangfj.product.organization.domain.vo.SelectPcmOrganizationDto;
import com.wangfj.product.organization.service.intf.IPcmOrganizationService;
import com.wangfj.util.Constants;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.MqRequestDataPara;

/**
 * 添加分类controller
 * 
 * @Class Name CategoryManageControllor
 * @Author duanzhaole
 * @Create In 2015年7月9日
 */
@Controller
@RequestMapping("/categorymanage")
public class PcmCategoryControllor extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(PcmCategoryControllor.class);
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd.HHmmssZ");

	@Autowired
	private ICategoryService categoryManageService;
	@Autowired
	private IPcmOrganizationService orgService;
	@Autowired
	private IPcmExceptionLogService exceptionLogService;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	/**
	 * 管理分类批量上传
	 * 
	 * @param categorypara
	 * @param request
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */

	@RequestMapping(value = "/uploadCategoryFromEfutureERP", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadCategoryFromEfutureERP(@RequestBody MqRequestDataPara mqlist,
			HttpServletRequest request) throws IllegalAccessException, InvocationTargetException {
		String s = "";
		PcmAddCategoryDto catedto = new PcmAddCategoryDto();
		JSONObject js = JSONObject.fromObject(mqlist.getData());
		JSONArray sq = (JSONArray) js.get("data");
		List<CopyToCategoryPara> lists = JSON.parseArray(sq.toString(), CopyToCategoryPara.class);
		List<CopyToCategoryPara> publishList = new ArrayList<CopyToCategoryPara>();
		if (lists.size() != 0) {// 数据不为空
			// 查询是否存在此门店的根节点
			PcmCategory entity = new PcmCategory();
			entity.setShopSid(lists.get(0).getStoreCode());
			entity.setCategoryType(1);
			entity.setLevel(0);
			entity.setStatus("Y");
			entity.setIsDisplay(1);
			List<PcmCategory> list1 = categoryManageService.selectListByParam(entity);
			String parentSid = null;
			if (list1.size() == 0) {
				// 不存在的话添加根节点
				SelectPcmOrganizationDto dto = new SelectPcmOrganizationDto();
				dto.setOrganizationCode(lists.get(0).getStoreCode());
				dto.setOrganizationType(3);
				List<PcmOrganizationResultDto> list2 = orgService.findListOrganization(dto);
				if (list2 == null || list2.size() == 0) {
					PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
					exceptionLogdto.setInterfaceName("findIndustryCategoryFromPCM");
					exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_CATEGORY.getStatus());
					exceptionLogdto.setErrorMessage(ErrorCode.SHOP_NULL.getErrorCode()
							+ ErrorCode.SHOP_NULL.getMemo());
					exceptionLogdto.setErrorCode(ErrorCode.SHOP_NULL.getErrorCode());
					exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
					throw new BleException(ErrorCode.SHOP_NULL.getErrorCode(),
							ErrorCode.SHOP_NULL.getMemo());
				}
				entity.setName(list2.get(0).getOrganizationName() + "管理分类");
				PcmCategory insert = categoryManageService.insertGlCategory(entity);
				if (insert == null) {
					PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
					exceptionLogdto.setInterfaceName("findIndustryCategoryFromPCM");
					exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_CATEGORY.getStatus());
					exceptionLogdto.setErrorMessage(ErrorCode.DATA_OPER_ERROR.getErrorCode()
							+ ErrorCode.DATA_OPER_ERROR.getMemo());
					exceptionLogdto.setErrorCode(ErrorCode.DATA_OPER_ERROR.getErrorCode());
					exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
					throw new BleException(ErrorCode.DATA_OPER_ERROR.getErrorCode(),
							ErrorCode.DATA_OPER_ERROR.getMemo());
				} else {
					parentSid = insert.getSid()+"";
				}
			} else {
				parentSid = list1.get(0).getSid()+"";
			}
			// 添加分类
			for (int i = 0; i < lists.size(); i++) {
				CopyToCategoryPara catep = new CopyToCategoryPara();
				try {
					BeanUtils.copyProperties(catep, lists.get(i));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				catedto.setActionCode(catep.getActionCode());
				catedto.setCategorySid(catep.getCODE());
				if (catep.getActionCode().equals("U")) {
					catedto.setSid(catep.getSid());
				} else if (catep.getActionCode().equals("D")) {
					catedto.setSid(catep.getSid());
					catedto.setCategoryCode(catep.getCODE());
					catedto.setShopSid(catep.getStoreCode());
					catedto.setStatus(Constants.N);
				} else {
					catedto.setCategoryType(Constants.PUBLIC_1);
					catedto.setCreateTime(new Date());
					catedto.setName(catep.getNAME());
					if ("0".equals(catep.getSJCODE())) {
						catedto.setParentSid(parentSid);
					} else {
						catedto.setParentSid(catep.getSJCODE());
					}
					catedto.setIsLeaf(catep.getFLAG());
					catedto.setLevel(Integer.parseInt(catep.getTYPE()));
					catedto.setStatus(catep.getSTATUS());
					catedto.setCategoryCode(catep.getCODE());
					catedto.setShopSid(catep.getStoreCode());
					catedto.setIsSelfBuilt(0);
					if (catep.getISZG().equals(Constants.Y)) {
						// Y=超市
						catedto.setIsMarket(Constants.PUBLIC_1 + "");
					} else if (catep.getISZG().equals(Constants.N)) {
						catedto.setIsMarket(Constants.PUBLIC_0 + "");
					}
				}
				try {
					s = categoryManageService.uploadeManagerCategory(catedto);
					if (s.equals(Constants.ADDSUCCESS) || s.equals(Constants.UPDATESUCCESS)) {
						publishList.add(catep);
						continue;
					}
					// 如果上传失败，插入异常表
				} catch (BleException e) {
					PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
					exceptionLogdto.setInterfaceName("findIndustryCategoryFromPCM");
					exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_CATEGORY.getStatus());
					exceptionLogdto.setErrorMessage("第" + (i + 1) + "条错误:" + e.getMessage());
					exceptionLogdto.setErrorCode(e.getCode());
					exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
					// return ResultUtil.creComErrorResult(e.getCode(),
					// e.getMessage());
				}
			}
			if (publishList.size() != 0) {
				final JSONArray publish = JSONArray.fromObject(publishList);
				// 上传成功后下发到促销
				final String pushToeFuture = PropertyUtil
						.getSystemUrl("category.synPushManageToEP");
				taskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							logger.info("API,addManageCategoryTOEp.htm,synPushToERP,request:"
									+ publish.toString());
							String response = HttpUtil.doPost(pushToeFuture, publish.toString().replaceAll("storeCode", "StoreCode"));
							logger.info("API,addManageCategoryTOEp.htm,synPushToERP,response:"
									+ response);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
		return ResultUtil.creComSucResult(s);

	}

	/**
	 * 统计分类增量下发
	 * 
	 * @param categorypara
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addTjCategoryTOErp", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addCategoryFromEfutureERP(
			@RequestBody @Valid PcmTJCategoryPara tongjicate, HttpServletRequest request) {

		final PcmAddCategoryDto catedto = new PcmAddCategoryDto();
		// 参数封装
		catedto.setSid(tongjicate.getSid());
		catedto.setActionCode(tongjicate.getActionCode());
		if (tongjicate.getActionCode().equals(Constants.U)) {
			catedto.setSid(tongjicate.getSid());
		}
		catedto.setCategoryType(Constants.PUBLIC_2);
		catedto.setActionDate(formatter.format(new Date()));
		catedto.setName(tongjicate.getNAME());
		catedto.setParentSid(tongjicate.getSJCODE());
		catedto.setIsLeaf(tongjicate.getFLAG());
		catedto.setLevel(Integer.parseInt(tongjicate.getTYPE()));
		catedto.setStatus(tongjicate.getSTATUS());

		String s = "";
		try {
			s = categoryManageService.uploadeCategory(catedto);
		} catch (Exception e1) {
			e1.printStackTrace();
			return ResultUtil.creComErrorResult(
					ComErrorCodeConstants.ErrorCode.DATA_OPER_ERROR.getErrorCode(), s);
		}
		if (s.equals("添加成功") || s.equals("修改成功")) {
			final String pushToeFuture = PropertyUtil.getSystemUrl("category.synPushToERP");
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						logger.info("API,addTjCategoryTOErp.htm,synPushToERP,request:"
								+ catedto.toString());
						String response = HttpUtil.doPost(pushToeFuture,
								JsonUtil.getJSONString(catedto));
						logger.info("API,addTjCategoryTOErp.htm,synPushToERP,response:" + response);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		// 如果上传失败，插入异常表
		else {
			PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
			exceptionLogdto.setInterfaceName("findIndustryCategoryFromPCM");
			exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_CATEGORY.getStatus());
			exceptionLogdto.setDataContent(tongjicate.toString());
			exceptionLogdto.setErrorMessage(ComErrorCodeConstants.ErrorCode.CATEGORY_UPLOAD
					.getMemo());
			exceptionLogdto.setErrorCode(ComErrorCodeConstants.ErrorCode.CATEGORY_UPLOAD.getErrorCode());
			exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
			return ResultUtil.creComErrorResult(
					ComErrorCodeConstants.ErrorCode.DATA_OPER_ERROR.getErrorCode(), s);
		}
		return ResultUtil.creComSucResult(s);

	}
	
	/**
	 * 电商管理分类批量上传
	 * 
	 * @param categorypara
	 * @param request
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws Exception
	 */

	@RequestMapping(value = "/uploadCategoryFromSAPERP", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadCategoryFromSAPERP(@RequestBody MqRequestDataListPara mqlist,
			HttpServletRequest request) throws IllegalAccessException, InvocationTargetException {
		String s = "";
		PcmAddCategoryDto catedto = new PcmAddCategoryDto();
//		JSONObject js = JSONObject.fromObject(mqlist.getData());
		JSONArray sq = JSONArray.fromObject(mqlist.getData());
		//JSONArray sq = JSONArray.fromObject(mqlist.getData());
		List<CopyToCategoryPara> lists = new ArrayList<CopyToCategoryPara>();
		
		for(Object o : sq){
			JSONObject json = (JSONObject)o;
			CopyToCategoryPara obj = new CopyToCategoryPara();
			obj.setStoreCode(json.getString("STORECODE"));
			obj.setCODE(json.getString("CODE"));
			obj.setNAME(json.getString("NAME"));
			obj.setSJCODE(json.getString("SJCODE"));
			obj.setFLAG(json.getString("FLAG"));
			obj.setTYPE(json.getString("TYPE").trim());
			obj.setSTATUS(json.getString("STATUS"));
			obj.setISZG(json.getString("ISZG"));
			obj.setActionCode(json.getString("ACTIONCODE"));
			obj.setActionDate(json.getString("ACTIONDATE"));
			obj.setActionPerson(json.getString("ACTIONPERSON"));
			lists.add(obj);
		}
		
		List<CopyToCategoryPara> publishList = new ArrayList<CopyToCategoryPara>();
		if (lists.size() != 0) {// 数据不为空
			// 查询是否存在此门店的根节点
			PcmCategory entity = new PcmCategory();
			entity.setShopSid(lists.get(0).getStoreCode());
			entity.setCategoryType(1);
			entity.setLevel(0);
			entity.setStatus("Y");
			entity.setIsDisplay(1);
			List<PcmCategory> list1 = categoryManageService.selectListByParam(entity);
			String parentSid = null;
			if (list1.size() == 0) {
				// 不存在的话添加根节点
				SelectPcmOrganizationDto dto = new SelectPcmOrganizationDto();
				dto.setOrganizationCode(lists.get(0).getStoreCode());
				dto.setOrganizationType(3);
				List<PcmOrganizationResultDto> list2 = orgService.findListOrganization(dto);
				if (list2 == null || list2.size() == 0) {
					PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
					exceptionLogdto.setInterfaceName("findIndustryCategoryFromPCM");
					exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_CATEGORY.getStatus());
					exceptionLogdto.setErrorMessage(ErrorCode.SHOP_NULL.getErrorCode()
							+ ErrorCode.SHOP_NULL.getMemo());
					exceptionLogdto.setErrorCode(ErrorCode.SHOP_NULL.getErrorCode());
					exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
					throw new BleException(ErrorCode.SHOP_NULL.getErrorCode(),
							ErrorCode.SHOP_NULL.getMemo());
				}
				entity.setName(list2.get(0).getOrganizationName() + "管理分类");
				PcmCategory insert = categoryManageService.insertGlCategory(entity);
				if (insert == null) {
					PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
					exceptionLogdto.setInterfaceName("findIndustryCategoryFromPCM");
					exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_CATEGORY.getStatus());
					exceptionLogdto.setErrorMessage(ErrorCode.DATA_OPER_ERROR.getErrorCode()
							+ ErrorCode.DATA_OPER_ERROR.getMemo());
					exceptionLogdto.setErrorCode(ErrorCode.DATA_OPER_ERROR.getErrorCode());
					exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
					throw new BleException(ErrorCode.DATA_OPER_ERROR.getErrorCode(),
							ErrorCode.DATA_OPER_ERROR.getMemo());
				} else {
					parentSid = insert.getCategorySid();
				}
			} else {
				parentSid = list1.get(0).getCategorySid();
			}
			// 添加分类
			for (int i = 0; i < lists.size(); i++) {
				CopyToCategoryPara catep = new CopyToCategoryPara();
				try {
					BeanUtils.copyProperties(catep, lists.get(i));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				catedto.setActionCode(catep.getActionCode());
				catedto.setCategorySid(catep.getCODE());
				catedto.setCategoryType(Constants.PUBLIC_1);
				catedto.setCreateTime(new Date());
				catedto.setName(catep.getNAME());
				if ("0".equals(catep.getSJCODE())) {
					catedto.setParentSid(parentSid);
				} else {
					catedto.setParentSid(catep.getSJCODE());
				}
				catedto.setIsLeaf(catep.getFLAG().equals("Y") ? "Y" : "N");
				catedto.setLevel(Integer.parseInt(catep.getTYPE()));
				catedto.setStatus(catep.getSTATUS());
				catedto.setCategoryCode(catep.getCODE());
				catedto.setShopSid(catep.getStoreCode());
				catedto.setIsSelfBuilt(0);
				catedto.setIsMarket(Constants.PUBLIC_2 + "");
				try {
					s = categoryManageService.uploadeManagerCategoryDS(catedto);
					if (s.equals(Constants.ADDSUCCESS) || s.equals(Constants.UPDATESUCCESS)) {
						publishList.add(catep);
						continue;
					}
					// 如果上传失败，插入异常表
				} catch (BleException e) {
					PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
					exceptionLogdto.setInterfaceName("findIndustryCategoryFromPCM");
					exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_CATEGORY.getStatus());
					exceptionLogdto.setErrorMessage("第" + (i + 1) + "条错误:" + e.getMessage());
					exceptionLogdto.setErrorCode(e.getCode());
					exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
					// return ResultUtil.creComErrorResult(e.getCode(),
					// e.getMessage());
				}
			}
			if (publishList.size() != 0) {
				final JSONArray publish = JSONArray.fromObject(publishList);
				// 上传成功后下发到促销
				final String pushToeFuture = PropertyUtil
						.getSystemUrl("category.synPushManageToEP");
				taskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							logger.info("API,addManageCategoryTOEp.htm,synPushToERP,request:"
									+ publish.toString());
							String response = HttpUtil.doPost(pushToeFuture, publish.toString().replaceAll("storeCode", "StoreCode"));
							logger.info("API,addManageCategoryTOEp.htm,synPushToERP,response:"
									+ response);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
		return ResultUtil.creComSucResult(s);

	}

}
