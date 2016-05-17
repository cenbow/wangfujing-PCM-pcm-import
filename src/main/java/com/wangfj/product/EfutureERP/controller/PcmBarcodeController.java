package com.wangfj.product.EfutureERP.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangfj.core.constants.ErrorCodeConstants;
import com.wangfj.core.framework.base.controller.BaseController;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.ThrowExcetpionUtil;
import com.wangfj.product.EfutureERP.controller.support.SupplierBarCodeFromEfuturePara;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.impl.PcmExceptionLogService;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.maindata.domain.vo.SupplierBarCodeFromEfutureDto;
import com.wangfj.product.maindata.service.intf.IPcmBarcodeService;
import com.wangfj.util.mq.MqRequestDataPara;
import com.wangfj.util.mq.MqUtil;

/**
 * 条码表管理
 * 
 * @Class Name PcmBarcodeController
 * @Author yedong
 * @Create In 2015年7月12日
 */
@Controller
@RequestMapping("/barcode")
public class PcmBarcodeController extends BaseController {

	@Autowired
	public IPcmBarcodeService pcmBarcodeService;
	@Autowired
	private PcmExceptionLogService pcmExceptionLogService;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	/**
	 * MDERP条码从门店ERP和电商ERP上传到主数据ERP
	 * 
	 * @Methods Name uploadSupplierBarCodeFromEfuture
	 * @Create In 2015年7月13日 By yedong
	 * @param pushSupplierlist
	 * @param request
	 *            void
	 * @return
	 */
	@RequestMapping(value = "/uploadSupplierBarCodeFromEfuture")
	@ResponseBody
	public String uploadSupplierBarCodeFromEfuture(@RequestBody MqRequestDataPara para1,
			HttpServletRequest request) {
		final MqRequestDataPara para = para1;
		// taskExecutor.execute(new Runnable() {
		// @Override
		// public void run() {
		//
		// }
		// });
		JSONObject jsono = JSONObject.fromObject(para.getData());
		List<SupplierBarCodeFromEfuturePara> listPara = JSONArray.toList((JSONArray) jsono
				.get("data"));
		List<String> excepList = new ArrayList<String>();
		for (int i = 0; i < listPara.size(); i++) {
			SupplierBarCodeFromEfuturePara paras = new SupplierBarCodeFromEfuturePara();
			try {
				BeanUtils.copyProperties(paras, listPara.get(i));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			SupplierBarCodeFromEfutureDto pslDto = new SupplierBarCodeFromEfutureDto();
			pslDto.setActionCode(paras.getActionCode());
			pslDto.setActionDate(paras.getActionDate());
			pslDto.setActionPerson(paras.getActionPerson());
			pslDto.setCounterCode(paras.getCOUNTERCODE());
			pslDto.setLifnr(paras.getLIFNR());
			pslDto.setMatnr(paras.getMATNR());
			pslDto.setSaleAmount(paras.getSALEAMOUNT());
			pslDto.setSalePrice(paras.getSALEPRICE());
			pslDto.setSaleUnit(paras.getSALEUNIT());
			pslDto.setSbarcode(paras.getSBARCODE());
			pslDto.setSbarcodeName(paras.getSBARCODENAME());
			pslDto.setSbarcodeType(paras.getSBARCODETYPE());
			pslDto.setStoreCode(paras.getSTORECODE());
			if (pslDto.getActionCode() != null) {
				try {
					pcmBarcodeService.importSupplierBarCodeFromEfuture(pslDto);
				} catch (BleException e) {
					if (ErrorCodeConstants.ErrorCode.vaildErrorCode(e.getCode())) {
						ThrowExcetpionUtil.splitExcetpion(new BleException(e.getCode(), e
								.getMessage()));
					}
					excepList.add(pslDto.getStoreCode() + "-" + pslDto.getSbarcode() + ":"
							+ e.toString());
				}
			} else {
				excepList.add(pslDto.getStoreCode() + "-" + pslDto.getSbarcode()
						+ "--ActionCode不能为空");
			}
		}
		// 写入异常表
		if (excepList != null && excepList.size() > 0) {
			PcmExceptionLogDto pcmExceptionLogDto = new PcmExceptionLogDto();
			pcmExceptionLogDto.setInterfaceName("uploadSupplierBarCodeFromEfuture");
			pcmExceptionLogDto.setExceptionType(StatusCode.EXCEPTION_PRODUCT.getStatus());
			pcmExceptionLogDto.setErrorMessage(JsonUtil.getJSONString(excepList));
			pcmExceptionLogDto.setDataContent(JsonUtil.getJSONString(para));
			pcmExceptionLogService.saveExceptionLogInfo(pcmExceptionLogDto);
		}
		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(para.getHeader()));
	}
}
