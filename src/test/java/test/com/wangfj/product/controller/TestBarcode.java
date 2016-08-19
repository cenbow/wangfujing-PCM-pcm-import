package test.com.wangfj.product.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wangfj.util.mq.MqRequestDataPara;
import kafka.utils.Json;
import org.junit.Test;

import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.EfutureERP.controller.support.SupplierBarCodeFromEfuturePara;
import com.wangfj.util.mq.RequestHeader;

public class TestBarcode {
    @Test
    public void gett() {
        RequestHeader he = new RequestHeader();
        SupplierBarCodeFromEfuturePara record = new SupplierBarCodeFromEfuturePara();
        record.setActionCode("A");
        record.setCOUNTERCODE("7");
        record.setLIFNR("7");
        record.setMATNR("7");
        record.setSALEAMOUNT("7");
        record.setSALEUNIT("7");
        record.setSALEPRICE(new BigDecimal("7"));
        record.setSBARCODE("17");
        record.setSBARCODENAME("7");
        record.setSTORECODE("7");
        record.setSBARCODETYPE("SE");

        SupplierBarCodeFromEfuturePara record1 = new SupplierBarCodeFromEfuturePara();
        record1.setActionCode("A");
        record1.setCOUNTERCODE("8");
        record1.setLIFNR("8");
        record1.setMATNR("8");
        record1.setSALEAMOUNT("8");
        record1.setSALEUNIT("8");
        record1.setSALEPRICE(new BigDecimal("8"));
        record1.setSBARCODE("18");
        record1.setSBARCODENAME("8");
        record1.setSTORECODE("8");
        record1.setSBARCODETYPE("");

        List<SupplierBarCodeFromEfuturePara> list = new ArrayList<SupplierBarCodeFromEfuturePara>();
        list.add(record);
        list.add(record1);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("version", "1");
        map.put("header", "");
        map.put("data", list);
        Map<String, Object> para111 = new HashMap<String, Object>();
        para111.put("data", JsonUtil.getJSONString(map));
        para111.put("header", he);
        System.out.println("Json-----------------" + JsonUtil.getJSONString(para111));
        String response = HttpUtil.doPost(
                "http://127.0.0.1:8085/pcm-import/barcode/uploadSupplierBarCodeFromEfuture.htm",
                JsonUtil.getJSONString(para111));
        System.out.println(response);
    }

    @Test
    public void testBatchUpload() {
        String storeCode = "21011";
        String storeCode1 = "21012";
        List<SupplierBarCodeFromEfuturePara> list = new ArrayList<SupplierBarCodeFromEfuturePara>();
        int max = 1000;
        for (int i = 0; i < max; i++) {
            SupplierBarCodeFromEfuturePara record = new SupplierBarCodeFromEfuturePara();
            record.setActionCode("A");
            record.setCOUNTERCODE("7");
            record.setLIFNR("7");
            record.setMATNR("7");
            record.setSALEAMOUNT("7");
            record.setSALEUNIT("7");
            record.setSALEPRICE(new BigDecimal("7"));
            record.setSBARCODE(i + "");
            record.setSBARCODENAME(i + "");
            if (i % 2 == 0) {
                record.setSTORECODE(storeCode);
            } else {
                record.setSTORECODE(storeCode1);
            }
            record.setSBARCODETYPE("SE");

            list.add(record);
            if (list.size() == 10 || max == (list.size() - 1)) {
                MqRequestDataPara requestDataPara = new MqRequestDataPara();
                RequestHeader hearder = new RequestHeader();
                requestDataPara.setHeader(hearder);
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("version", "1");
                dataMap.put("header", "");
                dataMap.put("data", list);
                String data = JsonUtil.getJSONString(dataMap);
                requestDataPara.setData(data);
                String url = "http://127.0.0.1:8085/pcm-import/barcode/uploadSupplierBarCodeFromEfuture.htm";
                String json = JsonUtil.getJSONString(requestDataPara);
                System.out.println(json);
                String doPost = HttpUtil.doPost(url, json);
                System.out.println(doPost);

                list.clear();
            }

        }
    }


}
