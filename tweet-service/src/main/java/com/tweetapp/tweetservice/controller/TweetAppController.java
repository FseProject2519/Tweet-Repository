package com.tweetapp.tweetservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.tweetservice.dto.TweetDto;
import com.tweetapp.tweetservice.service.TweetService;

@RequestMapping("api/v1.0/tweets")
@RestController
public class TweetAppController {

	@Autowired
	TweetService tweetService;

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

}
