package org.my.service.system.impl;

import org.apache.log4j.Logger;
import org.my.service.system.SystemService;
import org.springframework.stereotype.Service;

@Service
public class SystemServiceImpl implements SystemService {

	private static Logger logger = Logger.getLogger(SystemServiceImpl.class);
	
	public void test() {
		logger.info(111);
	}
}
