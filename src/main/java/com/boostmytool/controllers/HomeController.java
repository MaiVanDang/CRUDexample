package com.boostmytool.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class HomeController {
	
	@GetMapping({"","/"})
	public String home() {
		
		return "/index";
	}
	@GetMapping("/home")
	public String admin() {
		return "admin/index";
	}
}
