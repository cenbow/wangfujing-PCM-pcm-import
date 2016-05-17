package test.com.wangfj.product.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wangfj.core.constants.ComErrorCodeConstants.ErrorCode;
import com.wangfj.core.framework.exception.BleException;
import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.core.utils.ThrowExcetpionUtil;
import com.wangfj.product.SAPERP.controller.support.PcmStockPara;
import com.wangfj.product.maindata.domain.vo.PcmEdiProductStockDto;
import com.wangfj.product.maindata.domain.vo.QueryEdiProductStockDto;
import com.wangfj.product.maindata.persistence.PcmShoppeProductEdiRelationMapper;
import com.wangfj.util.mq.MqRequestDataListPara;
import com.wangfj.util.mq.RequestHeader;

public class TestStockCon {

	@Autowired
	private PcmShoppeProductEdiRelationMapper proEdiMapper;

	@Test
	public void Test() {
		 findStockImportFromPcm();
//		findStockImportFromPAD();
	}

	/* 导入 */
	public void findStockImportFromPcm() {// 导入
		MqRequestDataListPara<PcmStockPara> mqpara = new MqRequestDataListPara<PcmStockPara>();

		List<PcmStockPara> paraList = new ArrayList<PcmStockPara>();
		PcmStockPara para = new PcmStockPara();

		para.setSKU("123123424");
		para.setSOURCE("FJERP");
		para.setSUPPLYPRODUCTID("20000006");
		para.setTYPE("ALL");
		para.setINVENTORY(1000);
		para.setOPERATOR("导入终端");

		PcmStockPara para1 = new PcmStockPara();

		para1.setSKU("123123424");
		para1.setSOURCE("FJERP");
		para1.setSUPPLYPRODUCTID("30000110");
		para1.setTYPE("DELTA");
		para1.setINVENTORY(1000);
		para1.setOPERATOR("导入终端");

		paraList.add(para);
		RequestHeader header = new RequestHeader();
		header.setCallbackUrl("http://10.6.1.69:10101");
		mqpara.setData(paraList);
		mqpara.setHeader(header);
		// paraList.add(para1);
		System.out.println(JsonUtil.getJSONString(paraList));
		String response = HttpUtil.doPost(
				// "http://10.6.2.48:8085/pcm-import/stockImport/findStockImportFromPcm.htm",
				"http://127.0.0.1:8085/pcm-import/stockImport/SAPErp/findStockImportFromPcm.htm",
				JsonUtil.getJSONString(mqpara));
		System.out.println(response);
	}

	/* 导入 */
	public void findStockImportFromPAD() {// 导入

		List<com.wangfj.product.PAD.controller.support.PcmStockPara> paraList = new ArrayList<com.wangfj.product.PAD.controller.support.PcmStockPara>();
		com.wangfj.product.PAD.controller.support.PcmStockPara para = new com.wangfj.product.PAD.controller.support.PcmStockPara();

		para.setSource("PAD");
		para.setSupplyProductId("20000001");
		para.setType("DELTA");
		para.setInventory(90);
		para.setDefectiveInventory(100);
		para.setOperators("PAD:1000001-1");
		para.setChannelSid("0");

		paraList.add(para);
		// paraList.add(para1);

		System.out.println(JsonUtil.getJSONString(paraList));
		String response = HttpUtil.doPost(
				// "http://10.6.2.48:8085/pcm-import/stockImport/findStockImportFromPcm.htm",
				"http://127.0.0.1:8085/pcm-import/stockImport/findStockImportFromPAD.htm",
				JsonUtil.getJSONString(paraList));
		System.out.println(response);
	}

	@Test
	public void test11() {
		ThrowExcetpionUtil
				.splitExcetpion(new BleException(ErrorCode.ADD_CHANGE_PRICE_ERROR.getErrorCode(),
						ErrorCode.ADD_CHANGE_PRICE_ERROR.getMemo()));
	}
}
