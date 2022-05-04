package com.tweetapp.tweetservice.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.tweetservice.constants.TweetAppConstants;
import com.tweetapp.tweetservice.dto.TweetDto;
import com.tweetapp.tweetservice.dto.TweetSearchDto;
import com.tweetapp.tweetservice.dto.UserSearchDto;
import com.tweetapp.tweetservice.entity.TweetEntity;
import com.tweetapp.tweetservice.entity.UserEntity;
import com.tweetapp.tweetservice.exception.TweetServiceException;
import com.tweetapp.tweetservice.service.TweetService;
import com.tweetapp.tweetservice.service.UserService;

@RequestMapping("api/v1.0/tweets")
@RestController
public class TweetAppController {

	@Autowired
	TweetService tweetService;


	@Autowired
	UserService userService;

	// Tweet related methods

	
	@PostMapping("{username}/add")
	public ResponseEntity<String> postNewTweet(@PathVariable String username, @RequestBody TweetDto tweetDto) {


		return new ResponseEntity<>(tweetService.postTweet(username,tweetDto), HttpStatus.CREATED);
	}
	@PutMapping("{username}/update/{tweetId}")
	public ResponseEntity<String> updateTweet(@PathVariable String tweetId, @RequestBody TweetDto tweetDto,@PathVariable String username) {

		return new ResponseEntity<>(tweetService.updateTweet(tweetDto,tweetId), HttpStatus.CREATED);
	}
	@PutMapping("{username}/like/{tweetId}")
	public ResponseEntity<String> likeTweet(@PathVariable String tweetId, @PathVariable String username) {

		return new ResponseEntity<>(tweetService.likeTweet(tweetId,username), HttpStatus.OK);
	}
	@DeleteMapping("{username}/delete/{tweetId}")
	public ResponseEntity<String> deleteTweet(@PathVariable String tweetId){
		return new ResponseEntity<>(tweetService.deleteTweet(tweetId), HttpStatus.OK);
	}
	@PostMapping("{username}/reply/{tweetId}")
	public ResponseEntity<String> replyToTweet(@PathVariable String username,@PathVariable String tweetId,@RequestBody TweetDto tweetDto){
		return new ResponseEntity<>(tweetService.replyToTweet(username, tweetDto, tweetId),HttpStatus.CREATED);
	}

	@GetMapping("/all")
	public ResponseEntity<Page<TweetEntity>> getAllTweets(@RequestParam(required = false) String sortField,
			@RequestParam(required = false) String sortOrder, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) throws TweetServiceException {
		TweetSearchDto tweetSearchDto = TweetSearchDto.builder().sortField(sortField).sortOrder(sortOrder).build();
		return tweetService.getAllTweets(tweetSearchDto, page, size);
	}

	@GetMapping("/{username}")
	public ResponseEntity<Page<TweetEntity>> getUserTweets(@PathVariable("username") String createdBy,
			@RequestParam(required = false) String sortField, @RequestParam(required = false) String sortOrder,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size)
			throws TweetServiceException {
		TweetSearchDto tweetSearchDto = TweetSearchDto.builder().createdBy(createdBy).sortField(sortField)
				.sortOrder(sortOrder).build();
		return tweetService.searchTweets(tweetSearchDto, page, size);
	}

	@GetMapping("/search")
	public ResponseEntity<Page<TweetEntity>> searchTweets(@RequestParam(required = false) String tweetMessage,
			@RequestParam(required = false) String topic, @RequestParam(required = false) String createdBy,
			@RequestParam(required = false) String startDateTime, @RequestParam(required = false) String endDateTime,
			@RequestParam(required = false) String tag, @RequestParam(required = false) String repliedToTweet,
			@RequestParam(required = false) Set<String> likedBy, @RequestParam(required = false) String sortField,
			@RequestParam(required = false) String sortOrder, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) throws TweetServiceException {

		TweetSearchDto tweetSearchDto = TweetSearchDto.builder().tweetMessage(tweetMessage).tweetTopic(topic)
				.createdBy(createdBy)
				.startDateTime(
						startDateTime != null
								? LocalDateTime.parse(startDateTime,
										DateTimeFormatter.ofPattern(TweetAppConstants.DATE_TIME_PATTERN))
								: null)
				.endDateTime(
						endDateTime != null
								? LocalDateTime.parse(endDateTime,
										DateTimeFormatter.ofPattern(TweetAppConstants.DATE_TIME_PATTERN))
								: null)
				.tag(tag).repliedToTweet(repliedToTweet).likedBy(likedBy).sortField(sortField).sortOrder(sortOrder)
				.build();
		return tweetService.searchTweets(tweetSearchDto, page, size);
	}

	// User related methods

	@GetMapping("/users/all")
	public ResponseEntity<Page<UserEntity>> getAllUsers(@RequestParam(required = false) String sortField,
			@RequestParam(required = false) String sortOrder, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) throws TweetServiceException {
		UserSearchDto userSearchDto = UserSearchDto.builder().sortField(sortField).sortOrder(sortOrder).build();

		return userService.getAllUsers(userSearchDto, page, size);
	}

	@GetMapping("/users/search/{username}")
	public ResponseEntity<Page<UserEntity>> getUser(@PathVariable("username") String userId,
			@RequestParam(required = false) String sortField, @RequestParam(required = false) String sortOrder,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size)
			throws TweetServiceException {
		UserSearchDto userSearchDto = UserSearchDto.builder().userId(userId).sortField(sortField).sortOrder(sortOrder)
				.build();
		return userService.searchUsers(userSearchDto, page, size);
	}

	@GetMapping("/users/search")
	public ResponseEntity<Page<UserEntity>> searchUsers(@RequestParam(required = false) String userId,
			@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName,
			@RequestParam(required = false) String email, @RequestParam(required = false) String sortField,
			@RequestParam(required = false) String sortOrder, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) throws TweetServiceException {
		UserSearchDto userSearchDto = UserSearchDto.builder().userId(userId).firstName(firstName).lastName(lastName)
				.email(email).sortField(sortField).sortOrder(sortOrder).build();
		return userService.searchUsers(userSearchDto, page, size);
	}
}
