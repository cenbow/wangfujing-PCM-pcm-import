package com.wangfj.product.core.controller.brand;

import com.wangfj.core.constants.ComErrorCodeConstants;
import com.wangfj.core.framework.base.controller.BaseController;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.core.utils.StringUtils;
import com.wangfj.product.brand.domain.vo.PcmBrandUploadDto;
import com.wangfj.product.brand.domain.vo.PcmShopBrandUploadDto;
import com.wangfj.product.brand.service.intf.IPcmShopBrandRelationService;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.intf.IPcmExceptionLogService;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.core.controller.brand.support.PcmBrandUploadPara;
import com.wangfj.product.core.controller.brand.support.PcmShopBrandUploadEBusinessPara;
import com.wangfj.product.core.controller.brand.support.PcmShopBrandUploadPara;
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/shopBrand", produces = "application/json;charset=utf-8")
@Controller
public class PcmShopBrandController extends BaseController {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private IPcmExceptionLogService exceptionLogService;

    @Autowired
    private IPcmShopBrandRelationService shopBrandRelationService;

    /**
     * 门店品牌上传（门店）
     *
     * @param para
     * @return String
     * @Methods Name uploadShopBrand
     * @Create In 2016-03-23 By wangxuan
     */
    @RequestMapping(value = "/uploadShopBrand", method = {RequestMethod.POST,
            RequestMethod.GET})
    @ResponseBody
    public String uploadShopBrand(@RequestBody MqRequestDataPara para) {

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
                List<PcmBrandUploadPara> brandParaList = JSONArray.toList(jsonSPS);

                String callBackUrl = paraDest.getHeader().getCallbackUrl();
                String requestMsg = "";

                for (int i = 0; i < brandParaList.size(); i++) {

                    PcmBrandUploadDto dto = new PcmBrandUploadDto();

                    try {
                        BeanUtils.copyProperties(dto, brandParaList.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    Map<String, Object> resultMap = null;
                    PcmExceptionLogDto exceptionLogdto = null;
                    try {
                        resultMap = shopBrandRelationService.uploadBrand(dto);
                        if (resultMap != null) {
                            exceptionLogdto = new PcmExceptionLogDto();
                            String result = resultMap.get("result") + "";
                            if (result.equals(Constants.PUBLIC_0 + "")) {
                                String dataContent = "门店品牌上传时数据:" + dto.toString() + "时失败；异常信息：操作数据库失败";

                                exceptionLogdto.setInterfaceName("uploadShopBrand");
                                exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_BRAND.getStatus());
                                exceptionLogdto.setDataContent(paraDest.toString());
                                exceptionLogdto.setErrorCode(ComErrorCodeConstants.ErrorCode.BRAND_EXIST.getErrorCode());
                                exceptionLogdto.setErrorMessage(dataContent);

                                exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
                            }
                        }
                    } catch (BleException ble) {
                        if (resultMap == null) {
                            requestMsg = "门店品牌上传时数据:" + dto.toString() + "时失败";
                            String dataContent = "门店品牌上传时数据:" + dto.toString() + "时失败；异常信息：" + ble.getMessage();

                            exceptionLogdto = new PcmExceptionLogDto();
                            exceptionLogdto.setInterfaceName("uploadShopBrand");
                            exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_BRAND.getStatus());
                            exceptionLogdto.setDataContent(paraDest.toString());
                            exceptionLogdto.setErrorCode(ble.getCode());
                            exceptionLogdto.setErrorMessage(dataContent);

                            exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
                        }
                    }
                }
            }
        });

        return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(paraDest.getHeader()));
    }

    /**
     * 门店与门店品牌关系上传（门店）
     *
     * @param para
     * @return String
     * @Methods Name uploadShopBrandRelation
     * @Create In 2015-9-18 By wangxuan
     */
    @RequestMapping(value = "/uploadShopBrandRelation", method = {RequestMethod.POST,
            RequestMethod.GET})
    @ResponseBody
    public String uploadShopBrandRelation(@RequestBody MqRequestDataPara para) {

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
                List<PcmShopBrandUploadPara> shopBrandParaList = JSONArray.toList(jsonSPS);

                String callBackUrl = paraDest.getHeader().getCallbackUrl();
                String requestMsg = "";

                for (int i = 0; i < shopBrandParaList.size(); i++) {

                    PcmShopBrandUploadDto dto = new PcmShopBrandUploadDto();

                    try {
                        BeanUtils.copyProperties(dto, shopBrandParaList.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    try {
                        Map<String, Object> resultMap = shopBrandRelationService.uploadShopBrandRelation(dto);
                        String result = resultMap.get("result") + "";

                        if (result.toString().equals(Constants.PUBLIC_0 + "")) {
                            String dataContent = "门店与门店品牌关系上传时数据:" + dto.toString() + "时失败;异常信息：操作数据库失败";

                            PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
                            exceptionLogdto.setInterfaceName("uploadShopBrandRelation");
                            exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_BRAND.getStatus());
                            exceptionLogdto.setDataContent(paraDest.toString());
                            exceptionLogdto.setErrorCode(ComErrorCodeConstants.ErrorCode.BRAND_SHOP_RELATION_EXISTENCE.getErrorCode());
                            exceptionLogdto.setErrorMessage(dataContent);

                            exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
                        }

                        // 下发给线下搜索
                        if (result.toString().equals(Constants.PUBLIC_1 + "")) {
                            final String sid = resultMap.get("sid") + "";
                            if (StringUtils.isNotEmpty(sid)) {
                                List<Map<String, Object>> pushList = new ArrayList<Map<String, Object>>();
                                Map<String, Object> paraMap = new HashMap<String, Object>();
                                paraMap.put("sid", sid);
                                pushList.add(paraMap);
                                final String json = JsonUtil.getJSONString(pushList);
                                taskExecutor.execute(new Runnable() {
                                    @Override
                                    public void run() {//线下门店品牌下发给搜索
                                        String offlineSearchUrl = PropertyUtil.getSystemUrl("pcm-syn")
                                                + "pcmSynBrand/pushBrandToOfflineSearch.htm";
                                        HttpUtil.doPost(offlineSearchUrl, json);
                                    }
                                });
                                taskExecutor.execute(new Runnable() {//门店品牌及门店号下发给搜索-供应商平台查询
                                    @Override
                                    public void run() {
                                        String offlineSearchUrl = PropertyUtil.getSystemUrl("pcm-syn")
                                                + "pcmSynBrand/pushShopBrandToSearch.htm";
                                        HttpUtil.doPost(offlineSearchUrl, json);
                                    }
                                });
                            }
                        }
                    } catch (BleException ble) {
                        String dataContent = "门店与门店品牌关系上传时数据:" + dto.toString() + "时失败;异常信息：" + ble.getMessage();

                        PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
                        exceptionLogdto.setInterfaceName("uploadShopBrandRelation");
                        exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_BRAND.getStatus());
                        exceptionLogdto.setDataContent(paraDest.toString());
                        exceptionLogdto.setErrorCode(ble.getCode());
                        exceptionLogdto.setErrorMessage(dataContent);

                        exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
                    }
                }
            }
        });

        return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(paraDest.getHeader()));
    }

    /**
     * 门店与门店品牌的信息上传(电商)-赛特
     *
     * @param para
     * @return String
     * @Methods Name uploadShopBrandRelationFromEBusiness
     * @Create In 2015-11-20 By wangxuan
     */
    @RequestMapping(value = "/uploadShopBrandRelationFromEBusiness", method = {RequestMethod.POST,
            RequestMethod.GET})
    @ResponseBody
    public String uploadShopBrandRelationFromEBusiness(@RequestBody MqRequestDataPara para) {

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
                List<PcmShopBrandUploadEBusinessPara> shopBrandParaList = JSONArray.toList(jsonSPS);

                String callBackUrl = paraDest.getHeader().getCallbackUrl();
                String requestMsg = "";

                // 下发参数
                final List<Map<String, Object>> paraList = new ArrayList<Map<String, Object>>();
                for (int i = 0; i < shopBrandParaList.size(); i++) {

                    PcmShopBrandUploadEBusinessPara uploadPara = new PcmShopBrandUploadEBusinessPara();
                    try {
                        BeanUtils.copyProperties(uploadPara, shopBrandParaList.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    PcmShopBrandUploadDto dto = transformParaToDto(uploadPara);

                    try {
                        Map<String, Object> resultMap = shopBrandRelationService.uploadShopBrandInfo(dto);

                        String result = resultMap.get("result") + "";

                        if (result.equals(Constants.PUBLIC_0 + "")) {
                            requestMsg = "门店与门店品牌信息上传时数据:" + dto.toString() + "时失败";
                            String dataContent = "门店与门店品牌信息上传时数据:" + dto.toString() + "时失败;异常信息：操作数据库失败";

                            PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
                            exceptionLogdto.setInterfaceName("uploadShopBrandRelationFromEBusiness");
                            exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_BRAND.getStatus());
                            exceptionLogdto.setDataContent(paraDest.toString());
                            exceptionLogdto.setErrorCode(ComErrorCodeConstants.ErrorCode.BRAND_SHOP_RELATION_EXISTENCE.getErrorCode());
                            exceptionLogdto.setErrorMessage(dataContent);

                            exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
                        }
                        if (result.equals(Constants.PUBLIC_1 + "")) {
                            Map<String, Object> paraMap = new HashMap<String, Object>();
                            paraMap.put("sid", resultMap.get("sid"));
                            paraMap.put("actionCode", dto.getACT_CODE());
                            paraList.add(paraMap);
                        }

                    } catch (BleException ble) {
                        String dataContent = "门店与门店品牌信息上传时数据:" + dto.toString() + "时失败;异常信息：" + ble.getMessage();

                        PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
                        exceptionLogdto.setInterfaceName("uploadShopBrandRelationFromEBusiness");
                        exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_BRAND.getStatus());
                        exceptionLogdto.setDataContent(paraDest.toString());
                        exceptionLogdto.setErrorCode(ble.getCode());
                        exceptionLogdto.setErrorMessage(dataContent);

                        exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
                    }
                }

                if (!paraList.isEmpty()) {
                    // 下发门店品牌
                    taskExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            String url = PropertyUtil.getSystemUrl("pcm-syn")
                                    + "pcmSynBrand/pushBrand.htm";
                            String json = JsonUtil.getJSONString(paraList);
                            HttpUtil.doPost(url, json);
                        }
                    });
                }

            }
        });

        return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(paraDest.getHeader()));
    }

    private PcmShopBrandUploadDto transformParaToDto(PcmShopBrandUploadEBusinessPara uploadPara) {

        PcmShopBrandUploadDto dto = new PcmShopBrandUploadDto();
        dto.setACT_CODE(uploadPara.getACTIONCODE());
        dto.setACT_DATE(uploadPara.getACTIONDATE());
        dto.setACT_PERSON(uploadPara.getACTIONPERSON());
        dto.setBrandCode(uploadPara.getBRANDCODE());
        dto.setBranddesc(uploadPara.getBRANDDESC());
        dto.setBrandName(uploadPara.getBRANDNAME());
        dto.setBrandNameEN(uploadPara.getBRANDNAMEEN());
        dto.setBrandNameSecond(uploadPara.getBRANDNAMESECOND());
        dto.setBrandNameSpell(uploadPara.getBRANDNAMESPELL());
        dto.setBrandpic1(uploadPara.getBRANDPIC1());
        dto.setBrandpic2(uploadPara.getBRANDPIC2());
        dto.setIsDisplay(uploadPara.getISDISPLAY());
        dto.setStoreCode(uploadPara.getSTORECODE());
        dto.setStoreType(uploadPara.getSTORETYPE());

        return dto;
    }

}
