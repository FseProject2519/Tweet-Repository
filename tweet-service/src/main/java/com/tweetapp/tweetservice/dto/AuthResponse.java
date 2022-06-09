package com.tweetapp.tweetservice.dto;

import lombok.Getter;

@Getter
public class AuthResponse {
	// User Id
	private String uid;
	// Username
	private String name;
	// Is Token valid
	private boolean isValid;
}
