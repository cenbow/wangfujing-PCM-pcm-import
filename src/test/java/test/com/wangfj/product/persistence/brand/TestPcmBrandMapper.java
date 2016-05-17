package test.com.wangfj.product.persistence.brand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wangfj.product.brand.domain.entity.PcmBrand;
import com.wangfj.product.brand.domain.vo.PcmBrandDto;
import com.wangfj.product.brand.persistence.PcmBrandMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestPcmBrandMapper {
	@Autowired
	public PcmBrandMapper pcmBrandMapper;

	@Test
	public void test() {

	}

	public void selectListByParam() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandName", "s");
		List<PcmBrand> list = pcmBrandMapper.selectListByParam(map);
		System.out.println("TestPcmBrandMapper.selectListByParam()" + list.size());

	}

	public void selectPageList() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", 0);
		map.put("limit", 2);
		List<PcmBrand> list = pcmBrandMapper.selectListByParam(map);
		System.out.println("TestPcmBrandMapper.selectPageList()" + list.size());
	}

	public void selectCount() {
		Map<String, Object> map = new HashMap<String, Object>();
		// map.put("brandName", "s");
		Integer count = pcmBrandMapper.getCountByParam(map);
		System.out.println("TestPcmBrandMapper.selectCount()" + count);
	}

	public void testDto() {
		PcmBrandDto dto = new PcmBrandDto();
		dto.setSid(1l);
		dto.setBrandSid("2");
		dto.setBrandName("naike");
		dto.setBrandActiveBit(1l);
		dto.setBrandDesc("very good");
		List<PcmBrandDto> list = new ArrayList<PcmBrandDto>();
		list.add(dto);
		JSONArray jsonArray = new JSONArray();
		jsonArray.addAll(list);
		JSONObject object = new JSONObject();
		object.element("data", jsonArray);
		object.element("success", false);
		System.out.println(object.toString());
	}

	// @Test
	// public void testSelectBrandListByParamMap() {
	//
	// Map<String, Object> paramMap = new HashMap<String, Object>();
	// paramMap.put("shopType", 0);
	// paramMap.put("brandName", "add");
	// // paramMap.put("brandSid", "1");
	//
	// List<PcmBrand> pcmBrandList =
	// pcmBrandMapper.selectBrandListByParamMap(paramMap);
	//
	// System.out.println(pcmBrandList);
	//
	// System.out.println("================");
	//
	// }

}
