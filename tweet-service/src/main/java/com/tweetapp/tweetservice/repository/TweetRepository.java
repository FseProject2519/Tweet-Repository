package com.tweetapp.tweetservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tweetapp.tweetservice.entity.TweetEntity;

@Repository
public interface TweetRepository extends MongoRepository<TweetEntity, String>, TweetCriteriaRepository {

	@Query("{'created_by' : :#{#username}}")
	List<TweetEntity> getExportData(@Param("username") String username);

	List<TweetEntity> findAllByOrderById();

	void deleteByRepliedToTweet(String tweetId);

	@Query("{'replied_to_tweet' : :#{#id}}")
	List<TweetEntity> getComments(@Param("id") String id);

	@Query("{'id' : :#{#repliedToTweet}}")
	List<TweetEntity> getMainTweet(@Param("repliedToTweet") String repliedToTweet);

	void deleteByCreatedBy(String username);

	List<TweetEntity> findByCreatedBy(String username);
}
