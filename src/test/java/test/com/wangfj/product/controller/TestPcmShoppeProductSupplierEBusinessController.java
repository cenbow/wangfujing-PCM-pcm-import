package test.com.wangfj.product.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.core.controller.supplier.support.PcmShoppeProductSupplierEBusinessPara;
import com.wangfj.util.mq.RequestHeader;

public class TestPcmShoppeProductSupplierEBusinessController {

	@Test
	public void test() {

		PcmShoppeProductSupplierEBusinessPara para = new PcmShoppeProductSupplierEBusinessPara();

		para.setACTIONCODE("A");
		para.setSTORECODE("21011");
		para.setLIFNR("000343K");
		para.setS_MATNR("10000025");
		// para.setMATNR("");

		PcmShoppeProductSupplierEBusinessPara dest = new PcmShoppeProductSupplierEBusinessPara();
		try {
			BeanUtils.copyProperties(dest, para);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		System.out.println(dest);

	}

	/**
	 * 一品多供应商关系上传
	 * 
	 * @Methods Name upload
	 * @Create In 2015-10-10 By wangxuan void
	 */
	@Test
	public void upload() {

		RequestHeader header = new RequestHeader();
		header.setCallbackUrl("callBackUrlTest");
		Map<String, Object> para1 = new HashMap<String, Object>();

		// shop_sid = 149,supply_sid = 124,shoppe_product_sid=10000021
		// shoppeProSupplyPara.put("storeCode", "40001");
		// shoppeProSupplyPara.put("supplierCode", "1001000024");
		// shoppeProSupplyPara.put("supplierProductCode", "10000021");
		// shoppeProSupplyPara.put("productCode", "10007540003");
		//
		// shoppeProSupplyPara.put("ACTION_CODE", "A");

		para1.put("STORECODE", "21011");
		para1.put("LIFNR", "000343K");
		para1.put("S_MATNR", "10000025");
		// para1.put("MATNR", "10007540003");

		para1.put("ACTIONCODE", "A");
		// para1.put("ACTIONCODE", "D");

		Map<String, Object> para2 = new HashMap<String, Object>();

		para2.put("STORECODE", "21011");
		para2.put("LIFNR", "000341A");
		para2.put("S_MATNR", "10000025");
		// para2.put("MATNR", "10007540003");

		// para2.put("ACTIONCODE", "A");
		para2.put("ACTIONCODE", "D");

		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		dataList.add(para1);
		dataList.add(para2);

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("version", "1");
		dataMap.put("header", "");
		dataMap.put("data", dataList);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("data", JsonUtil.getJSONString(dataMap));
		data.put("header", header);
		System.out.println(JsonUtil.getJSONString(data));

		String url = "http://127.0.0.1:8085/pcm-import/pcmShoppeProductSupplierEBusiness/uploadShoppeProductSupplier.htm";
		String response = HttpUtil.doPost(url, JsonUtil.getJSONString(data));
		System.out.println(response);

	}

}
