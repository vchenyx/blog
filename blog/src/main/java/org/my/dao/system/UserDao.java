package org.my.dao.system;

import java.util.List;

import org.my.pojo.UserPojo;

public interface UserDao {

	int insertUser(UserPojo userPojo);
	
	List<UserPojo> selectUser();
}
