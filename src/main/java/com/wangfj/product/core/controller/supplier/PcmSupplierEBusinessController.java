package com.wangfj.product.core.controller.supplier;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wangfj.core.constants.ComErrorCodeConstants;
import com.wangfj.core.framework.exception.BleException;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangfj.core.framework.base.controller.BaseController;
import com.wangfj.core.utils.DateUtil;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.core.utils.StringUtils;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.intf.IPcmExceptionLogService;
import com.wangfj.product.constants.JcoSAPUtils;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.core.controller.supplier.support.PcmSupplierEBusinessPara;
import com.wangfj.product.supplier.domain.entity.PcmSupplyInfo;
import com.wangfj.product.supplier.service.intf.IPcmSupplyInfoService;
import com.wangfj.util.Constants;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.MqUtil;

@Controller
@RequestMapping(value = "/pcmSupplierEBusiness", produces = "application/json;charset=utf-8")
public class PcmSupplierEBusinessController extends BaseController {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private IPcmSupplyInfoService pcmSupplyInfoService;

    @Autowired
    private IPcmExceptionLogService exceptionLogService;

    /**
     * 供应商主数据从电商上传到Pcm
     *
     * @param para
     * @return String
     * @Methods Name uploadPcmSupplier
     * @Create In 2015-11-16 By wangxuan
     */
    @RequestMapping(value = "/uploadPcmSupplier", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String uploadPcmSupplier(
            @RequestBody MqRequestDataListPara<PcmSupplierEBusinessPara> para) {

        final MqRequestDataListPara<PcmSupplierEBusinessPara> paraDest = new MqRequestDataListPara<PcmSupplierEBusinessPara>();
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
                List<PcmSupplierEBusinessPara> supplierParaList = paraDest.getData();
                String callBackUrl = paraDest.getHeader().getCallbackUrl();
                String requestMsg = "";
                /* 将得到的参数赋到list中 */
                for (int i = 0; i < supplierParaList.size(); i++) {

                    PcmSupplierEBusinessPara tempPara = new PcmSupplierEBusinessPara();
                    try {
                        BeanUtils.copyProperties(tempPara, supplierParaList.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    PcmSupplyInfo supplyInfo = transformParaToEntity(tempPara);
                    supplyInfo.setStatus("Y");

                    try {
                        Map<String, Object> resultMap = pcmSupplyInfoService.uploadSupplierFromEBusiness(supplyInfo);

                        String result = resultMap.get("result") + "";
                        if (result.equals(Constants.PUBLIC_0 + "")) {
                            String dataContent = "电商上传供应商：" + supplyInfo.toString() + "时失败;异常信息：操作数据库失败";
                            PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
                            exceptionLogdto.setInterfaceName("uploadPcmSupplyInfoFromEFutureERP");
                            exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_SUPPLY.getStatus());
                            exceptionLogdto.setDataContent(paraDest.toString());
                            exceptionLogdto.setErrorMessage(dataContent);
                            exceptionLogdto.setErrorCode(ComErrorCodeConstants.ErrorCode.PCMSHOPPEPRODUCTSUPPLY_RELATION_EXISTENCE.getErrorCode());
                            exceptionLogService.saveExceptionLogInfo(exceptionLogdto);

                            List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("KEY_FIELD", supplyInfo.getSupplyCode());
                            map.put("FLAG", "5");
                            map.put("MESSAGE", dataContent);
                            JcoSAPUtils.functionExecute("ZFM_MD_PCM2SAP_ERROR_IN", "INPUT", listMap);
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
                                        String url = PropertyUtil
                                                .getSystemUrl("supplyInfo.pushSupplyInfoToPromotionUrl");
                                        String json = JsonUtil.getJSONString(paraMap);
                                        HttpUtil.doPost(url, json);
                                    }
                                });
                            }
                        }
                    }catch (BleException ble){
                        String dataContent = "电商上传供应商：" + supplyInfo.toString() + "时失败;异常信息：" + ble.getMessage();
                        PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
                        exceptionLogdto.setInterfaceName("uploadPcmSupplyInfoFromEFutureERP");
                        exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_SUPPLY.getStatus());
                        exceptionLogdto.setDataContent(paraDest.toString());
                        exceptionLogdto.setErrorMessage(dataContent);
                        exceptionLogdto.setErrorCode(ble.getCode());
                        exceptionLogService.saveExceptionLogInfo(exceptionLogdto);

                        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("KEY_FIELD", supplyInfo.getSupplyCode());
                        map.put("FLAG", "5");
                        map.put("MESSAGE", dataContent);
                        JcoSAPUtils.functionExecute("ZFM_MD_PCM2SAP_ERROR_IN", "INPUT", listMap);
                    }
                }

            }

        });

        return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(paraDest.getHeader()));
    }

    /**
     * 传入参数转实体
     *
     * @param tempPara
     * @return PcmSupplyInfo
     * @Methods Name transformParaToEntity
     * @Create In 2015-12-22 By wangxuan
     */
    private PcmSupplyInfo transformParaToEntity(PcmSupplierEBusinessPara tempPara) {
        PcmSupplyInfo supplyInfo = new PcmSupplyInfo();

        if (StringUtils.isNotEmpty(tempPara.getSTORECODE())) {
            supplyInfo.setShopSid(tempPara.getSTORECODE());
        }

        if (StringUtils.isNotEmpty(tempPara.getLIFNR())) {
            supplyInfo.setSupplyCode(tempPara.getLIFNR());
        }

        if (StringUtils.isNotEmpty(tempPara.getNAME1())) {
            supplyInfo.setSupplyName(tempPara.getNAME1());
        }

        String businessPattern = tempPara.getKTOKK();
        if (StringUtils.isNotEmpty(businessPattern)) {
            supplyInfo.setBusinessPattern(Integer.parseInt(businessPattern));
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
            supplyInfo.setInOutCity(tempPara.getZZREGION());
        }

        if (StringUtils.isNotEmpty(tempPara.getSTREET())) {
            supplyInfo.setAddress(tempPara.getSTREET());
        }

        if (StringUtils.isNotEmpty(tempPara.getPOST_CODE1())) {
            supplyInfo.setPostcode(tempPara.getPOST_CODE1());
        }

        if (StringUtils.isNotEmpty(tempPara.getBRSCH())) {
            supplyInfo.setIndustry(tempPara.getBRSCH());
        }

        // 组织机构代码
        if (StringUtils.isNotEmpty(tempPara.getZZORG())) {
            supplyInfo.setField1(tempPara.getZZORG());
        }

        if (StringUtils.isNotEmpty(tempPara.getZZLICENSE())) {
            supplyInfo.setBizCertificateNo(tempPara.getZZLICENSE());
        }

        String taxtype = tempPara.getFITYP();
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

        if (StringUtils.isNotEmpty(tempPara.getZZNAME_B())) {
            supplyInfo.setBank(tempPara.getZZNAME_B());
        }

        if (StringUtils.isNotEmpty(tempPara.getZZBANK())) {
            supplyInfo.setBankNo(tempPara.getZZBANK());
        }
        if (StringUtils.isNotEmpty(tempPara.getZZPROPERTY())) {
            supplyInfo.setEnterpriseProperty(tempPara.getZZPROPERTY());
        }
        if (StringUtils.isNotEmpty(tempPara.getZZID_NAME())) {
            supplyInfo.setLegalPerson(tempPara.getZZID_NAME());
        }
        if (StringUtils.isNotEmpty(tempPara.getZZID_NUM())) {
            supplyInfo.setLegalPersonIcCode(tempPara.getZZID_NUM());
        }
        if (StringUtils.isNotEmpty(tempPara.getZZCONTACT())) {
            supplyInfo.setContact(tempPara.getZZCONTACT());
        }
        if (StringUtils.isNotEmpty(tempPara.getZZCON_NUM())) {
            supplyInfo.setContactIcCode(tempPara.getZZCON_NUM());
        }

        String tax_RATE = tempPara.getZZMWSKZ();
        if (StringUtils.isNotEmpty(tax_RATE)) {
            supplyInfo.setTaxRate(new BigDecimal(tax_RATE));
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

//        String action_DATE = tempPara.getACTION_DATE();
//        if (StringUtils.isNotEmpty(tempPara.getACTION_DATE())) {
//
//            Date lastOptDate = DateUtil.formatDate(action_DATE, "yyyymmdd.HHMMSS");
//            supplyInfo.setLastOptDate(lastOptDate);
//
//        }

        if (StringUtils.isNotEmpty(tempPara.getACTION_PERSION())) {
            supplyInfo.setLastOptUser(tempPara.getACTION_PERSION());
        }

		/* 供应商类型 默认都为门店供应商 */
        supplyInfo.setSupplyType(0);

        supplyInfo.setZlyFlag(tempPara.getZLY_FLAG());
        supplyInfo.setZzxxhcFlag(tempPara.getZZXXHC_FLAG());

        return supplyInfo;
    }

}
