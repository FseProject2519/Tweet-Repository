package com.tweetapp.notification.listener;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tweetapp.notification.event.NotificationEvent;
import com.tweetapp.notification.service.NotificationService;

@Component
public class NotificationEventListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationEventListener.class);

	@Autowired
	private NotificationService notificationService;

	public NotificationEventListener() {
		super();
	}
	
	@RabbitListener(queues="${queue.name}")
	public void notificationDetailsQueue(NotificationEvent event) throws UnsupportedEncodingException, MessagingException {
		LOGGER.info("Event {}",event);
		notificationService.sendEmail(event);
	}
}
