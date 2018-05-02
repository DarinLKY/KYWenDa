package com.KyLee;

import com.KyLee.dao.QuestionDAO;
import com.KyLee.dao.UserDAO;
import com.KyLee.model.Question;
import com.KyLee.model.User;
import com.KyLee.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZhihuApplication.class)
@Sql("/init-schema.sql")
public class ZhihuApplicationTests {


	@Autowired
	UserService userService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	QuestionDAO questionDAO;
	@Test
	public void contextLoads() {
		Random random = new Random();
		for (int i = 0; i < 11; ++i) {
			User user = new User();
			//user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER%d", i));
			user.setPassword("3432");
			user.setSalt("ASfd");
			userDAO.addUser(user);


			Question question = new Question();
			question.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
			question.setCreatedDate(date);
			question.setUserId(i + 1);
			question.setTitle(String.format("TITLE{%d}", i));
			question.setContent(String.format("Balaababalalalal Content %d", i));
			questionDAO.addQuestion(question);
		}
	}

}
