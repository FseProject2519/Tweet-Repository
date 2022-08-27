package com.tweetapp.tweetservice.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TweetTrendEntity {

	private String hashtags;
	private Long count;

}
