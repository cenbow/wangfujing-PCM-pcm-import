package test.com.wangfj.product.persistence.price;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mysql.fabric.xmlrpc.base.Data;
import com.wangfj.core.utils.DateUtil;
import com.wangfj.core.utils.JsonUtil;
import com.wangfj.product.price.domain.entity.PcmPrice;
import com.wangfj.product.price.domain.vo.QueryPriceInfoDto;
import com.wangfj.product.price.persistence.PcmPriceMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestPcmPriceMapper {
	@Autowired
	public PcmPriceMapper pcmPriceMapper;

	@Test
	public void test() {
		// testInitProductPriceInfo();
		// testUpdateByShopProSidSelective();
		testProductPriceDelete();
	}

	/**
	 * service商品价格数据初始化
	 * 
	 * @Methods Name testInitProductPriceInfo
	 * @Create In 2015年7月28日 By kongqf void
	 */
	@Test
	public void testInitProductPriceInfo() {

		PcmPrice pcmPrice = new PcmPrice();
		pcmPrice.setShoppeProSid("10000000017");
		pcmPrice.setPromotionPrice(new BigDecimal(100));
		pcmPrice.setCurrentPrice(new BigDecimal(900));
		pcmPrice.setOriginalPrice(new BigDecimal(1000));
		pcmPrice.setOffValue(new BigDecimal(100 / 1000));
		pcmPrice.setPromotionBeginTime(DateUtil.formatDate("20150607", "yyyyMMdd"));
		pcmPrice.setPromotionEndTime(DateUtil.formatDate("20151007", "yyyyMMdd"));
		pcmPrice.setProductSid(Long.parseLong("100003"));
		pcmPrice.setPromotionBackup(new BigDecimal(100));
		pcmPrice.setChannelSid("2");
		pcmPrice.setUnit("RMB");
		// pcmPrice.setCreateTime(DateUtil.formatDate("20150615110901",
		// "yyyyMMddHHmmss"));
		try {
			int returnValue = pcmPriceMapper.insertSelective(pcmPrice);
			System.out.println("add successed,insert row " + returnValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 变价修改U
	 * 
	 * @Methods Name testUpdateByShopProSidSelective
	 * @Create In 2015年7月30日 By kongqf void
	 */
	public void testUpdateByShopProSidSelective() {
		PcmPrice pcmPrice = new PcmPrice();
		pcmPrice.setShoppeProSid("100000007545");
		pcmPrice.setPriceChannel("1");
		pcmPrice.setCurrentPrice(new BigDecimal("800"));
		pcmPrice.setPromotionBeginTime(DateUtil.formatToDate("2015-07-08", "yyyy-MM-dd"));
		pcmPrice.setPromotionEndTime(DateUtil.formatToDate("2015-08-08", "yyyy-MM-dd"));
		pcmPrice.setUnit("RMB");
		// int i = pcmPriceMapper.updateByShopProSidSelective(pcmPrice);
		// System.out.println(i);
	}

	/**
	 * 变价删除D
	 * 
	 * @Methods Name testProductPriceDelete
	 * @Create In 2015年7月30日 By kongqf void
	 */
	public void testProductPriceDelete() {
		PcmPrice pcmPrice = new PcmPrice();
		// pcmPrice.setShoppeProSid("100000007545");
		pcmPrice.setPriceChannel("1");
		pcmPrice.setCurrentPrice(new BigDecimal("800"));
		pcmPrice.setPromotionBeginTime(DateUtil.formatToDate("2015-07-08", "yyyy-MM-dd"));
		pcmPrice.setPromotionEndTime(DateUtil.formatToDate("2015-08-08", "yyyy-MM-dd"));
		pcmPrice.setUnit("RMB");
		pcmPrice.setIfdel(1);
		// int i = pcmPriceMapper.updateByShopProSidSelective(pcmPrice);
		// System.out.println(i);
	}

	@Test
	public void testselectMiddleActivePriceInfo() {
		QueryPriceInfoDto queryPriceInfoDto = new QueryPriceInfoDto();
		queryPriceInfoDto.setPromotionBeginTime(
				DateUtil.formatDate("2015-08-02 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		queryPriceInfoDto.setPromotionEndTime(
				DateUtil.formatDate("2015-09-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		queryPriceInfoDto.setShoppeProSid("100001853206");
		List<PcmPrice> pcmPrices = new ArrayList<PcmPrice>();
		pcmPrices = pcmPriceMapper.selectMiddleActivePriceInfo(queryPriceInfoDto);
		System.out.println(JsonUtil.getJSONString(pcmPrices));
		System.out.println(pcmPrices);
	}
}
