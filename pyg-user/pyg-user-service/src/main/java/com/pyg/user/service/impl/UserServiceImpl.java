package com.pyg.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.PatternSyntaxException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.mapper.TbUserMapper;
import com.pyg.pojo.TbUser;
import com.pyg.utils.PhoneFormatCheckUtils;
import com.pyg.utils.PygResult;

import pyg.user.service.UserService;

/**
 * 服务实现层
 * 
 * @author Administrator
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;

	/**
	 * 用户注册 需求:前台传递了password,和entity
	 */
	public PygResult register(String smsCode, TbUser user) {
		try {
			// 1判断手机格式
			boolean phoneFlag = PhoneFormatCheckUtils.isChinaPhoneLegal(user.getPhone());
			if (!phoneFlag) {
				return new PygResult(false, "手机格式错误");
			}

			// 2判断验证码
			// 从redis里面拿出验证码
			String code = (String) redisTemplate.boundHashOps("sms").get(user.getPhone());
			if (!code.equals(smsCode)) {
				return new PygResult(false, "验证码输入错误");
			}

			// 3.密码加密--使用spring自带的md5 加密
			String passwordMD5 = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
			user.setPassword(passwordMD5);

			// 4根据表中非空字段,补全信息,完成注册
			user.setCreated(new Date());
			user.setUpdated(new Date());
			userMapper.insert(user);
			// 添加成功
			return new PygResult(true, "注册成功");
		} catch (PatternSyntaxException e) {
			e.printStackTrace();
			return new PygResult(false, "注册失败");
		}
	}

	// 注入reids模板
	@Autowired
	private RedisTemplate redisTemplate;

	// 注入mq消息模板
	@Autowired
	private JmsTemplate jmsTemplate;

	/**
	 * 1.生成验证码 2.给手机发验证码,同时把验证码保存在redis服务器里, 便于后期注册时校验验证码 3
	 * 3.把手机号和验证码作为消息发给activemq消息服务器里
	 */

	public PygResult getSmsCode(String phone) {
		try {
			// 1，生成验证吗
			String code = (long) (Math.random() * 1000000) + "";
			// 2把验证码存入redis里
			redisTemplate.boundHashOps("sms".toString()).put(phone, code);
			// 设置保留的时间
			redisTemplate.boundHashOps("sms").expire(3, TimeUnit.MINUTES);
			// 3把手机号和验证码作为消息发给activemq消息服务器里
			// 创建map对象封装手机号和验证码
			Map<String, String> map = new HashMap<>();
			map.put("phone", phone);
			map.put("code", code + "");

			// 补充加入发短信的模板
			map.put("template_code", "SMS_138078083");
			// 短信签名
			map.put("sign_name", "品优购");
			jmsTemplate.convertAndSend("smsCodeQueue", map);
			return new PygResult(true, "发送成功");

		} catch (JmsException e) {
			e.printStackTrace();
			return new PygResult(false, "发送失败");
		}
	}

}
