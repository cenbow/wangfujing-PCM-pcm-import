package test.com.wangfj.product.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.EfutureERP.controller.support.PcmShoppeProductSupplyPara;

public class TestPcmShoppeProductSupplyRelationController {
	@Test
	public void test() {
		saveSupplyInfo();
	}

	public void saveSupplyInfo() {
		List<PcmShoppeProductSupplyPara> list = new ArrayList<PcmShoppeProductSupplyPara>();
		PcmShoppeProductSupplyPara dto = new PcmShoppeProductSupplyPara();
		dto.setActionCode("a");
		dto.setShopPuductSid("5");
		dto.setSupplySid("5");
		list.add(dto);
		String response = HttpUtil
				.doPost("http://127.0.0.1:8081/pcm-core/pcmShoppeProductSupply/uploadShoppeProductSupplyFromEFuture.htm",
						JsonUtil.getJSONString(list));
		System.out.println(response);
	}
}
