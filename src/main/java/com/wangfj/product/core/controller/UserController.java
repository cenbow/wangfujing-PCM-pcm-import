package com.wangfj.product.core.controller;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.wangfj.core.framework.base.controller.BaseController;
import com.wangfj.core.framework.base.page.Page;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.DataUtil;
import com.wangfj.core.utils.DateUtil;
import com.wangfj.core.utils.ResultUtil;
import com.wangfj.product.constants.ConnectSAPServer;
import com.wangfj.product.core.controller.support.SelectUserPagePara;
import com.wangfj.product.core.controller.support.UpdateUsersPara;
import com.wangfj.product.core.controller.support.UsersPara;
import com.wangfj.product.demo.domain.dto.SelectUserPageDto;
import com.wangfj.product.demo.domain.dto.UpdateUsersDto;
import com.wangfj.product.demo.domain.dto.UsersDto;
import com.wangfj.product.demo.service.intf.UserService;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	private static String dateFormateStr = "yyyy-MM-dd HH:mm:ss";
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	/**
	 * 
	 * @Methods Name addUser
	 * @Create In 2015年6月26日 By kongxs
	 * @param userPara
	 * @param request
	 * @return
	 * @throws Exception
	 *             Map<String,Object>
	 */
	@RequestMapping("/saveUser")
	@ResponseBody
	public Map<String, Object> saveUser(@RequestBody @Valid UsersPara userPara,
			HttpServletRequest request) throws Exception {
		UsersDto dto = new UsersDto();
		BeanUtils.copyProperties(userPara, dto);
		dto.setBirthday(DateUtil.formatDate(userPara.getBirthdayStr(), dateFormateStr));
		userService.saveUser(dto);
		return ResultUtil.creComSucResult("");
	}

	/**
	 * 
	 * @Methods Name updateUser
	 * @Create In 2015年6月27日 By kongxs
	 * @param para
	 * @param request
	 * @return
	 * @throws Exception
	 *             Map<String,Object>
	 */
	@RequestMapping("/modifyUser")
	@ResponseBody
	public Map<String, Object> modifyUser(@RequestBody @Valid UpdateUsersPara para,
			HttpServletRequest request) throws Exception {
		UpdateUsersDto userDto = new UpdateUsersDto();
		BeanUtils.copyProperties(para, userDto);
		userService.updateUser(userDto);
		return ResultUtil.creComSucResult("");
	}

	// @RequestMapping("/getUser")
	// @ResponseBody
	// public Map<String, Object> getUser(@RequestBody @Valid GetUsersPara para,
	// HttpServletRequest request) throws Exception {
	// GetUsersDto dto = new GetUsersDto();
	// BeanUtils.copyProperties(para, dto);
	// UsersDto userDto = userService.getUser(dto);
	// return ResultUtil.creObjSucResult(userDto);
	// }

	/**
	 * 分页查询
	 * 
	 * @Methods Name selectUserPage
	 * @Create In 2015年6月26日 By kongxs
	 * @param para
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping("/selectUserPage")
	@ResponseBody
	public Map<String, Object> selectUserPage(@RequestBody @Valid SelectUserPagePara para,
			HttpServletRequest request) throws Exception {
		String uuid = UUID.randomUUID().toString();
		System.out.println(DataUtil.readRequest(request));
		// 创建dto作为方法的参数
		SelectUserPageDto dto = new SelectUserPageDto();
		BeanUtils.copyProperties(para, dto);
		// 创建实体类dto的分页对象
		Page<UsersDto> page = new Page<UsersDto>();
		page.setCurrentPage(para.getCurrentPage());
		page.setPageSize(para.getPageSize());
		// 返回实体类的dto分页对象
		page = userService.selectUserPage(dto, page);

		// 返回实体类的dto分页对象
		page = userService.selectUserPage(dto, page);
		if (true) {
			throw new BleException("01", "测试异常");
		}
		return ResultUtil.creComSucResult(page);
	}

	@RequestMapping("/testJCO2")
	@ResponseBody
	public Map<String, Object> testJCO2() throws Exception {
		logger.info("start");
		JCoDestination jCoDestination = ConnectSAPServer.Connect();

		JCoFunction function;
		String RESULT = "";
		try {
			function = jCoDestination.getRepository().getFunction("Z_SOAP_TEST");
			if (function == null)
				throw new RuntimeException("Z_SOAP_TEST not found in SAP.");
			function.getImportParameterList().setValue("USER_LOGIN", "test10");
			function.execute(jCoDestination);
			// Object returnTable =
			// function.getTableParameterList().getValue("RESULT");
			JCoParameterList exportParam = function.getExportParameterList();
			RESULT = exportParam.getString("USER");

			System.err.println(RESULT);
		} catch (JCoException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return ResultUtil.creComSucResult(RESULT);
	}

	@RequestMapping("/testJCO")
	@ResponseBody
	public Map<String, Object> testJCO() throws Exception {
		JCoDestination jCoDestination = ConnectSAPServer.Connect();

		JCoFunction function;
		String RESULT = "";
		try {
			function = jCoDestination.getRepository().getFunction("ZSAP_CALCULATE");
			if (function == null)
				throw new RuntimeException("ZSAP_CALCULATE not found in SAP.");
			function.getImportParameterList().setValue("NUMBER1", "10");
			function.getImportParameterList().setValue("NUMBER2", "1");
			function.getImportParameterList().setValue("OPERATOR", "+");
			function.execute(jCoDestination);
			// Object returnTable =
			// function.getTableParameterList().getValue("RESULT");
			JCoParameterList exportParam = function.getExportParameterList();
			RESULT = exportParam.getString("RESULT");

			System.err.println(RESULT);
		} catch (JCoException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return ResultUtil.creComSucResult(RESULT);
	}
}
