package com.dridi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
	
	@RequestMapping(value="/hello")
	public String hello() {
		return "Hello ramzi";
		
	}

}
