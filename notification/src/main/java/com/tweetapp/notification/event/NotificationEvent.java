package com.tweetapp.notification.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {

	private String firstName;
	
	private String lastName;
	
	private String otp;
	
	private String email;
}
