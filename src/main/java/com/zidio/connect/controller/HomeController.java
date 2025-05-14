package com.zidio.connect.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@GetMapping("/")
	public String welcomeMessage() {
		return "<h1 align='center'>Hello, Welcome to my Multi Plateform website</h1>";
	}
}
