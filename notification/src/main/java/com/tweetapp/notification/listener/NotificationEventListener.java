package com.tweetapp.notification.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	public void notificationDetailsQueue(NotificationEvent event) {
		LOGGER.info("Event {}",event);
		notificationService.sendEmail(event);
	}
}
