package test.com.wangfj.product.controller;

import org.junit.Test;

import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.core.controller.support.SelectPcmBrandPagePara;

import java.util.HashMap;
import java.util.Map;

public class TestPcmBrandController {

	@Test
	public void test() {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> shoppeProductMap = null;
		shoppeProductMap = new HashMap<String, Object>();
		shoppeProductMap.put("sid", 22);
		shoppeProductMap.put("type", 1);
		resultMap.put("shoppeProductMap", shoppeProductMap);

		Map<String,Object> shoppeProductMap1 = (Map<String, Object>) resultMap.get("shoppeProductMap");
		System.out.println(shoppeProductMap1);
		System.out.println(shoppeProductMap1 != null);

	}

	@Test
	public void findBrandForPage() {

		SelectPcmBrandPagePara pcmBrandPara = new SelectPcmBrandPagePara();
		pcmBrandPara.setFromSystem("PCM");
		pcmBrandPara.setCurrentPage(2);
		pcmBrandPara.setPageSize(1);
		pcmBrandPara.setBrandName("a");

		String response = HttpUtil.doPost(
				"http://127.0.0.1:8081/pcm-core/pcmBrand/findBrandForPage.htm",
				JsonUtil.getJSONString(pcmBrandPara));
		System.out.println(response);

	}

}
