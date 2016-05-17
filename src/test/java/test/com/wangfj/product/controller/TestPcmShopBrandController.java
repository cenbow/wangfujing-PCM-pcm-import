package test.com.wangfj.product.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.util.mq.RequestHeader;

public class TestPcmShopBrandController {

	@Test
	public void test() {
		String url = PropertyUtil.getSystemUrl("brand.pushBrandUrl");
		System.out.println(url);
	}

	@Test
	public void uploadShopBrandRelationFromEBusiness() {

		RequestHeader header = new RequestHeader();
		header.setCallbackUrl("callBackUrlTest");
		Map<String, Object> shopBrandPara = new HashMap<String, Object>();

		shopBrandPara.put("STORECODE", "21011");
		shopBrandPara.put("BRANDCODE", "1000151");
		shopBrandPara.put("BRANDNAME", "测试上传2");
		shopBrandPara.put("STORETYPE", "1");
		shopBrandPara.put("BRANDPIC1", "201511191038.png");
		shopBrandPara.put("BRANDPIC2", "201511191038.jpg");
		shopBrandPara.put("ACTIONCODE", "A");
		// shopBrandPara.put("ACTIONCODE", "a");
		// shopBrandPara.put("ACTIONCODE", "D");
		// shopBrandPara.put("ACTIONCODE", "U");

		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		dataList.add(shopBrandPara);

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("version", "1");
		dataMap.put("header", "");
		dataMap.put("data", dataList);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("data", JsonUtil.getJSONString(dataMap));
		data.put("header", header);
		System.out.println(JsonUtil.getJSONString(data));

		String url = "http://127.0.0.1:8085/pcm-import/shopBrand/uploadShopBrandRelationFromEBusiness.htm";
		String response = HttpUtil.doPost(url, JsonUtil.getJSONString(data));
		System.out.println(response);

	}

	@Test
	public void uploadShopBrandRelation() {

		RequestHeader header = new RequestHeader();
		header.setCallbackUrl("callBackUrlTest");
		Map<String, Object> shopBrandPara = new HashMap<String, Object>();

		shopBrandPara.put("storeCode", "21011");
		shopBrandPara.put("brandCode", "1000151");
		shopBrandPara.put("brandName", "测试上传2");
		shopBrandPara.put("storeType", "1");
		shopBrandPara.put("actionCode", "A");
		// shopBrandPara.put("actionCode", "a");
		// shopBrandPara.put("actionCode", "D");
		// shopBrandPara.put("actionCode", "U");

		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		dataList.add(shopBrandPara);

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("version", "1");
		dataMap.put("header", "");
		dataMap.put("data", dataList);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("data", JsonUtil.getJSONString(dataMap));
		data.put("header", header);
		System.out.println(JsonUtil.getJSONString(data));

		String url = "http://127.0.0.1:8085/pcm-import/shopBrand/uploadShopBrandRelation.htm";
		String response = HttpUtil.doPost(url, JsonUtil.getJSONString(data));
		System.out.println(response);

	}

}
