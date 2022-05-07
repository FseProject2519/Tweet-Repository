package com.tweetapp.tweetservice.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.tweetapp.tweetservice.dto.TweetDto;
import com.tweetapp.tweetservice.dto.TweetSearchDto;
import com.tweetapp.tweetservice.entity.TweetEntity;
import com.tweetapp.tweetservice.entity.TweetTrendEntity;
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
	public String postTweet(String username, TweetDto tweetDto) throws TweetServiceException {
		try {
			TweetEntity tweetEntity = modelMapper.map(tweetDto, TweetEntity.class);
			tweetEntity.setCreatedBy(username);
			tweetEntity.setCreatedDateTime(LocalDateTime.now());
			tweetEntity.setLastModifiedDateTime(LocalDateTime.now());
			return tweetRepository.save(tweetEntity) != null ? "Tweet Posted Successfully" : "Tweet Post Failed";

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());
		}
	}

	@Override
	public String updateTweet(TweetDto tweetDto, String tweetId) throws TweetServiceException {
		try {
			TweetEntity tweetEntity = modelMapper.map(tweetDto, TweetEntity.class);
			Optional<TweetEntity> tweet = tweetRepository.findById(tweetId);
			if (tweet.isPresent()) {
				tweetEntity.setId(tweetId);
				tweetEntity.setLastModifiedDateTime(LocalDateTime.now());
				return tweetRepository.save(tweetEntity) != null ? "Tweet Updated Successfully" : "Tweet Update Failed";
			} else {
				return "Tweet Not Found";
			}

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());
		}
	}

	@Override
	public String deleteTweet(String tweetId) throws TweetServiceException {
		try {
			Optional<TweetEntity> tweet = tweetRepository.findById(tweetId);
			if (tweet.isPresent()) {
				tweetRepository.deleteById(tweetId);
				return "Tweet deleted Successfully";
			} else {
				return "Tweet Not Found";
			}

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());
		}
	}

	@Override
	public String likeTweet(String tweetId, String username) throws TweetServiceException {
		try {
			Optional<TweetEntity> tweet = tweetRepository.findById(tweetId);
			if (tweet.isPresent()) {
				tweet.get().getLikedBy().add(username);
				return tweetRepository.save(tweet.get()) != null ? "Tweet Liked Successfully" : "Tweet Like Failed";
			} else {
				return "Tweet Not Found";
			}

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());
		}
	}

	@Override
	public String replyToTweet(String username, TweetDto tweetDto, String tweetId) throws TweetServiceException {
		try {
			TweetEntity tweetEntity = modelMapper.map(tweetDto, TweetEntity.class);
			tweetEntity.setCreatedBy(username);
			tweetEntity.setRepliedToTweet(tweetId);
			tweetEntity.setCreatedDateTime(LocalDateTime.now());
			tweetEntity.setLastModifiedDateTime(LocalDateTime.now());
			return tweetRepository.save(tweetEntity) != null ? "Replied to TweetId: " + tweetId + " Successfully"
					: "Replied to TweetId: " + tweetId + " Failed";

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());
		}
	}

	@Override
	public Page<TweetEntity> getAllTweets(TweetSearchDto tweetSearchDto, Integer page, Integer size)
			throws TweetServiceException {
		try {
			Sort sort = getTweetSort(tweetSearchDto);
			Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10, sort);
			return tweetRepository.findAll(pageable);

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());
		}
	}

	@Override
	public Page<TweetEntity> searchTweets(TweetSearchDto tweetSearchDto, Integer page, Integer size)
			throws TweetServiceException {
		try {
			Sort sort = getTweetSort(tweetSearchDto);
			Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10, sort);
			return tweetRepository.searchTweets(tweetSearchDto, pageable);
		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}

	@Override
	public List<TweetTrendEntity> getTrendingTopics(TweetSearchDto tweetSearchDto) throws TweetServiceException {
		try {
			return tweetRepository.getTrendingTopics(tweetSearchDto);
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
