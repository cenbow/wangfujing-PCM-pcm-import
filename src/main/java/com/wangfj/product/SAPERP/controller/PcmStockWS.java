package com.wangfj.product.SAPERP.controller;

import java.lang.reflect.InvocationTargetException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangfj.core.constants.ComErrorCodeConstants.ErrorCode;
import com.wangfj.product.SAPERP.controller.support.PcmStockInfoPara;
import com.wangfj.product.stocks.domain.vo.PcmStockDto;
import com.wangfj.product.stocks.service.intf.IPcmStockService;
import com.wangfj.util.Constants;

@WebService(serviceName = "PcmStockWS")
public class PcmStockWS {
	@Autowired
	private IPcmStockService pcmStockService;

	@WebMethod(operationName = "findStockCountFromPcm", action = "findStockCountFromPcm")
	@WebResult(name = "OUTPUT")
	public PcmStockInfoPara findStockCountFromPcm(
			@WebParam(name = "INPUT") PcmStockInfoPara pcmStockPara) {
		PcmStockDto dto = new PcmStockDto();
		try {
			BeanUtils.copyProperties(dto, pcmStockPara);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		if (StringUtils.isNotBlank(dto.getShoppeProSid())) {
			dto.setStockTypeSid(Constants.PCMSTOCK_TYPE_SALE);
			String shoppeProSid = dto.getShoppeProSid() + Constants.DEFAULT_CHANNEL_SID;
			if (pcmStockPara.getChannelSid() != null) {
				shoppeProSid = dto.getShoppeProSid() + pcmStockPara.getChannelSid();
			}
			Integer proSum = (int) pcmStockService.findStockCountFromPcm(shoppeProSid, dto);
			pcmStockPara.setSuccess("true");
			pcmStockPara.setProSum(proSum);
		} else {
			pcmStockPara.setSuccess("false");
			pcmStockPara.setErrorMsg(ErrorCode.STOCK_SHOPPEPROSID_IS_NULL.getMemo());
		}

		return pcmStockPara;
	}
}