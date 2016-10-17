package com.wangfj.product.core.controller.supplier;

import com.wangfj.core.constants.ComErrorCodeConstants;
import com.wangfj.core.framework.base.controller.BaseController;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.StringUtils;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.intf.IPcmExceptionLogService;
import com.wangfj.product.constants.StatusCodeConstants;
import com.wangfj.product.core.controller.supplier.support.PcmSupplyGroupPara;
import com.wangfj.product.supplier.domain.entity.PcmSupplyGroup;
import com.wangfj.product.supplier.service.intf.IPcmSupplyGroupService;
import com.wangfj.util.Constants;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.MqUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangxuan on 2016-10-12 0012.
 * 集团供应商
 */
@Controller
@RequestMapping(value = {"/supplyGroup"}, produces = "application/json;charset=utf-8")
public class PcmSupplyGroupController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PcmSupplyGroupController.class);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private IPcmSupplyGroupService supplyGroupService;

    @Autowired
    private IPcmExceptionLogService exceptionLogService;

    /**
     * 集团供应商从供应商平台上传到PCM
     *
     * @param para
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadPcmSupplyGroupFromSupplierERP", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    public String uploadPcmSupplyGroupFromSupplierERP(@RequestBody MqRequestDataListPara<PcmSupplyGroupPara> para,
                                                      HttpServletRequest request) {

        final MqRequestDataListPara<PcmSupplyGroupPara> paraDest = new MqRequestDataListPara<PcmSupplyGroupPara>();
        try {
            BeanUtils.copyProperties(paraDest, para);
        } catch (IllegalAccessException e1) {
            logger.info(e1.getMessage());
        } catch (InvocationTargetException e1) {
            logger.info(e1.getMessage());
        }

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<PcmSupplyGroupPara> supplierParaList = paraDest.getData();
                String callBackUrl = paraDest.getHeader().getCallbackUrl();
                String requestMsg = "";
                /* 将得到的参数赋到list中 */
                for (int i = 0; i < supplierParaList.size(); i++) {

                    PcmSupplyGroupPara tempPara = new PcmSupplyGroupPara();
                    try {
                        BeanUtils.copyProperties(tempPara, supplierParaList.get(i));
                    } catch (IllegalAccessException e) {
                        logger.info(e.getMessage());
                    } catch (InvocationTargetException e) {
                        logger.info(e.getMessage());
                    }

                    PcmSupplyGroup supplyInfo = transformParaToEntity(tempPara);
                    Map<String, Object> dtoMap = new HashMap<String, Object>();
                    dtoMap.put("actionCode", tempPara.getACTION_CODE());

                    try {
                        Map<String, Object> resultMap = supplyGroupService.uploadPcmSupplyGroupFromSupplierERP(supplyInfo, dtoMap);
                        String result = resultMap.get("result") + "";
                        if (result.equals(Constants.PUBLIC_0 + "")) {
                            String dataContent = "供应商平台上传集团供应商:" + supplyInfo.toString() + "时失败;异常信息：操作数据库失败！";
                            PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
                            exceptionLogdto.setInterfaceName("uploadPcmSupplyGroupFromSupplierERP");
                            exceptionLogdto.setExceptionType(StatusCodeConstants.StatusCode.EXCEPTION_SUPPLY.getStatus());
                            exceptionLogdto.setDataContent(paraDest.toString());
                            exceptionLogdto.setErrorMessage(dataContent);
                            exceptionLogdto.setErrorCode(ComErrorCodeConstants.ErrorCode.SUPPLYINFO_NOT_EXISTENCE.getErrorCode());
                            exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
                        }
                    } catch (BleException ble) {
                        String dataContent = "供应商平台上传集团供应商:" + supplyInfo.toString() + "时失败;异常信息：" + ble.getMessage();
                        PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
                        exceptionLogdto.setInterfaceName("uploadPcmSupplyGroupFromSupplierERP");
                        exceptionLogdto.setExceptionType(StatusCodeConstants.StatusCode.EXCEPTION_SUPPLY.getStatus());
                        exceptionLogdto.setDataContent(paraDest.toString());
                        exceptionLogdto.setErrorMessage(dataContent);
                        exceptionLogdto.setErrorCode(ble.getCode());
                        exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
                    }
                }
            }
        });

        return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(paraDest.getHeader()));
    }

    private PcmSupplyGroup transformParaToEntity(PcmSupplyGroupPara tempPara) {

        PcmSupplyGroup supplyInfo = new PcmSupplyGroup();

        if (StringUtils.isNotEmpty(tempPara.getSUPPLIERCODE())) {
            supplyInfo.setSupplyCode(tempPara.getSUPPLIERCODE());
        }

        if (StringUtils.isNotEmpty(tempPara.getSUPPLIERNAME())) {
            supplyInfo.setSupplyName(tempPara.getSUPPLIERNAME());
        }

        if (StringUtils.isNotEmpty(tempPara.getSMTP_ADDR())) {
            supplyInfo.setEmail(tempPara.getSMTP_ADDR());
        }

        if (StringUtils.isNotEmpty(tempPara.getFAX_NUMBER())) {
            supplyInfo.setFax(tempPara.getFAX_NUMBER());
        }

        if (StringUtils.isNotEmpty(tempPara.getCONTACT_ADDR())) {
            supplyInfo.setStreet(tempPara.getCONTACT_ADDR());
        }

        // 组织机构代码
        if (StringUtils.isNotEmpty(tempPara.getZZORG())) {
            supplyInfo.setField1(tempPara.getZZORG());
        }

        if (StringUtils.isNotEmpty(tempPara.getZZLICENSE())) {
            supplyInfo.setBizCertificateNo(tempPara.getZZLICENSE());
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

        if (StringUtils.isNotEmpty(tempPara.getZZID_NAME())) {
            supplyInfo.setLegalPerson(tempPara.getZZID_NAME());
        }
        if (StringUtils.isNotEmpty(tempPara.getZZID_NUM())) {
            supplyInfo.setLegalPersonIcCode(tempPara.getZZID_NUM());
        }

        if (StringUtils.isNotEmpty(tempPara.getCONTACT_NAME())) {
            supplyInfo.setContact(tempPara.getCONTACT_NAME());
        }

        if (StringUtils.isNotEmpty(tempPara.getZZCON_NUM())) {
            supplyInfo.setContactIcCode(tempPara.getZZCON_NUM());
        }
        if (StringUtils.isNotEmpty(tempPara.getCONTACT_WAY())) {
            supplyInfo.setContactWay(tempPara.getCONTACT_WAY());
        }

        if (StringUtils.isNotEmpty(tempPara.getACTION_PERSION())) {
            supplyInfo.setLastOptUser(tempPara.getACTION_PERSION());
        }

		/* 供应商类型 供应商平台上传的是集团供应商 */
        supplyInfo.setSupplyType(1);

        if (StringUtils.isNotEmpty(tempPara.getTAXPAYER_CERTIFICATE())) {
            supplyInfo.setTaxpayerCertificate(tempPara.getTAXPAYER_CERTIFICATE());
        }

        if (StringUtils.isNotEmpty(tempPara.getACCOUNT_NUMBER())) {
            supplyInfo.setAccountNumber(tempPara.getACCOUNT_NUMBER());
        }

        return supplyInfo;
    }


}
