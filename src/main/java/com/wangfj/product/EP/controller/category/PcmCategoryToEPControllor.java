/**
 * @Probject Name: pcm-core
 * @Path: com.wangfj.product.EfutureERP.controller.categoryCategoryManageControllor.java
 * @Create By duanzhaole
 * @Create In 2015年7月9日 下午3:09:39
 * TODO
 */
package com.wangfj.product.EP.controller.category;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangfj.core.constants.ComErrorCodeConstants;
import com.wangfj.core.framework.base.controller.BaseController;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.core.utils.ResultUtil;
import com.wangfj.product.EfutureERP.controller.support.PcmManageCategoryPara;
import com.wangfj.product.EfutureERP.controller.support.PcmTJCategoryPara;
import com.wangfj.product.category.domain.vo.IndustryCategoryDto;
import com.wangfj.product.category.domain.vo.PcmAddCategoryDto;
import com.wangfj.product.category.service.intf.ICategoryService;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.intf.IPcmExceptionLogService;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.util.Constants;

/**
 * 添加分类controller
 * 
 * @Class Name CategoryManageControllor
 * @Author duanzhaole
 * @Create In 2015年7月9日
 */
@Controller
@RequestMapping("/categorytoep")
public class PcmCategoryToEPControllor extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(PcmCategoryToEPControllor.class);

	@Autowired
	private ICategoryService categoryManageService;
	@Autowired
	private IPcmExceptionLogService exceptionLogService;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	/**
	 * 统计分类增量下发
	 * 
	 * @param categorypara
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addTjCategoryTOEp", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addTjCategoryTOEp(
			@RequestBody @Valid PcmTJCategoryPara tongjicate, HttpServletRequest request) {

		final PcmAddCategoryDto catedto = new PcmAddCategoryDto();
		// 参数封装
		catedto.setActionCode(tongjicate.getActionCode());
		if(tongjicate.getActionCode().equals(Constants.U)){
			catedto.setSid(tongjicate.getSid());
		}
		catedto.setCategoryType(Constants.PUBLIC_2);
//		catedto.setActionDate((formatter.format(new Date())));
		catedto.setName(tongjicate.getNAME());
		catedto.setParentSid(tongjicate.getSJCODE());
		catedto.setIsLeaf(tongjicate.getFLAG());
		catedto.setLevel(Integer.parseInt(tongjicate.getTYPE()));
		catedto.setStatus(tongjicate.getSTATUS());
		String s="";
		try {
			s = categoryManageService.uploadeCategory(catedto);
		} catch (Exception e1) {
			e1.printStackTrace();
			return ResultUtil.creComErrorResult(ComErrorCodeConstants.ErrorCode.DATA_OPER_ERROR.getErrorCode(),s);
		}
		if (s.equals(Constants.ADDSUCCESS)||s.equals(Constants.UPDATESUCCESS)) {
			final String pushToeFuture = PropertyUtil.getSystemUrl("category.synPushStaToEP");
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

			exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
			return ResultUtil.creComErrorResult(ComErrorCodeConstants.ErrorCode.DATA_OPER_ERROR.getErrorCode(),s);
		}
		return ResultUtil.creComSucResult(s);

	}
	
	
	/**
	 * 管理分类增量下发
	 * 
	 * @param categorypara
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addManageCategoryTOEp", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addManageCategoryTOEp(
			@RequestBody @Valid PcmManageCategoryPara manageCate, HttpServletRequest request) {

		final PcmAddCategoryDto catedto = new PcmAddCategoryDto();
		// 参数封装
		catedto.setActionCode(manageCate.getActionCode());
		if(manageCate.getActionCode().equals(Constants.U)){
			catedto.setSid(manageCate.getSid());
		}
		catedto.setCategorySid(manageCate.getCODE());
		catedto.setShopSid(manageCate.getStoreCode());
		catedto.setCategoryType(Constants.PUBLIC_1);
		catedto.setName(manageCate.getNAME());
		catedto.setParentSid(manageCate.getSJCODE());
		catedto.setIsLeaf(manageCate.getFLAG());
		catedto.setLevel(Integer.parseInt(manageCate.getTYPE()));
		catedto.setStatus(manageCate.getSTATUS());
		String s="";
		try {
			s = categoryManageService.uploadeCategory(catedto);
		} catch (Exception e1) {
			return ResultUtil.creComErrorResult(ComErrorCodeConstants.ErrorCode.DATA_OPER_ERROR.getErrorCode(),s);
		}
		if (s.equals(Constants.ADDSUCCESS)||s.equals(Constants.UPDATESUCCESS)) {
			final String pushToeFuture = PropertyUtil.getSystemUrl("category.synPushManageToEP");
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						logger.info("API,addManageCategoryTOEp.htm,synPushToERP,request:"
								+ catedto.toString());
						String response = HttpUtil.doPost(pushToeFuture,
								JsonUtil.getJSONString(catedto));
						logger.info("API,addManageCategoryTOEp.htm,synPushToERP,response:" + response);

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
			exceptionLogdto.setDataContent(manageCate.toString());
			exceptionLogdto.setErrorMessage(ComErrorCodeConstants.ErrorCode.CATEGORY_UPLOAD
					.getMemo());

			exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
			return ResultUtil.creComErrorResult(ComErrorCodeConstants.ErrorCode.DATA_OPER_ERROR.getErrorCode(),s);
		}
		return ResultUtil.creComSucResult(s);

	}
	
	/**
	 * 工业分类增量下发
	 * 
	 * @param categorypara
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addIndustryCategoryTOEp", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addIndustryCategoryTOEp(
			@RequestBody @Valid IndustryCategoryDto IndustryCate, HttpServletRequest request) {

		final PcmAddCategoryDto catedto = new PcmAddCategoryDto();
		// 参数封装
		if(IndustryCate.getActionCode().equals(Constants.U)){
			catedto.setSid(IndustryCate.getSid());
		}
		catedto.setCategoryType(Constants.PUBLIC_0);
		catedto.setCategorySid(IndustryCate.getCode());
		catedto.setParentSid(IndustryCate.getParentCode());
		catedto.setName(IndustryCate.getName());
		catedto.setActionCode(IndustryCate.getActionCode());
		String s="";
		try {
			s = categoryManageService.uploadeCategory(catedto);
		} catch (Exception e1) {
			e1.printStackTrace();
			return ResultUtil.creComErrorResult(ComErrorCodeConstants.ErrorCode.DATA_OPER_ERROR.getErrorCode(),s);
		}
		if (s.equals(Constants.ADDSUCCESS)||s.equals(Constants.UPDATESUCCESS)) {
			final String pushToeFuture = PropertyUtil.getSystemUrl("category.synPushIndustryToEP");
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						logger.info("API,addIndustryCategoryTOEp.htm,synPushIndustryToEP,request:"
								+ catedto.toString());
						String response = HttpUtil.doPost(pushToeFuture,
								JsonUtil.getJSONString(catedto));
						logger.info("API,addIndustryCategoryTOEp.htm,synPushIndustryToEP,response:" + response);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		// 如果上传失败，插入异常表
		else {
			PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
			exceptionLogdto.setInterfaceName("addIndustryCategoryTOEp");
			exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_CATEGORY.getStatus());
			exceptionLogdto.setDataContent(IndustryCate.toString());
			exceptionLogdto.setErrorMessage(ComErrorCodeConstants.ErrorCode.CATEGORY_UPLOAD
					.getMemo());

			exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
			return ResultUtil.creComErrorResult(ComErrorCodeConstants.ErrorCode.DATA_OPER_ERROR.getErrorCode(),s);
		}
		return ResultUtil.creComSucResult(s);

	}

}
