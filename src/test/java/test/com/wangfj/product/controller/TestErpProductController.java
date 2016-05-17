package test.com.wangfj.product.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.util.mq.RequestHeader;

/**
 * erp商品信息controller测试
 * 
 * @Class Name TestErpProductController
 * @Author zhangxy
 * @Create In 2015年7月16日
 */
public class TestErpProductController {
	@Test
	public void test() {
		add();
		// selectErpProduct();
	}

	/**
	 * erp商品信息上传
	 * 
	 * @Methods Name add
	 * @Create In 2015年7月16日 By zhangxy void
	 */
	public void add() {
		// MqRequestDataListPara<Map<String, Object>> para111 = new
		// MqRequestDataListPara<Map<String, Object>>();
		RequestHeader he = new RequestHeader();
		he.setCallbackUrl("callBackUrlTest");
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("skuType", "3"); // 大码类型
		para.put("brandCode", "10001"); // 品牌编码
		para.put("articleNum", "ss111"); // 货号
		para.put("productCode", "123123123"); // erp商品编码
		para.put("counterCode", "2001010101"); // 专柜编码
		para.put("storeCode", "20001"); // 门店编码
		para.put("supplierCode", "200000001"); // 供应商编码
		para.put("commissionRate", "33.33"); // 标准扣率
		para.put("discountLimit", "33.33"); // 折扣底限
		para.put("formatType", "1"); // 业态类型
		para.put("inputTax", "33.33"); // 进项税
		para.put("isAdjustPrice", "N"); // 是否允许调价
		para.put("isPromotion", "N"); // 是否允许促销
		para.put("manageCategory", "200"); // 管理分类
		para.put("originLand", "原产地1"); // 原产地
		para.put("outputTax", "33.33"); // 销项税
		para.put("productCategory", "1"); // 产品类别
		para.put("productName", "商品名称"); // 商品名称
		para.put("productType", "Z001"); // 经营方式
		para.put("status", "T"); // 商品状态
		para.put("salesPrice", "33.33"); // 售价
		para.put("salesTax", "33.33"); // 消费税
		para.put("supplierBarcode", "1000010001"); // 供应商自编条码
		para.put("statCategory", "300"); // 统计分类
		para.put("specName", "250ml"); // 规格名称
		para.put("salesUnit", "个");// 销售单位
		para.put("serviceFeeType", "1");// 服务费类型
		para.put("actionCode", "A");// 操作编码
		para.put("actionDate", "19901010.101010");
		para.put("actionPerson", "123");
		List<Map<String, Object>> li = new ArrayList<Map<String, Object>>();
		li.add(para);
		Map<String, Object> para2 = new HashMap<String, Object>();
		para2.put("skuType", "3"); // 大码类型
		para2.put("brandCode", "10001"); // 品牌编码
		para2.put("articleNum", "ss111"); // 货号
		para2.put("productCode", "3333333333"); // erp商品编码
		para2.put("counterCode", "2001010101"); // 专柜编码
		para2.put("storeCode", "20001"); // 门店编码
		para2.put("supplierCode", "200000001"); // 供应商编码
		para2.put("commissionRate", "33.33"); // 标准扣率
		para2.put("discountLimit", "33.33"); // 折扣底限
		para2.put("formatType", "1"); // 业态类型
		para2.put("inputTax", "33.33"); // 进项税
		para2.put("isAdjustPrice", "N"); // 是否允许调价
		para2.put("isPromotion", "N"); // 是否允许促销
		para2.put("manageCategory", "200"); // 管理分类
		para2.put("originLand", "原产地1"); // 原产地
		para2.put("outputTax", "33.33"); // 销项税
		para2.put("productCategory", "1"); // 产品类别
		para2.put("productName", "商品名称"); // 商品名称
		para2.put("productType", "Z001"); // 经营方式
		para2.put("status", "T"); // 商品状态
		para2.put("salesPrice", "33.33"); // 售价
		para2.put("salesTax", "33.33"); // 消费税
		para2.put("supplierBarcode", "1000010001"); // 供应商自编条码
		para2.put("statCategory", "300"); // 统计分类
		para2.put("specName", "250ml"); // 规格名称
		para2.put("salesUnit", "个");// 销售单位
		para2.put("serviceFeeType", "1");// 服务费类型
		para2.put("actionCode", "A");// 操作编码
		para2.put("actionDate", "19901010.101010");
		para2.put("actionPerson", "123");
		li.add(para2);
		Map<String, Object> para3 = new HashMap<String, Object>();
		para3.put("ACTIONPERSON", "123");
		li.add(para3);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("version", "1");
		map.put("header", "");
		map.put("data", li);
		Map<String, Object> para111 = new HashMap<String, Object>();
		para111.put("data", JsonUtil.getJSONString(map));
		para111.put("header", he);
		System.out.println(JsonUtil.getJSONString(para111));
		String response = HttpUtil
				.doPost("http://127.0.0.1:8083/pcm-import/erpProductEfuture/uploadErpProductFromEFuture.htm",
						JsonUtil.getJSONString(para111));
		System.out.println("test:" + response);
	}

	// void testPara() {
	// Map<String, Object> para = new HashMap<String, Object>();
	// para.put("productCode", "sss33"); // erp商品编码
	// String response = HttpUtil.doPost(
	// "http://127.0.0.1:8083/pcm-import/erpProductEfuture/testPara.htm",
	// JsonUtil.getJSONString(para));
	// System.out.println("test:" + response);
	// }

	/**
	 * 测试下发erp商品接口
	 * 
	 * @Methods Name selectErpProduct
	 * @Create In 2015年7月17日 By zhangxy void
	 */
	public void selectErpProduct() {
		Map<String, Object> para = new HashMap<String, Object>();
		// para.put("brandCode", "jt1");
		// para.put("erpSkuType", 3);
		// para.put("erpProductCode", 1111);
		// // para.put("limit", 2);
		// // para.put("start", 1);
		// para.put("currentPage", 2);
		// para.put("pageSize", 2);
		String response = HttpUtil.doPost(
				"http://127.0.0.1:8088/pcm-core/erpProductPIS/findErpProductFromPcm.htm",
				JsonUtil.getJSONString(para));
		System.out.println("test:" + response);
	}
}
