package com.tweetapp.tweetservice.dto;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetSearchDto {

	private String tweetMessage;

	private String tweetTopic;

	private String createdBy;

	private LocalDateTime startDateTime;

	private LocalDateTime endDateTime;

	private String repliedToTweet;

	private String tag;

	private Set<String> likedBy;

	private String sortField;

	private String sortOrder;

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

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
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

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

}
