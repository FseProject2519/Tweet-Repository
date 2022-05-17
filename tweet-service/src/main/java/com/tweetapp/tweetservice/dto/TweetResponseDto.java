package com.tweetapp.tweetservice.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.tweetapp.tweetservice.entity.TweetEntity;
import com.tweetapp.tweetservice.entity.TweetTrendEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TweetResponseDto {

	private String responseMessage;
	private List<TweetAppError> validationErrors;
	private Page<TweetEntity> tweetsPaged;
	private List<TweetEntity> tweetsList;
	private List<TweetTrendEntity> tweetTrendsList;
	private List<String> hashtagsList;

}
