package com.activemq.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class QueneProducer {

	@Test
	public void queneTest() throws JMSException {
		// 1.创建连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.104:61616");
		// 从连接工厂里获取连接
		Connection connection = connectionFactory.createConnection();
		// 启动连接
		connection.start();
		// 4从连接对象中获取session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 创建队列,并指定名称
		Queue queue = session.createQueue("testQueue");
		// 创建消息生产者,并制定发到哪个消息空间
		MessageProducer producer = session.createProducer(queue);
		// 创建消息
		TextMessage message = session.createTextMessage("传智播客大数据");
		// 8./发送消息
		producer.send(message);
		// 9.关闭资源
		producer.close();
		session.close();
		connection.close();

	}
}
