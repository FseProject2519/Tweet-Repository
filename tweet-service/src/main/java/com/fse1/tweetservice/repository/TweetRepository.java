package com.fse1.tweetservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fse1.tweetservice.entity.TweetEntity;

@Repository
public interface TweetRepository extends MongoRepository<TweetEntity, String> {

}
