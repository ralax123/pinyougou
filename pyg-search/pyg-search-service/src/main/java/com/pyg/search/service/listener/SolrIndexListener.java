package com.pyg.search.service.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class SolrIndexListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub

		TextMessage textMessage = (TextMessage) message;
		try {
			String text = textMessage.getText();
			System.out.println("接收到消息的点对点消息是:" + text);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
