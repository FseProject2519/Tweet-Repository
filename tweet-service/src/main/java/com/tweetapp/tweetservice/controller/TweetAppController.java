package com.tweetapp.tweetservice.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.tweetservice.constants.TweetAppConstants;
import com.tweetapp.tweetservice.dto.TweetAppError;
import com.tweetapp.tweetservice.dto.TweetDto;
import com.tweetapp.tweetservice.dto.TweetResponseDto;
import com.tweetapp.tweetservice.dto.TweetSearchDto;
import com.tweetapp.tweetservice.dto.UserResponseDto;
import com.tweetapp.tweetservice.dto.UserSearchDto;
import com.tweetapp.tweetservice.exception.TweetServiceException;
import com.tweetapp.tweetservice.service.TweetService;
import com.tweetapp.tweetservice.service.UserService;
import com.tweetapp.tweetservice.utility.DateUtils;
import com.tweetapp.tweetservice.utility.TweetPdfExportUtil;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("api/v1.0/tweets")
@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class TweetAppController {

	@Autowired
	TweetService tweetService;

	UserService userService;

	// Example for setter based injection
	@Autowired
	void setUserService(UserService userService) {
		this.userService = userService;
	}

	// Tweet related methods

	@PostMapping("/{username}/add")
	public ResponseEntity<TweetResponseDto> postNewTweet(@RequestHeader("Authorization") String token,
			@PathVariable String username, @RequestBody @Valid TweetDto tweetDto, BindingResult bindingResult) {
		TweetResponseDto tweetResponseDto = new TweetResponseDto();

		try {

			log.info("Start - postNewTweet");

			if (bindingResult.hasErrors()) {
				log.info("Validation errors encountered when posting tweet");
				tweetResponseDto.setValidationErrors(extractErrors(bindingResult));
				return new ResponseEntity<>(tweetResponseDto, HttpStatus.BAD_REQUEST);
			}
			tweetResponseDto.setResponseMessage(tweetService.postTweet(username, tweetDto));
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.CREATED);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			tweetResponseDto.setResponseMessage(e.getMessage());
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PutMapping("/{username}/update/{tweetId}")
	public ResponseEntity<TweetResponseDto> updateTweet(@RequestHeader("Authorization") String token,
			@PathVariable String tweetId, @RequestBody @Valid TweetDto tweetDto, BindingResult bindingResult,
			@PathVariable String username) {

		TweetResponseDto tweetResponseDto = new TweetResponseDto();

		try {

			log.info("Start - updateTweet");
			if (bindingResult.hasErrors()) {
				log.info("Validation errors encountered when updating tweet");
				tweetResponseDto.setValidationErrors(extractErrors(bindingResult));
				return new ResponseEntity<>(tweetResponseDto, HttpStatus.BAD_REQUEST);
			}

			tweetResponseDto.setResponseMessage(tweetService.updateTweet(tweetDto, tweetId));
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.CREATED);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());

			tweetResponseDto.setResponseMessage(e.getMessage());
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PatchMapping("/{username}/like/{tweetId}")
	public ResponseEntity<TweetResponseDto> likeTweet(@RequestHeader("Authorization") String token,
			@PathVariable String tweetId, @PathVariable String username) {

		TweetResponseDto tweetResponseDto = new TweetResponseDto();

		try {
			log.info("Start - likeTweet");
			tweetResponseDto.setResponseMessage(tweetService.likeTweet(tweetId, username));
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			tweetResponseDto.setResponseMessage(e.getMessage());
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@DeleteMapping("/{username}/delete/{tweetId}")
	public ResponseEntity<TweetResponseDto> deleteTweet(@RequestHeader("Authorization") String token,
			@PathVariable String tweetId) {

		TweetResponseDto tweetResponseDto = new TweetResponseDto();

		try {
			log.info("Start - deleteTweet");

			tweetResponseDto.setResponseMessage(tweetService.deleteTweet(tweetId));
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());

			tweetResponseDto.setResponseMessage(e.getMessage());
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PostMapping("/{username}/reply/{tweetId}")
	public ResponseEntity<TweetResponseDto> replyToTweet(@RequestHeader("Authorization") String token,
			@PathVariable String username, @PathVariable String tweetId, @RequestBody @Valid TweetDto tweetDto) {

		TweetResponseDto tweetResponseDto = new TweetResponseDto();

		try {
			log.info("Start - replyToTweet");

			tweetResponseDto.setResponseMessage(tweetService.replyToTweet(username, tweetDto, tweetId));
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.CREATED);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());

			tweetResponseDto.setResponseMessage(e.getMessage());
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/all")
	public ResponseEntity<TweetResponseDto> getAllTweets(@RequestHeader("Authorization") String token,
			@RequestParam(required = false) String sortField, @RequestParam(required = false) String sortOrder,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
			@RequestParam(required = false) boolean isPaged) {

		TweetResponseDto tweetResponseDto = new TweetResponseDto();

		try {

			log.info("Start - getAllTweets");
			TweetSearchDto tweetSearchDto = TweetSearchDto.builder().sortField(sortField).sortOrder(sortOrder).build();
			if (isPaged) {
				tweetResponseDto.setTweetsPaged(tweetService.getAllTweetsPaged(tweetSearchDto, page, size));
			} else {
				tweetResponseDto.setTweetsList(tweetService.getAllTweets());
			}

			return new ResponseEntity<>(tweetResponseDto, HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			tweetResponseDto.setResponseMessage(e.getMessage());
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/{username}")
	public ResponseEntity<TweetResponseDto> getUserTweets(@RequestHeader("Authorization") String token,
			@PathVariable("username") String createdBy, @RequestParam(required = false) String sortField,
			@RequestParam(required = false) String sortOrder, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size, @RequestParam(required = false) boolean isPaged,
			@RequestParam(required = false) boolean withComments) {

		TweetResponseDto tweetResponseDto = new TweetResponseDto();

		try {

			log.info("Start - getUserTweets");
			TweetSearchDto tweetSearchDto = TweetSearchDto.builder().createdBy(createdBy).withComments(withComments)
					.sortField(sortField).sortOrder(sortOrder).build();
			if (isPaged) {
				tweetResponseDto.setTweetsPaged(tweetService.searchTweetsPaged(tweetSearchDto, page, size));
			} else {
				tweetResponseDto.setTweetsList(tweetService.searchTweets(tweetSearchDto));
			}

			return new ResponseEntity<>(tweetResponseDto, HttpStatus.OK);

		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			tweetResponseDto.setResponseMessage(e.getMessage());
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/search")
	public ResponseEntity<TweetResponseDto> searchTweets(@RequestHeader("Authorization") String token,
			@RequestParam(required = false) String tweetMessage, @RequestParam(required = false) String createdBy,
			@RequestParam(required = false) String startDateTime, @RequestParam(required = false) String endDateTime,
			@RequestParam(required = false) Set<String> tag, @RequestParam(required = false) String repliedToTweet,
			@RequestParam(required = false) Set<String> likedBy, @RequestParam(required = false) String sortField,
			@RequestParam(required = false) String sortOrder, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size, @RequestParam(required = false) boolean isPaged,
			@RequestParam(required = false) boolean withComments) {
		TweetResponseDto tweetResponseDto = new TweetResponseDto();

		try {
			log.info("Start - searchTweets");
			TweetSearchDto tweetSearchDto = TweetSearchDto.builder().tweetMessage(tweetMessage).createdBy(createdBy)
					.startDateTime(DateUtils.processDateTime(startDateTime))
					.endDateTime(DateUtils.processDateTime(endDateTime)).tag(tag).repliedToTweet(repliedToTweet)
					.likedBy(likedBy).withComments(withComments).sortField(sortField).sortOrder(sortOrder).build();
			if (isPaged) {
				tweetResponseDto.setTweetsPaged(tweetService.searchTweetsPaged(tweetSearchDto, page, size));
			} else {
				tweetResponseDto.setTweetsList(tweetService.searchTweets(tweetSearchDto));
			}

			return new ResponseEntity<>(tweetResponseDto, HttpStatus.OK);

		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());

			tweetResponseDto.setResponseMessage(e.getMessage());
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (DateTimeParseException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());

			return returnBadRequestResponse(tweetResponseDto, e);

		}
	}

	@GetMapping("/trend")
	public ResponseEntity<TweetResponseDto> getTrendingTopics(@RequestHeader("Authorization") String token,
			@RequestParam(required = false) String startDateTime, @RequestParam(required = false) String endDateTime) {

		TweetResponseDto tweetResponseDto = new TweetResponseDto();
		try {
			log.info("Start - getTrendingTopics");
			TweetSearchDto tweetSearchDto = TweetSearchDto.builder()
					.startDateTime(DateUtils.processDateTime(startDateTime))
					.endDateTime(DateUtils.processDateTime(endDateTime)).build();
			tweetResponseDto.setTweetTrendsList(tweetService.getTrendingTopics(tweetSearchDto));
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());

			tweetResponseDto.setResponseMessage(e.getMessage());
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (DateTimeParseException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());

			return returnBadRequestResponse(tweetResponseDto, e);

		}
	}

	@GetMapping("/all/hashtags")
	public ResponseEntity<TweetResponseDto> getHashtags(@RequestHeader("Authorization") String token) {
		TweetResponseDto tweetResponseDto = new TweetResponseDto();

		try {
			log.info("Start - getHashtags");
			tweetResponseDto.setHashtagsList(tweetService.getHashtags());
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			tweetResponseDto.setResponseMessage(e.getMessage());
			return new ResponseEntity<>(tweetResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	private ResponseEntity<TweetResponseDto> returnBadRequestResponse(TweetResponseDto tweetResponseDto,
			DateTimeParseException e) {
		tweetResponseDto.setResponseMessage(e.getMessage());
		return new ResponseEntity<>(tweetResponseDto, HttpStatus.BAD_REQUEST);
	}

	// User related methods

	@GetMapping("/users/all")
	public ResponseEntity<UserResponseDto> getAllUsers(@RequestHeader("Authorization") String token,
			@RequestParam(required = false) String sortField, @RequestParam(required = false) String sortOrder,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
			@RequestParam(required = false) boolean isPaged) throws TweetServiceException {

		UserResponseDto userResponseDto = new UserResponseDto();
		try {
			log.info("Start - getAllUsers");

			UserSearchDto userSearchDto = UserSearchDto.builder().sortField(sortField).sortOrder(sortOrder).build();

			if (isPaged) {
				userResponseDto.setUsersPaged(userService.getAllUsersPaged(userSearchDto, page, size));
			} else {
				userResponseDto.setUsersList(userService.getAllUsers());
			}
			return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());

			return sendExceptionResponse(userResponseDto, e);
		}
	}

	@GetMapping("/users/search/{username}")
	public ResponseEntity<UserResponseDto> getUser(@RequestHeader("Authorization") String token,
			@PathVariable("username") String userId, @RequestParam(required = false) String sortField,
			@RequestParam(required = false) String sortOrder, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size, @RequestParam(required = false) boolean isPaged)
			throws TweetServiceException {

		UserResponseDto userResponseDto = new UserResponseDto();

		try {
			log.info("Start - getUser");

			UserSearchDto userSearchDto = UserSearchDto.builder().userId(userId).sortField(sortField)
					.sortOrder(sortOrder).build();

			if (isPaged) {
				userResponseDto.setUsersPaged(userService.searchUsersPaged(userSearchDto, page, size));
			} else {
				userResponseDto.setUsersList(userService.searchUsers(userSearchDto));
			}

			return new ResponseEntity<>(userResponseDto, HttpStatus.OK);

		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return sendExceptionResponse(userResponseDto, e);
		}
	}

	@GetMapping("/users/search")
	public ResponseEntity<UserResponseDto> searchUsers(@RequestHeader("Authorization") String token,
			@RequestParam(required = false) String userId, @RequestParam(required = false) String firstName,
			@RequestParam(required = false) String lastName, @RequestParam(required = false) String email,
			@RequestParam(required = false) String sortField, @RequestParam(required = false) String sortOrder,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
			@RequestParam(required = false) boolean isPaged) throws TweetServiceException {

		UserResponseDto userResponseDto = new UserResponseDto();

		try {
			log.info("Start - searchUsers");

			UserSearchDto userSearchDto = UserSearchDto.builder().userId(userId).firstName(firstName).lastName(lastName)
					.email(email).sortField(sortField).sortOrder(sortOrder).build();
			if (isPaged) {
				userResponseDto.setUsersPaged(userService.searchUsersPaged(userSearchDto, page, size));
			} else {
				userResponseDto.setUsersList(userService.searchUsers(userSearchDto));
			}

			return new ResponseEntity<>(userResponseDto, HttpStatus.OK);

		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return sendExceptionResponse(userResponseDto, e);
		}
	}

	@GetMapping("/users/usertags")
	public ResponseEntity<UserResponseDto> getUsertags(@RequestHeader("Authorization") String token,
			@RequestParam(required = false) String userId) throws TweetServiceException {
		UserResponseDto userResponseDto = new UserResponseDto();

		try {
			log.info("Start - searchUsers");
			userResponseDto.setUsertagsList(userService.getUsertags());
			return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return sendExceptionResponse(userResponseDto, e);
		}
	}

	@CrossOrigin(value = "*", exposedHeaders = { "Content-Disposition" })
	@GetMapping("/{username}/export")
	public void exportCSV(@RequestHeader("Authorization") String token, HttpServletResponse response,
			@PathVariable("username") String username) throws IOException {
		try {
			log.info("Start - exportCSV");

			response.setContentType("application/pdf");

			String filename = username + "_" + DateUtils.userFriendlyDateFormat(LocalDateTime.now());
			filename = filename.replaceAll("[^a-zA-Z0-9]", "_") + ".pdf";

			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=" + filename;
			response.setHeader(headerKey, headerValue);

			TweetPdfExportUtil exporter = new TweetPdfExportUtil(tweetService.getExportData(username));
			exporter.export(response);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}

	}

	private List<TweetAppError> extractErrors(BindingResult bindingResult) {
		List<TweetAppError> errorList = new ArrayList<>();
		bindingResult.getFieldErrors().forEach(error -> {
			TweetAppError tweetAppError = TweetAppError.builder().fieldName(error.getField())
					.message(error.getDefaultMessage()).build();
			errorList.add(tweetAppError);
		});
		return errorList;
	}

	private ResponseEntity<UserResponseDto> sendExceptionResponse(UserResponseDto userResponseDto,
			TweetServiceException e) {
		userResponseDto.setResponseMessage(e.getMessage());
		return new ResponseEntity<>(userResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
