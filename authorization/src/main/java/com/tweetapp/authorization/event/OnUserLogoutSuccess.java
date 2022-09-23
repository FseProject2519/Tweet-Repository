package com.tweetapp.authorization.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class OnUserLogoutSuccess {
	private String userName;
	private String token;

}
