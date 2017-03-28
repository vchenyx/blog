package org.my.controller.system;

import org.my.service.system.JmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/jms")
public class JmsController {

	@Autowired
	private JmsService jmsService;
	
	@RequestMapping("/test")
	public void testJMS() {
		jmsService.testJMS();
	}
	
}
