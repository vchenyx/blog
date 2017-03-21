package org.my.controller.system;

import java.util.List;

import org.my.pojo.UserPojo;
import org.my.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/insertUser")
	public int insertUser(UserPojo userPojo) {
		return userService.insertUser(userPojo);
	}
	@RequestMapping("/selectUser")
	public List<UserPojo> selectUser() {
		return userService.selectUser();
	}
}
