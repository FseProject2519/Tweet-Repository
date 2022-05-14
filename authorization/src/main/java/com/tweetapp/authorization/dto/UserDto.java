package com.tweetapp.authorization.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.tweetapp.authorization.util.EmailConstraint;
import com.tweetapp.authorization.util.PasswordMatchConstraint;
import com.tweetapp.authorization.util.UserIdConstraint;
@PasswordMatchConstraint.List({ 
    @PasswordMatchConstraint(
      password = "password", 
      confirmPassword = "confirmPassword", 
      message = "Passwords do not match!"
    )})
public class UserDto {

	private String id;
	
	@NotBlank(message = "Username is mandatory, please provide a valid username")
	@Size(min = 8, max = 30, message = "Password should be of  8 to 30 characters")
	@Pattern(regexp = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]", message = "Please enter a valid password")
	@UserIdConstraint
	private String userId;
	
	@NotBlank(message = "Password is mandatory, please provide a valid password")
	@Size(min = 8, max = 20, message = "Password should be of  8 to 20 characters")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\\\S+$).{8, 20}", message = "Please enter a valid password")
	private String password;
	
	@NotBlank(message = "Password confirmation is mandatory, please confirm your password")
	private String confirmPassword;
	
	@NotBlank(message = "First name is mandatory, please provide a valid first name")
	@Size(min=2)
	@Pattern(regexp="^[a-zA-Z]*",message="Please enter a valid first name")
	private String firstName;
	
	@NotBlank(message = "Last name is mandatory, please provide a valid last name")
	@Size(min=2)
	@Pattern(regexp="^[a-zA-Z]*",message="Please enter a valid last name")
	private String lastName;
	
	@NotBlank(message = "Email is mandatory, please provide a valid email")
	@Pattern(regexp="^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}",message="Please enter a valid email id")
	@EmailConstraint
	private String email;
	
	@NotBlank(message = "Contact number is mandatory, please provide a valid contact number")
	@Pattern(regexp="^[0-9]{10}",message="Please enter a 10 digit contact number")
	private String contactNumber;

}
