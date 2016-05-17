/**
 * 
 */
package test.com.wangfj.product.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.mysql.fabric.xmlrpc.base.Data;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.EfutureERP.controller.support.PcmManageCategoryPara;
import com.wangfj.product.EfutureERP.controller.support.PcmSelectCategoryPara;
import com.wangfj.product.EfutureERP.controller.support.PcmTJCategoryPara;
import com.wangfj.product.category.domain.vo.IndustryCategoryDto;
import com.wangfj.util.mq.RequestHeader;

/**
 * 品类信息上传controller测试
 * 
 * @Class Name TestCategoryController
 * @Author duanzhaole
 * @Create In 2015年7月17日
 */
public class TestCategoryController {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd.HHmmssZ");

	@Test
	public void test() {
		testAddManageCate();
	}

	/**
	 * 统计分类增量下发测试
	 * 
	 * @Methods Name testAddCate
	 * @Create In 2015年7月17日 By duanzhaole void
	 */
	public void testAddCate() {
		PcmTJCategoryPara pcmcatedto = new PcmTJCategoryPara();
		pcmcatedto.setActionCode("U");
		pcmcatedto.setSid(235L);
		pcmcatedto.setCategoryType(2);
		pcmcatedto.setFLAG("Y");;
		pcmcatedto.setNAME("礼品");
		pcmcatedto.setSJCODE("148");
		pcmcatedto.setTYPE("1");
		pcmcatedto.setSTATUS("Y");
		

		String response = HttpUtil.doPost(
				"http://127.0.0.1:8085/pcm-import/categorymanage/addTjCategoryTOErp.htm",
				JsonUtil.getJSONString(pcmcatedto));
		System.out.println(response);
	}

	public void testAddManage() {
		PcmManageCategoryPara pcmcatedto = new PcmManageCategoryPara();
		pcmcatedto.setActionCode("A");
		pcmcatedto.setStoreCode("1");
		pcmcatedto.setCategoryType(1);
		pcmcatedto.setFLAG("Y");
		pcmcatedto.setISZG("Y");
		pcmcatedto.setNAME("商品");
		pcmcatedto.setSJCODE("141");
		pcmcatedto.setTYPE("1");
		pcmcatedto.setSTATUS("Y");
		String response = HttpUtil.doPost(
				"http://127.0.0.1:8085/pcm-import/categorymanage/uploadCategoryFromEfutureERP.htm",
				JsonUtil.getJSONString(pcmcatedto));
		System.out.println(response);
	}
	
	/**
	 * 管理分类批量上传
	 */
	@Test
	public void testAddManageCate() {
		PcmManageCategoryPara pcmcatedto = new PcmManageCategoryPara();
		PcmManageCategoryPara pcmcatedto2 = new PcmManageCategoryPara();
		PcmManageCategoryPara pcmcatedto3 = new PcmManageCategoryPara();
//		pcmcatedto.setCODE("201330150029");
		pcmcatedto.setSid(287L);
		pcmcatedto.setActionCode("D");
//		pcmcatedto.setActionDate(new Date()+"");
//		pcmcatedto.setActionPerson("test");
		
		pcmcatedto2.setActionCode("D");
		pcmcatedto2.setSid(288L);
//		pcmcatedto2.setActionDate(new Date()+"");
//		pcmcatedto2.setActionPerson("test");
//		pcmcatedto2.setSTATUS("Y");

		pcmcatedto3.setActionCode("D");
		pcmcatedto3.setSid(289L);
//		pcmcatedto2.setActionDate(new Date()+"");
//		pcmcatedto2.setActionPerson("test");
//		pcmcatedto3.setCODE("201530150030");
//		pcmcatedto3.setStoreCode("1");
//		pcmcatedto3.setCategoryType(1);
//		pcmcatedto3.setFLAG("Y");
//		pcmcatedto3.setISZG("N");
//		pcmcatedto3.setNAME("sssss");
//		pcmcatedto3.setSJCODE("201530150028");
//		pcmcatedto3.setTYPE("1");
//		pcmcatedto3.setSTATUS("Y");
		List<PcmManageCategoryPara> lists=new ArrayList<PcmManageCategoryPara>();
		lists.add(pcmcatedto);
		lists.add(pcmcatedto2);
		lists.add(pcmcatedto3);
		Map<String, Object> maps=new HashMap<String, Object>();
//		Header h=new Header();
	    RequestHeader rh=new RequestHeader();
		maps.put("data", lists);
		
		maps.put("header",rh);
		JSONObject js1=JSONObject.fromObject(maps);
		Map<String, Object> maps2=new HashMap<String, Object>();
		maps2.put("data", js1.toString());
		JSONObject js=JSONObject.fromObject(maps2);
		
		
//		MqRequestDataPara mqlist=new MqRequestDataPara();

//		mqlist.setHeader(header);
//		MqRequestDataListPara<PcmManageCategoryPara> mqlist =new MqRequestDataListPara<PcmManageCategoryPara>();
//		mqlist.setData(listpara);
//		RequestHeader he=new RequestHeader();
//		mqlist.setHeader(he);
		
		String response = HttpUtil.doPost(
				"http://127.0.0.1:8085/pcm-import/categorymanage/uploadCategoryFromEfutureERP.htm",
				JsonUtil.getJSONString(js));
		System.out.println(response);
	}
	
	/**
	 * 对接促销工业分类增量下发
	 * @Methods Name testAddIndustryCategory
	 * @Create In 2015年9月11日 By duanzhaole void
	 */
	public void testAddIndustryCategory() {
		IndustryCategoryDto pcmcatedto = new IndustryCategoryDto();
		pcmcatedto.setActionCode("A");
		pcmcatedto.setName("短袖衬衫");
		pcmcatedto.setParentCode("145");
		String response = HttpUtil.doPost(
				"http://127.0.0.1:8085/pcm-import/categorytoep/addIndustryCategoryTOEp.htm",
				JsonUtil.getJSONString(pcmcatedto));
		System.out.println(response);
	}
	
	
	/**
	 * 对接促销管理分类增量下发
	 * @Methods Name testAddIndustryCategory
	 * @Create In 2015年9月11日 By duanzhaole void
	 */
	public void testAddManageCategory() {
		PcmManageCategoryPara pcmcatedto = new PcmManageCategoryPara();
		pcmcatedto.setActionCode("A");
		pcmcatedto.setCODE("2013301500010");
		pcmcatedto.setStoreCode("1");
		pcmcatedto.setCategoryType(1);
		pcmcatedto.setFLAG("Y");
		pcmcatedto.setISZG("Y");
		pcmcatedto.setNAME("桌子");
		pcmcatedto.setSJCODE("201330150005");
		pcmcatedto.setTYPE("1");
		pcmcatedto.setSTATUS("Y");
		String response = HttpUtil.doPost(
				"http://127.0.0.1:8085/pcm-import/categorytoep/addManageCategoryTOEp.htm",
				JsonUtil.getJSONString(pcmcatedto));
		System.out.println(response);
	}

	/**
	 * 修改品类
	 * 
	 * @Methods Name updateCateByParam
	 * @Create In 2015年7月29日 By duanzhaole void
	 */
	public void updateCateByParam() {
		PcmSelectCategoryPara pcmcatedto = new PcmSelectCategoryPara();
		// pcmcatedto.setFromSystem("CMS");
		pcmcatedto.setSid(175L);
		pcmcatedto.setCategoryType(1);
		pcmcatedto.setIsLeaf("Y");
		pcmcatedto.setIsParent(1);
		pcmcatedto.setIsMarket("Y");
		pcmcatedto.setName("黄金");
		pcmcatedto.setLevel(1);
		pcmcatedto.setParentSid("MC_WFJ");
		pcmcatedto.setStatus("Y");
		pcmcatedto.setShopSid("11");
		String response = HttpUtil.doPost(
				"http://127.0.0.1:8081/pcm-core/categoryinfocontroller/uploadCategoryFromEfutureERP.htm",
				JsonUtil.getJSONString(pcmcatedto));
		System.out.println(response);
	}

	
}
