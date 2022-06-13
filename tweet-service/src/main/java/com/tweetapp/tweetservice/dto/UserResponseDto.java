package com.tweetapp.tweetservice.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.tweetapp.tweetservice.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

	private String responseMessage;
	private List<TweetAppError> validationErrors;
	private Page<UserEntity> usersPaged;
	private List<UserEntity> usersList;
	private List<String> usertagsList;

}
