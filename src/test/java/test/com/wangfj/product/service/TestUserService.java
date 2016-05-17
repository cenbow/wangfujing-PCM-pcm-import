package test.com.wangfj.product.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wangfj.core.utils.CacheUtils;
import com.wangfj.product.demo.domain.dto.UpdateUsersDto;
import com.wangfj.product.demo.domain.entity.Users;
import com.wangfj.product.demo.service.intf.UserService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
        "classpath:applicationContext.xml"
        })
public class TestUserService  {
	
	@Autowired
	public UserService userService;
//	@Autowired
//	protected  MongoTemplate mongoTemplate;
	@Autowired
	protected CacheUtils utils;
	
	@Test
    public void test(){
//		updateUser();
		// insertUser();
    }
	public void updateUser(){
		UpdateUsersDto para = new UpdateUsersDto();
		para.setName("ko");
		para.setSid(2);
		
		userService.updateUser(para);
	}
	public void insertUser(){
		Users para = new Users();
		para.setName("hello");
		para.setSid(2);	
		for(int i=0;i<2;i++){
			//mongoTemplate.save(para);
		}
		
		System.out.println("===========================================================");
	}
}
