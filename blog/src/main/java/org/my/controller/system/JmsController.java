package org.my.controller.system;

import org.my.service.jms.JmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jms")
public class JmsController {

	@Autowired
	private JmsService jmsService;
	
	@RequestMapping("/test")
	public void testJMS() {
		System.out.println("进入JMS  Test方法");
		jmsService.testJMS();
	}
	@RequestMapping("/test1")
	public void testJmsSession() {
		System.out.println("进入JMS  Test Session方法");
		jmsService.testJmsSession();
	}
	
}
