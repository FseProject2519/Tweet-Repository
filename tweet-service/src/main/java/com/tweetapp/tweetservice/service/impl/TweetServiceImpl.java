package com.tweetapp.tweetservice.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.tweetapp.tweetservice.dto.TweetDto;
import com.tweetapp.tweetservice.dto.TweetExportDto;
import com.tweetapp.tweetservice.dto.TweetSearchDto;
import com.tweetapp.tweetservice.entity.TweetEntity;
import com.tweetapp.tweetservice.entity.TweetTrendEntity;
import com.tweetapp.tweetservice.exception.TweetServiceException;
import com.tweetapp.tweetservice.repository.TweetRepository;
import com.tweetapp.tweetservice.service.TweetService;
import com.tweetapp.tweetservice.service.UserService;

@Component
public class TweetServiceImpl implements TweetService {

	private static final String TWEET_NOT_FOUND = "Tweet Not Found";

	private TweetRepository tweetRepository;

	@Autowired
	ModelMapper modelMapper;

	UserService userService;

	// Example for setter based injection
	@Autowired
	void setTweetRepository(TweetRepository tweetRepository) {
		this.tweetRepository = tweetRepository;
	}

	@Override
	public String postTweet(String username, TweetDto tweetDto) throws TweetServiceException {
		try {
			TweetEntity tweetEntity = modelMapper.map(tweetDto, TweetEntity.class);
			tweetEntity.setCreatedBy(username);
			tweetEntity.setCreatedDateTime(LocalDateTime.now());
			tweetEntity.setLastModifiedDateTime(LocalDateTime.now());
			tweetEntity.setHashtags(parseMessageForTags(tweetEntity.getTweetMessage(), "#"));
			tweetEntity.setUsertags(parseMessageForTags(tweetEntity.getTweetMessage(), "@"));
			return tweetRepository.save(tweetEntity).getId() != null ? "Tweet Posted Successfully"
					: "Tweet Post Failed";

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
				tweetEntity.setHashtags(parseMessageForTags(tweetEntity.getTweetMessage(), "#"));
				tweetEntity.setUsertags(parseMessageForTags(tweetEntity.getTweetMessage(), "@"));
				return tweetRepository.save(tweetEntity).getId() != null ? "Tweet Updated Successfully"
						: "Tweet Update Failed";
			} else {
				return TWEET_NOT_FOUND;
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
				return TWEET_NOT_FOUND;
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
				return tweetRepository.save(tweet.get()).getId() != null ? "Tweet Liked Successfully"
						: "Tweet Like Failed";
			} else {
				return TWEET_NOT_FOUND;
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
			tweetEntity.setHashtags(parseMessageForTags(tweetEntity.getTweetMessage(), "#"));
			tweetEntity.setUsertags(parseMessageForTags(tweetEntity.getTweetMessage(), "@"));
			return tweetRepository.save(tweetEntity).getId() != null
					? "Replied to TweetId: " + tweetId + " Successfully"
					: "Replied to TweetId: " + tweetId + " Failed";

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());
		}
	}

	@Override
	public Page<TweetEntity> getAllTweetsPaged(TweetSearchDto tweetSearchDto, Integer page, Integer size)
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
	public List<TweetEntity> getAllTweets(TweetSearchDto tweetSearchDto) throws TweetServiceException {
		try {
			return tweetRepository.findAllByOrderById();

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}

	@Override
	public Page<TweetEntity> searchTweetsPaged(TweetSearchDto tweetSearchDto, Integer page, Integer size)
			throws TweetServiceException {
		try {
			Sort sort = getTweetSort(tweetSearchDto);
			Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10, sort);
			return tweetRepository.searchTweetsPaged(tweetSearchDto, pageable);
		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}

	@Override
	public List<TweetEntity> searchTweets(TweetSearchDto tweetSearchDto) throws TweetServiceException {
		try {
			return tweetRepository.searchTweets(tweetSearchDto);
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

	@Override
	public List<String> getHashtags() throws TweetServiceException {
		try {
			List<List<String>> hashtagList = tweetRepository.getHashtags();

			Set<String> hashtagsSet = hashtagList.stream().flatMap(List::stream).collect(Collectors.toList()).stream()
					.filter(hashtag -> !StringUtils.isEmpty(hashtag)).map(hashtag -> hashtag.substring(1))
					.collect(Collectors.toSet());
			List<String> hashtags = new ArrayList<>(hashtagsSet);
			Collections.sort(hashtags);
			return hashtags;

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}

	@Override
	public List<TweetExportDto> getExportData(String username) throws TweetServiceException {
		try {
			List<TweetExportDto> tweetExportList = new ArrayList<>();
			List<TweetEntity> tweetEntityList = tweetRepository.getExportData(username);

			if (!tweetEntityList.isEmpty()) {

				tweetEntityList.forEach(tweetEntity -> {

					String repliedToTweetMsg = null;
					String repliedToTweetUser = null;
					String likedBy = null;

					if (tweetEntity.getRepliedToTweet() != null) {
						Optional<TweetEntity> replyTweetOriginalOptional = tweetRepository
								.findById(tweetEntity.getRepliedToTweet());
						if (replyTweetOriginalOptional.isPresent()) {
							repliedToTweetMsg = replyTweetOriginalOptional.get().getTweetMessage();
							repliedToTweetUser = replyTweetOriginalOptional.get().getCreatedBy();
						}
					}
					if (tweetEntity.getLikedBy() != null) {
						likedBy = String.join(",", tweetEntity.getLikedBy());

					}
					tweetExportList.add(TweetExportDto.builder().tweetMessage(tweetEntity.getTweetMessage())
							.tweetTopic(tweetEntity.getTweetTopic()).createdDateTime(tweetEntity.getCreatedDateTime())
							.lastModifiedDateTime(tweetEntity.getLastModifiedDateTime()).likedBy(likedBy)
							.repliedToTweetMsg(repliedToTweetMsg).repliedToTweetUser(repliedToTweetUser).build());

				});
			}
			return tweetExportList;
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

	private Set<String> parseMessageForTags(String tweetMessage, String delimiter) {
		List<String> tweetWordList = Arrays.asList(tweetMessage.split(" "));
		return tweetWordList.stream().filter(word -> word.matches(delimiter + "[a-zA-Z0-9_]+") && word.length() <= 50)
				.map(word -> word.substring(1)).collect(Collectors.toSet());
	}

}
