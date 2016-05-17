package test.com.wangfj.product.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.EfutureERP.controller.support.PcmShoppePara;

public class TestShoppeCon {
	// @Test
	// public void getshoppe() {/* 专柜信息下发 */
	// Map map = new HashMap();
	// String response = HttpUtil.doPost(
	// "http://127.0.0.1:8081/pcm-core/shoppe/findShoppeByParamFromPcm.htm",
	// JsonUtil.getJSONString(map));
	// System.out.println(response);
	// }
	// 添加专柜信息
	@Test
	public void saveShoppe() {
		List<PcmShoppePara> paralist = new ArrayList<PcmShoppePara>();
		PcmShoppePara para = new PcmShoppePara();
		para.setActionCode("a");
		para.setBusinessTypeSid("1");
		para.setFloorCode("a100");
		para.setOrgCode("c001");
		para.setShoppeCode("c4000");
		para.setShoppeName("基层");
		paralist.add(para);
		String response = HttpUtil
				.doPost("http://127.0.0.1:8081/pcm-core/shoppe/saveOrUPdateShoppeByParamFromErp.htm",
						JsonUtil.getJSONString(paralist));
		System.out.println(response);
	}
}
