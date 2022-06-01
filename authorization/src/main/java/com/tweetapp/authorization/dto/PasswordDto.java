package com.tweetapp.authorization.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.tweetapp.authorization.util.PasswordMatchConstraint;

import lombok.Data;

@PasswordMatchConstraint.List({ 
    @PasswordMatchConstraint(
      password = "password", 
      confirmPassword = "confirmPassword", 
      message = "Passwords do not match!"
    )})
@Data
public class PasswordDto {
	@NotBlank(message = "Password is mandatory, please provide a valid password")
	@Size(min = 8, max = 20, message = "Password should be of  8 to 20 characters")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}", message = "Please enter a valid password")
	private String password;
	
	@NotBlank(message = "Password confirmation is mandatory, please confirm your password")
	private String confirmPassword;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
