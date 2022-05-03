package com.tweetapp.tweetservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tweetapp.tweetservice.dto.TweetSearchDto;
import com.tweetapp.tweetservice.entity.TweetEntity;

public interface TweetCriteriaRepository {

	Page<TweetEntity> searchTweets(TweetSearchDto tweetSearchDto, Pageable pageable);

	Page<TweetEntity> findAll(Pageable pageable);

}
