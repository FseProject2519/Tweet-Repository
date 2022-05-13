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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.tweetservice.constants.TweetAppConstants;
import com.tweetapp.tweetservice.dto.TweetAppError;
import com.tweetapp.tweetservice.dto.TweetDto;
import com.tweetapp.tweetservice.dto.TweetSearchDto;
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
public class TweetAppController {

	@Autowired
	TweetService tweetService;

	@Autowired
	UserService userService;

	// Tweet related methods

	@PostMapping("/{username}/add")
	public ResponseEntity<?> postNewTweet(@PathVariable String username, @RequestBody @Valid TweetDto tweetDto,
			BindingResult bindingResult) {
		try {

			log.info("Start - postNewTweet");
			if (bindingResult.hasErrors()) {
				log.info("Validation errors encountered when posting tweet");
				return new ResponseEntity<>(extractErrors(bindingResult), HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(tweetService.postTweet(username, tweetDto), HttpStatus.CREATED);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PutMapping("/{username}/update/{tweetId}")
	public ResponseEntity<?> updateTweet(@PathVariable String tweetId, @RequestBody @Valid TweetDto tweetDto,
			BindingResult bindingResult, @PathVariable String username) {
		try {

			log.info("Start - updateTweet");
			if (bindingResult.hasErrors()) {
				log.info("Validation errors encountered when updating tweet");
				return new ResponseEntity<>(extractErrors(bindingResult), HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(tweetService.updateTweet(tweetDto, tweetId), HttpStatus.CREATED);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PatchMapping("/{username}/like/{tweetId}")
	public ResponseEntity<?> likeTweet(@PathVariable String tweetId, @PathVariable String username) {
		try {
			log.info("Start - likeTweet");
			return new ResponseEntity<>(tweetService.likeTweet(tweetId, username), HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@DeleteMapping("/{username}/delete/{tweetId}")
	public ResponseEntity<?> deleteTweet(@PathVariable String tweetId) {
		try {
			log.info("Start - deleteTweet");
			return new ResponseEntity<>(tweetService.deleteTweet(tweetId), HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PostMapping("/{username}/reply/{tweetId}")
	public ResponseEntity<?> replyToTweet(@PathVariable String username, @PathVariable String tweetId,
			@RequestBody @Valid TweetDto tweetDto) {
		try {
			log.info("Start - replyToTweet");
			return new ResponseEntity<>(tweetService.replyToTweet(username, tweetDto, tweetId), HttpStatus.CREATED);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllTweets(@RequestParam(required = false) String sortField,
			@RequestParam(required = false) String sortOrder, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size, @RequestParam(required = false) boolean isPaged) {
		try {

			log.info("Start - getAllTweets");
			TweetSearchDto tweetSearchDto = TweetSearchDto.builder().sortField(sortField).sortOrder(sortOrder).build();
			return isPaged
					? new ResponseEntity<>(tweetService.getAllTweetsPaged(tweetSearchDto, page, size), HttpStatus.OK)
					: new ResponseEntity<>(tweetService.getAllTweets(tweetSearchDto), HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/{username}")
	public ResponseEntity<?> getUserTweets(@PathVariable("username") String createdBy,
			@RequestParam(required = false) String sortField, @RequestParam(required = false) String sortOrder,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
			@RequestParam(required = false) boolean isPaged) {
		try {

			log.info("Start - getUserTweets");
			TweetSearchDto tweetSearchDto = TweetSearchDto.builder().createdBy(createdBy).sortField(sortField)
					.sortOrder(sortOrder).build();
			return isPaged
					? new ResponseEntity<>(tweetService.searchTweetsPaged(tweetSearchDto, page, size), HttpStatus.OK)
					: new ResponseEntity<>(tweetService.searchTweets(tweetSearchDto), HttpStatus.OK);

		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchTweets(@RequestParam(required = false) String tweetMessage,
			@RequestParam(required = false) String tweetTopic, @RequestParam(required = false) String createdBy,
			@RequestParam(required = false) String startDateTime, @RequestParam(required = false) String endDateTime,
			@RequestParam(required = false) Set<String> tag, @RequestParam(required = false) String repliedToTweet,
			@RequestParam(required = false) Set<String> likedBy, @RequestParam(required = false) String sortField,
			@RequestParam(required = false) String sortOrder, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size, @RequestParam(required = false) boolean isPaged) {
		try {
			log.info("Start - searchTweets");
			TweetSearchDto tweetSearchDto = TweetSearchDto.builder().tweetMessage(tweetMessage).tweetTopic(tweetTopic)
					.createdBy(createdBy).startDateTime(DateUtils.processDateTime(startDateTime))
					.endDateTime(DateUtils.processDateTime(endDateTime)).tag(tag).repliedToTweet(repliedToTweet)
					.likedBy(likedBy).sortField(sortField).sortOrder(sortOrder).build();
			return isPaged
					? new ResponseEntity<>(tweetService.searchTweetsPaged(tweetSearchDto, page, size), HttpStatus.OK)
					: new ResponseEntity<>(tweetService.searchTweets(tweetSearchDto), HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (DateTimeParseException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		}
	}

	@GetMapping("/trend")
	public ResponseEntity<?> getTrendingTopics(@RequestParam(required = false) String startDateTime,
			@RequestParam(required = false) String endDateTime) {
		try {
			log.info("Start - getTrendingTopics");
			TweetSearchDto tweetSearchDto = TweetSearchDto.builder()
					.startDateTime(DateUtils.processDateTime(startDateTime))
					.endDateTime(DateUtils.processDateTime(endDateTime)).build();
			return new ResponseEntity<>(tweetService.getTrendingTopics(tweetSearchDto), HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (DateTimeParseException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		}
	}

	@GetMapping("/all/hashtags")
	public ResponseEntity<?> getHashtags() {
		try {
			log.info("Start - getHashtags");

			return new ResponseEntity<>(tweetService.getHashtags(), HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	// User related methods

	@GetMapping("/users/all")
	public ResponseEntity<?> getAllUsers(@RequestParam(required = false) String sortField,
			@RequestParam(required = false) String sortOrder, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size, @RequestParam(required = false) boolean isPaged)
			throws TweetServiceException {
		try {
			log.info("Start - getAllUsers");

			UserSearchDto userSearchDto = UserSearchDto.builder().sortField(sortField).sortOrder(sortOrder).build();

			return isPaged
					? new ResponseEntity<>(userService.getAllUsersPaged(userSearchDto, page, size), HttpStatus.OK)
					: new ResponseEntity<>(userService.getAllUsers(userSearchDto), HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/users/search/{username}")
	public ResponseEntity<?> getUser(@PathVariable("username") String userId,
			@RequestParam(required = false) String sortField, @RequestParam(required = false) String sortOrder,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
			@RequestParam(required = false) boolean isPaged) throws TweetServiceException {
		try {
			log.info("Start - getUser");

			UserSearchDto userSearchDto = UserSearchDto.builder().userId(userId).sortField(sortField)
					.sortOrder(sortOrder).build();
			return isPaged
					? new ResponseEntity<>(userService.searchUsersPaged(userSearchDto, page, size), HttpStatus.OK)
					: new ResponseEntity<>(userService.searchUsers(userSearchDto), HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/users/search")
	public ResponseEntity<?> searchUsers(@RequestParam(required = false) String userId,
			@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName,
			@RequestParam(required = false) String email, @RequestParam(required = false) String sortField,
			@RequestParam(required = false) String sortOrder, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size, @RequestParam(required = false) boolean isPaged)
			throws TweetServiceException {
		try {
			log.info("Start - searchUsers");

			UserSearchDto userSearchDto = UserSearchDto.builder().userId(userId).firstName(firstName).lastName(lastName)
					.email(email).sortField(sortField).sortOrder(sortOrder).build();
			return isPaged
					? new ResponseEntity<>(userService.searchUsersPaged(userSearchDto, page, size), HttpStatus.OK)
					: new ResponseEntity<>(userService.searchUsers(userSearchDto), HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/users/usertags")
	public ResponseEntity<?> getUsertags(@RequestParam(required = false) String userId) throws TweetServiceException {
		try {
			log.info("Start - searchUsers");

			return new ResponseEntity<>(userService.getUsertags(), HttpStatus.OK);
		} catch (TweetServiceException e) {
			log.info(TweetAppConstants.EXCEPTION, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{username}/export")
	public void exportCSV(HttpServletResponse response, @PathVariable("username") String username) throws IOException {
		try {
			response.setContentType("application/pdf");

			String filename = username + "_" + DateUtils.userFriendlyFormat(LocalDateTime.now());
			filename = filename.replaceAll("[^a-zA-Z0-9]", "_") + ".pdf";

			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=" + filename;
			response.setHeader(headerKey, headerValue);

			TweetPdfExportUtil exporter = new TweetPdfExportUtil(tweetService.getExportData(username));
			exporter.export(response);
		} catch (TweetServiceException e) {
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

}
