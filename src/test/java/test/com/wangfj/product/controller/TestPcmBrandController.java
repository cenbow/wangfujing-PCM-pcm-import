package test.com.wangfj.product.controller;

import com.alibaba.fastjson.JSON;
import com.wangfj.product.core.controller.brand.support.PcmShopBrandUploadEBusinessPara;
import com.wangfj.util.mq.MqRequestDataPara;
import com.wangfj.util.mq.RequestHeader;
import net.sf.json.JSONNull;
import org.junit.Test;

import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.core.controller.support.SelectPcmBrandPagePara;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestPcmBrandController {

    @Test
    public void uploadShopBrandRelationFromEBusiness() {
        MqRequestDataPara para = new MqRequestDataPara();
        RequestHeader header = new RequestHeader();
        header.setBizType("00");
        header.setCallbackUrl("xxxxxxxxxxxx");
        header.setCount("0");
        header.setCreateTime("2016-10-25 10:32:52.913");
        header.setDestCallType(0);
        header.setDestUrl("http://localhost:8085/pcm-import/shopBrand/uploadShopBrandRelationFromEBusiness.htm");
        header.setVersion("1");
        header.setPriority("1");
        header.setServiceID("P191");
        header.setSourceSysID("P012_02");
        header.setMessageID("57bc318b-0b02-44d3-909c-5f4308b51dbe");
        header.setRouteKey("EfutureERP.Supplier.PCM");
        para.setHeader(header);

        PcmShopBrandUploadEBusinessPara uploadEBusinessPara = new PcmShopBrandUploadEBusinessPara();
        uploadEBusinessPara.setACTIONCODE("A");
        uploadEBusinessPara.setACTIONDATE("20141219.183441+0800");
        uploadEBusinessPara.setACTIONPERSON("EFUTERP");
        uploadEBusinessPara.setBRANDCODE("1000025");
        uploadEBusinessPara.setBRANDDESC("品牌描述");
        uploadEBusinessPara.setBRANDNAME("品牌名称");
        uploadEBusinessPara.setBRANDNAMEEN("English name");
        uploadEBusinessPara.setBRANDNAMESECOND("品牌第二名称");
        uploadEBusinessPara.setBRANDNAMESPELL("品牌拼音");
        uploadEBusinessPara.setBRANDPIC1("1.jpg");
        uploadEBusinessPara.setBRANDPIC2("2.jpg");
        uploadEBusinessPara.setISDISPLAY("0");
        uploadEBusinessPara.setSTORECODE("21011");
        uploadEBusinessPara.setSTORETYPE("2");

        List<PcmShopBrandUploadEBusinessPara> list = new ArrayList<PcmShopBrandUploadEBusinessPara>();
        list.add(uploadEBusinessPara);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("version", "1");
        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("titile", "消息描述");
        headerMap.put("reset", false);
        headerMap.put("count", 1);
        paramMap.put("header", headerMap.toString());
        paramMap.put("header", "");
        paramMap.put("data", list);

        String data = JsonUtil.getJSONString(paramMap);
        System.out.println("data" + data);
        para.setData(data);

        System.out.println("参数：" + JsonUtil.getJSONString(para));

        String url = "http://localhost:8085/pcm-import/shopBrand/uploadShopBrandRelationFromEBusiness.htm";
        String doPost = HttpUtil.doPost(url, JsonUtil.getJSONString(para));
        System.out.println(doPost);


    }


    @Test
    public void test() {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> shoppeProductMap = null;
        shoppeProductMap = new HashMap<String, Object>();
        shoppeProductMap.put("sid", 22);
        shoppeProductMap.put("type", 1);
        resultMap.put("shoppeProductMap", shoppeProductMap);

        Map<String, Object> shoppeProductMap1 = (Map<String, Object>) resultMap.get("shoppeProductMap");
        System.out.println(shoppeProductMap1);
        System.out.println(shoppeProductMap1 != null);

    }

    @Test
    public void findBrandForPage() {

        SelectPcmBrandPagePara pcmBrandPara = new SelectPcmBrandPagePara();
        pcmBrandPara.setFromSystem("PCM");
        pcmBrandPara.setCurrentPage(2);
        pcmBrandPara.setPageSize(1);
        pcmBrandPara.setBrandName("a");

        String response = HttpUtil.doPost(
                "http://127.0.0.1:8081/pcm-core/pcmBrand/findBrandForPage.htm",
                JsonUtil.getJSONString(pcmBrandPara));
        System.out.println(response);

    }

}
