package com.spring.security.simple.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SimpleController {

	@GetMapping("/")
	public String home()
	{
		return "login";
	}
	
	@GetMapping("/login")
	public String login()
	{
		return "home";
	}
	
	@GetMapping("/logout")
	public String logout()
	{
		return "login";
	}
}
