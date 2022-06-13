package com.tweetapp.tweetservice.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tweetapp.tweetservice.exception.AuthException;
import com.tweetapp.tweetservice.restclient.AuthClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthUtil {

	@Autowired
	AuthClient authClient;

	public Boolean isSessionValid(String token) {
		log.info("START - isSessionValid");

		try {
			authClient.getUserValidity(token);
		} catch (Exception e) {
			log.info("EXCEPTION - " + e.getMessage());
			throw new AuthException();
		}
		log.info("END - isSessionValid");

		return true;
	}
}
