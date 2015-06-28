package com.SMA.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.SMA.Application;
import com.SMA.controller.UserController;
import com.SMA.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class UserTest {
	
	final static Logger logger = LoggerFactory.getLogger(UserTest.class);

	@Autowired
	private UserController userController;

	@Test
	public void addUser() {
		User user = new User();
		user.setEmpId(null);
		user.setPassword("admin");
		user.setUsername("hi");
		user.setRoleId(1);
		String msg=userController.addUser(user);
		logger.info(msg);
	}
}
