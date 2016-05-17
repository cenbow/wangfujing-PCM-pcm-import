package test.com.wangfj.product.service;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wangfj.product.brand.domain.vo.PcmBrandRelationDto;
import com.wangfj.product.brand.service.intf.IPcmBrandRelationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestPcmBrandRelationServiceImpl {

	@Autowired
	private IPcmBrandRelationService iPcmBrandRelationService;

	@Test
	public void test() {
		maintainRelation();
	}

	public void maintainRelation() {

		PcmBrandRelationDto pcmBrandRelationDto = new PcmBrandRelationDto();
		pcmBrandRelationDto.setBrandSid("1");
		pcmBrandRelationDto.setBrandRootSid("1000001");

		try {
			Integer result = iPcmBrandRelationService.addRelation(pcmBrandRelationDto);
			System.out.println(result);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

}
