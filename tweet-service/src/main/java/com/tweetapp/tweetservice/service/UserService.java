package com.tweetapp.tweetservice.service;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tweetapp.tweetservice.dto.UserDto;
import com.tweetapp.tweetservice.dto.UserSearchDto;
import com.tweetapp.tweetservice.entity.UserEntity;
import com.tweetapp.tweetservice.exception.TweetServiceException;

@Service
public interface UserService {

	String createUser(UserDto userDto);

	ResponseEntity<Page<UserEntity>> searchUsers(UserSearchDto userSearchDto, Integer page, Integer size) throws TweetServiceException;

	ResponseEntity<Page<UserEntity>> getAllUsers(UserSearchDto userSearchDto, Integer page, Integer size) throws TweetServiceException;

}
