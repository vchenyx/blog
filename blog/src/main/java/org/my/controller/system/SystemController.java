package org.my.controller.system;

import org.my.service.system.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

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
