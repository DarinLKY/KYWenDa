package com.KyLee;

import com.KyLee.dao.UserDAO;
import com.KyLee.model.User;
import com.KyLee.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class ZhihuApplicationTests {


	@Autowired
	UserService userService;
	@Autowired
	UserDAO userDAO;
	@Test
	public void contextLoads() {
		User user = new User();
		user.setEmail("fdf@qq.com");
		user.setId(3);
		user.setName("test");
		user.setPassword("3");
		user.setSlat("ASDs");
		userService.insertUser(user);
		System.out.println("成功插入");

	}

}
