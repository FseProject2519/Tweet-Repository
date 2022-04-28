package com.fse1.tweetservice.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fse1.tweetservice.dto.TweetDto;
import com.fse1.tweetservice.entity.TweetEntity;
import com.fse1.tweetservice.repository.TweetRepository;
import com.fse1.tweetservice.service.TweetService;

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
