package com.tweetapp.authorization.service;

import org.springframework.stereotype.Service;

import com.tweetapp.authorization.dto.UserDto;
import com.tweetapp.authorization.exception.TweetServiceException;
@Service
public interface RegisterService {

	String registerUser(UserDto userDto) throws TweetServiceException;
	
	String forgotPassword(String username) throws TweetServiceException;
	
	
}
