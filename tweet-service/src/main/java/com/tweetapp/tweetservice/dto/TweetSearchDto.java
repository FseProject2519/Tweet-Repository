package com.tweetapp.tweetservice.dto;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TweetSearchDto {

	private String tweetMessage;

	private String createdBy;

	private LocalDateTime startDateTime;

	private LocalDateTime endDateTime;

	private String repliedToTweet;

	private Set<String> tag;

	private Set<String> likedBy;

	private String sortField;

	private String sortOrder;

	private boolean withComments;

}
