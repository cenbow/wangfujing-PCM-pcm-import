/**
 * 
 */
package test.com.wangfj.product.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.wangfj.core.utils.DateUtil;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.EfutureERP.controller.support.PcmPriceEFERPPara;
import com.wangfj.product.PIS.controller.support.PcmPricePISPara;
import com.wangfj.product.PIS.controller.support.PcmPricePara;
import com.wangfj.util.Constants;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.MqRequestDataPara;
import com.wangfj.util.mq.RequestHeader;

import net.sf.json.JSONObject;

/**
 * 变价准入测试
 * 
 * @Class Name TestPriceController
 * @Author kongqf
 * @Create In 2015年8月3日
 */
public class TestPcmPricePISController {

	// String URL = "http://10.6.2.48:8085";
	String URL = "http://127.0.0.1:8085";

	@Test
	public void test() {
		testPISAddpriceList2();
	}

	/**
	 * 批量变价请求导入终端
	 * 
	 * @Methods Name testPISAddpriceList
	 * @Create In 2015年8月6日 By kongqf void
	 */
	public void testPISAddpriceList(PcmPricePara pricepara) {
		MqRequestDataListPara<PcmPricePara> mqpara = new MqRequestDataListPara<PcmPricePara>();
		List<PcmPricePara> listparm = new ArrayList<PcmPricePara>();
		pricepara.setStorecode("D001");
		pricepara.setMatnr("");
		// pricepara.setSitecode("2102000003");
		pricepara.setWaers("RMB");
		pricepara.setAction_code("A");
		pricepara.setAction_persion("0025610");
		pricepara.setAction_date(DateUtil.formatToStr(new Date(), "yyyyMMdd.HHmmssZ"));
		pricepara.setGuid(UUID.randomUUID().toString());

		listparm.add(pricepara);

		mqpara.setData(listparm);
		RequestHeader header = new RequestHeader();
		header.setCallbackUrl("http://");
		mqpara.setHeader(header);

		String response = HttpUtil.doPost(
				URL + "/pcm-import/changeprice/pis/importProPriceInfo.htm",
				JsonUtil.getJSONString(mqpara));
		System.out.println(JsonUtil.getJSONString(response));
	}

	public void testPISAddpriceList3(PcmPricePara pricepara) {
		List<PcmPricePara> listparm = new ArrayList<PcmPricePara>();
		pricepara.setStorecode("D001");
		pricepara.setMatnr("");
		pricepara.setSitecode("2101100099");
		pricepara.setWaers("RMB");
		pricepara.setAction_code("A");
		pricepara.setAction_persion("0025610");
		pricepara.setGuid(UUID.randomUUID().toString());

		listparm.add(pricepara);
		String response = HttpUtil.doPost(
				"http://127.0.0.1:8011/pcm-syn/pcmprice/pushpricetoefuture.htm",
				JsonUtil.getJSONString(listparm));
		System.out.println(response);
	}

	public void testPISAddpriceList2() {
		String SUPPLIERPRODCODE = "20000006";
		PcmPricePara pricepara = new PcmPricePara();
		// 价格生效期段拆分
		pricepara.setSupplierprodcode(SUPPLIERPRODCODE);
		pricepara.setZsprice("450");
		pricepara.setBdate("20150910.000000");
		pricepara.setEdate("99991231.235959");
		pricepara.setChangecode("100001");
		// 99991231.235959

		pricepara.setSupplierprodcode(SUPPLIERPRODCODE);
		pricepara.setZsprice("2.5");
		pricepara.setBdate("20160310.000000");
		pricepara.setEdate("20160315.235959");
		pricepara.setChangecode("100007");
		// 99991231.235959

		testPISAddpriceList(pricepara);
		// 价格生效期段拆分
		pricepara.setSupplierprodcode(SUPPLIERPRODCODE);
		pricepara.setZsprice("350");
		pricepara.setBdate("20150902.000000");
		pricepara.setEdate("20150903.235959");
		pricepara.setChangecode("100003");

		// 相同类型单据在相同生效期内，后单覆盖前单；
		pricepara.setSupplierprodcode(SUPPLIERPRODCODE);
		pricepara.setZsprice("300");
		pricepara.setBdate("20150920.000000");
		pricepara.setEdate("99991231.235959");
		pricepara.setChangecode("100004");

		// 不同类型单据在相同生效期内，临时价格优先于永久价格而生效
		pricepara.setSupplierprodcode(SUPPLIERPRODCODE);
		pricepara.setZsprice("111");
		pricepara.setBdate("20150810.000000");
		pricepara.setEdate("99991231.235959");
		pricepara.setChangecode("100004");

		// 不同类型单据在相同生效期内，临时价格优先于永久价格而生效
		pricepara.setSupplierprodcode(SUPPLIERPRODCODE);
		pricepara.setZsprice("230");
		pricepara.setBdate("20150807.083000");
		pricepara.setEdate("20150828.060000");
		pricepara.setChangecode("100007");

		// 不同类型单据在相同生效期内，临时价格优先于永久价格而生效
		pricepara.setSupplierprodcode(SUPPLIERPRODCODE);
		pricepara.setZsprice("100");
		pricepara.setBdate("20150910.000000");
		pricepara.setEdate("20151001.235959");
		pricepara.setChangecode("100006");

	}

	public void testPISAddpriceList22() {
		String SUPPLIERPRODCODE = "100001853244";
		PcmPricePara pricepara = new PcmPricePara();
		// 价格生效期段拆分
		// pricepara.setSupplierprodcode(SUPPLIERPRODCODE);
		// pricepara.setZsprice("500");
		// pricepara.setBdate("20150817");
		// pricepara.setEdate("99991231");
		// pricepara.setChangecode("100001");

		// 价格生效期段拆分
		// pricepara.setSupplierprodcode(SUPPLIERPRODCODE);
		// pricepara.setZsprice("450");
		// pricepara.setBdate("20150820");
		// pricepara.setEdate("20150822");
		// pricepara.setChangecode("100002");

		// 相同类型单据在相同生效期内，后单覆盖前单；
		// pricepara.setSupplierprodcode(SUPPLIERPRODCODE);
		// pricepara.setZsprice("400");
		// pricepara.setBdate("20150821");
		// pricepara.setEdate("20150901");
		// pricepara.setChangecode("100003");

		// 不同类型单据在相同生效期内，临时价格优先于永久价格而生效
		// pricepara.setSupplierprodcode(SUPPLIERPRODCODE);
		// pricepara.setZsprice("400");
		// pricepara.setBdate("20150819");
		// pricepara.setEdate("99991231");
		// pricepara.setChangecode("100004");

		// 不同类型单据在相同生效期内，临时价格优先于永久价格而生效
		// pricepara.setSupplierprodcode(SUPPLIERPRODCODE);
		// pricepara.setZsprice("300");
		// pricepara.setBdate("20150901");
		// pricepara.setEdate("99991231");
		// pricepara.setChangecode("100005");

		// 不同类型单据在相同生效期内，临时价格优先于永久价格而生效
		pricepara.setSupplierprodcode(SUPPLIERPRODCODE);
		pricepara.setZsprice("100");
		pricepara.setBdate("20150910");
		pricepara.setEdate("20151001");
		pricepara.setChangecode("100006");

		testPISAddpriceList(pricepara);
	}

	public void testBatchBase() {
		MqRequestDataListPara<PcmPricePISPara> mqpara = new MqRequestDataListPara<PcmPricePISPara>();
		PcmPricePISPara pcmPricePISPara = new PcmPricePISPara();
		pcmPricePISPara.setStorecode("21020");
		pcmPricePISPara.setChangecode("Z100088888");
		pcmPricePISPara.setBdate("20151220.000000");
		pcmPricePISPara.setEdate("20151225.235959");
		pcmPricePISPara.setSalepricetype(Constants.PRICE_CHANGE_TYPE2);
		pcmPricePISPara.setSalepricevalue("15");
		pcmPricePISPara.setSalepricereason("变价原因");
		pcmPricePISPara.setShoppecode("2102000003");
		pcmPricePISPara.setSuppliercode("");
		pcmPricePISPara.setCategorycode("");
		// pcmPricePISPara.setChannelsid("0");
		List<PcmPricePISPara> pcmPricePISParaList = new ArrayList<PcmPricePISPara>();
		pcmPricePISParaList.add(pcmPricePISPara);

		// Map<String, Object> maps = new HashMap<String, Object>();
		// RequestHeader rh = new RequestHeader();
		// maps.put("data", pcmPricePISParaList);
		//
		// maps.put("header", rh);
		// JSONObject js1 = JSONObject.fromObject(maps);
		// Map<String, Object> maps2 = new HashMap<String, Object>();
		// maps2.put("data", js1.toString());
		// JSONObject js = JSONObject.fromObject(maps2);
		//
		// String response = HttpUtil.doPost(
		// "http://127.0.0.1:8085/pcm-import/changeprice/pis/batchimportproprice.htm",
		// JsonUtil.getJSONString(js));
		// System.out.println(JsonUtil.getJSONString(response));

		mqpara.setData(pcmPricePISParaList);
		RequestHeader header = new RequestHeader();
		header.setCallbackUrl("http://");
		mqpara.setHeader(header);

		String response = HttpUtil.doPost(
				URL + "/pcm-import/changeprice/pis/batchimportproprice.htm",
				JsonUtil.getJSONString(mqpara));
		System.out.println(JsonUtil.getJSONString(response));

	}

	/***
	 * 测试大码变价
	 * 
	 * @Methods Name testERP
	 * @Create In 2015年10月30日 By kongqf void
	 */
	@Test
	public void testERP() {
		MqRequestDataPara mqpara = new MqRequestDataPara();
		List<PcmPricePara> listparm = new ArrayList<PcmPricePara>();
		PcmPricePara pricepara = new PcmPricePara();
		pricepara.setStorecode("21011");
		pricepara.setMatnr("123456");
		pricepara.setSitecode("10000000");
		pricepara.setWaers("RMB");
		pricepara.setAction_code("A");
		pricepara.setAction_persion("0025610");
		// pricepara.setGuid(UUID.randomUUID().toString());
		pricepara.setZsprice("100");
		pricepara.setBdate("20150910");
		pricepara.setEdate("20151001");
		pricepara.setChangecode("100006");

		listparm.add(pricepara);

		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("STORECODE", "D001");
		map.put("MATNR", "123456");
		map.put("SupplierProdCode", "20000267");
		map.put("ZSPRICE", "100");
		map.put("SITECODE", "10000000");
		map.put("WAERS", "RMB");
		map.put("BDATE", "20160318");
		map.put("EDATE", "20160320");
		map.put("CHANGECODE", "100006");
		map.put("ACTION_CODE", "A");
		// map.put("ACTION_DATE", "");
		map.put("ACTION_PERSION", "0025610");
		listMap.add(map);

		Map<String, Object> maps = new HashMap<String, Object>();
		RequestHeader rh = new RequestHeader();
		maps.put("data", JsonUtil.getJSONString(listMap));

		maps.put("header", rh);
		JSONObject js1 = JSONObject.fromObject(maps);
		Map<String, Object> maps2 = new HashMap<String, Object>();
		maps2.put("data", js1.toString());
		JSONObject js = JSONObject.fromObject(maps2);

		String response = HttpUtil.doPost(
				"http://127.0.0.1:8085/pcm-import/changeprice/eferp/importProPriceInfo.htm",
				JsonUtil.getJSONString(js));
		System.out.println(JsonUtil.getJSONString(response));
	}

	/**
	 * 测试营销中心变价上传
	 * 
	 * @Methods Name testEFUTUREERP
	 * @Create In 2015年11月30日 By kongqf void
	 */
	public void testEFUTUREERP() {
		List<PcmPriceEFERPPara> listparm = new ArrayList<PcmPriceEFERPPara>();
		PcmPriceEFERPPara pricepara = new PcmPriceEFERPPara();
		pricepara.setCOCODE("21011");
		pricepara.setMATNR("01004301");
		// pricepara.setCRPSCODE("10000050");
		pricepara.setCSLCODE("2101100001");
		// pricepara.setGuid(UUID.randomUUID().toString());
		pricepara.setNSPRICE("360");
		pricepara.setDTHIS("20150910");
		pricepara.setDEND("20151001");
		pricepara.setNEVTID("10000000000001");

		listparm.add(pricepara);

		MqRequestDataListPara<PcmPriceEFERPPara> mqpara = new MqRequestDataListPara<PcmPriceEFERPPara>();
		mqpara.setData(listparm);
		RequestHeader header = new RequestHeader();
		header.setCallbackUrl("http://");
		mqpara.setHeader(header);

		String response = HttpUtil.doPost(
				"http://127.0.0.1:8085/pcm-import/changeprice/eferp/importPromotionProPriceInfo.htm",
				JsonUtil.getJSONString(mqpara));
		System.out.println(JsonUtil.getJSONString(response));
	}

	@Test
	public void testSUP() {
		List<com.wangfj.product.SUPPLIERCENTER.controller.support.PcmPricePara> listparm = new ArrayList<com.wangfj.product.SUPPLIERCENTER.controller.support.PcmPricePara>();
		com.wangfj.product.SUPPLIERCENTER.controller.support.PcmPricePara pricepara = new com.wangfj.product.SUPPLIERCENTER.controller.support.PcmPricePara();
		pricepara.setStorecode("D001");
		pricepara.setMatnr("01004301");
		pricepara.setSupplierprodcode("20000267");
		pricepara.setZsprice("3");
		pricepara.setSitecode("D00100007");
		pricepara.setBdate("20160318");
		pricepara.setEdate("20160320");
		pricepara.setWaers("RMB");
		pricepara.setChangecode("1000001");
		pricepara.setAction_code("A");
		pricepara.setAction_date(DateUtil.formatToStr(new Date(), "yyyyMMddHHmmssZ"));
		pricepara.setAction_persion("0025610");
		listparm.add(pricepara);

		MqRequestDataListPara<com.wangfj.product.SUPPLIERCENTER.controller.support.PcmPricePara> mqpara = new MqRequestDataListPara<com.wangfj.product.SUPPLIERCENTER.controller.support.PcmPricePara>();
		mqpara.setData(listparm);
		RequestHeader header = new RequestHeader();
		header.setCallbackUrl("http://");
		mqpara.setHeader(header);

		String response = HttpUtil.doPost(
				"http://127.0.0.1:8085/pcm-import/changeprice/Suppliers/importProPriceInfo.htm",
				JsonUtil.getJSONString(mqpara));
		System.out.println(JsonUtil.getJSONString(response));
	}

}
