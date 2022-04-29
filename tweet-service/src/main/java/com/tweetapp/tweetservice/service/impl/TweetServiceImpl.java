package com.tweetapp.tweetservice.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tweetapp.tweetservice.dto.TweetDto;
import com.tweetapp.tweetservice.entity.TweetEntity;
import com.tweetapp.tweetservice.repository.TweetRepository;
import com.tweetapp.tweetservice.service.TweetService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TweetServiceImpl implements TweetService {

	@Autowired
	TweetRepository tweetRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public String createTweet(TweetDto tweetDto) {

		TweetEntity tweetEntity = modelMapper.map(tweetDto, TweetEntity.class);
		return tweetRepository.save(tweetEntity) != null ? "Tweet Created Successfully" : "Tweet Creation Failed";
	}
}
