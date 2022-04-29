package com.tweetapp.tweetservice.service.impl;

import org.springframework.stereotype.Component;

import com.tweetapp.tweetservice.dto.UserDto;
import com.tweetapp.tweetservice.service.UserService;

@Component
public class UserServiceImpl implements UserService {
	
	@Override
	public String createUser(UserDto userDto) {
		return "";
	}
}
