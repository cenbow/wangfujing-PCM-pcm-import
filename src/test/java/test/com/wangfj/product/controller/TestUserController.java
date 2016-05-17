package test.com.wangfj.product.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
import com.wangfj.core.constants.ComErrorCodeConstants.ErrorCode;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyConfigurer;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.product.core.controller.support.GetUsersPara;
import com.wangfj.product.core.controller.support.SelectUserPagePara;
import com.wangfj.product.core.controller.support.UpdateUsersPara;
import com.wangfj.product.core.controller.support.UsersPara;
import com.wangfj.product.organization.domain.vo.PushCounterDto;
import com.wangfj.product.organization.persistence.PcmOrganizationMapper;
import com.wangfj.product.organization.service.impl.PcmOrganizationServiceImpl;

public class TestUserController {

	@Autowired
	PcmOrganizationServiceImpl pcmOrganizationService;
	@Autowired
	public PcmOrganizationMapper pcmorganizationMapper;

	@Test
	public void test() {
		// addUser();
		// modifyUser();
		// getUser();
		selectUser();
	}

	public void gett() {
		String response = HttpUtil.doPost(
				"http://127.0.0.1:8083/pcm-syn/category/pushStatCategoryFromMdErp.htm", null);
		System.out.println(response);
	}

	public void gettt() {
		PushCounterDto pcdto = new PushCounterDto();
		String response = HttpUtil.doPost(
				"http://127.0.0.1:8081/pcm-core/shoppe/findShoppeByParamFromPcm.htm",
				JsonUtil.getJSONString(pcdto));
		System.out.println("aaaaaaaaaa" + response);
	}

	public void addUser() {
		UsersPara para = new UsersPara();
		para.setFromSystem("PCM");
		para.setName("test1");
		para.setAge(1);
		para.setBirthdayStr("2015-06-23 00:00:00");
		String response = HttpUtil.doPost("http://127.0.0.1:8081/pcm-syn/organization/saveUser.htm",
				JsonUtil.getJSONString(para));
		System.out.println(response);
	}

	public void modifyUser() {
		UpdateUsersPara para = new UpdateUsersPara();
		para.setFromSystem("PCM");
		para.setName("123456");
		para.setSid(2);
		String response = HttpUtil.doPost(
				"http://127.0.0.1:8083/pcm-syn/organization/modifyUser.htm",
				JsonUtil.getJSONString(para));
		System.out.println(response);
	}

	public void getUser() {
		GetUsersPara para = new GetUsersPara();
		para.setFromSystem("PCM");
		para.setSid(2);
		String response = HttpUtil.doPost("http://127.0.0.1:8081/pcm-core/user/getUser.htm",
				JsonUtil.getJSONString(para));
		System.out.println(response);
	}

	public void selectUser() {
		SelectUserPagePara para = new SelectUserPagePara();
		para.setFromSystem("PCM");
		para.setName("程森军");
		para.setPageSize(3);
		para.setCurrentPage(3);
		String response = HttpUtil.doPost("http://127.0.0.1:8081/pcm-core/user/selectUserPage.htm",
				JsonUtil.getJSONString(para));
		System.out.println(response);
	}

	@Test
	public void test11() {

		System.out.println(ErrorCode.ADD_CHANGE_PRICE_ERROR.getErrorCode());
		// System.out.println(PropertyConfigurer.getContextProperty("system.excption.code"));
		System.out.println(PropertyUtil.getValue("/system.properties", "system.excption.code"));
	}
}
