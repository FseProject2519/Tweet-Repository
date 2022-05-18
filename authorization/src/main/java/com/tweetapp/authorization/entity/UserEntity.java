package com.tweetapp.authorization.entity;


import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("UserCollection")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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
	
	@Field(name = "forgot_pwd_token")
	private String forgotPwdToken;
	
	@Field(name="last_modified_date")
	private LocalDateTime lastModifiedDate;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getForgotPwdToken() {
		return forgotPwdToken;
	}

	public void setForgotPwdToken(String forgotPwdToken) {
		this.forgotPwdToken = forgotPwdToken;
	}
	
	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}
	
	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate=lastModifiedDate;
	}
	

}
