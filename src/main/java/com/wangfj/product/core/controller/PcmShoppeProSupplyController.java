package com.wangfj.product.core.controller;

import com.wangfj.core.constants.ComErrorCodeConstants;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.StringUtils;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.intf.IPcmExceptionLogService;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.core.controller.support.PcmShoppeProSupplyUploadPara;
import com.wangfj.product.supplier.domain.vo.PcmShoppeProSupplyUploadDto;
import com.wangfj.product.supplier.service.intf.IPcmShoppeProductSupplyService;
import com.wangfj.util.mq.MqRequestDataPara;
import com.wangfj.util.mq.MqUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Controller
@RequestMapping(value = "/pcmImportShoppeProSupply", produces = "application/json;charset=utf-8")
public class PcmShoppeProSupplyController {

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
	 * @param para
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = "/uploadShoppeProductSupplyFromEFuture", method = { RequestMethod.POST,
			RequestMethod.GET })
	@ResponseBody
	public String uploadShoppeProductSupplyFromEFuture(@RequestBody MqRequestDataPara para,
			HttpServletRequest request) {

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
				List<PcmShoppeProSupplyUploadPara> shoppeProSupplyParaList = JSONArray
						.toList(jsonSPS);

				String callBackUrl = paraDest.getHeader().getCallbackUrl();
				String requestMsg = "";

				for (int i = 0; i < shoppeProSupplyParaList.size(); i++) {

					PcmShoppeProSupplyUploadDto dto = new PcmShoppeProSupplyUploadDto();

					try {
						BeanUtils.copyProperties(dto, shoppeProSupplyParaList.get(i));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

					// 参数校验
					String storeCode = dto.getStoreCode();
					String supplierCode = dto.getSupplierCode();
					String action_CODE = dto.getACTION_CODE();
					if (!StringUtils.isNotEmpty(storeCode)) {
						throw new BleException(
								ComErrorCodeConstants.ErrorCode.PCMSHOPPEPRODUCTSUPPLY_STORECODE_IS_NULL
										.getErrorCode(),
								ComErrorCodeConstants.ErrorCode.PCMSHOPPEPRODUCTSUPPLY_STORECODE_IS_NULL
										.getMemo());
					}
					if (!StringUtils.isNotEmpty(supplierCode)) {
						throw new BleException(
								ComErrorCodeConstants.ErrorCode.PCMSHOPPEPRODUCTSUPPLY_SUPPLIERCODE_IS_NULL
										.getErrorCode(),
								ComErrorCodeConstants.ErrorCode.PCMSHOPPEPRODUCTSUPPLY_SUPPLIERCODE_IS_NULL
										.getMemo());
					}
					if (!StringUtils.isNotEmpty(action_CODE)) {
						throw new BleException(
								ComErrorCodeConstants.ErrorCode.PCMSHOPPEPRODUCTSUPPLY_ACTIONCODE_IS_NULL
										.getErrorCode(),
								ComErrorCodeConstants.ErrorCode.PCMSHOPPEPRODUCTSUPPLY_ACTIONCODE_IS_NULL
										.getMemo());
					}

					Integer result = shoppeProductSupplyService.uploadShoppeProSupply(dto);

					if (result == 0) {
						String dataContent = "一品多供应商关系上传时数据:" + dto.toString() + "时失败";
						requestMsg = "一品多供应商关系上传时数据:" + dto.toString() + "时失败";

						PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
						exceptionLogdto.setInterfaceName("uploadShoppeProductSupplyFromEFuture");
						exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_SUPPLY.getStatus());
						exceptionLogdto.setDataContent(paraDest.toString());
						exceptionLogdto.setErrorMessage(dataContent);

						exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
					}

				}

			}
		});

		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(paraDest.getHeader()));

	}

}
