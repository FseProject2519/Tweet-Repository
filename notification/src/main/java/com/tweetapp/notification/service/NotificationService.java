package com.tweetapp.notification.service;

import org.springframework.stereotype.Service;

import com.tweetapp.notification.event.NotificationEvent;

@Service
public interface NotificationService {

	void sendEmail(NotificationEvent event);
}
