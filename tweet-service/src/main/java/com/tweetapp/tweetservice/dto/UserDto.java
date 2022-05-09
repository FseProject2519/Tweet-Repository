package com.tweetapp.tweetservice.dto;

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
public class UserDto {

	private String id;

	private String userId;

	private String password;

	private String firstName;

	private String lastName;

	private String email;

	private String contactNumber;

}
