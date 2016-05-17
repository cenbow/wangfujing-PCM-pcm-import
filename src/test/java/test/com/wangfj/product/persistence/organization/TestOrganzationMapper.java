package test.com.wangfj.product.persistence.organization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wangfj.product.organization.persistence.PcmOrganizationMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestOrganzationMapper {
	@Autowired
	PcmOrganizationMapper pcmOrganizationMapper;

	// @Test
	// public void getOrganzation() {
	// Map<String, Object> paramMap = new HashMap<String, Object>();
	// List<Map<String, Object>> list = pcmOrganizationMapper
	// .findOrganizationByParamFromPcm(paramMap);
	// System.out.println(list);
	// }
}
