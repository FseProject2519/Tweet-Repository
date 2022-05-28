package com.tweetapp.tweetservice.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("UserCollection")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {

	@MongoId(FieldType.OBJECT_ID)
	private String id;

	@Indexed(unique = true)
	@Field(name = "user_id")
	private String userId;

	@Field(name = "password")
	private String password;

	@Field(name = "first_name")
	private String firstName;

	@Field(name = "last_name")
	private String lastName;

	@Indexed(unique = true)
	@Field(name = "email")
	private String email;

	@Field(name = "contact_number")
	private String contactNumber;
	
	@Field(name = "forgot_pwd_otp")
	private String forgotPwdOtp;
	
	@Field(name="last_modified_date")
	private LocalDateTime lastModifiedDate;

}
