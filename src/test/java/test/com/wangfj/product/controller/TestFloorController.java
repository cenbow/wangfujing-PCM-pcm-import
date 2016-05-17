package test.com.wangfj.product.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.EfutureERP.controller.support.PcmFloorPara;
import com.wangfj.product.organization.domain.vo.PcmFloorDto;

public class TestFloorController {
	@Test
	public void test() {
		// getFloor();
		// saveFloor();
		updatefloor();
	}

	public void getFloor() {/* 楼层信息下发 */

		PcmFloorDto pfDto = new PcmFloorDto();
		String response = HttpUtil.doPost(
				"http://127.0.0.1:8081/pcm-core/floor/findFloorByParamFromPcm.htm",
				JsonUtil.getJSONString(pfDto));
		System.out.println(response);
	}

	public void saveFloor() {
		List<PcmFloorPara> floorPara = new ArrayList<PcmFloorPara>();
		PcmFloorPara para = new PcmFloorPara();
		para.setActionCode("a");
		// para.setCode("s310");
		para.setStoreCode("s400");
		para.setName("三层百货楼层名称");
		floorPara.add(para);
		String response = HttpUtil
				.doPost("http://127.0.0.1:8081/pcm-core/floor/uploadFloorByParamFromPcm.htm",
						JsonUtil.getJSONString(floorPara));
		System.out.println(response);
	}

	public void updatefloor() {
		List<PcmFloorPara> floorPara = new ArrayList<PcmFloorPara>();
		PcmFloorPara para = new PcmFloorPara();
		para.setSid(7l);
		para.setActionCode("u");
		// para.setOldCode("s320");
		para.setCode("s650");
		para.setStoreCode("c103");
		para.setName("四层楼");
		// para.setOldName("三层大楼");
		floorPara.add(para);
		String response = HttpUtil
				.doPost("http://127.0.0.1:8081/pcm-core/floor/updateFloorByParamFromPcm.htm",
						JsonUtil.getJSONString(floorPara));
		System.out.println(response);
	}
}


