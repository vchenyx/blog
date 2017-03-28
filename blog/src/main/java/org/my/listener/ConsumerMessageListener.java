package org.my.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ConsumerMessageListener implements MessageListener {

	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		System.out.println("接收一个纯文本消息");
		try {
			System.out.println("消息内容是：" + textMessage.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
