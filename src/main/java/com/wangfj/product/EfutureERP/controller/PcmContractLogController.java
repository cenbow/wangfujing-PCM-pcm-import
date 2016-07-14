package com.wangfj.product.EfutureERP.controller;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangfj.core.constants.ComErrorCodeConstants;
import com.wangfj.core.constants.ErrorCodeConstants;
import com.wangfj.core.framework.base.controller.BaseController;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.ResultUtil;
import com.wangfj.core.utils.ThrowExcetpionUtil;
import com.wangfj.product.EfutureERP.controller.support.PcmContractLogPara;
import com.wangfj.product.maindata.domain.vo.PcmContractLogDto;
import com.wangfj.product.maindata.service.impl.PcmContractLogServiceImpl;
import com.wangfj.product.maindata.service.intf.IPcmContractLogService;
import com.wangfj.util.mq.MqRequestDataPara;

/**
 * 要约信息Controller
 * 
 * @Class Name PcmContractLogController
 * @Author liuhp
 * @Create In 2015-8-21
 */
@Controller
@RequestMapping("/contract")
public class PcmContractLogController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(PcmContractLogServiceImpl.class);

	@Autowired
	IPcmContractLogService pcmContractLogService;

	/**
	 * 门店ERP上传要约信息到PCM
	 * 
	 * @Methods Name uploadContractLogFromEFuture
	 * @Create In 2015-8-21 By liuhp
	 * @param contracts
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "/uploadContractLog", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadContractLogFromEFuture(@RequestBody MqRequestDataPara mqlist,
			HttpServletRequest request) {
		JSONObject js = JSONObject.fromObject(mqlist.getData());
		JSONArray sq = (JSONArray) js.get("data");
		List<PcmContractLogPara> contracts = JSONArray.toList(sq);
		List<PcmContractLogDto> contractLogDtos = new ArrayList<PcmContractLogDto>();
		for (int i = 0; i < contracts.size(); i++) {
			PcmContractLogPara pcmContractLogPara = new PcmContractLogPara();
			try {
				try {
					BeanUtils.copyProperties(pcmContractLogPara, contracts.get(i));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				PcmContractLogDto pcmContractLogDto = new PcmContractLogDto();
				pcmContractLogDto.setStoreCode(pcmContractLogPara.getStoreCode());
				pcmContractLogDto.setContractCode(pcmContractLogPara.getContractCode());
				if (StringUtils.isNotBlank(pcmContractLogPara.getBuyType())) {
					pcmContractLogDto.setBuyType(Integer.valueOf(pcmContractLogPara.getBuyType()));
				}
				pcmContractLogDto.setOperMode(Integer.valueOf(pcmContractLogPara.getOperMode()));
				pcmContractLogDto.setSupplyCode(pcmContractLogPara.getSupplierCode());
				pcmContractLogDto.setOptTime(new Date());
				pcmContractLogDto.setCol1(pcmContractLogPara.getManageCategory());

				if ("E".equals(pcmContractLogPara.getBusinessType())) {
					pcmContractLogDto.setBusinessType(0);
				}
				if ("C".equals(pcmContractLogPara.getBusinessType())) {
					pcmContractLogDto.setBusinessType(1);
				}
				if (StringUtils.isNotBlank(pcmContractLogPara.getInputTax())) {
					pcmContractLogDto.setInputTax(new BigDecimal(pcmContractLogPara.getInputTax()));
				}
				if (StringUtils.isNotBlank(pcmContractLogPara.getOutputTax())) {
					pcmContractLogDto
							.setOutputTax(new BigDecimal(pcmContractLogPara.getOutputTax()));
				}
				if (StringUtils.isNotBlank(pcmContractLogPara.getCommissionRate())) {
					pcmContractLogDto.setCommissionRate(new BigDecimal(pcmContractLogPara
							.getCommissionRate()));
				}

				if ("A".equals(pcmContractLogPara.getActionCode())) {
					pcmContractLogDto.setFlag(0);
				}
				if ("U".equals(pcmContractLogPara.getActionCode())) {
					pcmContractLogDto.setFlag(1);
				}

				// 经营方式

				// 经销
				if ("9".equals(pcmContractLogPara.getManageType())) {
					pcmContractLogDto.setManageType(0);
				}
				// 代销
				if ("2".equals(pcmContractLogPara.getManageType())) {
					pcmContractLogDto.setManageType(1);
				}
				// 联营
				if ("0".equals(pcmContractLogPara.getManageType())) {
					pcmContractLogDto.setManageType(2);
				}
				// 租赁
				if ("3".equals(pcmContractLogPara.getManageType())) {
					pcmContractLogDto.setManageType(4);
				}

				pcmContractLogDto.setManageCategory(pcmContractLogPara.getManageCategory());
				contractLogDtos.add(pcmContractLogDto);
			} catch (BleException e) {
				logger.error(e.getMessage());
				if (ErrorCodeConstants.ErrorCode.vaildErrorCode(e.getCode())) {
					ThrowExcetpionUtil
							.splitExcetpion(new BleException(e.getCode(), e.getMessage()));
				}
				return ResultUtil.creComErrorResult(
						ComErrorCodeConstants.ErrorCode.DATA_OPER_ERROR.getErrorCode(),
						ComErrorCodeConstants.ErrorCode.DATA_OPER_ERROR.getMemo());
			}
		}
		pcmContractLogService.uploadContractLogBatch(contractLogDtos);
		return ResultUtil.creComSucResult("添加成功");
	}

}
