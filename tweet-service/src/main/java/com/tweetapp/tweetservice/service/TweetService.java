package com.tweetapp.tweetservice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.tweetapp.tweetservice.dto.TweetDto;
import com.tweetapp.tweetservice.dto.TweetExportDto;
import com.tweetapp.tweetservice.dto.TweetSearchDto;
import com.tweetapp.tweetservice.entity.TweetEntity;
import com.tweetapp.tweetservice.entity.TweetTrendEntity;
import com.tweetapp.tweetservice.exception.TweetServiceException;

@Service
public interface TweetService {

	String postTweet(String username, TweetDto tweetDto) throws TweetServiceException;

	String updateTweet(TweetDto tweetDto, String tweedId) throws TweetServiceException;

	String deleteTweet(String tweetId) throws TweetServiceException;

	String likeTweet(String tweetId, String username) throws TweetServiceException;

	String replyToTweet(String username, TweetDto tweetDto, String tweedId) throws TweetServiceException;

	Page<TweetEntity> getAllTweetsPaged(TweetSearchDto tweetSearchDto, Integer page, Integer size)
			throws TweetServiceException;

	List<TweetEntity> getAllTweets(TweetSearchDto tweetSearchDto) throws TweetServiceException;

	Page<TweetEntity> searchTweetsPaged(TweetSearchDto tweetSearchDto, Integer page, Integer size)
			throws TweetServiceException;

	List<TweetTrendEntity> getTrendingTopics(TweetSearchDto tweetSearchDto) throws TweetServiceException;

	List<TweetExportDto> getExportData(String username) throws TweetServiceException;

	List<String> getHashtags() throws TweetServiceException;

	List<TweetEntity> searchTweets(TweetSearchDto tweetSearchDto) throws TweetServiceException;

}
