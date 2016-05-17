package test.com.wangfj.product.persistence.organization;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wangfj.product.organization.persistence.PcmFloorMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestFloorMapper {
	@Autowired
	private PcmFloorMapper pcmFloorMapper;

	@Test
	public void testFloor() throws IllegalAccessException, InvocationTargetException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = pcmFloorMapper.findFloorByParamFromPcm(paramMap);
		System.out.println(list);
	}
}
