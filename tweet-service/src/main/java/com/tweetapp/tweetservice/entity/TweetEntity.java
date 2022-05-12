package com.tweetapp.tweetservice.entity;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tweetapp.tweetservice.constants.TweetAppConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("TweetCollection")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TweetAppConstants.DATE_TIME_PATTERN)
	@Field(name = "created_date_time")
	private LocalDateTime createdDateTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TweetAppConstants.DATE_TIME_PATTERN)
	@Field(name = "last_modified_date_time")
	private LocalDateTime lastModifiedDateTime;

	@Field(name = "replied_to_tweet")
	private String repliedToTweet;

	@Field(name = "hashtags")
	private Set<String> hashtags;

	@Field(name = "usertags")
	private Set<String> usertags;

	@Field(name = "liked_by")
	private Set<String> likedBy;

}
