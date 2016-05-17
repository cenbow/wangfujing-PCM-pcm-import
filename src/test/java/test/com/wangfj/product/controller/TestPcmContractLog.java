package test.com.wangfj.product.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.wangfj.core.utils.HttpUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.EfutureERP.controller.support.PcmContractLogPara;

public class TestPcmContractLog {

	@Test
	public void test() {
		this.uploadContractLogFromEFuture();
	}

	/**
	 * 门店ERP上传要约信息到门店
	 * 
	 * @Methods Name uploadContractLogFromEFuture
	 * @Create In 2015-8-21 By liuhp void
	 */
	public void uploadContractLogFromEFuture() {
		List<PcmContractLogPara> contracts = new ArrayList<PcmContractLogPara>();

		PcmContractLogPara contract1 = new PcmContractLogPara();
		contract1.setStoreCode("104");
		contract1.setContractCode("85789830");
		contract1.setManageType("9");
		contract1.setBuyType("2");
		contract1.setOperMode("3");
		// contract1.setSupplyCode("0001000018");
		contract1.setBusinessType("E");
		contract1.setInputTax("0.17");
		contract1.setOutputTax("0.13");
		contract1.setCommissionRate("0.25");
		contract1.setActionCode("U");
		contract1.setActionPerson("jsak");
		contracts.add(contract1);

		PcmContractLogPara contract2 = new PcmContractLogPara();
		contract2.setStoreCode("105");
		contract2.setContractCode("85789830");
		contract2.setManageType("9");
		contract2.setBuyType("3");
		contract2.setOperMode("2");
		// contract2.setSupplyCode("0001000019");
		contract2.setBusinessType("E");
		contract2.setInputTax("0.17");
		contract2.setOutputTax("0.13");
		contract2.setCommissionRate("0.25");
		contract2.setActionCode("A");
		contract2.setActionPerson("jsak");
		contracts.add(contract2);

		String response = HttpUtil.doPost(
				"http://127.0.0.1:8085/pcm-import/contract/uploadContractLog.htm",
				JsonUtil.getJSONString(contracts));
		System.out.println(response);
	}
}
