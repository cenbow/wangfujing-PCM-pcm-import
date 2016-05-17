package test.com.wangfj.product.controller;

import org.junit.Test;

import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.core.controller.support.SelectPcmBrandPagePara;

public class TestPcmBrandController {

	@Test
	public void test() {

		// updatePcmBrand();
		// findBrand();
		findBrandForPage();

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
