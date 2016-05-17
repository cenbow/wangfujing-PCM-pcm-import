package com.wangfj.product.core.controller.supplier;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.intf.IPcmExceptionLogService;
import com.wangfj.product.constants.JcoSAPUtils;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.core.controller.supplier.support.PcmShoppeProductSupplierEBusinessPara;
import com.wangfj.product.supplier.domain.vo.PcmShoppeProSupplyUploadDto;
import com.wangfj.product.supplier.service.intf.IPcmShoppeProductSupplyService;
import com.wangfj.util.mq.MqRequestDataPara;
import com.wangfj.util.mq.MqUtil;

@Controller
@RequestMapping(value = "/pcmShoppeProductSupplierEBusiness", produces = "application/json;charset=utf-8")
public class PcmShoppeProductSupplierEBusinessController {

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private IPcmShoppeProductSupplyService shoppeProductSupplyService;

	@Autowired
	private IPcmExceptionLogService exceptionLogService;

	/**
	 * 一品多供应商关系上传
	 * 
	 * @Methods Name uploadShoppeProductSupplyFromEFuture
	 * @Create In 2015-8-28 By wangxuan
	 * @param shoppeProSupplyParaList
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = "/uploadShoppeProductSupplier", method = { RequestMethod.POST,
			RequestMethod.GET })
	@ResponseBody
	public String uploadShoppeProductSupplier(@RequestBody MqRequestDataPara para) {

		final MqRequestDataPara paraDest = new MqRequestDataPara();
		try {
			BeanUtils.copyProperties(paraDest, para);
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}

		taskExecutor.execute(new Runnable() {

			@Override
			public void run() {

				JSONObject jsonData = JSONObject.fromObject(paraDest.getData());
				JSONArray jsonSPS = JSONArray.fromObject(jsonData.get("data"));
				List<PcmShoppeProductSupplierEBusinessPara> paraList = getListShoppeProductSupplierPara(jsonSPS);

				String callBackUrl = paraDest.getHeader().getCallbackUrl();
				String requestMsg = "";

				for (int i = 0; i < paraList.size(); i++) {

					PcmShoppeProductSupplierEBusinessPara tempPara = new PcmShoppeProductSupplierEBusinessPara();
					tempPara = paraList.get(i);

					PcmShoppeProSupplyUploadDto dto = new PcmShoppeProSupplyUploadDto();

					dto.setStoreCode(tempPara.getSTORECODE());
					dto.setSupplierCode(tempPara.getLIFNR());
					dto.setProductCode(tempPara.getMATNR());
					dto.setSupplierProductCode(tempPara.getS_MATNR());
					dto.setACTION_CODE(tempPara.getACTIONCODE());
					dto.setACTION_DATE(tempPara.getACTIONDATE());
					dto.setACTION_PERSON(tempPara.getACTIONPERSON());

					Integer result = shoppeProductSupplyService.uploadShoppeProSupply(dto);

					if (result == 0) {
						String dataContent = "一品多供应商关系上传时数据库操作:" + dto.toString() + "数据时失败";
						requestMsg = "一品多供应商关系上传时数据库操作第:" + dto.toString() + "数据时失败";

						PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
						exceptionLogdto.setInterfaceName("uploadShoppeProductSupplyFromEFuture");
						exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_SUPPLY.getStatus());
						exceptionLogdto.setDataContent(paraDest.toString());
						exceptionLogdto.setErrorMessage(dataContent);
						exceptionLogService.saveExceptionLogInfo(exceptionLogdto);

						List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("KEY_FIELD", dto.getSupplierCode());
						map.put("FLAG", "9");
						map.put("MESSAGE", dataContent);
						JcoSAPUtils.functionExecute("ZFM_MD_PCM2SAP_ERROR_IN", "INPUT", listMap);
					}

				}

			}
		});

		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(paraDest.getHeader()));

	}

	private List<PcmShoppeProductSupplierEBusinessPara> getListShoppeProductSupplierPara(
			JSONArray jsonArray) {

		List<PcmShoppeProductSupplierEBusinessPara> paraList = new ArrayList<PcmShoppeProductSupplierEBusinessPara>();
		if (jsonArray != null) {
			for (int i = 0; i < jsonArray.size(); i++) {
				PcmShoppeProductSupplierEBusinessPara para = new PcmShoppeProductSupplierEBusinessPara();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				para.setACTIONCODE(jsonObject.get("ACTIONCODE") + "");
				para.setACTIONDATE(jsonObject.get("ACTIONDATE") + "");
				para.setACTIONPERSON(jsonObject.get("ACTIONPERSON") + "");
				para.setLIFNR(jsonObject.get("LIFNR") + "");
				para.setMATNR(jsonObject.get("MATNR") + "");
				para.setS_MATNR(jsonObject.get("S_MATNR") + "");
				para.setSTORECODE(jsonObject.get("STORECODE") + "");
				paraList.add(para);
			}
		}

		return paraList;
	}

}
