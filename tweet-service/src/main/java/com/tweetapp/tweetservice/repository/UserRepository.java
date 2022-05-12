package com.tweetapp.tweetservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.tweetservice.entity.UserEntity;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String>, UserCriteriaRepository {

	List<UserEntity> findAllByOrderByUserId();

}