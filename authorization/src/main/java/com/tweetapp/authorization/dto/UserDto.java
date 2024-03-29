package com.tweetapp.authorization.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.tweetapp.authorization.util.EmailConstraint;
import com.tweetapp.authorization.util.PasswordMatchConstraint;
import com.tweetapp.authorization.util.UpdatePasswordConstraint;
import com.tweetapp.authorization.util.UserIdConstraint;

@PasswordMatchConstraint.List({
		@PasswordMatchConstraint(password = "password", confirmPassword = "confirmPassword", message = "Password and Confirmed password do not match", groups = {
				Register.class }) })
public class UserDto {

	private String id;

	@NotBlank(message = "Username is mandatory, please provide a valid username", groups = { Register.class })
	@Size(min = 5, max = 20, message = "Username should be of  5 to 20 characters", groups = Register.class)
	@Pattern(regexp = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]", message = "Username should contain alphanumeric characters, lowercase, or uppercase, dot (.), underscore (_), and hyphen (-).?Username cannot start or end with dot (.), underscore (_), or hyphen (-).?Username cannot have the dot (.), underscore (_), or hyphen (-) appear consecutively, e.g., james..bond", groups = Register.class)
	@UserIdConstraint(groups = Register.class)
	private String userId;

	@NotBlank(message = "Password is mandatory, please provide a valid password", groups = { Register.class })
	@Size(min = 8, max = 20, message = "Password should be of  8 to 20 characters", groups = { Register.class })
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}", message = "Password should contain at least one digit,atleast one upper case alphabet, at least one lower case alphabet."
			+ "?Password should contain at least one special character which includes !@#$%&*()-+=^."
			+ "?Password cannot contain any white space.", groups = { Register.class })
	private String password;

	@UpdatePasswordConstraint(groups = Update.class)
	private String updatePassword;

	@NotBlank(message = "Password confirmation is mandatory, please confirm your password", groups = Register.class)

	private String confirmPassword;

	@NotBlank(message = "First name is mandatory, please provide a valid first name", groups = { Register.class,
			Update.class })
	@Size(min = 2, groups = { Register.class, Update.class })
	@Pattern(regexp = "^[a-zA-Z]*", message = "First Name- Only alphabets allowed", groups = { Register.class,
			Update.class })
	private String firstName;

	@NotBlank(message = "Last name is mandatory, please provide a valid last name", groups = { Register.class,
			Update.class })
	@Size(min = 1, groups = { Register.class, Update.class })
	@Pattern(regexp = "^[a-zA-Z]*", message = "Last Name- Only alphabets allowed", groups = { Register.class,
			Update.class })
	private String lastName;

	@NotBlank(message = "Email is mandatory, please provide a valid email", groups = { Register.class, Update.class })
	@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "Enter a valid email id", groups = {
			Register.class, Update.class })
	@EmailConstraint(groups = { Register.class })
	private String email;

	@NotBlank(message = "Contact number is mandatory, please provide a valid contact number", groups = { Register.class,
			Update.class })
	@Pattern(regexp = "^[0-9]{10}", message = "Contact number should contain 10 digits ", groups = { Register.class,
			Update.class })
	private String contactNumber;

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

	public String getUpdatePassword() {
		return updatePassword;
	}

	public void setUpdatePassword(String updatePassword) {
		this.updatePassword = updatePassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
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

}
