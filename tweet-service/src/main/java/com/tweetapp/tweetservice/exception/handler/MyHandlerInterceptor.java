package com.tweetapp.tweetservice.exception.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.tweetapp.tweetservice.utility.AuthUtil;

@Component
public class MyHandlerInterceptor implements HandlerInterceptor {

	@Autowired
	AuthUtil authUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return authUtil.isSessionValid(request.getHeader("AUTHORIZATION"));
	}

}