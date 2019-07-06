package com.fort.webapp.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping
public class HomeController {
	
	@RequestMapping("/")
	public String index() {
		return "redirect:/login";
	}
	
	@RequestMapping(value = "/login",method= {RequestMethod.GET,RequestMethod.POST})
	public String login() {
		return "login";
	}
	
	@RequestMapping(value = "/layout",method= {RequestMethod.GET})
	public String layout() {
		return "pages/layout/main";
	}
}
