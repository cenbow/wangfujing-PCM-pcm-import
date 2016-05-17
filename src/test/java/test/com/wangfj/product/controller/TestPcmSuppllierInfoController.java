package test.com.wangfj.product.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.wangfj.core.utils.DateUtil;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.util.mq.RequestHeader;

public class TestPcmSuppllierInfoController {

	@Test
	public void test() {

		Date date = new Date();
		String formatToStr = DateUtil.formatToStr(date, "yyyymmdd");
		System.out.println(formatToStr);

	}

	/**
	 * 供应商主数据从门店ERP上传到Pcm
	 * 
	 * @Methods Name upload
	 * @Create In 2015-9-24 By wangxuan void
	 */
	@Test
	public void upload() {

		RequestHeader header = new RequestHeader();
		header.setCallbackUrl("callBackUrlTest");
		Map<String, Object> para = new HashMap<String, Object>();

		para.put("SUPPLIERCODE", "0008530");
		para.put("SUPPLIERNAME", "北京同仁天堂药品有限公司*Z");
		para.put("STORECODE", "21011");
		para.put("ZZLICENSE", "8888888888");

		// para.put("BUSINESSPATTERN", "Z001");
		para.put("BUSINESSPATTERN", "Z002");
		// para.put("BUSINESSPATTERN", "Z003");
		// para.put("BUSINESSPATTERN", "Z004");
		// para.put("BUSINESSPATTERN", "Z005");

		para.put("ACTION_CODE", "A");

		para.put("ACTION_DATE", "20150916.170538+0800");
		// para.put("STATUS", "正常");
		// para.put("STATUS", "未批准");
		// para.put("STATUS", "终止");
		// para.put("STATUS", "待审批");
		para.put("STATUS", "淘汰");
		// para.put("STATUS", "停货");

		para.put("TAX_RATE", "17");

		para.put("ZFLG", 1);

		para.put("SHOARTNAME", "北京同仁天堂药品");
		para.put("TEL_NUMBER", "18500023845");
		para.put("SMTP_ADDR", "wang250xuan@163.com");
		para.put("FAX_NUMBER", "0755-83537935");
		para.put("COUNTRY", "中国");
		para.put("CITY1", "北京");
		para.put("REGIO", "");
		para.put("ZZREGION", "北京市");
		para.put("STREET", "北京市海淀区稻香园西里5号楼");
		para.put("CONTACT_ADDR", "北京市海淀区稻香园西里5号楼");
		para.put("POST_CODE1", "100089");
		para.put("ORG_CODE", "");
		para.put("INDUSTRY", "制造业");
		para.put("ZZORG", "222222225");
		para.put("TAXTYPE", "增值税一般纳税人");
		para.put("STCD1", "25698");
		para.put("ZZBANK", "5268433");
		para.put("ZZPROPERTY", "国企");
		para.put("BUSINESS_CATEGORY", "经销商");
		para.put("INOUT_CITY", "市内");

		para.put("ACTION_PERSION", "EFUTERP");
		para.put("ADMISSIONDATE", "20150624");

		Map<String, Object> para1 = new HashMap<String, Object>();

		para1.put("SUPPLIERCODE", "0008530");
		para1.put("SUPPLIERNAME", "北京同仁天堂药品有限公司*Z");
		para1.put("STORECODE", "21011");
		para1.put("ZZLICENSE", "8888888888");

		para1.put("BUSINESSPATTERN", "Z002");

		para1.put("ACTION_CODE", "A");

		para1.put("ACTION_DATE", "20150916.170538+0800");
		// para1.put("STATUS", "正常");
		// para1.put("STATUS", "未批准");
		// para1.put("STATUS", "终止");
		// para1.put("STATUS", "待审批");
		para1.put("STATUS", "淘汰");
		// para1.put("STATUS", "停货");

		para1.put("TAX_RATE", "17");

		para1.put("ZFLG", 1);

		para1.put("SHOARTNAME", "北京同仁天堂药品");
		para1.put("TEL_NUMBER", "18500023845");
		para1.put("SMTP_ADDR", "wang250xuan@163.com");
		para1.put("FAX_NUMBER", "0755-83537935");
		para1.put("COUNTRY", "中国");
		para1.put("CITY1", "北京");
		para1.put("REGIO", "");
		para1.put("ZZREGION", "北京市");
		para1.put("STREET", "北京市海淀区稻香园西里5号楼");
		para1.put("CONTACT_ADDR", "北京市海淀区稻香园西里5号楼");
		para1.put("POST_CODE1", "100089");
		para1.put("ORG_CODE", "");
		para1.put("INDUSTRY", "制造业");
		para1.put("ZZORG", "222222225");
		para1.put("TAXTYPE", "增值税一般纳税人");
		para1.put("STCD1", "25698");
		para1.put("ZZBANK", "5268433");
		para1.put("ZZPROPERTY", "国企");
		para1.put("BUSINESS_CATEGORY", "经销商");
		para1.put("INOUT_CITY", "市内");

		para1.put("ACTION_PERSION", "EFUTERP");
		para1.put("ADMISSIONDATE", "20150624");

		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		dataList.add(para);
		dataList.add(para1);

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("version", "1");
		dataMap.put("header", "");
		dataMap.put("data", dataList);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("data", JsonUtil.getJSONString(dataMap));
		data.put("header", header);
		System.out.println(JsonUtil.getJSONString(data));

		String url = "http://127.0.0.1:8085/pcm-import/pcmSupplyInfo/uploadPcmSupplyInfoFromEFutureERP.htm";
		String response = HttpUtil.doPost(url, JsonUtil.getJSONString(data));
		System.out.println(response);
	}

}
