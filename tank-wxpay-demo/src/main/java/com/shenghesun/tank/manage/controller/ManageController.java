package com.shenghesun.tank.manage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class ManageController {

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		
		return "ok";
	}
}
