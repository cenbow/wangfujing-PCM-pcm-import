package com.wangfj.product.core.controller;

import com.wangfj.core.framework.base.controller.BaseController;
import com.wangfj.core.utils.*;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.intf.IPcmExceptionLogService;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.core.controller.support.PcmSupplyInfoPara;
import com.wangfj.product.supplier.domain.entity.PcmSupplyInfo;
import com.wangfj.product.supplier.service.intf.IPcmSupplyInfoService;
import com.wangfj.util.Constants;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商主数据从门店ERP上传到Pcm
 * 
 * @Class Name PcmSupplyInfoController
 * @Author wangxuan
 * @Create In 2015-8-25
 */
@Controller
@RequestMapping(value = "/pcmSupplyInfo", produces = "application/json; charset=utf-8")
public class PcmSupplierInfoController extends BaseController {

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private IPcmSupplyInfoService pcmSupplyInfoService;

	@Autowired
	private IPcmExceptionLogService exceptionLogService;

	// /**
	// * 供应商主数据从门店ERP上传到Pcm
	// *
	// * @Methods Name uploadPcmSupplyInfoFromERP
	// * @Create In 2015-8-25 By wangxuan
	// * @param para
	// * @return String
	// */
	// @RequestMapping(value = "/uploadPcmSupplyInfoFromEFutureERP", method = {
	// RequestMethod.GET,
	// RequestMethod.POST })
	// @ResponseBody
	// public String uploadPcmSupplyInfoFromEFutureERP(@RequestBody
	// MqRequestDataPara para,
	// HttpServletRequest request) {
	//
	// final MqRequestDataPara paraDest = new MqRequestDataPara();
	// try {
	// BeanUtils.copyProperties(paraDest, para);
	// } catch (IllegalAccessException e1) {
	// e1.printStackTrace();
	// } catch (InvocationTargetException e1) {
	// e1.printStackTrace();
	// }
	//
	// taskExecutor.execute(new Runnable() {
	//
	// @Override
	// public void run() {
	//
	// JSONObject jsonData = JSONObject.fromObject(paraDest.getData());
	// JSONArray jsonSupply = JSONArray.fromObject(jsonData.get("data"));
	// List<PcmSupplyInfoPara> supplyInfoParaList =
	// JSONArray.toList(jsonSupply);
	//
	// String callBackUrl = paraDest.getHeader().getCallbackUrl();
	// String requestMsg = "";
	// /* 将得到的参数赋到list中 */
	// for (int i = 0; i < supplyInfoParaList.size(); i++) {
	//
	// PcmSupplyInfoPara tempPara = new PcmSupplyInfoPara();
	// try {
	// BeanUtils.copyProperties(tempPara, supplyInfoParaList.get(i));
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// } catch (InvocationTargetException e) {
	// e.printStackTrace();
	// }
	//
	// PcmSupplyInfo supplyInfo = transformParaToEntity(tempPara);
	//
	// String action_CODE = tempPara.getACTION_CODE();
	// if (StringUtils.isNotEmpty(action_CODE)) {
	//
	// Integer result = Constants.PUBLIC_0;
	// if (action_CODE.toUpperCase().equals(Constants.A)) {
	// result = pcmSupplyInfoService.addSupplyInfo(supplyInfo);
	// if (result == 0) {
	// String dataContent = "向数据库插入:" + supplyInfo.toString() + "时失败";
	// requestMsg = "向数据库插入:" + supplyInfo.toString() + "时失败";
	// PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
	// exceptionLogdto
	// .setInterfaceName("uploadPcmSupplyInfoFromEFutureERP");
	// exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_SUPPLY
	// .getStatus());
	// exceptionLogdto.setDataContent(paraDest.toString());
	// exceptionLogdto.setErrorMessage(dataContent);
	// exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
	// } else {
	//
	// // 供应商信息下发（增量）
	// final Long sid = supplyInfo.getSid();
	// if (sid != null) {
	// taskExecutor.execute(new Runnable() {
	// @Override
	// public void run() {
	// Map<String, Object> paraMap = new HashMap<String, Object>();
	// paraMap.put("sid", sid);
	// paraMap.put("actionCode", Constants.A);
	// String url = PropertyUtil.getSystemUrl("pcm-syn")
	// + "pcmSynSupplyInfo/pushSupplyInfo.htm";
	// String json = JsonUtil.getJSONString(paraMap);
	// HttpUtil.doPost(url, json);
	// }
	// });
	// }
	//
	// }
	// }
	//
	// if (action_CODE.toUpperCase().equals(Constants.U)) {
	// result = pcmSupplyInfoService.updateSupplyInfoBySupplyCode(supplyInfo);
	// if (result == 0) {
	// String dataContent = "数据库修改:" + supplyInfo.toString() + "时失败";
	// requestMsg = "数据库修改:" + supplyInfo.toString() + "时失败";
	// PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
	// exceptionLogdto
	// .setInterfaceName("uploadPcmSupplyInfoFromEFutureERP");
	// exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_SUPPLY
	// .getStatus());
	// exceptionLogdto.setDataContent(paraDest.toString());
	// exceptionLogdto.setErrorMessage(dataContent);
	// exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
	//
	// } else {
	//
	// // 供应商信息下发（增量）
	// final String supplyCode = supplyInfo.getSupplyCode();
	// final String shopSid = supplyInfo.getShopSid();
	// taskExecutor.execute(new Runnable() {
	// @Override
	// public void run() {
	// Map<String, Object> paraMap = new HashMap<String, Object>();
	// paraMap.put("supplyCode", supplyCode);
	// paraMap.put("shopSid", shopSid);
	// paraMap.put("actionCode", Constants.U);
	// String url = PropertyUtil.getSystemUrl("pcm-syn")
	// + "pcmSynSupplyInfo/pushSupplyInfo.htm";
	// String json = JsonUtil.getJSONString(paraMap);
	// HttpUtil.doPost(url, json);
	// }
	// });
	// }
	// }
	// }
	//
	// }
	// }
	//
	// });
	//
	// return
	// JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(paraDest.getHeader()));
	// }

	/**
	 * 供应商主数据从门店ERP上传到Pcm
	 *
	 * @Methods Name uploadPcmSupplyInfoFromERP
	 * @Create In 2015-8-25 By wangxuan
	 * @param para
	 * @return String
	 */
	@RequestMapping(value = "/uploadPcmSupplyInfoFromEFutureERP", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String uploadPcmSupplyInfoFromEFutureERP(@RequestBody MqRequestDataPara para,
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
				JSONArray jsonSupply = JSONArray.fromObject(jsonData.get("data"));
				List<PcmSupplyInfoPara> supplyInfoParaList = JSONArray.toList(jsonSupply);

				String callBackUrl = paraDest.getHeader().getCallbackUrl();
				String requestMsg = "";
				/* 将得到的参数赋到list中 */
				for (int i = 0; i < supplyInfoParaList.size(); i++) {

					PcmSupplyInfoPara tempPara = new PcmSupplyInfoPara();
					try {
						BeanUtils.copyProperties(tempPara, supplyInfoParaList.get(i));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

					PcmSupplyInfo supplyInfo = transformParaToEntity(tempPara);

					Map<String, Object> resultMap = pcmSupplyInfoService
							.uploadSupplierFromEFutureERP(supplyInfo);
					String result = resultMap.get("result") + "";
					if (result.equals(Constants.PUBLIC_0 + "")) {
						String dataContent = "门店上传:" + supplyInfo.toString() + "时失败";
						requestMsg = "门店上传:" + supplyInfo.toString() + "时失败";
						PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
						exceptionLogdto.setInterfaceName("uploadPcmSupplyInfoFromEFutureERP");
						exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_SUPPLY.getStatus());
						exceptionLogdto.setDataContent(paraDest.toString());
						exceptionLogdto.setErrorMessage(dataContent);
						exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
					} else if (result.equals(Constants.PUBLIC_1 + "")) {
						// 供应商信息下发（增量）
						final String sid = resultMap.get("sid") + "";
						final String actionCode = resultMap.get("actionCode") + "";
						if (StringUtils.isNotEmpty(sid)) {
							taskExecutor.execute(new Runnable() {
								@Override
								public void run() {
									Map<String, Object> paraMap = new HashMap<String, Object>();
									paraMap.put("sid", sid);
									paraMap.put("actionCode", actionCode);
									String url = PropertyUtil.getSystemUrl("pcm-syn")
											+ "pcmSynSupplyInfo/pushSupplyInfo.htm";
									String json = JsonUtil.getJSONString(paraMap);
									HttpUtil.doPost(url, json);
								}
							});
						}
					}
				}
			}
		});

		return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(paraDest.getHeader()));
	}

	private PcmSupplyInfo transformParaToEntity(PcmSupplyInfoPara tempPara) {

		PcmSupplyInfo supplyInfo = new PcmSupplyInfo();

		if (StringUtils.isNotEmpty(tempPara.getSTORECODE())) {
			supplyInfo.setShopSid(tempPara.getSTORECODE());
		}

		if (StringUtils.isNotEmpty(tempPara.getSUPPLIERCODE())) {
			supplyInfo.setSupplyCode(tempPara.getSUPPLIERCODE());
		}

		if (StringUtils.isNotEmpty(tempPara.getSUPPLIERNAME())) {
			supplyInfo.setSupplyName(tempPara.getSUPPLIERNAME());
		}

		String businessPattern = tempPara.getBUSINESSPATTERN();
		if (StringUtils.isNotEmpty(businessPattern)) {

			if (businessPattern.equals(Constants.PCMSUPPLYINFO_BUSINESS_PATTERN_Z1_STR)) {
				supplyInfo.setBusinessPattern(Constants.PCMSUPPLYINFO_BUSINESS_PATTERN_Z1);
			}

			if (businessPattern.equals(Constants.PCMSUPPLYINFO_BUSINESS_PATTERN_Z2_STR)) {
				supplyInfo.setBusinessPattern(Constants.PCMSUPPLYINFO_BUSINESS_PATTERN_Z2);
			}

			if (businessPattern.equals(Constants.PCMSUPPLYINFO_BUSINESS_PATTERN_Z3_STR)) {
				supplyInfo.setBusinessPattern(Constants.PCMSUPPLYINFO_BUSINESS_PATTERN_Z3);
			}

			if (businessPattern.equals(Constants.PCMSUPPLYINFO_BUSINESS_PATTERN_Z4_STR)) {
				supplyInfo.setBusinessPattern(Constants.PCMSUPPLYINFO_BUSINESS_PATTERN_Z4);
			}

			if (businessPattern.equals(Constants.PCMSUPPLYINFO_BUSINESS_PATTERN_Z5_STR)) {
				supplyInfo.setBusinessPattern(Constants.PCMSUPPLYINFO_BUSINESS_PATTERN_Z5);
			}

		}

		if (StringUtils.isNotEmpty(tempPara.getSHOARTNAME())) {
			supplyInfo.setShortName(tempPara.getSHOARTNAME());
		}

		if (StringUtils.isNotEmpty(tempPara.getTEL_NUMBER())) {
			supplyInfo.setPhone(tempPara.getTEL_NUMBER());
		}

		if (StringUtils.isNotEmpty(tempPara.getSMTP_ADDR())) {
			supplyInfo.setEmail(tempPara.getSMTP_ADDR());
		}

		if (StringUtils.isNotEmpty(tempPara.getFAX_NUMBER())) {
			supplyInfo.setFax(tempPara.getFAX_NUMBER());
		}

		// 供应商状态
		String status = tempPara.getSTATUS();
		if (StringUtils.isNotEmpty(status)) {

			if (status.equals(Constants.PCMSUPPLYINFO_STATUS_Y_TXT)) {
				supplyInfo.setStatus(Constants.PCMSUPPLYINFO_STATUS_Y_CODE);
			}

			if (status.equals(Constants.PCMSUPPLYINFO_STATUS_T_TXT)) {
				supplyInfo.setStatus(Constants.PCMSUPPLYINFO_STATUS_T_CODE);
			}

			if (status.equals(Constants.PCMSUPPLYINFO_STATUS_N_TXT)) {
				supplyInfo.setStatus(Constants.PCMSUPPLYINFO_STATUS_N_CODE);
			}

			if (status.equals(Constants.PCMSUPPLYINFO_STATUS_L_TXT)) {
				supplyInfo.setStatus(Constants.PCMSUPPLYINFO_STATUS_L_CODE);
			}

			if (status.equals(Constants.PCMSUPPLYINFO_STATUS_3_TXT)) {
				supplyInfo.setStatus(Constants.PCMSUPPLYINFO_STATUS_3_CODE);
			}

			if (status.equals(Constants.PCMSUPPLYINFO_STATUS_4_TXT)) {
				supplyInfo.setStatus(Constants.PCMSUPPLYINFO_STATUS_4_CODE);
			}

			if (status.equals(Constants.PCMSUPPLYINFO_STATUS_5_TXT)) {
				supplyInfo.setStatus(Constants.PCMSUPPLYINFO_STATUS_5_CODE);
			}

			if (status.equals(Constants.PCMSUPPLYINFO_STATUS_6_TXT)) {
				supplyInfo.setStatus(Constants.PCMSUPPLYINFO_STATUS_6_CODE);
			}

		}

		if (StringUtils.isNotEmpty(tempPara.getCOUNTRY())) {
			supplyInfo.setCountry(tempPara.getCOUNTRY());
		}

		if (StringUtils.isNotEmpty(tempPara.getCITY1())) {
			supplyInfo.setCity(tempPara.getCITY1());
		}

		if (StringUtils.isNotEmpty(tempPara.getREGIO())) {
			supplyInfo.setZone(tempPara.getREGIO());
		}

		if (StringUtils.isNotEmpty(tempPara.getZZREGION())) {
			supplyInfo.setShopRegion(tempPara.getZZREGION());
		}

		if (StringUtils.isNotEmpty(tempPara.getSTREET())) {
			supplyInfo.setAddress(tempPara.getSTREET());
		}

		if (StringUtils.isNotEmpty(tempPara.getCONTACT_ADDR())) {
			supplyInfo.setStreet(tempPara.getCONTACT_ADDR());
		}

		if (StringUtils.isNotEmpty(tempPara.getPOST_CODE1())) {
			supplyInfo.setPostcode(tempPara.getPOST_CODE1());
		}

		if (StringUtils.isNotEmpty(tempPara.getORG_CODE())) {
			supplyInfo.setOrgCode(tempPara.getORG_CODE());
		}

		if (StringUtils.isNotEmpty(tempPara.getINDUSTRY())) {
			supplyInfo.setIndustry(tempPara.getINDUSTRY());
		}

		// 组织机构代码
		if (StringUtils.isNotEmpty(tempPara.getZZORG())) {
			supplyInfo.setField1(tempPara.getZZORG());
		}

		if (StringUtils.isNotEmpty(tempPara.getZZLICENSE())) {
			supplyInfo.setBizCertificateNo(tempPara.getZZLICENSE());
		}

		String taxtype = tempPara.getTAXTYPE();
		if (StringUtils.isNotEmpty(taxtype)) {

			if (taxtype.equals(Constants.PCMSUPPLYINFO_SUPPLYTYPE_TAXTYPE_1_TXT)) {
				supplyInfo.setTaxType(Constants.PCMSUPPLYINFO_SUPPLYTYPE_TAXTYPE_1_CODE);
			}

			if (taxtype.equals(Constants.PCMSUPPLYINFO_SUPPLYTYPE_TAXTYPE_2_TXT)) {
				supplyInfo.setTaxType(Constants.PCMSUPPLYINFO_SUPPLYTYPE_TAXTYPE_2_CODE);
			}

			if (taxtype.equals(Constants.PCMSUPPLYINFO_SUPPLYTYPE_TAXTYPE_3_TXT)) {
				supplyInfo.setTaxType(Constants.PCMSUPPLYINFO_SUPPLYTYPE_TAXTYPE_3_CODE);
			}

			if (taxtype.equals(Constants.PCMSUPPLYINFO_SUPPLYTYPE_TAXTYPE_4_TXT)) {
				supplyInfo.setTaxType(Constants.PCMSUPPLYINFO_SUPPLYTYPE_TAXTYPE_4_CODE);
			}

			if (taxtype.equals(Constants.PCMSUPPLYINFO_SUPPLYTYPE_TAXTYPE_5_TXT)) {
				supplyInfo.setTaxType(Constants.PCMSUPPLYINFO_SUPPLYTYPE_TAXTYPE_5_CODE);
			}

		}

		if (StringUtils.isNotEmpty(tempPara.getSTCD1())) {
			supplyInfo.setTaxNumbe(tempPara.getSTCD1());
		}

		if (StringUtils.isNotEmpty(tempPara.getZZNAME_BANK())) {
			supplyInfo.setBank(tempPara.getZZNAME_BANK());
		}

		if (StringUtils.isNotEmpty(tempPara.getZZBANK())) {
			supplyInfo.setBankNo(tempPara.getZZBANK());
		}
		if (StringUtils.isNotEmpty(tempPara.getREGISTERED_CAPITAL())) {
			supplyInfo.setRegisteredCapital(tempPara.getREGISTERED_CAPITAL());
		}
		if (StringUtils.isNotEmpty(tempPara.getZZPROPERTY())) {
			supplyInfo.setEnterpriseProperty(tempPara.getZZPROPERTY());
		}
		if (StringUtils.isNotEmpty(tempPara.getBUSINESS_CATEGORY())) {
			supplyInfo.setBusinessCategory(tempPara.getBUSINESS_CATEGORY());
		}
		if (StringUtils.isNotEmpty(tempPara.getZZID_NAME())) {
			supplyInfo.setLegalPerson(tempPara.getZZID_NAME());
		}
		if (StringUtils.isNotEmpty(tempPara.getZZID_NUM())) {
			supplyInfo.setLegalPersonIcCode(tempPara.getZZID_NUM());
		}
		if (StringUtils.isNotEmpty(tempPara.getLEGAL_PERSON_CONTACT())) {
			supplyInfo.setLegalPersonContact(tempPara.getLEGAL_PERSON_CONTACT());
		}
		if (StringUtils.isNotEmpty(tempPara.getAGENT_NAME())) {
			supplyInfo.setAgent(tempPara.getAGENT_NAME());
		}
		if (StringUtils.isNotEmpty(tempPara.getAGENT_NUM())) {
			supplyInfo.setAgentIcCode(tempPara.getAGENT_NUM());
		}
		if (StringUtils.isNotEmpty(tempPara.getAGENT_CONTACT())) {
			supplyInfo.setAgentContact(tempPara.getAGENT_CONTACT());
		}
		if (StringUtils.isNotEmpty(tempPara.getCONTACT_NAME())) {
			supplyInfo.setContact(tempPara.getCONTACT_NAME());
		}
		if (StringUtils.isNotEmpty(tempPara.getCONTACT_TITLE())) {
			supplyInfo.setContactTitle(tempPara.getCONTACT_TITLE());
		}
		if (StringUtils.isNotEmpty(tempPara.getZZCON_NUM())) {
			supplyInfo.setContactIcCode(tempPara.getZZCON_NUM());
		}
		if (StringUtils.isNotEmpty(tempPara.getCONTACT_WAY())) {
			supplyInfo.setContactWay(tempPara.getCONTACT_WAY());
		}
		if (StringUtils.isNotEmpty(tempPara.getBUSINESS_SCOPE())) {
			supplyInfo.setBusinessScope(tempPara.getBUSINESS_SCOPE());
		}
		/* 是否是重点供应商 */
		String key_SUPPLIER = tempPara.getKEY_SUPPLIER();
		if (StringUtils.isNotEmpty(key_SUPPLIER)) {

			if (tempPara.getKEY_SUPPLIER().equals("Y")) {
				supplyInfo.setKeySupplier(1);
			}

			if (tempPara.getKEY_SUPPLIER().equals("N")) {
				supplyInfo.setKeySupplier(0);
			}
		}

		String tax_RATE = tempPara.getTAX_RATE();
		if (StringUtils.isNotEmpty(tax_RATE)) {
			supplyInfo.setTaxRate(new BigDecimal(tax_RATE));
		}
		if (StringUtils.isNotEmpty(tempPara.getINOUT_CITY())) {
			supplyInfo.setInOutCity(tempPara.getINOUT_CITY());
		}

		// 准入日期
		String admissiondate = tempPara.getADMISSIONDATE();
		if (StringUtils.isNotEmpty(tempPara.getADMISSIONDATE())) {
			String formatDateStr = DateUtil.formatDateStr(admissiondate, "yyyymmdd");
			supplyInfo.setAdmissionDate(formatDateStr);
		}
		/* 是否退货自供应商 */
		String zzreturnv = tempPara.getZZRETURNV();
		if (StringUtils.isNotEmpty(zzreturnv)) {

			if (zzreturnv.equals("Y")) {
				supplyInfo.setReturnSupply(1);
			}

			if (zzreturnv.equals("N")) {
				supplyInfo.setReturnSupply(0);
			}
		}

		/* 仅仅针对电商。如果ZZRETURN为Y，这个字段保存客户的退货地址，不超过200个中文字符。否则为空 */
		if (StringUtils.isNotEmpty(tempPara.getZZJOIN_SITE())) {
			supplyInfo.setJoinSite(tempPara.getZZJOIN_SITE());
		}
		/* 拆单标识 */
		String apart_ORDER = tempPara.getAPART_ORDER();
		if (StringUtils.isNotEmpty(apart_ORDER)) {
			if (apart_ORDER.equals("Y")) {
				supplyInfo.setApartOrder(1);
			}
			if (apart_ORDER.equals("N")) {
				supplyInfo.setApartOrder(0);
			}
		}

		/* 区分奥莱和其它虚库标识（Y N） */
		String dropship = tempPara.getDROPSHIP();
		if (StringUtils.isNotEmpty(dropship)) {
			if (dropship.equals("Y")) {
				supplyInfo.setDropship(1);
			}
			if (dropship.equals("N")) {
				supplyInfo.setDropship(0);
			}
		}

		String action_DATE = tempPara.getACTION_DATE();
		if (StringUtils.isNotEmpty(tempPara.getACTION_DATE())) {

			Date lastOptDate = DateUtil.formatDate(action_DATE, "yyyymmdd.HHMMSS");
			supplyInfo.setLastOptDate(lastOptDate);

		}

		if (StringUtils.isNotEmpty(tempPara.getACTION_PERSION())) {
			supplyInfo.setLastOptUser(tempPara.getACTION_PERSION());
		}

		/* 供应商类型 默认都为门店供应商 */
		supplyInfo.setSupplyType(0);
		// String zflg = tempPara.getZFLG();
		// if (StringUtils.isNotEmpty(zflg)) {
		// if (zflg.equals("1")) {
		// supplyInfo.setSupplyType(0);
		// }
		// if (zflg.equals("2")) {
		// supplyInfo.setSupplyType(1);
		// }
		// }
		return supplyInfo;
	}

}
