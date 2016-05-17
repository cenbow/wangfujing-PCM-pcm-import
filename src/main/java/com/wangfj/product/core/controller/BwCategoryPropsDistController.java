package com.wangfj.product.core.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wangfj.core.utils.ResultUtil;
import com.wangfj.product.category.domain.entity.PcmCategory;
import com.wangfj.product.category.domain.entity.PcmCategoryPropValues;
import com.wangfj.product.category.domain.entity.PcmCategoryPropsDict;
import com.wangfj.product.category.domain.entity.PcmCategoryValuesDict;
import com.wangfj.product.category.service.intf.ICategoryPropValuesService;
import com.wangfj.product.category.service.intf.ICategoryPropsDictService;
import com.wangfj.product.category.service.intf.ICategoryService;
import com.wangfj.product.category.service.intf.ICategoryValuesDictService;
import com.wangfj.product.core.controller.support.CategoryPropsDictPara;

@Controller
@RequestMapping("/propsdictcontroller")
public class BwCategoryPropsDistController {

	@Autowired
	private ICategoryPropsDictService ssdCategoryPropsDictService;

	@Autowired
	private ICategoryValuesDictService ssdCategoryValuesDictService;

	@Autowired
	private ICategoryPropValuesService ssdCategoryPropValuesService;

	@Autowired
	private ICategoryService categoryService;

	@ResponseBody
	@RequestMapping(value = "/bw/propsdictList", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> list(Model model, HttpServletRequest request,
			HttpServletResponse response, @RequestBody CategoryPropsDictPara propsPara) {
		JSONObject jsons = new JSONObject();
		PcmCategoryPropsDict scp = new PcmCategoryPropsDict();
		// 获取参数
		String propsName = propsPara.getPropsName();
		String propsDesc = propsPara.getPropsDesc();
		Long channelSid = propsPara.getChannelSid();
		Integer limit = propsPara.getLimit();
		Integer start = propsPara.getStart();
		if (!(propsName == null || "".equals(propsName))) {
			scp.setPropsName(propsName);
		}
		if (!(propsDesc == null || "".equals(propsDesc))) {
			scp.setPropsDesc(propsDesc);
		}

		// String channelSid = LoadProperties.readValue("channel.WEB");
		scp.setChannelSid(channelSid);
		scp.setStart(start);
		scp.setPageSize(limit);
		int total = this.ssdCategoryPropsDictService.selectPageTotal(scp);
		List lists = this.ssdCategoryPropsDictService.selectPage(scp);
		int pageCount = total % limit == 0 ? total / limit : (total / limit + 1);
		jsons.put("list", lists);
		jsons.put("pageCount", pageCount);
		return ResultUtil.creComSucResult(jsons);
	}

	@ResponseBody
	@RequestMapping(value = "/bw/propsdictAdd", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> add(Model m, HttpServletRequest request,
			HttpServletResponse response, @RequestBody CategoryPropsDictPara propsPara)
			throws UnsupportedEncodingException {
		// 获取参数
		String sid = propsPara.getSid();
		String id = propsPara.getId();
		String propsName = propsPara.getPropsName();
		String propsDesc = propsPara.getPropsDesc();
		String propsCode = propsPara.getPropsCode();
		Long status = propsPara.getStatus();
		String delete1 = propsPara.getDelete1();
		String update1 = propsPara.getUpdate1();
		String insert1 = propsPara.getInsert1();
		Long channelSid = propsPara.getChannelSid();
		int flag;
		Long propSid = 0L;
		// String channelSid = LoadProperties.readValue("channel.WEB");
		if (sid == null || "".equals(sid)) {
			PcmCategoryPropsDict scpd = new PcmCategoryPropsDict();
			scpd.setPropsName(propsName);
			scpd.setPropsDesc(propsDesc);
			scpd.setPropsCode(propsCode);
			scpd.setStatus(Long.valueOf(status));
			scpd.setIsErpProp(0);// 0非ERP同步
			Long sortOrder = this.ssdCategoryPropsDictService.getMaxSortOrder(channelSid);
			scpd.setSortOrder(sortOrder + 1);
			scpd.setChannelSid(Long.valueOf(channelSid));
			flag = this.ssdCategoryPropsDictService.save(scpd);
			propSid = scpd.getSid();
			scpd.setPropsSid(scpd.getSid());
			flag = this.ssdCategoryPropsDictService.update(scpd);
		} else {
			PcmCategoryPropsDict scpd = this.ssdCategoryPropsDictService.get(Long.valueOf(sid));
			propSid = scpd.getPropsSid();
			scpd.setPropsName(propsName);
			scpd.setPropsDesc(propsDesc);
			scpd.setPropsCode(propsCode);
			scpd.setStatus(Long.valueOf(status));
			scpd.setChannelSid(Long.valueOf(channelSid));
			flag = this.ssdCategoryPropsDictService.update(scpd);

			PcmCategoryPropValues scpv = new PcmCategoryPropValues();
			scpv.setPropsSid(propSid);
			scpv.setChannelSid(Long.valueOf(channelSid));
			List<PcmCategoryPropValues> list = this.ssdCategoryPropValuesService.selectList(scpv);
			if (list.size() > 0) {
				for (PcmCategoryPropValues ss : list) {
					ss.setPropsName(propsName);
					if ("1".equals(status)) {
						flag = this.ssdCategoryPropValuesService.update(ss);
					} else {
						flag = this.ssdCategoryPropValuesService.delete(ss.getSid());
					}
				}
			}
		}
		if (!(insert1 == null || "".equals(insert1))) {
			System.out.println("SSD insert before:" + insert1);
			// String name = new String(insert1.getBytes("iso8859-1"),"utf-8");
			// //去掉这句Linux没有乱码,window会有乱码;加上这句window正常,Linux会有乱码
			String name = insert1;
			System.out.println("SSD insert after:" + name);
			List<PcmCategoryValuesDict> listInsert = JSON.parseArray(name,
					PcmCategoryValuesDict.class);
			for (PcmCategoryValuesDict scvd : listInsert) {
				scvd.setChannelSid(Long.valueOf(channelSid));
				Long sortOrder = this.ssdCategoryValuesDictService.getMaxSortOrder(
						Long.valueOf(channelSid), propSid);
				scvd.setSortOrder(sortOrder + 1);
				// scvd.setStatus(1L);
				scvd.setIsErpValue(0L);
				scvd.setPropsSid(propSid);
				flag = this.ssdCategoryValuesDictService.save(scvd);
				scvd.setValuesSid(scvd.getSid());
				flag = this.ssdCategoryValuesDictService.update(scvd);

				PcmCategoryPropValues scpv = new PcmCategoryPropValues();
				scpv.setPropsSid(propSid);
				scpv.setChannelSid(Long.valueOf(channelSid));
				List<PcmCategoryPropValues> list = this.ssdCategoryPropValuesService
						.selectCateVO(scpv);
				if (list.size() > 0) {
					for (PcmCategoryPropValues ss : list) {
						PcmCategoryPropValues scpv1 = new PcmCategoryPropValues();
						scpv1.setCategorySid(ss.getCategorySid());
						scpv1.setCategoryName(ss.getCategoryName());
						scpv1.setPropsSid(propSid);
						scpv1.setPropsName(propsName);
						scpv1.setValuesName(scvd.getValuesName());
						scpv1.setValuesSid(scvd.getValuesSid());
						scpv1.setChannelSid(Long.valueOf(channelSid));
						scpv1.setOptDate(new Date());
						if (scvd.getStatus().equals(Long.valueOf(1)) && "1".equals(status)) {
							flag = this.ssdCategoryPropValuesService.saveorupdate(scpv1);
						}
					}
				}
			}
		}
		if (!(update1 == null || "".equals(update1))) {
			System.out.println("SSD update before:" + update1);
			// String name = new String(update1.getBytes("iso8859-1"),"utf-8");
			String name = update1;
			System.out.println("SSD update after:" + name);
			List<PcmCategoryValuesDict> listUpdate = JSON.parseArray(name,
					PcmCategoryValuesDict.class);
			for (PcmCategoryValuesDict scvd : listUpdate) {
				this.ssdCategoryValuesDictService.update(scvd);
				PcmCategoryPropValues scpv = new PcmCategoryPropValues();
				scpv.setPropsSid(propSid);
				scpv.setChannelSid(Long.valueOf(channelSid));
				List<PcmCategoryPropValues> list = this.ssdCategoryPropValuesService
						.selectList(scpv);
				if (list.size() > 0) {
					for (PcmCategoryPropValues ss : list) {
						PcmCategoryPropValues scpv1 = new PcmCategoryPropValues();
						scpv1.setCategorySid(ss.getCategorySid());
						scpv1.setCategoryName(ss.getCategoryName());
						scpv1.setPropsSid(propSid);
						scpv1.setPropsName(propsName);
						scpv1.setValuesName(scvd.getValuesName());
						scpv1.setValuesSid(scvd.getValuesSid());
						scpv1.setChannelSid(Long.valueOf(channelSid));
						scpv1.setOptDate(new Date());
						if (scvd.getStatus().equals(Long.valueOf(1)) && "1".equals(status)) {
							flag = this.ssdCategoryPropValuesService.saveorupdate(scpv1);
						} else {
							flag = this.ssdCategoryPropValuesService.deleteorupdate(scpv1);
						}
					}
				}
			}
		}
		if (!(delete1 == null || "".equals(delete1))) {
			System.out.println("SSD delete before:" + delete1);
			// String name = new String(delete1.getBytes("iso8859-1"),"utf-8");
			String name = delete1;
			System.out.println("SSD delete after:" + name);
			List<PcmCategoryValuesDict> listDelete = JSON.parseArray(name,
					PcmCategoryValuesDict.class);
			for (PcmCategoryValuesDict scvd : listDelete) {
				scvd.setStatus(0L);
				flag = this.ssdCategoryValuesDictService.update(scvd);

				flag = this.ssdCategoryPropValuesService.deleteByValueAndChan(scvd.getValuesSid(),
						Long.valueOf(channelSid));
			}
		}

		Map<String, Object> result = new HashMap<String, Object>();
		if (flag >= 1) {
			result = ResultUtil.creComSucResult(flag);
		} else {
			result = ResultUtil.creComSucResult(flag);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/bw/propsdictEdit", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> edit(Model m, HttpServletRequest request,
			HttpServletResponse response, @RequestBody CategoryPropsDictPara propsPara) {
		PcmCategoryPropsDict scpd = this.ssdCategoryPropsDictService.get(Long.valueOf(propsPara
				.getSid()));
		JSONObject json = new JSONObject();
		json.put("propsName", scpd.getPropsName());
		json.put("propsDesc", scpd.getPropsDesc());
		json.put("propsCode", scpd.getPropsCode());
		json.put("status", scpd.getStatus());
		json.put("channelSid", scpd.getChannelSid());

		return ResultUtil.creComSucResult(json);
	}

	@ResponseBody
	@RequestMapping(value = "/bw/propsdictDel", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> del(Model m, HttpServletRequest request,
			HttpServletResponse response, @RequestBody CategoryPropsDictPara propsPara) {

		Map<String, Object> results = new HashMap<String, Object>();
		try {
			System.out.println("进入删除controller" + "                                "
					+ propsPara.getSid());
			PcmCategoryPropsDict scpd = this.ssdCategoryPropsDictService.get(Long.valueOf(propsPara
					.getSid()));
			scpd.setStatus(0L);
			Long propsSid = scpd.getPropsSid();
			System.out.println("propsSid:" + propsSid);
			int flag = this.ssdCategoryPropsDictService.update(scpd);
			System.out.println("flag:                                      " + flag);
			Long channelSid = scpd.getChannelSid();
			System.out.println("channelSid:" + channelSid);
			this.ssdCategoryPropValuesService.deleteByPropAndChan(propsSid,
					Long.valueOf(channelSid));

			if (flag == 1) {
				results = ResultUtil.creComSucResult(flag);
			} else {
				results = ResultUtil.creComSucResult(flag);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	// @RequestMapping(value = "/findProPageByParamFromPcm", method =
	// RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	@RequestMapping(value = "/bw/propscomboxList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String comboxlist(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestBody CategoryPropsDictPara propsPara) {

		PcmCategory sc = this.categoryService.get(Long.valueOf(propsPara.getSid()));
		PcmCategoryPropsDict scp = new PcmCategoryPropsDict();
		scp.setChannelSid(sc.getChannelSid());
		scp.setStatus(1L);
		List<PcmCategoryPropsDict> lists = this.ssdCategoryPropsDictService.selectList(scp);
		JSONArray jsons = new JSONArray();
		for (PcmCategoryPropsDict scpd : lists) {
			net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(scpd);
			jsons.add(json);
		}
		return jsons.toString();
	}
}
