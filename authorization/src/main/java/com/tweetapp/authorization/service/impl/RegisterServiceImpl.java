package com.tweetapp.authorization.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tweetapp.authorization.dto.UserDto;
import com.tweetapp.authorization.entity.UserEntity;
import com.tweetapp.authorization.exception.TweetServiceException;
import com.tweetapp.authorization.repository.UserRepository;
import com.tweetapp.authorization.service.RegisterService;
import org.modelmapper.ModelMapper;

@Component
public class RegisterServiceImpl implements RegisterService{
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	UserRepository userRepository;

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
	

}
