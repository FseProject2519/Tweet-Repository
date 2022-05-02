package com.tweetapp.tweetservice.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

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

	@Override
	public String createTweet(TweetDto tweetDto) {
		TweetEntity tweetEntity = modelMapper.map(tweetDto, TweetEntity.class);
		return tweetRepository.save(tweetEntity) != null ? "Tweet Created Successfully" : "Tweet Creation Failed";
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
