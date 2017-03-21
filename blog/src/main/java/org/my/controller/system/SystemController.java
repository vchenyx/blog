package org.my.controller.system;

import org.my.service.system.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys")
public class SystemController {

	@Autowired
	private SystemService systemService;
	
	@RequestMapping("/test")
	public String test() {
		systemService.test();
		return "success";
	}
}
