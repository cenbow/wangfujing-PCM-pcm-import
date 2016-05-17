package test.com.wangfj.product.controller;

import org.junit.Test;

import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.core.controller.support.PcmBrandGroupPara;
import com.wangfj.product.core.controller.support.SelectPcmBrandGroupPagePara;

public class TestPcmBrandGroupController {

	@Test
	public void test() {

		// updateBrandGroup();
		// findBrandGroup();
		findBrandGroupForPage();

	}

	@Test
	public void findBrandGroupForPage() {

		SelectPcmBrandGroupPagePara pcmBrandGroupPara = new SelectPcmBrandGroupPagePara();
		pcmBrandGroupPara.setFromSystem("PCM");
		pcmBrandGroupPara.setCurrentPage(2);
		pcmBrandGroupPara.setPageSize(3);
		pcmBrandGroupPara.setBrandName("耐");

		String response = HttpUtil.doPost(
				"http://127.0.0.1:8081/pcm-core/pcmBrandGroup/findBrandGroupForPage.htm",
				JsonUtil.getJSONString(pcmBrandGroupPara));
		System.out.println(response);

	}

	@Test
	public void findBrandGroup() {

		PcmBrandGroupPara pcmBrandGroupPara = new PcmBrandGroupPara();

		pcmBrandGroupPara.setBrandSid("1000002");
		pcmBrandGroupPara.setBrandName("耐");
		// pcmBrandGroupPara.setBrandNameAlias("nai");
		// pcmBrandGroupPara.setBrandNameEn("");
		// pcmBrandGroupPara.setBrandNameSpell("");

		String response = HttpUtil.doPost(
				"http://127.0.0.1:8081/pcm-core/pcmBrandGroup/findBrandGroup.htm",
				JsonUtil.getJSONString(pcmBrandGroupPara));
		System.out.println(response);

	}

	public void updateBrandGroup() {

		PcmBrandGroupPara pcmBrandGroupPara = new PcmBrandGroupPara();

		pcmBrandGroupPara.setSid(2L);
		pcmBrandGroupPara.setBrandName("耐克");
		pcmBrandGroupPara.setBrandSid("1000001");
		pcmBrandGroupPara.setBrandNameSpell("naike");

		String response = HttpUtil.doPost(
				"http://127.0.0.1:8081/pcm-core/pcmBrandGroup/updateBrandGroup.htm",
				JsonUtil.getJSONString(pcmBrandGroupPara));
		System.out.println(response);

	}

}
