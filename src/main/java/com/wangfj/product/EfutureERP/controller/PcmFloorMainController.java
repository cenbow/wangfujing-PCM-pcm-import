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
import com.wangfj.product.organization.domain.vo.PcmFloorDto;
import com.wangfj.product.organization.domain.vo.PushCounterDto;
import com.wangfj.product.organization.service.intf.IPcmFloorService;

@Controller
@RequestMapping("/floor")
public class PcmFloorMainController extends BaseController {

	@Autowired
	private IPcmFloorService pcmFloorService;

	@ResponseBody
	@RequestMapping("/findFloorByParamFromPcm")
	public String findFloorByParamFromPcm(@RequestBody Map<String, Object> paramMap) {
		List<PcmFloorDto> findFloor = null;
		Page<PushCounterDto> page = new Page<PushCounterDto>();
		if (paramMap.get("pageSize") != null) {
			page.setPageSize((Integer) paramMap.get("pageSize"));
		}
		if (paramMap.get("currentPage") != null) {
			page.setCurrentPage((Integer) paramMap.get("currentPage"));
		}

		Integer count = pcmFloorService.getCountByParam(paramMap);
		page.setCount(count);
		paramMap.put("start", page.getStart());
		paramMap.put("limit", page.getLimit());

		try {
			findFloor = pcmFloorService.findFloorByParamFromPcm(paramMap);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JsonUtil.getJSONString(findFloor);
	}

}
