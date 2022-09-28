package com.tweetapp.authorization.service.impl;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tweetapp.authorization.dto.OtpDto;
import com.tweetapp.authorization.dto.PasswordDto;
import com.tweetapp.authorization.dto.UserDto;
import com.tweetapp.authorization.entity.UserEntity;
import com.tweetapp.authorization.event.NotificationEvent;
import com.tweetapp.authorization.event.OnUserLogoutSuccess;
import com.tweetapp.authorization.exception.TweetServiceException;
import com.tweetapp.authorization.repository.UserRepository;
import com.tweetapp.authorization.service.RegisterService;
import com.tweetapp.authorization.util.LoggedOutJwtTokenCache;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserRepository userRepository;

	@Value("${queue.name}")
	private String notificationDetailsQueue;

	@Autowired
	private RabbitTemplate messageTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private LoggedOutJwtTokenCache tokenCache;

	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterServiceImpl.class);
	private static final String AWS_USER_ENDPOINT = "https://5m6zq8a7zh.execute-api.ap-northeast-1.amazonaws.com/users";

	@Override
	public String registerUser(UserDto userDto) throws TweetServiceException {

		try {
			String encodedPwd = passwordEncoder.encode(userDto.getPassword());
			userDto.setPassword(encodedPwd);
			userDto.setConfirmPassword(encodedPwd);
			UserEntity user = modelMapper.map(userDto, UserEntity.class);
			user.setLastModifiedDate(LocalDateTime.now());
			user.setValidOtp(false);
			saveUserCloud(user);
			return userRepository.save(user).getId() != null ? "User Registered Successfully"
					: "User Registration Failed";
		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());
		}

	}

	private void saveUserCloud(UserEntity user) throws TweetServiceException {
		try {
			URL url;
			url = new URL(AWS_USER_ENDPOINT);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Content-Type", "application/json");
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			String postData = mapper.writeValueAsString(user);
			log.info(postData);
			out.write(postData);
			out.close();
			connection.connect();
			log.info("Response Code = {}", connection.getResponseCode());
		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());
		}
	}

	@Override
	public String updateUser(String userId, UserDto userDto) throws TweetServiceException {

		try {
			if (!StringUtils.isEmpty(userDto.getUpdatePassword())) {
				String encodedPwd = passwordEncoder.encode(userDto.getUpdatePassword());
				userDto.setPassword(encodedPwd);
				userDto.setConfirmPassword(encodedPwd);
			}
			UserEntity user = modelMapper.map(userDto, UserEntity.class);
			Optional<UserEntity> existingUserOptional = userRepository.findByUserId(userId);

			if (existingUserOptional.isPresent()) {
				UserEntity existingUserEntity = existingUserOptional.get();
				existingUserEntity.setContactNumber(user.getContactNumber());
				existingUserEntity.setEmail(user.getEmail());
				existingUserEntity.setFirstName(user.getFirstName());
				existingUserEntity.setLastName(user.getLastName());
				if (!StringUtils.isEmpty(user.getPassword())) {
					existingUserEntity.setPassword(user.getPassword());
				}
				existingUserEntity.setLastModifiedDate(LocalDateTime.now());
				log.info("Updating User - {}", existingUserEntity);
				saveUserCloud(existingUserEntity);
				return userRepository.save(existingUserEntity).getId() != null ? "User Updated Successfully"
						: "User Updation Failed";

			} else {
				return "User Not Found";
			}

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());
		}

	}

	@Override
	public String forgotPassword(String username) throws TweetServiceException {
		try {
			Optional<UserEntity> user = userRepository.findByUserId(username);
			if (user.isPresent()) {
				String email = user.get().getEmail();
				if (StringUtils.isNotEmpty(email)) {
					String otp = RandomString.make(8);
					String encodedOtp = passwordEncoder.encode(otp);
					user.get().setForgotPwdOtp(encodedOtp);
					user.get().setLastModifiedDate(LocalDateTime.now());
					userRepository.save(user.get());
					NotificationEvent notificationEvent = NotificationEvent.builder()
							.firstName(user.get().getFirstName()).lastName(user.get().getLastName())
							.email(user.get().getEmail()).otp(otp).build();
					LOGGER.info("Sending message [{}] to queue [{}]", notificationEvent, notificationDetailsQueue);
					messageTemplate.convertAndSend(notificationDetailsQueue, notificationEvent);
					int index = email.indexOf("@");
					StringBuilder secureEmail = new StringBuilder(email.substring(0, 1));
					String substringEmail = email.substring(1, index - 1).replaceAll(".", "*");
					secureEmail.append(substringEmail).append(email.substring(index - 1));
					return "Token  to reset your password has been sent to your registered email "
							+ secureEmail.toString();

				} else {
					return "No valid email id found for the user";
				}
			} else {
				return "Not a Valid Username";
			}
		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}

	@Override
	public String verifyOtp(String username, OtpDto otp) throws TweetServiceException {
		try {
			Optional<UserEntity> user = userRepository.findByUserId(username);

			if (user.isPresent()) {

				if (user.get().getLastModifiedDate().plusMinutes(5).isBefore(LocalDateTime.now())) {
					user.get().setForgotPwdOtp("");
					userRepository.save(user.get());
					LOGGER.info("Otp expired please click resend OTP link to get a new OTP");
					return "Otp expired please click resend OTP link to get a new OTP";
				} else {
					if (null != user.get().getForgotPwdOtp()
							&& passwordEncoder.matches(otp.getOtp(), user.get().getForgotPwdOtp())) {
						user.get().setForgotPwdOtp("");
						user.get().setValidOtp(true);
						userRepository.save(user.get());
						LOGGER.info("OTP verification done successfully");
						return "OTP verification done successfully";
					} else {
						LOGGER.info("Invalid OTP");
						return "Invalid OTP";
					}
				}
			} else {
				LOGGER.info("User not found");
				return "User not found";
			}
		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}

	@Override
	public String resetPassword(String username, PasswordDto password) throws TweetServiceException {
		try {
			String encodedPwd = passwordEncoder.encode(password.getPassword());
			Optional<UserEntity> user = userRepository.findByUserId(username);

			if (user.isPresent()) {
				if (user.get().getValidOtp()) {
					user.get().setPassword(encodedPwd);
					user.get().setLastModifiedDate(LocalDateTime.now());
					user.get().setValidOtp(false);
					userRepository.save(user.get());
					return "Password resetted Successfully";
				} else {
					return "Unauthorised Request";
				}
			}

			else {
				return "User Not found";
			}
		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());
		}
	}

	@Override
	public void userLogout(OnUserLogoutSuccess onUserLogoutSuccess) {
		LOGGER.info("Invalidating the token for user logout");
		tokenCache.markLogoutEventForToken(onUserLogoutSuccess);

	}

	@Override
	public boolean validateTokenIsNotForALoggedOut(String authToken) {
		LOGGER.info("validateTokenIsNotForALoggedOut checking...");
		OnUserLogoutSuccess previouslyLoggedOutEvent = tokenCache.getLogoutEventForToken(authToken);
		if (previouslyLoggedOutEvent != null) {
			String userName = previouslyLoggedOutEvent.getUserName();
			String errorMessage = String
					.format("Token corresponds to an already logged out user [%s]. Please login again", userName);
			LOGGER.info(errorMessage);
			return false;
		} else {
			return true;
		}

	}

}
