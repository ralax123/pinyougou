package com.pyg.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

	@RequestMapping("getName")
	public Map<String, String> getName() {
		Map<String, String> maps = new HashMap<String, String>();

		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		maps.put("loginName", name);
		return maps;
	}
}
