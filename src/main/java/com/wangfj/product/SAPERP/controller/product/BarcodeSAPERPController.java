package com.wangfj.product.SAPERP.controller.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.wangfj.product.SAPERP.controller.support.SupplierBarCodeFromSAPERPPara;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.impl.PcmExceptionLogService;
import com.wangfj.product.constants.JcoSAPUtils;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.maindata.domain.vo.SupplierBarCodeFromEfutureDto;
import com.wangfj.product.maindata.service.intf.IPcmBarcodeService;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.MqUtil;

/**
 * 条码表管理
 * 
 * @Class Name PcmBarcodeController
 * @Author yedong
 * @Create In 2015年7月12日
 */
@Controller
@RequestMapping("/barcodeSAPERP")
public class BarcodeSAPERPController extends BaseController {

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
	 * @return void
	 */
	@RequestMapping(value = "/uploadSupplierBarCodeFromSAPERP")
	@ResponseBody
	public String uploadSupplierBarCodeFromSAPERP(@RequestBody MqRequestDataListPara<SupplierBarCodeFromSAPERPPara> para1,
			HttpServletRequest request) {
		final MqRequestDataListPara<SupplierBarCodeFromSAPERPPara> para = para1;
		//List<SupplierBarCodeFromSAPERPPara> jsono = para.getData();//JSONObject.fromObject(para.getData());		
		List<SupplierBarCodeFromSAPERPPara> listPara = para.getData();
		// 返回信息LIST		
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
		List<String> excepList = new ArrayList<String>();
		for (int i = 0; i < listPara.size(); i++) {
			SupplierBarCodeFromSAPERPPara paras = new SupplierBarCodeFromSAPERPPara();
			try {
				BeanUtils.copyProperties(paras, listPara.get(i));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			SupplierBarCodeFromEfutureDto pslDto = new SupplierBarCodeFromEfutureDto();
			pslDto.setActionCode(paras.getACTION_CODE());
			pslDto.setActionDate(paras.getACTION_DATE());
			pslDto.setActionPerson(paras.getACTION_PERSON());
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
					pcmBarcodeService.getSupplierBarCodeFromEfuture(pslDto);
				} catch (BleException e) {
					if (ErrorCodeConstants.ErrorCode.vaildErrorCode(e.getCode())) {
						ThrowExcetpionUtil.splitExcetpion(new BleException(e.getCode(), e
								.getMessage()));
					}
					Map<String, Object> resMap = new HashMap<String, Object>();
					resMap.put("KEY_FIELD", paras.getMATNR());
					resMap.put("FLAG", 3);
					resMap.put("MESSAGE", e.getMessage());
					resList.add(resMap);
					excepList.add(pslDto.getStoreCode() + "-" + pslDto.getSbarcode() + ":"
							+ e.toString());
				}
			} else {
				Map<String, Object> resMap = new HashMap<String, Object>();
				resMap.put("KEY_FIELD", paras.getMATNR());
				resMap.put("FLAG", 3);
				resMap.put("MESSAGE", "ActionCode不能为空");
				resList.add(resMap);
				excepList.add(pslDto.getStoreCode() + "-" + pslDto.getSbarcode()
						+ "--ActionCode不能为空");
			}
		}
		// 写入异常表
		if (excepList != null && excepList.size() > 0) {
			try {
				JcoSAPUtils.functionExecute("ZFM_MD_PCM2SAP_ERROR_IN", "INPUT", resList);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
