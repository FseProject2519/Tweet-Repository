package com.tweetapp.tweetservice.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tweetapp.tweetservice.exception.AuthException;

@ControllerAdvice
public class AuthExceptionHandler {
	@ExceptionHandler(AuthException.class)
	@ResponseBody
	public Map<String, Object> handler() {
		Map<String, Object> m1 = new HashMap<>();
		m1.put("status", "error");
		m1.put("message", "Sorry, your provided token information expired or not exists.");
		return m1;
	}
}