package com.tweetapp.tweetservice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.tweetapp.tweetservice.dto.UserSearchDto;
import com.tweetapp.tweetservice.entity.UserEntity;
import com.tweetapp.tweetservice.exception.TweetServiceException;

@Service
public interface UserService {

	Page<UserEntity> searchUsersPaged(UserSearchDto userSearchDto, Integer page, Integer size)
			throws TweetServiceException;

	Page<UserEntity> getAllUsersPaged(UserSearchDto userSearchDto, Integer page, Integer size)
			throws TweetServiceException;

	List<UserEntity> getAllUsers() throws TweetServiceException;

	List<UserEntity> searchUsers(UserSearchDto userSearchDto) throws TweetServiceException;

	List<String> getUsertags() throws TweetServiceException;

	String deleteUser(String username) throws TweetServiceException;

	List<UserEntity> searchUsersCloud(UserSearchDto userSearchDto) throws TweetServiceException;

	List<UserEntity> getAllUsersCloud() throws TweetServiceException;

}
