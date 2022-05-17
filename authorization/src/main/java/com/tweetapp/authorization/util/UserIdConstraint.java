package com.tweetapp.authorization.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import com.tweetapp.authorization.validator.UserIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UserIdValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserIdConstraint {
	 String message() default "Username already exists";
	    Class<?>[] groups() default {};
	    Class<? extends Payload>[] payload() default {};
}

