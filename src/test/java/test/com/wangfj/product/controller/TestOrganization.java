package test.com.wangfj.product.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.EfutureERP.controller.support.PcmOrganizaPara;

public class TestOrganization {
	@Test
	public void test() {
		// getOrganization();
		saveOrganization();
	}
	public void getOrganization() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageSize", 2);
		map.put("currentPage", 2);
		String response = HttpUtil.doPost(
				"http://127.0.0.1:8081/pcm-core/organization/findOrganizationByParamFromPcm.htm",
				JsonUtil.getJSONString(map));
		System.out.println(response);
	}

	// @Test
	// public void getStoreCode() {
	// Map<String, Object> map = new HashMap<String, Object>();
	// String response = HttpUtil.doPost(
	// "http://127.0.0.1:8081/pcm-core/organization/findStoreCodeByParamFromPcm.htm",
	// JsonUtil.getJSONString(map));
	// System.out.println(response);
	// }

	public void saveOrganization() {
		List<PcmOrganizaPara> paralist = new ArrayList<PcmOrganizaPara>();
		PcmOrganizaPara para = new PcmOrganizaPara();
		para.setActionCode("u");
		para.setSid(6l);
		para.setCode("c102");
		para.setAreaCode("0001");
		para.setName("门店");
		para.setStoreType("3");
		para.setType("STORE");
		para.setSuperCode("b001");
		para.setUpdateTime(new Date());
		paralist.add(para);

		String response = HttpUtil.doPost(
"http://127.0.0.1:8081/pcm-core/organization/uploadOrganizationByParamFromErp.htm",
						JsonUtil.getJSONString(paralist));
		System.out.println(response);
	}

}
