package com.activemq.test;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class TopicProducer {

	// 订阅模式的消费者
	@Test
	public void topicProducerTest() throws JMSException {
		// 1创建连接工厂
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.104:61616");
		// 从工厂里创建连接对象
		Connection connection = connectionFactory.createConnection();
		// 3.打开连接
		connection.start();
		// 创建session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5.指定空间对象和名称
		Topic topic = session.createTopic("topic-test");
		// 6创建订阅模式生产者
		MessageProducer producer = session.createProducer(topic);
		// 7创建消息
		TextMessage message = session.createTextMessage("ad防守打法说悟空");
		// 8发送消息
		producer.send(message);
		// 释放资源
		producer.close();
		session.close();
		connection.close();

	}
}
