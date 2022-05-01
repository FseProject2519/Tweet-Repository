package com.tweetapp.tweetservice.service;

import org.springframework.stereotype.Service;

import com.tweetapp.tweetservice.dto.TweetDto;

@Service
public interface TweetService {

	String createTweet(TweetDto tweetDto);

}
