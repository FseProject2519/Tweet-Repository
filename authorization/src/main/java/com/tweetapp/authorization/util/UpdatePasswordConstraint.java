package com.tweetapp.authorization.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.tweetapp.authorization.validator.UpdatePasswordValidator;

@Documented
@Constraint(validatedBy = UpdatePasswordValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdatePasswordConstraint {
	String message() default "Password should contain at least one digit,atleast one upper case alphabet, at least one lower case alphabet.?Password should contain at least one special character which includes !@#$%&*()-+=^.?Password cannot contain any white space.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
