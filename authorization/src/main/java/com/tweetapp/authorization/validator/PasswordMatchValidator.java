package com.tweetapp.authorization.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

import com.tweetapp.authorization.util.PasswordMatchConstraint;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatchConstraint, Object> {
	private String password;
	private String confirmPassword;

	@Override
	public void initialize(PasswordMatchConstraint constraintAnnotation) {
		this.password = constraintAnnotation.password();
		this.confirmPassword = constraintAnnotation.confirmPassword();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Object passwordValue = new BeanWrapperImpl(value).getPropertyValue(password);
		Object confirmPasswordValue = new BeanWrapperImpl(value).getPropertyValue(confirmPassword);

		if (passwordValue != null) {
			return passwordValue.equals(confirmPasswordValue);
		} else {
			return confirmPasswordValue == null;
		}

	}

}
