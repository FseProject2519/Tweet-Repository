package com.tweetapp.authorization.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.authorization.entity.UserEntity;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

}
