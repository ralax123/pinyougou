package com.activemq.test;

import java.awt.Desktop.Action;
import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class QueneConsumer {
	@Test
	public void consumertest() throws JMSException, IOException {
		// 1创建连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.104:61616");
		// 2从工厂里创建连接对象
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 4创建session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5.指定消息空间
		Queue queue = session.createQueue("testQueue");
		// 6创建消费者并指定消费空间
		MessageConsumer consumer2 = session.createConsumer(queue);
		// 7.使用监听模式来消费信息
		consumer2.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				// TODO Auto-generated method stub
				TextMessage textMessage = (TextMessage) message;
				try {
					System.out.println("消息是" + textMessage.getText());
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		// 8让端口处于阻塞换填,等待键盘录取
		System.in.read();
		// 9释放资源
		consumer2.close();
		session.close();
		connection.close();

	}

}
