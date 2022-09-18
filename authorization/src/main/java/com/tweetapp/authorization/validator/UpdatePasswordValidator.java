package com.tweetapp.authorization.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tweetapp.authorization.repository.UserRepository;
import com.tweetapp.authorization.util.UpdatePasswordConstraint;

public class UpdatePasswordValidator implements ConstraintValidator<UpdatePasswordConstraint, String> {

	@Autowired
	UserRepository userRepository;

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if (!StringUtils.isEmpty(password)) {
			Pattern p = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}");
			Matcher m = p.matcher(password);
			return m.matches();
		} else {
			return true;
		}

	}

}
