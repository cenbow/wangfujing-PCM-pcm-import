package test.com.wangfj.product.persistence.supplier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wangfj.product.supplier.domain.entity.PcmShoppeProductSupply;
import com.wangfj.product.supplier.persistence.PcmShoppeProductSupplyMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
      "classpath:applicationContext.xml"
      })
public class TestShoppeProductSupplyMapper {
	@Autowired
	private PcmShoppeProductSupplyMapper pcmShoppeProductSupplyMapper;

	@Test
	public void test() {
		// insertShopProductSu();
	}
	
	public void insertShopProductSu() {
		PcmShoppeProductSupply p = new PcmShoppeProductSupply();
		/*
		 * p.setShoppeProductSid("1"); p.setSupplySid("1");
		 */
		int r = 0;
		try {
			r = pcmShoppeProductSupplyMapper.insertSelective(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(r);
	}
}
