package com.fse1.tweetservice.service;

import org.springframework.stereotype.Service;

import com.fse1.tweetservice.dto.UserDto;

@Service
public interface UserService {

	String createUser(UserDto userDto);

}
