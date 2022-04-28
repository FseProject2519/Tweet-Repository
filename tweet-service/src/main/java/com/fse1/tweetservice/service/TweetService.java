package com.fse1.tweetservice.service;

import org.springframework.stereotype.Service;

import com.fse1.tweetservice.dto.TweetDto;

@Service
public interface TweetService {

	String createTweet(TweetDto tweetDto);

}
