package com.tweetapp.authorization.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.tweetapp.authorization.entity.UserEntity;
import com.tweetapp.authorization.repository.UserRepository;
import com.tweetapp.authorization.util.UserIdConstraint;

public class UserIdValidator implements ConstraintValidator<UserIdConstraint,String>{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public boolean isValid(String userId, ConstraintValidatorContext context) {
		Optional<UserEntity> user=userRepository.findByUserId(userId);
		if(user.isPresent()) {
			return false;
		}
		else {
			return true;
		}
		
	}

}
