package com.tweetapp.notification.service.impl;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.tweetapp.notification.event.NotificationEvent;
import com.tweetapp.notification.service.NotificationService;

@Component
public class NotificationServiceImpl implements NotificationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

	@Autowired
	JavaMailSender mailSender;

	@Value("${email.value}")
	private String email;
	@Value("${personal.value}")
	private String personal;

	@Override
	public void sendEmail(NotificationEvent event) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		LOGGER.info("From email {} ,personal value {}", email, personal);
		helper.setFrom(email, personal);
		helper.setTo(event.getEmail());

		String subject = "Forgot Password- Here's your OTP to reset password";

		String content = "<p>Hi " + event.getFirstName() + "</p>" + "<p>You have requested to reset your password. "
				+ "Please use this One Time Password to reset your password:</p>" + "<p><b>" + event.getOtp()
				+ "</b></p>" + "<br>" + "<p>Note: this OTP is set to expire in 5 minutes.</p>" + "<br>"
				+ "<p>Ignore this email if you do remember your password, " + "or you have not made the request.</p>";

		helper.setSubject(subject);

		helper.setText(content, true);

		mailSender.send(message);
		LOGGER.info("Sending notification to reset password to {} {} registered email is {}. {} is the OTP",
				event.getFirstName(), event.getLastName(), event.getEmail(), event.getOtp());

	}

}
