package test.com.wangfj.product.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;

public class TestPcmBrandRelationController {

	@Test
	public void test() {
		maintainRelation();
	}

	public void maintainRelation() {

		Map<String, Object> paraMap = new HashMap<String, Object>();
		
		paraMap.put("brandSid", "1");
		paraMap.put("goupBrandSid", "1000001");
		
		String response = HttpUtil.doPost(
				"http://127.0.0.1:8081/pcm-core/pcmBrandRelation/maintainRelation.htm",
				JsonUtil.getJSONString(paraMap));
		System.out.println(response);

	}

}
