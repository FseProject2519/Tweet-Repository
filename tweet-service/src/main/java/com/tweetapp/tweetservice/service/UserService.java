package com.tweetapp.tweetservice.service;

import org.springframework.stereotype.Service;

import com.tweetapp.tweetservice.dto.UserDto;

@Service
public interface UserService {

	String createUser(UserDto userDto);

}
