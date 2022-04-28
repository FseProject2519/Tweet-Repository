package com.fse1.tweetservice.dto;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetDto {

	private String id;

	private String tweetMessage;

	private String tweetTopic;

	private String createdBy;

	private LocalDateTime createdDateTime;

	private String repliedToTweet;

	private String tag;

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
