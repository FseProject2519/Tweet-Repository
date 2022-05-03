package com.tweetapp.tweetservice.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tweetapp.tweetservice.dto.TweetDto;
import com.tweetapp.tweetservice.entity.TweetEntity;
import com.tweetapp.tweetservice.repository.TweetRepository;
import com.tweetapp.tweetservice.service.TweetService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TweetServiceImpl implements TweetService {

	@Autowired
	TweetRepository tweetRepository;

	@Autowired
	ModelMapper modelMapper;

	@SuppressWarnings("deprecation")
	@Override
	public String postTweet(String username,TweetDto tweetDto) {

		TweetEntity tweetEntity = modelMapper.map(tweetDto, TweetEntity.class);
		tweetEntity.setCreatedBy(username);
		tweetEntity.setCreatedDateTime(LocalDateTime.now());
		if(!StringUtils.isEmpty(tweetDto.getTweetMessage())) {
		return tweetRepository.save(tweetEntity) != null ? "Tweet Posted Successfully" : "Tweet Post Failed";
		}
		else {
			return "Please enter a message to post your tweet";
		}
	}

	@Override
	public String updateTweet(TweetDto tweetDto,String tweetId) {
		
		TweetEntity tweetEntity = modelMapper.map(tweetDto, TweetEntity.class);
		Optional<TweetEntity> tweet=tweetRepository.findById(tweetId);
		if(tweet.isPresent()) {
			tweetEntity.setId(tweetId);
			tweetEntity.setCreatedDateTime(LocalDateTime.now());
			return tweetRepository.save(tweetEntity) != null ? "Tweet Updated Successfully" : "Tweet Update Failed";
		}
		else {
			return "Tweet Not Found";
		}
		
	}

	@Override
	public String deleteTweet(String tweetId) {
		Optional<TweetEntity> tweet=tweetRepository.findById(tweetId);
		if(tweet.isPresent()) {
		tweetRepository.deleteById(tweetId);
		return "Tweet deleted Successfully";
		}
		else {
			return "Tweet Not Found";
		}
		
	}

	@Override
	public String likeTweet(String tweetId, String username) {
		Optional<TweetEntity> tweet=tweetRepository.findById(tweetId);
		if(tweet.isPresent()) {
			tweet.get().getLikedBy().add(username);
			return tweetRepository.save(tweet.get()) != null ? "Tweet Liked Successfully" : "Tweet Like Failed";
		}
		else {
			return "Tweet Not Found";
		}
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public String replyToTweet(String username,TweetDto tweetDto,String tweetId) {
		TweetEntity tweetEntity = modelMapper.map(tweetDto, TweetEntity.class);
		tweetEntity.setCreatedBy(username);
		tweetEntity.setRepliedToTweet(tweetId);
		tweetEntity.setCreatedDateTime(LocalDateTime.now());
		if(!StringUtils.isEmpty(tweetDto.getTweetMessage())) {
		return tweetRepository.save(tweetEntity) != null?"Replied to TweetId:"+tweetId+"Successfully":"Replied to TweetId:"+tweetId+" Failed";
		}
		else {
			return "Please enter a message to post your tweet";
		}
	}
	
}
