package com.tweetapp.tweetservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.tweetservice.entity.TweetEntity;

@Repository
public interface TweetRepository extends MongoRepository<TweetEntity, String>, TweetCriteriaRepository {

}
