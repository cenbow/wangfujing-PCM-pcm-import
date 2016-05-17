package test.com.wangfj.product.persistence.brand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wangfj.product.brand.domain.entity.PcmBrandGroup;
import com.wangfj.product.brand.domain.vo.PcmBrandGroupDto;
import com.wangfj.product.brand.domain.vo.SelectPcmBrandGroupPageDto;
import com.wangfj.product.brand.persistence.PcmBrandGroupMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestPcmBrandGroupMapper {
	@Autowired
	public PcmBrandGroupMapper pcmBrandGroupMapper;

	@Test
	public void test() {
		// selectListByParam();
		// selectPageList();
		// selectCount();
		// selectListByBrandSidOrBrandName();
		// selectListByParaSelective();
		// getCountByParaForPage();
		selectListByParaForPage();
	}

	public void selectListByParaForPage() {

		SelectPcmBrandGroupPageDto pageDto = new SelectPcmBrandGroupPageDto();
		pageDto.setBrandName("耐");

		pageDto.setStart(0);
		pageDto.setLimit(2);

		List<PcmBrandGroup> brandGroupList = pcmBrandGroupMapper.selectListByParaForPage(pageDto);

		System.out.println(brandGroupList.size());
		System.out.println(brandGroupList);

	}

	public void getCountByParaForPage() {

		SelectPcmBrandGroupPageDto pageDto = new SelectPcmBrandGroupPageDto();
		pageDto.setBrandName("耐");

		Long count = pcmBrandGroupMapper.getCountByParaForPage(pageDto);

		System.out.println(count);

	}

	public void selectListByParaSelective() {

		PcmBrandGroupDto brandGroupDto = new PcmBrandGroupDto();
		brandGroupDto.setBrandSid("1");
		// brandGroupDto.setBrandName("耐");
		brandGroupDto.setBrandNameAlias("nai");
		// brandGroupDto.setBrandNameEn("");
		// brandGroupDto.setBrandNameSpell("");

		List<PcmBrandGroup> brandGroupList = pcmBrandGroupMapper
				.selectListByParaSelective(brandGroupDto);

		System.out.println(brandGroupList.size());
		System.out.println(brandGroupList);

	}

	public void selectListByBrandSidOrBrandName() {

		Map<String, Object> paraMap = new HashMap<String, Object>();
		// paraMap.put("brandName", "耐克");
		paraMap.put("brandSid", "1000001");
		List<PcmBrandGroup> pcmBrandGroupList = pcmBrandGroupMapper
				.selectListByBrandSidOrBrandName(paraMap);

		System.out.println(pcmBrandGroupList);

	}

	public void selectListByParam() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandName", "s");
		List<PcmBrandGroup> selectListByParam = pcmBrandGroupMapper.selectListByParam(map);
		System.out
				.println("TestPcmBrandGroupMapper.selectListByParam()" + selectListByParam.size());

	}

	public void selectPageList() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", 0);
		map.put("limit", 2);
		List<PcmBrandGroup> selectListByParam = pcmBrandGroupMapper.selectListByParam(map);
		for (PcmBrandGroup pcmBrandGroup : selectListByParam) {
			System.out.println(pcmBrandGroup);
			System.out.println("========");
		}
		System.out.println("TestPcmBrandGroupMapper.selectPageList()" + selectListByParam.size());
	}

	public void selectCount() {
		Map<String, Object> map = new HashMap<String, Object>();
		// map.put("brandName", "s");
		Integer count = pcmBrandGroupMapper.getCountByParam(map);
		System.out.println("TestPcmBrandGroupMapper.selectCount()" + count);
	}

}
