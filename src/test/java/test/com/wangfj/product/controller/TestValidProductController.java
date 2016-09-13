package test.com.wangfj.product.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.PIS.controller.support.ProductListPara;
import com.wangfj.product.PIS.controller.support.ValidProductPara;
import com.wangfj.product.SAPERP.controller.support.ProductsSAPERP;
import com.wangfj.product.SAPERP.controller.support.SaveContractParaSAPERP;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.RequestHeader;

public class TestValidProductController {
	@Test
	public void test() {
		// getPISValidProductFromEfuture();
		// pullProductFromYongliPIS();
		// set33();
		set44();
		// set4455();
	}

	// public void pullProductFromYongliPIS() {
	// PullProductPara ppp = new PullProductPara();
	// PullHeaderPara php = new PullHeaderPara();
	// PullDataPara pdp = new PullDataPara();
	// ppp.setFromSystem("PIS");
	// ppp.setVersion("1");
	// php.setCount("1");
	// php.setReset("false");
	// php.setTitile("商品准入导入信息");
	// pdp.setBrandCode("1010909");// 集团品牌编码
	// pdp.setBrandSid("10001");// 门店品牌编码
	// pdp.setColorCode("红绿");// 色码
	// pdp.setColorName("红绿");// 色码描述
	// pdp.setColorSid("2");// 色系
	// pdp.setCounterCode("D00100002");// 专柜编码
	// pdp.setErpProductCode("10007540002");// 大码编码
	// pdp.setErpSkuType("4");// 大码类型
	// pdp.setGlCategotyCode("10101010");// 管理分类编码
	// pdp.setIsManageInventory("Y");// 是否管库存
	// pdp.setManageType("1");// 管理类型
	// pdp.setMarketPrice("10000");// 吊牌价（原价）
	// pdp.setModelNum("RB 4187 856/13");// 货号
	// pdp.setOperateMode("Z003");// 经营方式（Z001经销，Z002代销，Z003联营，Z004平台服务，Z005租赁）
	// pdp.setProdCategoryCode("10101020");// 工业分类编码
	// pdp.setProductAbbr("RB");// 商品简称
	// pdp.setProductName("Ray-Ban");// 商品名称
	// pdp.setProductNum("OUS   3TM91700323");// 款号
	// pdp.setSeasonCode("01");// 季节编码
	// pdp.setSizeCode("19");// 尺码/规格
	// pdp.setStandardBarCode("");// 商品企业内部条码（如果有企业内部条码则提供，否则为空）
	// pdp.setStoreCode("10001");// 门店编码
	// pdp.setSupplierBarCode("");// 国标条码（如果商品有国标条码则提供，否则为空）
	// pdp.setSupplierIntProdCode("GYSPRO-01");// 供应商内部系统商品编码（供应商系统内部商品编码）
	// pdp.setSupplierCode("0001000018");// 供应商编码
	// pdp.setUnitCode("个");// 销售单位
	// pdp.setYearToMarket("20150715.100644+0800");// 上市年份
	//
	// List<PullDataPara> lpdp = new ArrayList<PullDataPara>();
	// lpdp.add(pdp);
	// ppp.setHeader(php);
	// ppp.setData(lpdp);
	// System.out.println(JsonUtil.getJSONString(ppp));
	// String response = HttpUtil.doPost(
	// "http://127.0.0.1:8088/pcm-core/validProduct/pullProductFromYongliPIS.htm",
	// JsonUtil.getJSONString(ppp));
	// System.out.println(response);
	// }

	public void getPISValidProductFromEfuture() {
		ValidProductPara vpp = new ValidProductPara();
		List<ProductListPara> lplp = new ArrayList<ProductListPara>();
		vpp.setSupplierCode("001830A");
		vpp.setCounterCode("2101100023");
		vpp.setFromSystem("PIS");
		for (int i = 0; i < 2; i++) {
			ProductListPara plp = new ProductListPara();
			if (i == 0) {
				plp.setErpProductCode("03529836");
				plp.setProductNum("6903148190821");
				plp.setBrandCode("1010807");
				plp.setColorCode("红色");
				plp.setSizeCode("100g");
			} else {
				plp.setErpProductCode("-1");
				plp.setProductNum("-1");
				plp.setBrandCode("-1");
				plp.setColorCode("-1");
				plp.setSizeCode("-1");
			}
			lplp.add(plp);
		}
		vpp.setProductCount(String.valueOf(lplp.size()));
		vpp.setListProductPara(lplp);
		System.out.println(JsonUtil.getJSONString(vpp));
		String response = HttpUtil.doPost(
				"http://127.0.0.1:8081/pcm-core/validProduct/getPISValidProductFromEfuture.htm",
				null);
		// JsonUtil.getJSONString(vpp));
		System.out.println(response);
	}

	public void set44() {
		MqRequestDataListPara<ProductsSAPERP> param = new MqRequestDataListPara<ProductsSAPERP>();
		RequestHeader header = new RequestHeader();
		header.setCallbackUrl("url");
		param.setHeader(header);
		List<ProductsSAPERP> products = new ArrayList<ProductsSAPERP>();
		ProductsSAPERP product = new ProductsSAPERP();
		product.setCOLORSID("3");// 色系
		product.setGOODCLASS("201511251");// 款号
		product.setIS_GIFT("4");// 赠品类型
		product.setLIFNR("0001000023");// 供应商
		product.setMAKTX("商品描述(短文本");// 商品描述(短文本
		product.setMATKL("145");// 商品类目(工业分类)
		product.setMATNR("1111");// 原系统商品编码
		product.setMEINS("基本计量单位");// 基本计量单位
		product.setMSTAV("N");// 跨分销链商品状态(停售标记)（Y/N）
		product.setMTART("0");// 经营方式0经销，1代销，2联营，3平台服务，4租赁
		product.setSAISO("01");// 季节（01 春02夏03秋04冬05春夏06春秋07秋冬08四季09春夏秋）
		product.setSCATE("340");// 统计分类
		product.setTAXKM1("0.15");// 销项税
		product.setTAXKM2("0.15");// 消费税
		product.setTAXKM3("0.15");// 进费税
		product.setUNIT("件");// 销售单位
		product.setZCOLOR("特性色码");// 特性-颜色
		product.setZGID("4000100111");// 专柜编码
		product.setZLAND("原产国");// 原产国
		product.setZLOCAL("原产地");// 原产地
		product.setZLY_FLAG("Y");// 虚库标志（Y/N）
		product.setZSIZE("特性规格");// 特性-尺码/规格
		product.setZSPRICE("25.22"); // 售价
		product.setZZSSDATE("20151118");// 上市日期（yyyymmdd）
		product.setZZBRAND_ID("200002");// 门店品牌
		product.setZZCARD("N");// 是否可贺卡
		product.setZZCOD("N");// 是否可货到付款
		product.setZZCOLORCODE("绿色");// 色码
		product.setZZDKNO("321321");// 货号
		product.setZZGENDER("4");// 适用性别
		product.setZZPACK("N");// 是否可包装
		product.setZZPRICE("29.22");// 原价
		product.setZZSIZECODE("xxl");// 尺码
		// product.setZZVDBC("99999009");// 条码
		product.setZZVDMAT("1010111");// 供应商商品编码
		product.setZZWLLX("4");// 物流类型
		product.setZZXXHC_FLAG("N");// 是否先销后采
		product.setZZYCBZ("N");// 是否原厂包装
		product.setS_MATNR("");
		product.setOFFERNUMBER("2015112509");
		ProductsSAPERP product2 = new ProductsSAPERP();
		product2.setCOLORSID("4");// 色系
		product2.setGOODCLASS("201511302");// 款号
		product2.setIS_GIFT("4");// 赠品类型
		product2.setLIFNR("0001000023");// 供应商
		product2.setMAKTX("商品描述(短文本");// 商品描述(短文本
		product2.setMATKL("145");// 商品类目(工业分类)
		product2.setMATNR("2222");// 原系统商品编码
		product2.setMEINS("基本计量单位");// 基本计量单位
		product2.setMSTAV("N");// 跨分销链商品状态(停售标记)（Y/N）
		product2.setMTART("0");// 经营方式0经销，1代销，2联营，3平台服务，4租赁
		product2.setSAISO("01");// 季节（01 春02夏03秋04冬05春夏06春秋07秋冬08四季09春夏秋）
		product2.setSCATE("009002001");// 统计分类
		product2.setTAXKM1("0.15");// 销项税
		product2.setTAXKM2("0.15");// 消费税
		product2.setTAXKM3("0.15");// 进费税
		product2.setUNIT("件");// 销售单位
		product2.setZCOLOR("特性色码");// 特性-颜色
		product2.setZGID("4000100111");// 专柜编码
		product2.setZLAND("原产国");// 原产国
		product2.setZLOCAL("原产地");// 原产地
		product2.setZLY_FLAG("Y");// 虚库标志（Y/N）
		product2.setZSIZE("特性规格");// 特性-尺码/规格
		product2.setZSPRICE("25.22"); // 售价
		product2.setZZSSDATE("20151118");// 上市日期（yyyymmdd）
		product2.setZZBRAND_ID("200002");// 门店品牌
		product2.setZZCARD("N");// 是否可贺卡
		product2.setZZCOD("N");// 是否可货到付款
		product2.setZZCOLORCODE("蓝色");// 色码
		product2.setZZDKNO("321321");// 货号
		product2.setZZGENDER("4");// 适用性别
		product2.setZZPACK("N");// 是否可包装
		product2.setZZPRICE("29.22");// 原价
		product2.setZZSIZECODE("l");// 尺码
		// product2.setZZVDBC("1010110101");// 条码
		product2.setZZVDMAT("1010111");// 供应商商品编码
		product2.setZZWLLX("4");// 物流类型
		product2.setZZXXHC_FLAG("N");// 是否先销后采
		product2.setZZYCBZ("N");// 是否原厂包装
		product2.setOFFERNUMBER("2015112509");
		// product2.setZZPICCODE("");// 照片编码
		// product2.setWERKS("");// 工厂（门店）
		// products.add(product2);

		ProductsSAPERP product3 = new ProductsSAPERP();
		product3.setCOLORSID("2");// 色系
		product3.setGOODCLASS("2015113013");// 款号
		product3.setIS_GIFT("4");// 赠品类型
		product3.setLIFNR("0001000023");// 供应商
		product3.setMAKTX("商品描述(短文本");// 商品描述(短文本
		product3.setMATKL("145");// 商品类目(工业分类)
		product3.setMATNR("1111");// 原系统商品编码
		product3.setMEINS("基本计量单位");// 基本计量单位
		product3.setMSTAV("N");// 跨分销链商品状态(停售标记)（Y/N）
		product3.setMTART("0");// 经营方式0经销，1代销，2联营，3平台服务，4租赁
		product3.setSAISO("01");// 季节（01 春02夏03秋04冬05春夏06春秋07秋冬08四季09春夏秋）
		product3.setSCATE("340");// 统计分类
		product3.setTAXKM1("0.15");// 销项税
		product3.setTAXKM2("0.15");// 消费税
		product3.setTAXKM3("0.15");// 进费税
		product3.setUNIT("件");// 销售单位
		product3.setZCOLOR("特性色码");// 特性-颜色
		product3.setZGID("4000100111");// 专柜编码
		product3.setZLAND("原产国");// 原产国
		product3.setZLOCAL("原产地");// 原产地
		product3.setZLY_FLAG("Y");// 虚库标志（Y/N）
		product3.setZSIZE("特性规格");// 特性-尺码/规格
		product3.setZSPRICE("25.22"); // 售价
		product3.setZZSSDATE("20151118");// 上市日期（yyyymmdd）
		product3.setZZBRAND_ID("200002");// 门店品牌
		product3.setZZCARD("N");// 是否可贺卡
		product3.setZZCOD("N");// 是否可货到付款
		product3.setZZCOLORCODE("黄色");// 色码
		product3.setZZDKNO("321321");// 货号
		product3.setZZGENDER("4");// 适用性别
		product3.setZZPACK("N");// 是否可包装
		product3.setZZPRICE("29.22");// 原价
		product3.setZZSIZECODE("xxl");// 尺码
		// product3.setZZVDBC("1010110101");// 条码
		product3.setZZVDMAT("1010111");// 供应商商品编码
		product3.setZZWLLX("4");// 物流类型
		product3.setZZXXHC_FLAG("N");// 是否先销后采
		product3.setZZYCBZ("N");// 是否原厂包装
		// product3.setOFFERNUMBER("");
		product3.setS_MATNR("30000145");
		// product3.setZZPICCODE("");// 照片编码
		// product3.setWERKS("");// 工厂（门店）
		ProductsSAPERP product4 = new ProductsSAPERP();
		product4.setCOLORSID("2");// 色系
		product4.setGOODCLASS("2015113024");// 款号
		product4.setIS_GIFT("4");// 赠品类型
		product4.setLIFNR("0001000023");// 供应商
		product4.setMAKTX("商品描述(短文本");// 商品描述(短文本
		product4.setMATKL("145");// 商品类目(工业分类)
		product4.setMATNR("2222");// 原系统商品编码
		product4.setMEINS("基本计量单位");// 基本计量单位
		product4.setMSTAV("N");// 跨分销链商品状态(停售标记)（Y/N）
		product4.setMTART("0");// 经营方式0经销，1代销，2联营，3平台服务，4租赁
		product4.setSAISO("01");// 季节（01 春02夏03秋04冬05春夏06春秋07秋冬08四季09春夏秋）
		product4.setSCATE("340");// 统计分类
		product4.setTAXKM1("0.15");// 销项税
		product4.setTAXKM2("0.15");// 消费税
		product4.setTAXKM3("0.15");// 进费税
		product4.setUNIT("件");// 销售单位
		product4.setZCOLOR("特性色码");// 特性-颜色
		product4.setZGID("4000100111");// 专柜编码
		product4.setZLAND("原产国");// 原产国
		product4.setZLOCAL("原产地");// 原产地
		product4.setZLY_FLAG("Y");// 虚库标志（Y/N）
		product4.setZSIZE("特性规格");// 特性-尺码/规格
		product4.setZSPRICE("25.22"); // 售价
		product4.setZZSSDATE("20151118");// 上市日期（yyyymmdd）
		product4.setZZBRAND_ID("200002");// 门店品牌
		product4.setZZCARD("N");// 是否可贺卡
		product4.setZZCOD("N");// 是否可货到付款
		product4.setZZCOLORCODE("黄色");// 色码
		product4.setZZDKNO("321321");// 货号
		product4.setZZGENDER("4");// 适用性别
		product4.setZZPACK("N");// 是否可包装
		product4.setZZPRICE("29.22");// 原价
		product4.setZZSIZECODE("m");// 尺码
		// product4.setZZVDBC("1010110101");// 条码
		product4.setZZVDMAT("1010111");// 供应商商品编码
		product4.setZZWLLX("4");// 物流类型
		product4.setZZXXHC_FLAG("N");// 是否先销后采
		product4.setZZYCBZ("N");// 是否原厂包装
		// product4.setZZPICCODE("");// 照片编码
		// product4.setWERKS("");// 工厂（门店
		product4.setOFFERNUMBER("2015111901");
		product4.setS_MATNR("30000146");
		// products.add(product);
		products.add(product2);
		// products.add(product3);
		// products.add(product4);
		param.setData(products);
		System.out.println(JsonUtil.getJSONString(param));
		String response = HttpUtil.doPost(
				"http://127.0.0.1:8085/pcm-import/saveProduct/saveProductFromSAPERP.htm",
				JsonUtil.getJSONString(param));
		System.out.println(response);
	}

	public void set4455() {
		MqRequestDataListPara<SaveContractParaSAPERP> param = new MqRequestDataListPara<SaveContractParaSAPERP>();
		RequestHeader header = new RequestHeader();
		header.setCallbackUrl("url");
		param.setHeader(header);
		List<SaveContractParaSAPERP> list = new ArrayList<SaveContractParaSAPERP>();
		SaveContractParaSAPERP para = new SaveContractParaSAPERP();
		para.setACTION_CODE("A");
		para.setACTION_DATE("20150101");
		para.setACTION_PERSON("Test");
		para.setBUSINESSTYPE("C");
		para.setBUYTYPE("2");
		para.setCOMMISSIONRATE("0.15");
		para.setCONTRACTCODE("2015112509");
		para.setINPUTTAX("0.17");
		para.setMANAGETYPE("3");
		para.setOPERMODE("2");
		para.setOUTPUTTAX("0.17");
		para.setSTORECODE("40001");
		para.setSUPPLIERCODE("0001000044");
		SaveContractParaSAPERP para2 = new SaveContractParaSAPERP();
		para2.setACTION_CODE("A");
		para2.setACTION_DATE("20150101");
		para2.setACTION_PERSON("Test");
		para2.setBUSINESSTYPE("D");
		para2.setBUYTYPE("1");
		para2.setCOMMISSIONRATE("0.35");
		para2.setCONTRACTCODE("2015112502");
		para2.setINPUTTAX("0.17");
		para2.setMANAGETYPE("9");
		para2.setOPERMODE("1");
		para2.setOUTPUTTAX("0.17");
		para2.setSTORECODE("40001");
		para2.setSUPPLIERCODE("00010000211");
		// list.add(para2);
		list.add(para);
		param.setData(list);
		System.out.println(JsonUtil.getJSONString(param));
		String response = HttpUtil.doPost(
				"http://10.6.2.48:8085/pcm-import/saveProduct/saveContractFromSAPERP.htm",
				JsonUtil.getJSONString(param));
		System.out.println(response);
	}

}
