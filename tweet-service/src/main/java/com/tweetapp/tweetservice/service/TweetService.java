package com.tweetapp.tweetservice.service;

import org.springframework.stereotype.Service;

import com.tweetapp.tweetservice.dto.TweetDto;

@Service
public interface TweetService {

	String postTweet(String username,TweetDto tweetDto);
	
	String updateTweet(TweetDto tweetDto,String tweedId);
	
	String deleteTweet(String tweetId);
	
	String likeTweet(String tweetId,String username);
	
	String replyToTweet(String username,TweetDto tweetDto,String tweedId);

}
