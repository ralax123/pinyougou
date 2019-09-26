package com.activemq.test;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

/**
 * spring整合jms点对点发送
 * 
 * @author ASUS
 *
 */
public class SpringMQProducer {

	@Test
	public void sendMessageByPTP() {
		// 加载spring配置文件
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"classpath*:spring/applicationContext-mq-producer.xml");

		// 获取发送消息模板对象
		JmsTemplate jmsTemplate = ac.getBean(JmsTemplate.class);
		// 获取目的地对象
		ActiveMQQueue queue = ac.getBean(ActiveMQQueue.class);
		// 发送消息
		jmsTemplate.send(queue, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
			
				return 	session.createTextMessage("彭青松");
			}
		});

	}

}
