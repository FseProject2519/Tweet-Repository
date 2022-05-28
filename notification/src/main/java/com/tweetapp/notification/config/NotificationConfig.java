package com.tweetapp.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class NotificationConfig {
	@Bean
	public JavaMailSender mailSender ()
	{
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		
	    return javaMailSender;
	}

}
