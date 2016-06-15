package com.wangfj.product.core.controller;

import com.wangfj.core.constants.ComErrorCodeConstants;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.PropertyUtil;
import com.wangfj.core.utils.StringUtils;
import com.wangfj.product.common.domain.vo.PcmExceptionLogDto;
import com.wangfj.product.common.service.intf.IPcmExceptionLogService;
import com.wangfj.product.constants.StatusCodeConstants.StatusCode;
import com.wangfj.product.core.controller.support.PcmShoppeProSupplyUploadPara;
import com.wangfj.product.supplier.domain.vo.PcmShoppeProSupplyUploadDto;
import com.wangfj.product.supplier.service.intf.IPcmShoppeProductSupplyService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param para
     * @param request
     * @return String
     * @Methods Name uploadShoppeProductSupplyFromEFuture
     * @Create In 2015-8-28 By wangxuan
     */
    @RequestMapping(value = "/uploadShoppeProductSupplyFromEFuture", method = {RequestMethod.POST,
            RequestMethod.GET})
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

                //下发专柜商品
                List<Map<String, Object>> sidParaList = new ArrayList<Map<String, Object>>();
                for (int i = 0; i < shoppeProSupplyParaList.size(); i++) {
                    PcmShoppeProSupplyUploadDto dto = new PcmShoppeProSupplyUploadDto();

                    try {
                        BeanUtils.copyProperties(dto, shoppeProSupplyParaList.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    try {
                        Map<String, Object> resultMap = shoppeProductSupplyService.uploadShoppeProSupply(dto);

                        String result = resultMap.get("result") + "";
                        if (Constants.PUBLIC_0.equals(result)) {
                            String dataContent = "一品多供应商关系上传时数据:" + dto.toString() + "时失败;异常信息：操作数据库失败";
                            PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
                            exceptionLogdto.setInterfaceName("uploadShoppeProductSupplyFromEFuture");
                            exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_SUPPLY.getStatus());
                            exceptionLogdto.setDataContent(paraDest.toString());
                            exceptionLogdto.setErrorCode(ComErrorCodeConstants.ErrorCode.PCMSHOPPEPRODUCTSUPPLY_RELATION_EXISTENCE.getErrorCode());
                            exceptionLogdto.setErrorMessage(dataContent);
                            exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
                        }

                        Map<String, Object> shoppeProductMap = (Map<String, Object>) resultMap.get("shoppeProductMap");
                        if (shoppeProductMap != null) {
                            sidParaList.add(shoppeProductMap);
                        }
                    } catch (BleException ble) {
                        String dataContent = "一品多供应商关系上传时数据:" + dto.toString() + "时失败;异常信息：" + ble.getMessage();
                        PcmExceptionLogDto exceptionLogdto = new PcmExceptionLogDto();
                        exceptionLogdto.setInterfaceName("uploadShoppeProductSupplyFromEFuture");
                        exceptionLogdto.setExceptionType(StatusCode.EXCEPTION_SUPPLY.getStatus());
                        exceptionLogdto.setDataContent(paraDest.toString());
                        exceptionLogdto.setErrorMessage(dataContent);
                        exceptionLogdto.setErrorCode(ble.getCode());
                        exceptionLogService.saveExceptionLogInfo(exceptionLogdto);
                    }
                }

                if (sidParaList.size() > 0) {//专柜商品下发
                    final Map<String, Object> pushMap = new HashMap<String, Object>();
                    pushMap.put("paraList", sidParaList);
                    pushMap.put("PcmProSearch", 1);
                    taskExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            String shoppeProductUrl = PropertyUtil.getSystemUrl("product.pushShoppeProduct");
                            HttpUtil.doPost(shoppeProductUrl, JsonUtil.getJSONString(pushMap));
                        }
                    });
                }
            }
        });

        return JsonUtil.getJSONString(MqUtil.GetMqResponseInfo(paraDest.getHeader()));

    }

}
