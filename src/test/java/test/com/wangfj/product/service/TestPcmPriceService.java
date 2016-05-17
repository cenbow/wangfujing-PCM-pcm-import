package test.com.wangfj.product.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wangfj.core.framework.base.page.Page;
import com.wangfj.product.PIS.controller.support.PcmPricePISPara;
import com.wangfj.product.price.domain.vo.PcmPricePISDto;
import com.wangfj.product.price.domain.vo.QueryShoppeProSidDto;
import com.wangfj.product.price.domain.vo.SelectPcmPriceToERPPDto;
import com.wangfj.product.price.service.intf.IPcmPriceService;
import com.wangfj.util.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestPcmPriceService {
	@Autowired
	private IPcmPriceService pcmPriceService;

	@Test
	public void test() {
		testqueryPushBatchChangePriceInfo();
	}

	@Test
	public void testqueryBatchChangePriceInfo() {
		PcmPricePISDto pcmPricePISDto = new PcmPricePISDto();
		pcmPricePISDto.setStorecode("104");
		pcmPricePISDto.setChangecode("10010");
		pcmPricePISDto.setBdate("20150910");
		pcmPricePISDto.setEdate("99991231");
		pcmPricePISDto.setSalepricetype(Constants.PRICE_CHANGE_TYPE1);
		pcmPricePISDto.setSalepricevalue("100");
		pcmPricePISDto.setSalepricereason("变价原因");
		pcmPricePISDto.setShoppecode("D00100002");
		pcmPricePISDto.setSuppliercode("0001000018");
		pcmPricePISDto.setCategorycode("10101010");
		pcmPricePISDto.setChannelsid("0");
		pcmPricePISDto.setCurrentPage(1);
		pcmPricePISDto.setPageSize(200);

		Page<QueryShoppeProSidDto> pageDto = new Page<QueryShoppeProSidDto>();
		pcmPriceService.queryBatchChangePriceInfo(pcmPricePISDto);

		System.out.println(pageDto.getList().toString());
	}

	@Test
	public void testqueryPushBatchChangePriceInfo() {
		SelectPcmPriceToERPPDto pcmPricePISPara = new SelectPcmPriceToERPPDto();
		pcmPricePISPara.setStoreCode("104");
		pcmPricePISPara.setChangeCode("10010");
		pcmPricePISPara.setBeginDate("20150910");
		pcmPricePISPara.setBeginDate("99991231");
		pcmPricePISPara.setPriceType(Constants.PRICE_CHANGE_TYPE1);

	}
}
