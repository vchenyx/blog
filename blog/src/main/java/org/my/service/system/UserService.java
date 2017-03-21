package org.my.service.system;

import java.util.List;

import org.my.pojo.UserPojo;

public interface UserService {

	int insertUser(UserPojo userPojo);
	
	List<UserPojo> selectUser();
}
