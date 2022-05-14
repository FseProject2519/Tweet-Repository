package com.tweetapp.authorization.dto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
@Getter
@Setter
public class UserDetailsErrors {
	private String fieldName;
	private String message;
}
