package test.com.wangfj.product.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wangfj.product.supplier.service.intf.IPcmSupplyInfoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestPcmSupplyInfoService {
	@Autowired
	private IPcmSupplyInfoService pcmSupplyInfoService;

}
