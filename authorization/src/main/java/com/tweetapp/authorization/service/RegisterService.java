package com.tweetapp.authorization.service;

import org.springframework.stereotype.Service;

import com.tweetapp.authorization.dto.OtpDto;
import com.tweetapp.authorization.dto.PasswordDto;
import com.tweetapp.authorization.dto.UserDto;
import com.tweetapp.authorization.exception.TweetServiceException;
@Service
public interface RegisterService {

	String registerUser(UserDto userDto) throws TweetServiceException;
	
	String forgotPassword(String username) throws TweetServiceException;

	String verifyOtp(String username,OtpDto otp) throws TweetServiceException ;

	String resetPassword(String username, PasswordDto password) throws TweetServiceException ;
	
	
}
