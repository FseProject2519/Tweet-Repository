package com.tweetapp.tweetservice.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tweetapp.tweetservice.dto.TweetDto;
import com.tweetapp.tweetservice.dto.TweetSearchDto;
import com.tweetapp.tweetservice.entity.TweetEntity;
import com.tweetapp.tweetservice.exception.TweetServiceException;
import com.tweetapp.tweetservice.repository.TweetRepository;
import com.tweetapp.tweetservice.service.TweetService;

@Component
public class TweetServiceImpl implements TweetService {

	@Autowired
	private TweetRepository tweetRepository;

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


	@Override
	public ResponseEntity<Page<TweetEntity>> getAllTweets(TweetSearchDto tweetSearchDto, Integer page, Integer size)
			throws TweetServiceException {
		try {
			Sort sort = getTweetSort(tweetSearchDto);
			Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10, sort);
			return new ResponseEntity<>(tweetRepository.findAll(pageable), HttpStatus.OK);

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<Page<TweetEntity>> searchTweets(TweetSearchDto tweetSearchDto, Integer page, Integer size)
			throws TweetServiceException {
		try {
			Sort sort = getTweetSort(tweetSearchDto);
			Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10, sort);
			return new ResponseEntity<>(tweetRepository.searchTweets(tweetSearchDto, pageable), HttpStatus.OK);
		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}

	private Sort getTweetSort(TweetSearchDto tweetSearchDto) throws TweetServiceException {
		try {
			Sort sort = null;
			if (tweetSearchDto.getSortField() == null && tweetSearchDto.getSortOrder() == null) {
				sort = Sort.by("createdDateTime").descending();
			} else {
				sort = tweetSearchDto.getSortField() != null ? Sort.by(tweetSearchDto.getSortField())
						: Sort.by("createdDateTime");
				sort = tweetSearchDto.getSortOrder() != null && "desc".equalsIgnoreCase(tweetSearchDto.getSortOrder())
						? sort.descending()
						: sort.ascending();
			}
			return sort;
		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());
		}
	}

}
