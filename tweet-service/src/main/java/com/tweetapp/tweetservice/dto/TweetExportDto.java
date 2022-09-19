package com.tweetapp.tweetservice.dto;

import java.time.LocalDateTime;

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
public class TweetExportDto {

	private String tweetMessage;

	private String repliedToTweetMsg;

	private String repliedToTweetUser;

	private String likedBy;

	private LocalDateTime createdDateTime;

	private LocalDateTime lastModifiedDateTime;
}
