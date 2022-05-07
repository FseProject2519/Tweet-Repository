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
public class UserSearchDto {

	private String userId;

	private String firstName;

	private String lastName;

	private String email;

	private String sortField;

	private String sortOrder;

}
