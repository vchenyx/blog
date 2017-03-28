package org.my.controller.system;

import java.util.List;

import org.my.pojo.UserPojo;
import org.my.service.system.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/insertUser")
	public int insertUser(UserPojo userPojo) {
		return userService.insertUser(userPojo);
	}
	@RequestMapping("/selectUser")
	public List<UserPojo> selectUser() {
		List<UserPojo> list = userService.selectUser();
		logger.debug("结果为：{}", list.toString());
		return list;
	}
}
