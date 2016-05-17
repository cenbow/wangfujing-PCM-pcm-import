package com.wangfj.product.core.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.wangfj.product.category.domain.entity.PcmCategory;
import com.wangfj.product.category.domain.entity.PcmCategoryPropValues;
import com.wangfj.product.category.domain.entity.PcmCategoryPropsDict;
import com.wangfj.product.category.domain.entity.PcmCategoryValuesDict;
import com.wangfj.product.category.domain.vo.PropsVO;
import com.wangfj.product.category.domain.vo.ValuesVO;
import com.wangfj.product.category.service.intf.ICategoryPropValuesService;
import com.wangfj.product.category.service.intf.ICategoryPropsDictService;
import com.wangfj.product.category.service.intf.ICategoryService;
import com.wangfj.product.category.service.intf.ICategoryValuesDictService;
import com.wangfj.product.core.controller.support.CategoryPropsValuePara;
import com.wangfj.product.organization.domain.entity.PcmChannel;
import com.wangfj.product.organization.service.intf.IPcmChannelService;
import com.wangfj.util.AjaxMessageVO;

@Controller
@RequestMapping("/propvaluecontroller")
public class BwCategoryPropValueController {

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private IPcmChannelService ssdChannelService;

	@Autowired
	private ICategoryPropsDictService ssdCategoryPropsDictService;

	@Autowired
	private ICategoryValuesDictService ssdCategoryValuesDictService;

	@Autowired
	private ICategoryPropValuesService ssdCategoryPropValuesService;

	/**
	 * 根据分类获取属性及属性值
	 * 
	 * @Methods Name getPropsAndValuesByCategory
	 * @Create In 2015-3-5 By chengsj
	 * @param model
	 * @param request
	 * @param response
	 * @param cid
	 * @return String
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "/bw/getPropsAndValuesByCategory", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String getPropsAndValuesByCategory(Model model, HttpServletRequest request,
			HttpServletResponse response, @RequestBody CategoryPropsValuePara propvalue) {

		// SsdCategory sc = this.categoryService.get(Long.valueOf(cid)); //13071
		// 获取参数
		String categoryId = propvalue.getCategoryId();
		PcmCategoryPropValues scpv = new PcmCategoryPropValues();
		scpv.setCategorySid(categoryId); // 1269
		scpv.setChannelSid(Long.valueOf(2l)); // 2
		List<PcmCategoryPropValues> lists = this.ssdCategoryPropValuesService
				.selectPropsVOList(scpv);
		List<PropsVO> propsList = new ArrayList<PropsVO>();
		if (lists != null && lists.size() > 0) {
			for (int i = 0; i < lists.size(); i++) {
				PcmCategoryPropValues pv = lists.get(i);
				Map map = new HashMap();
				map.put("categorySid", pv.getCategorySid());
				map.put("propsSid", pv.getPropsSid());
				map.put("channelSid", pv.getChannelSid());
				List<ValuesVO> valueslist = ssdCategoryPropValuesService
						.getAllCategoryValuesVOs(map);
				PropsVO propsVO = new PropsVO();
				propsVO.setPropsSid(pv.getPropsSid());
				propsVO.setPropsName(pv.getPropsName());
				propsVO.setProvals(valueslist);
				propsList.add(propsVO);
			}
		}
		Gson gson = new Gson();
		String result = gson.toJson(propsList);
		System.out.println(result);
		return result;
	}

	/** 获取类目属性右侧列表 **/
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "/bw/listAllValue", method = { RequestMethod.GET, RequestMethod.POST })
	public String listAl(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestBody CategoryPropsValuePara propvalue) {
		// 获取传入参数
		Integer limit = propvalue.getLimit();
		Integer start = propvalue.getStart();
		String propsName = propvalue.getPropsName();
		String cid = propvalue.getCid();

		JSONObject json = new JSONObject();
		PcmCategory sc = new PcmCategory();
		PcmCategoryPropValues scpv = new PcmCategoryPropValues();
		if (!(propsName == null || "".equals(propsName))) {
			scpv.setPropsName(propsName);
		}
		int total = 0;
		List lists = new ArrayList();
		if (null != cid && !"".equals(cid)) {
			sc = this.categoryService.get(Long.valueOf(cid));
			scpv.setCategorySid(sc.getCategorySid()); // 1269
			scpv.setChannelSid(sc.getChannelSid()); // 2
			total = this.ssdCategoryPropValuesService.selectPropsVOTotal(scpv);
			scpv.setStart(start);
			scpv.setPageSize(limit);
			lists = this.ssdCategoryPropValuesService.selectPropsVO(scpv);
		}
		int pageCount = total % limit == 0 ? total / limit : (total / limit + 1);
		if (lists != null && lists.size() != 0) {
			json.put("list", lists);
			json.put("pageCount", pageCount);
		} else {
			json.put("list", lists);
			json.put("pageCount", 0);
		}
		return json.toString();
	}

	@SuppressWarnings("unused")
	@ResponseBody
	@RequestMapping(value = "/bw/Lists", method = { RequestMethod.GET, RequestMethod.POST })
	public String valuelist(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestBody CategoryPropsValuePara propvalue) {
		// 获取传入参数
		String propsSid = propvalue.getPropsSid();
		String categorySid = propvalue.getCategoryId();
		String channelSid = propvalue.getChannelSid();
		String page = propvalue.getPage();
		String rows = propvalue.getRows();
		PcmCategoryPropValues scpv = new PcmCategoryPropValues();
		scpv.setCategorySid(categorySid);
		scpv.setChannelSid(Long.valueOf(channelSid));
		scpv.setPropsSid(Long.valueOf(propsSid));
		// int total = this.ssdCategoryPropValuesService.selectPageTotal(scpv);

		// scpv.setStart((Integer.valueOf(page)-1)*Integer.valueOf(rows));
		// scpv.setPageSize(Integer.valueOf(rows));
		List<PcmCategoryPropValues> lists = this.ssdCategoryPropValuesService.selectPage(scpv);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		// jsonMap.put("total", total);
		jsonMap.put("rows", lists);
		String result = JSONObject.fromObject(jsonMap).toString();
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/bw/addd", method = { RequestMethod.POST, RequestMethod.GET })
	public String addPropsAndValuess(Model model, HttpServletRequest request,
			HttpServletResponse response, @RequestBody CategoryPropsValuePara propvalue) {

		// 获取传入参数
		String propsid = propvalue.getPropsSid();
		String name = propvalue.getName();
		String cid = propvalue.getCid();
		int flag = -1;
		long start = System.currentTimeMillis();
		List<PcmCategoryPropValues> scpvlist = new ArrayList<PcmCategoryPropValues>();
		StringBuffer sb = new StringBuffer();
		PcmCategory sc = this.categoryService.get(Long.valueOf(cid));
		if (propsid != null && propsid.length() > 0) {
			String[] propid = propsid.split(",");
			for (String pid : propid) {
				PcmCategoryPropsDict scpd = this.ssdCategoryPropsDictService.get(Long.valueOf(pid));
				PcmCategoryValuesDict scvd = new PcmCategoryValuesDict();
				scvd.setPropsSid(scpd.getPropsSid());
				scvd.setStatus(1L);
				scvd.setChannelSid(scpd.getChannelSid());
				List<PcmCategoryValuesDict> scvds = this.ssdCategoryValuesDictService
						.selectList(scvd);
				if (scvds.size() > 0) {
					for (PcmCategoryValuesDict sd : scvds) {
						PcmCategoryPropValues scpv = new PcmCategoryPropValues();
						scpv.setCategoryName(name);
						scpv.setCategorySid(sc.getCategorySid());
						scpv.setChannelSid(scpd.getChannelSid());
						scpv.setPropsSid(scpd.getPropsSid());
						scpv.setPropsName(scpd.getPropsName());
						scpv.setValuesSid(sd.getValuesSid());
						scpv.setValuesName(sd.getValuesName());
						scpv.setOptDate(new Date());
						scpvlist.add(scpv);
					}
				} else {
					if (sb.length() == 0) {
						sb.append(scpd.getPropsName());
					} else {
						sb.append("," + scpd.getPropsName());
					}
				}
			}
		} else {
			this.ssdCategoryPropValuesService.deleteByProperties(sc.getCategorySid(),
					sc.getChannelSid());
			flag = 1;
		}
		if (sb.length() > 0) {
			flag = -2;
		}
		if (scpvlist.size() > 0 && flag != -2) {
			this.ssdCategoryPropValuesService.deleteByProperties(sc.getCategorySid(),
					sc.getChannelSid());
			for (PcmCategoryPropValues scpv : scpvlist) {
				flag = this.ssdCategoryPropValuesService.saveorupdate(scpv);
			}
			// flag = this.ssdCategoryPropValuesService.save(scpvlist);
		}
		AjaxMessageVO result = new AjaxMessageVO();
		if (flag >= 0) {
			result.setStatus("success");
			result.setMessage("操作成功");
		} else if (flag == -2) {
			result.setStatus("failure");
			String message = "你选择的[" + sb.toString() + "]等属性没有有效的属性值!";
			result.setMessage(message);
		} else {
			result.setStatus("failure");
			result.setMessage("操作失败");
		}
		long end = System.currentTimeMillis();
		System.out.println("update propval time is :" + (end - start));
		Gson gson = new Gson();
		return gson.toJson(result);
	}

	@ResponseBody
	@RequestMapping(value = "/bw/editl", method = { RequestMethod.POST, RequestMethod.GET })
	public String update(Model m, HttpServletRequest request, HttpServletResponse response,
			 @RequestBody CategoryPropsValuePara propvalue) {

		//获取传入参数
		String cid = propvalue.getCid();
		PcmCategory sc = this.categoryService.get(Long.valueOf(cid));
		PcmCategoryPropValues s = new PcmCategoryPropValues();
		s.setCategorySid(sc.getCategorySid());
		s.setChannelSid(sc.getChannelSid());
		List<PcmCategoryPropValues> scpvs = this.ssdCategoryPropValuesService.selectPropsVOList(s);
		List<Long> vids = new ArrayList<Long>();
		if (scpvs.size() > 0) {
			for (PcmCategoryPropValues ss : scpvs) {
				PcmCategoryPropsDict scpd = new PcmCategoryPropsDict();
				scpd.setChannelSid(sc.getChannelSid());
				scpd.setPropsSid(ss.getPropsSid());
				List<PcmCategoryPropsDict> list = this.ssdCategoryPropsDictService.selectList(scpd);
				vids.add(list.get(0).getSid());
			}
		}
		JSONObject json = new JSONObject();
		json.put("name", sc.getName());
		json.put("propsid", vids);
		String data = json.toString();
		return data;
	}

	@ResponseBody
	@RequestMapping(value = "/bw/comboxlistlist", method = { RequestMethod.GET, RequestMethod.POST })
	public String comboxlist(Model model, HttpServletRequest request, HttpServletResponse response) {
		PcmChannel sc = new PcmChannel();

		List<PcmChannel> lists = this.ssdChannelService.selectList(sc);
		JSONArray jsons = new JSONArray();
		for (PcmChannel s : lists) {
			JSONObject json = JSONObject.fromObject(s);
			jsons.add(json);
		}
		String result = jsons.toString();
		return result;
	}

}
