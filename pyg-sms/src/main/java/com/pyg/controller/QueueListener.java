package com.pyg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// 消息发送者
public class QueueListener {

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	// 发送消息
	@RequestMapping("send/{m}")
	public String send(@PathVariable String m) {
		jmsMessagingTemplate.convertAndSend("itcast", m);
		return m;
	}

	// 接收消息
	@JmsListener(destination = "itcast")
	public String recMessage(String message) {
		System.out.println("接收的参数是:" + message);
		return message;
	}

}
