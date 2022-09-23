package com.tweetapp.authorization.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.tweetapp.authorization.entity.UserEntity;
import com.tweetapp.authorization.repository.UserRepository;
import com.tweetapp.authorization.util.EmailConstraint;

public class EmailValidator implements ConstraintValidator<EmailConstraint, String> {

	@Autowired
	UserRepository userRepository;

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		Optional<UserEntity> user = userRepository.findByEmail(email);
		if (user.isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}
