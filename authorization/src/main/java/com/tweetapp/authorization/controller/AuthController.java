package com.tweetapp.authorization.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.authorization.dto.AuthResponse;
import com.tweetapp.authorization.dto.UserCredentials;
import com.tweetapp.authorization.service.DetailsService;
import com.tweetapp.authorization.service.JwtUtil;

@RestController
public class AuthController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private JwtUtil jwtutil ;
	
	@Autowired
	private DetailsService detailsService;

	@PostMapping("/userlogin")
	public ResponseEntity<?> userlogin(@RequestBody UserCredentials userlogincredentials) {
		LOGGER.info("Start - userlogin");
		try {
			final UserDetails userdetails = detailsService.loadUserByUsername(userlogincredentials.getUserId());

			String uname = "";
			String generateToken = "";
			String role = "";
			if (userdetails.getPassword().equals(userlogincredentials.getPassword()))
					{
				uname = userlogincredentials.getUserId();
				generateToken = jwtutil.generateToken(userdetails);
				LOGGER.info("End - userlogin - Successful");

				return new ResponseEntity<>(new UserCredentials(uname, null, generateToken), HttpStatus.OK);
			} else {
				LOGGER.info("End - userlogin - Wrong Credentials");
				return new ResponseEntity<>("Not Accesible", HttpStatus.FORBIDDEN);
			}
		} catch (Exception e) {
			LOGGER.info("End - userlogin - Username Not Found");

			return new ResponseEntity<>("Not Accesible", HttpStatus.FORBIDDEN);

		}
	}

	@GetMapping("/uservalidate")
	public ResponseEntity<?> getUserValidity(@RequestHeader("Authorization") String token) {
		LOGGER.info("Start - getUserValidity");

		AuthResponse res = new AuthResponse();
		if (token == null) {
			res.setValid(false);
			LOGGER.info("End - getUserValidity - Null token");

			return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);
		} else {
			String token1 = token.substring(7);
			if (jwtutil.validateToken(token1)) {
					res.setName(jwtutil.extractUsername(token1));
					res.setValid(true);
				
			} else {
				res.setValid(false);
				LOGGER.info("End - getUserValidity - Invalid token");

				return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);

			}
		}
		LOGGER.info("End - getUserValidity - Successful");

		return new ResponseEntity<>(res, HttpStatus.OK);

	}
}