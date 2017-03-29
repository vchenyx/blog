package org.my.service.jms.impl;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.my.service.jms.JmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service
public class JmsServiceImpl implements JmsService {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private Destination queueDestination;
	
	@Autowired  
    @Qualifier("sessionAwareQueueDestination")  
    private Destination sessionAwareQueueDestination;
	/*public void testJMS() {
		for (int i = 0; i < 10; i++) {
			sendMessage(destination, "你好，生产者！这是消息：" + (i+1));
		}
	}*/
	
	public void testJMS() {
		for (int i = 0; i < 10; i++) {
			final String message = "你好，生产者！这是消息：" + (i + 1);
			jmsTemplate.send(queueDestination, new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(message);
				}
			});
		}
	}
	
	public void testJmsSession() {
		final String message = "测试SessionListener";
		jmsTemplate.send(sessionAwareQueueDestination, new MessageCreator() {
			
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				return session.createTextMessage(message);
			}
		});
	}
}
