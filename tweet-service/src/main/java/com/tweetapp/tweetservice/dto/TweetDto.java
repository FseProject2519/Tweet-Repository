package com.tweetapp.tweetservice.dto;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
public class TweetDto {

	private String id;

	@NotBlank(message = "Message cannot be empty")
	@Size(max = 144, message = "Message cannot exceed 144 characters")
	private String tweetMessage;

	@NotBlank(message = "Tweet Topic cannot be empty")
	private String tweetTopic;

	@NotBlank(message = "Created By cannot be empty")
	private String createdBy;

	private LocalDateTime createdDateTime;

	private LocalDateTime lastModifiedDateTime;

	private String repliedToTweet;

	private Set<@Size(max = 50, message = "Tag cannot exceed 50 characters") String> tag;

	private Set<String> likedBy;
}
