package org.my.service.jms.listener;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

public class ConsumerSessionAwareListener implements SessionAwareMessageListener<TextMessage> {

	private Destination destination;
	
	public Destination getSessionAwareQueue() {
		return destination;
	}

	public void setSessionAwareQueue(Destination destination) {
		this.destination = destination;
	}



	public void onMessage(TextMessage message, Session session) throws JMSException {
		System.out.println("收到一条消息");
		System.out.println("消息的内容是：" + message.getText());
		MessageProducer messageProducer = session.createProducer(destination);
		TextMessage textMessage = session.createTextMessage("ConsumerSessionAwareMessageListener。。。");
		messageProducer.send(textMessage);
	}
}
