package org.my.service.system.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.my.dao.system.UserDao;
import org.my.pojo.UserPojo;
import org.my.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDao userDao;
	
	public List<UserPojo> selectUser() {
		List<UserPojo> list = userDao.selectUser();
		for (UserPojo userPojo : list) {
			logger.debug(userPojo.getName());
		}
		return list;
	}
	
	public int insertUser(UserPojo userPojo) {
		int i = userDao.insertUser(userPojo);
		logger.debug(i);
		return i;
	}
	
}
