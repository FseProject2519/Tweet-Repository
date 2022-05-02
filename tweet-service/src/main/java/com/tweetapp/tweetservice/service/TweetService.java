package com.tweetapp.tweetservice.service;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tweetapp.tweetservice.dto.TweetDto;
import com.tweetapp.tweetservice.dto.TweetSearchDto;
import com.tweetapp.tweetservice.entity.TweetEntity;
import com.tweetapp.tweetservice.exception.TweetServiceException;

@Service
public interface TweetService {

	String createTweet(TweetDto tweetDto);

	ResponseEntity<Page<TweetEntity>> getAllTweets(TweetSearchDto tweetSearchDto, Integer page, Integer size) throws TweetServiceException;

	ResponseEntity<Page<TweetEntity>> searchTweets(TweetSearchDto tweetSearchDto, Integer page, Integer size) throws TweetServiceException;

}
