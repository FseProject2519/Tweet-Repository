package com.tweetapp.authorization.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tweetapp.authorization.dto.UserDto;
import com.tweetapp.authorization.entity.UserEntity;
import com.tweetapp.authorization.event.NotificationEvent;
import com.tweetapp.authorization.exception.TweetServiceException;
import com.tweetapp.authorization.repository.UserRepository;
import com.tweetapp.authorization.service.DetailsService;
import com.tweetapp.authorization.service.RegisterService;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RegisterServiceImpl implements RegisterService{
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	UserRepository userRepository;
	
	@Value("${queue.name}")
	private String notificationDetailsQueue;
	
	@Autowired
	private RabbitTemplate messageTemplate;
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterServiceImpl.class);

	@Override
	public String registerUser(UserDto userDto) throws TweetServiceException {
		
		try {
			UserEntity user=modelMapper.map(userDto, UserEntity.class);
			return userRepository.save(user).getId()!=null?"User Registered Successfully":"User Registration Failed";
		}
		catch(Exception e){
			throw new TweetServiceException(e.getMessage());
		}
		
	}

	@Override
	public String forgotPassword(String username) throws TweetServiceException {
		try {
			Optional<UserEntity> user = userRepository.findByUserId(username);
			if (user.isPresent()) {
				String email = user.get().getEmail();
				if (StringUtils.isNotEmpty(email)) {
					String token=RandomString.make(30);
					user.get().setForgotPwdToken(token);
					userRepository.save(user.get());
					NotificationEvent notificationEvent = NotificationEvent.builder()
							.firstName(user.get().getFirstName()).lastName(user.get().getLastName())
							.email(user.get().getEmail()).token(user.get().getForgotPwdToken()).build();
					LOGGER.info("Sending message [{]} to queue [{}]",notificationEvent,notificationDetailsQueue);
					messageTemplate.convertAndSend(notificationDetailsQueue, notificationEvent);
					int index = email.indexOf("@");
					StringBuilder secureEmail = new StringBuilder(email.substring(0, 2));
					String substringEmail = email.substring(2, index - 2).replaceAll(".", "*");
					secureEmail.append(substringEmail).append(email.substring(index-2));
					return "Token  to reset your password has been sent to your registered email "
							+ secureEmail.toString();

				}
				else {
					return "No valid email id found for the user";
				}
			}
			else {
				return "Not a Valid Username";
			}
		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}
	

}
