package com.tweetapp.authorization.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

import com.tweetapp.authorization.validator.PasswordMatchValidator;

@Constraint(validatedBy = PasswordMatchValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatchConstraint {
	String message() default "Password and confirmed password do not match";
	
	String password();
	String confirmPassword();
	 @Target({ ElementType.TYPE })
	    @Retention(RetentionPolicy.RUNTIME)
	    @interface List {
		 PasswordMatchConstraint[] value();
	    }
}
    
