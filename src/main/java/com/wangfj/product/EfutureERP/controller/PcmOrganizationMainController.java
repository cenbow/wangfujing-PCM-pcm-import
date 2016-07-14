package com.wangfj.product.EfutureERP.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangfj.core.framework.base.controller.BaseController;
import com.wangfj.core.framework.base.page.Page;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.organization.domain.vo.PublishOrganizationDto;
import com.wangfj.product.organization.domain.vo.PushCounterDto;
import com.wangfj.product.organization.service.intf.IPcmOrganizationService;
import com.wangfj.util.Constants;

/**
 * 基础组织机构信息管理 - MQ
 * 
 * @Class Name PcmOrganizationMainController
 * @Author yedong
 * @Create In 2015年7月16日
 */
@Controller
@RequestMapping("/organization")
public class PcmOrganizationMainController extends BaseController {
	@Autowired
	IPcmOrganizationService pcmOrganizationService;

	/**
	 * 基础组织结构信息下发-实时 门店地址信息查询
	 * 
	 * @Methods Name findOrganizationByParamFromPcm
	 * @Create In 2015年7月15日 By yedong
	 * @param map
	 * @return String
	 */
	@RequestMapping("/findOrganizationByParamFromPcm")
	@ResponseBody
	public String findOrganizationByParamFromPcm(@RequestBody Map<String, Object> map) {
		List<PublishOrganizationDto> publishOrganization = null;
		Page<PublishOrganizationDto> page = new Page<PublishOrganizationDto>();
		if (map.get("pageSize") != null) {
			page.setPageSize((Integer) map.get("pageSize"));
		}
		if (map.get("currentPage") != null) {
			page.setCurrentPage((Integer) map.get("currentPage"));
		}

		Integer count = pcmOrganizationService.getCountByParam(map);
		page.setCount(count);
		map.put("start", page.getStart());
		map.put("limit", page.getLimit());

		try {
			publishOrganization = pcmOrganizationService.findOrganizationByParamFromPcm(map);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return JsonUtil.getJSONString(publishOrganization);
	}

	/**
	 * 门店地址信息(到城市)分发到其他系统
	 * 
	 * @Methods Name findStoreCodeByParamFromPcm
	 * @Create In 2015年7月15日 By yedong
	 * @param map
	 * @return String
	 */
	@RequestMapping("/findStoreCodeByParamFromPcm")
	@ResponseBody
	public String findStoreCodeByParamFromPcm(@RequestBody Map<String, Object> map) {
		map.put("storeType", Constants.PUBLIC_3);

		Page<PushCounterDto> page = new Page<PushCounterDto>();
		if (map.get("pageSize") != null) {
			page.setPageSize((Integer) map.get("pageSize"));
		}
		if (map.get("currentPage") != null) {
			page.setCurrentPage((Integer) map.get("currentPage"));
		}

		Integer count = pcmOrganizationService.getCountByParam(map);
		page.setCount(count);
		map.put("start", page.getStart());
		map.put("limit", page.getLimit());

		List<PublishOrganizationDto> publishOrganization = null;
		try {
			publishOrganization = pcmOrganizationService.findOrganizationByParamFromPcm(map);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return JsonUtil.getJSONString(publishOrganization);
	}

}
