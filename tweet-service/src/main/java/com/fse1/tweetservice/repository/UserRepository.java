package com.fse1.tweetservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fse1.tweetservice.entity.UserEntity;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

}