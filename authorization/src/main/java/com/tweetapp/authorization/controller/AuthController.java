package com.tweetapp.authorization.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.authorization.dto.AuthResponse;
import com.tweetapp.authorization.dto.OtpDto;
import com.tweetapp.authorization.dto.PasswordDto;
import com.tweetapp.authorization.dto.UserCredentials;
import com.tweetapp.authorization.dto.UserDetailsErrors;
import com.tweetapp.authorization.dto.UserDto;
import com.tweetapp.authorization.exception.TweetServiceException;
import com.tweetapp.authorization.service.DetailsService;
import com.tweetapp.authorization.service.JwtUtil;
import com.tweetapp.authorization.service.RegisterService;

@RestController
@RequestMapping("api/v1.0/authorization/tweets")
public class AuthController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private JwtUtil jwtutil ;
	
	@Autowired
	private DetailsService detailsService;
	
	@Autowired
	private RegisterService registerService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody @Valid UserDto userDto,BindingResult bindingResult) {

		LOGGER.info("Start- User Registration");
		try {
			if (bindingResult.hasErrors()) {
				LOGGER.info("Validation errors encountered when registering user");
				return new ResponseEntity<>(extractErrors(bindingResult), HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(registerService.registerUser(userDto), HttpStatus.CREATED);

		}
		catch(TweetServiceException e){
			LOGGER.info("Exception encountered in user registeration:{}",e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private List<UserDetailsErrors> extractErrors(BindingResult bindingResult) {
		List<UserDetailsErrors> errorList = new ArrayList<>();
		bindingResult.getFieldErrors().forEach(error -> {
			UserDetailsErrors err = UserDetailsErrors.builder().fieldName(error.getField())
					.message(error.getDefaultMessage()).build();
			errorList.add(err);
		});
		return errorList;
	}
	@GetMapping("/{username}/forgot")
	public ResponseEntity<?> forgotPassword(@PathVariable("username") String username){
		try {
			LOGGER.info("Start- Forgot Password");
			return new ResponseEntity<>(registerService.forgotPassword(username), HttpStatus.OK);

		}
		catch(TweetServiceException e) {
			LOGGER.info("Exception occurred when resetting password", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
		
		
	}

	@PostMapping("/{username}/verifyOtp")
	public ResponseEntity<?> verifyOtp(@PathVariable("username") String username, @RequestBody OtpDto otp) {
		
			try {
				LOGGER.info("Start- Verify OTP");
				return new ResponseEntity<>(registerService.verifyOtp(username, otp), HttpStatus.OK);

			} catch (TweetServiceException e) {
				LOGGER.info("Exception occurred when verifying Otp", e.getMessage());
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

			}
		

	}
	@PostMapping("/{username}/resetpassword")
	public ResponseEntity<?> resetPassword(@PathVariable("username") String username,@RequestBody PasswordDto password,BindingResult bindingResult) {
		
			try {
				LOGGER.info("Start- Password Reset");
				if (bindingResult.hasErrors()) {
					LOGGER.info("Validation errors encountered when resetting password");
					return new ResponseEntity<>(extractErrors(bindingResult), HttpStatus.BAD_REQUEST);
				}
				return new ResponseEntity<>(registerService.resetPassword(username, password), HttpStatus.OK);

			} catch (TweetServiceException e) {
				LOGGER.info("Exception occurred when resetting password", e.getMessage());
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

			}
		

	}
	

	@PostMapping("/userlogin")
	public ResponseEntity<?> userlogin(@RequestBody UserCredentials userlogincredentials) {
		LOGGER.info("Start - userlogin");
		try {
			final UserDetails userdetails = detailsService.loadUserByUsername(userlogincredentials.getUserId());

			String uname = "";
			String generateToken = "";
			if (passwordEncoder.matches(userlogincredentials.getPassword(), userdetails.getPassword()))
					{
				uname = userlogincredentials.getUserId();
				generateToken = jwtutil.generateToken(userdetails);
				LOGGER.info("End - userlogin - Successful");

				return new ResponseEntity<>(new UserCredentials(uname, null, generateToken), HttpStatus.OK);
			} 
				LOGGER.info("End - userlogin - Wrong Credentials");
				return new ResponseEntity<>("Not Accesible", HttpStatus.FORBIDDEN);
			
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