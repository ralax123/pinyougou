package com.pyg.user.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbUser;
import com.pyg.utils.PygResult;

import pyg.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Reference
	private UserService userService;

	/**
	 * 用户注册 需求:前台传递了smsCode,和entity
	 */
	@RequestMapping("/register")
	public PygResult register(String smsCode, @RequestBody TbUser user) {
		return userService.register(smsCode, user);
	}

	// 发送验证码
	/**
	 * 1.生成验证码
	 *  2.给手机发验证码,同时把验证码保存在redis服务器里,
	 *  便于后期注册时校验验证码 3
	 *  3.把手机号和验证码作为消息发给activemq消息服务器里
	 */
	@RequestMapping("/getSmsCod")
	public PygResult getSmsCode(String phone) {
		return userService.getSmsCode(phone);
	}

	// 发送验证码
	

	@RequestMapping("/aaa")
	public String aaa() {
		return "aaa";
	}
}
