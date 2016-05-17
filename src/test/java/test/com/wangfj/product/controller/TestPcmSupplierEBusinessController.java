package test.com.wangfj.product.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.util.mq.RequestHeader;

public class TestPcmSupplierEBusinessController {

	@Test
	public void uploadPcmSupplier() {
		RequestHeader header = new RequestHeader();
		header.setCallbackUrl("callBackUrlTest");
		Map<String, Object> para = new HashMap<String, Object>();

		para.put("STORECODE", "21011");
		para.put("LIFNR", "0008530");
		para.put("NAME1", "北京同仁天堂药品有限公司*Z");
		// para.put("NAME1", "同仁天堂*Z");

		// para.put("KTOKK", "Z001");
		// para.put("KTOKK", "Z002");
		// para.put("KTOKK", "Z003");
		para.put("KTOKK", "Z004");
		// para.put("KTOKK", "Z005");

		para.put("TEL_NUMBER", "18500023845");
		para.put("SMTP_ADDR", "wang250xuan@163.com");
		para.put("FAX_NUMBER", "0755-83537935");
		para.put("COUNTRY", "中国");
		para.put("CITY1", "北京");
		para.put("REGIO", "135412");
		para.put("ZZREGION", "北京市");
		para.put("STREET", "北京市海淀区稻香园西里5号楼");
		para.put("POST_CODE1", "100089");
		para.put("BRSCH", "建筑业");
		para.put("ZZORG", "222222225");
		para.put("ZZLICENSE", "8888888888");
		para.put("FITYP", "增值税一般纳税人");
		para.put("STCD1", "25698");
		para.put("ZZNAME_B", "中国建设银行");
		para.put("ZZBANK", "64428197319310912");
		para.put("ZZPROPERTY", "国企");
		para.put("ZZID_NAME", "王韵致");
		para.put("ZZID_NUM", "4209988901");
		para.put("ZZCONTACT", "王魅之");
		para.put("ZZCON_NUM", "4209988901");
		para.put("ZZMWSKZ", "17");
		para.put("ZZRETURNV", "Y");
		para.put("ZZJOIN_SITE", "北京市东城区王府井百货大楼255号");
		para.put("APART_ORDER", "Y");
		para.put("DROPSHIP", "Y");

		para.put("ACTION_CODE", "A");
		// para.put("ACTION_CODE", "U");
		para.put("ACTION_DATE", "20150916.170538+0800");
		para.put("ACTION_PERSION", "EFUTERP");

		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		dataList.add(para);

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("version", "1");
		dataMap.put("header", "");
		dataMap.put("data", dataList);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("data", JsonUtil.getJSONString(dataMap));
		data.put("header", header);
		System.out.println(JsonUtil.getJSONString(data));

		String url = "http://127.0.0.1:8085/pcm-import/pcmSupplierEBusiness/uploadPcmSupplier.htm";
		String response = HttpUtil.doPost(url, JsonUtil.getJSONString(data));
		System.out.println(response);
	}
}
