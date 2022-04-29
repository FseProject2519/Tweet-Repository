package com.tweetapp.tweetservice.entity;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Document("TweetCollection")
@Slf4j
@Builder
public class TweetEntity {

	@MongoId(FieldType.OBJECT_ID)
	private String id;

	@Indexed(unique = true)
	@Field(name = "tweet_message")
	private String tweetMessage;

	@Field(name = "tweet_topic")
	private String tweetTopic;

	@Field(name = "created_by")
	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]")
	@Field(name = "created_date_time")
	private LocalDateTime createdDateTime;

	@Field(name = "replied_to_tweet")
	private String repliedToTweet;

	@Field(name = "tag")
	private String tag;

	@Field(name = "liked_by")
	private Set<String> likedBy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTweetMessage() {
		return tweetMessage;
	}

	public void setTweetMessage(String tweetMessage) {
		this.tweetMessage = tweetMessage;
	}

	public String getTweetTopic() {
		return tweetTopic;
	}

	public void setTweetTopic(String tweetTopic) {
		this.tweetTopic = tweetTopic;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getRepliedToTweet() {
		return repliedToTweet;
	}

	public void setRepliedToTweet(String repliedToTweet) {
		this.repliedToTweet = repliedToTweet;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Set<String> getLikedBy() {
		return likedBy;
	}

	public void setLikedBy(Set<String> likedBy) {
		this.likedBy = likedBy;
	}

}
