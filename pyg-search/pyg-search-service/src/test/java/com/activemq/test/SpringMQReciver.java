package com.activemq.test;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMQReciver {

	@Test
	public void reveiverMessage() throws IOException {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"classpath*:spring/applicationContext-mq-consumer.xml");
		System.in.read();
	}
}
