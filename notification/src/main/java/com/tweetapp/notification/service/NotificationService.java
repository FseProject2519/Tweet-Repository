package com.tweetapp.notification.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;

import com.tweetapp.notification.event.NotificationEvent;

@Service
public interface NotificationService {

	void sendEmail(NotificationEvent event) throws UnsupportedEncodingException, MessagingException;
}
