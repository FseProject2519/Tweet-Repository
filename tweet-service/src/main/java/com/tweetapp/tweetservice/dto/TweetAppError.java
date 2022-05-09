package com.tweetapp.tweetservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
@Getter
@Setter
public class TweetAppError {
	private String fieldName;
	private String message;
}
