package com.tweetapp.tweetservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.tweetapp.tweetservice.dto.TweetDto;
import com.tweetapp.tweetservice.dto.TweetSearchDto;
import com.tweetapp.tweetservice.entity.TweetEntity;
import com.tweetapp.tweetservice.entity.TweetTrendEntity;
import com.tweetapp.tweetservice.exception.TweetServiceException;
import com.tweetapp.tweetservice.repository.TweetRepository;

@ExtendWith(MockitoExtension.class)
class TweetServiceImplTest {

	private static final String TEST = "TEST";

	private static final String VALID_TWEET_MESSAGE = "TEST #TEST";

	@InjectMocks
	TweetServiceImpl tweetServiceImpl;

	@Mock
	TweetRepository tweetRepository;

	@Mock
	ModelMapper modelMapper;

	private List<TweetTrendEntity> getTrendingTopics() {
		List<TweetTrendEntity> trendingTopicsList = new ArrayList<>();
		trendingTopicsList.add(TweetTrendEntity.builder().tweetTopic("TEST1").count(10L).build());
		trendingTopicsList.add(TweetTrendEntity.builder().tweetTopic("TEST2").count(2L).build());
		return trendingTopicsList;
	}

	private TweetDto getTweetDto() {
		return TweetDto.builder().createdBy(TEST).tweetMessage(TEST).createdDateTime(LocalDateTime.now())
				.likedBy(new HashSet<>()).tweetTopic(TEST).build();

	}

	private Page<TweetEntity> getTweetEntityPage() {
		List<TweetEntity> tweetEntityList = getTweetEntityList();
		return new PageImpl<>(tweetEntityList);

	}

	private TweetEntity getTweetEntity() {
		Set<String> likedBy = new HashSet<>();
		likedBy.add(TEST);
		TweetEntity tweetEntity = TweetEntity.builder().id(TEST).createdBy(TEST).tweetMessage(VALID_TWEET_MESSAGE)
				.repliedToTweet(TEST).createdDateTime(LocalDateTime.now()).likedBy(likedBy).tweetTopic(TEST).build();
		return tweetEntity;
	}

	private TweetSearchDto buildTweetSearchDto(String sortField, String sortOrder) {
		return TweetSearchDto.builder().createdBy(TEST).tweetMessage(TEST).startDateTime(LocalDateTime.now())
				.endDateTime(LocalDateTime.now()).likedBy(new HashSet<>()).sortField(sortField).sortOrder(sortOrder)
				.tweetTopic(TEST).build();

	}

	private List<TweetEntity> getTweetEntityList() {
		TweetEntity tweetEntity = getTweetEntity();
		List<TweetEntity> tweetEntityList = new ArrayList<>();
		tweetEntityList.add(tweetEntity);
		return tweetEntityList;
	}

	private List<List<String>> getHashtags() {
		List<String> hashtags = new ArrayList<>();
		hashtags.add(TEST);
		hashtags.add("");
		List<List<String>> hashtagList = new ArrayList<>();
		hashtagList.add(hashtags);
		return hashtagList;
	}

	@Test
	void testSetterSuccess() throws TweetServiceException {
		tweetServiceImpl.setTweetRepository(tweetRepository);
		assertNotNull(tweetServiceImpl.getTweetRepository());

	}

	@Test
	void testGetAllTweetsSuccess() throws TweetServiceException {
		when(tweetRepository.findAllByOrderById()).thenReturn(getTweetEntityList());
		assertEquals(VALID_TWEET_MESSAGE, tweetServiceImpl.getAllTweets().get(0).getTweetMessage());

	}

	@Test
	void testGetAllTweetsException() throws TweetServiceException {
		when(tweetRepository.findAllByOrderById()).thenThrow(new RuntimeException(TEST));
		assertThrows(TweetServiceException.class, () -> {
			tweetServiceImpl.getAllTweets();
		});
	}

	@Test
	void testGetAllTweetsPagedSuccess() throws TweetServiceException {
		TweetSearchDto tweetSearchDto = buildTweetSearchDto("createdBy", "asc");
		when(tweetRepository.findAll(isA(Pageable.class))).thenReturn(getTweetEntityPage());
		assertEquals(VALID_TWEET_MESSAGE,
				tweetServiceImpl.getAllTweetsPaged(tweetSearchDto, 0, 1).getContent().get(0).getTweetMessage());

	}

	@Test
	void testGetAllTweetsPagedSuccessWithNullSortParams() throws TweetServiceException {
		TweetSearchDto tweetSearchDto = buildTweetSearchDto(null, null);
		when(tweetRepository.findAll(isA(Pageable.class))).thenReturn(getTweetEntityPage());
		assertEquals(VALID_TWEET_MESSAGE,
				tweetServiceImpl.getAllTweetsPaged(tweetSearchDto, null, null).getContent().get(0).getTweetMessage());

	}

	@Test
	void testGetAllTweetsPagedException() throws TweetServiceException {
		TweetSearchDto tweetSearchDto = null;
		assertThrows(TweetServiceException.class, () -> {
			tweetServiceImpl.getAllTweetsPaged(tweetSearchDto, null, null);
		});
	}

	@Test
	void testSearchTweetsPagedSuccess() throws TweetServiceException {
		TweetSearchDto tweetSearchDto = buildTweetSearchDto("createdBy", "desc");
		when(tweetRepository.searchTweetsPaged(any(), isA(Pageable.class))).thenReturn(getTweetEntityPage());
		assertEquals(VALID_TWEET_MESSAGE,
				tweetServiceImpl.searchTweetsPaged(tweetSearchDto, 0, 1).getContent().get(0).getTweetMessage());

	}

	@Test
	void testSearchTweetsPagedSuccessWithNullSortField() throws TweetServiceException {
		TweetSearchDto tweetSearchDto = buildTweetSearchDto(null, "desc");
		when(tweetRepository.searchTweetsPaged(any(), isA(Pageable.class))).thenReturn(getTweetEntityPage());
		assertEquals(VALID_TWEET_MESSAGE,
				tweetServiceImpl.searchTweetsPaged(tweetSearchDto, 0, 1).getContent().get(0).getTweetMessage());

	}

	@Test
	void testSearchTweetsPagedSuccessWithNullSortOrder() throws TweetServiceException {
		TweetSearchDto tweetSearchDto = buildTweetSearchDto("createdBy", null);
		when(tweetRepository.searchTweetsPaged(any(), isA(Pageable.class))).thenReturn(getTweetEntityPage());
		assertEquals(VALID_TWEET_MESSAGE,
				tweetServiceImpl.searchTweetsPaged(tweetSearchDto, null, null).getContent().get(0).getTweetMessage());

	}

	@Test
	void testSearchTweetsPagedException() throws TweetServiceException {
		TweetSearchDto tweetSearchDto = null;
		assertThrows(TweetServiceException.class, () -> {
			tweetServiceImpl.searchTweetsPaged(tweetSearchDto, null, null);
		});
	}

	@Test
	void testSearchTweetsSuccess() throws TweetServiceException {
		TweetSearchDto tweetSearchDto = buildTweetSearchDto("createdBy", "desc");
		when(tweetRepository.searchTweets(tweetSearchDto)).thenReturn(getTweetEntityList());
		assertEquals(VALID_TWEET_MESSAGE, tweetServiceImpl.searchTweets(tweetSearchDto).get(0).getTweetMessage());

	}

	@Test
	void testSearchTweetsException() throws TweetServiceException {
		TweetSearchDto tweetSearchDto = null;
		when(tweetRepository.searchTweets(tweetSearchDto)).thenThrow(new RuntimeException(TEST));
		assertThrows(Exception.class, () -> {
			tweetServiceImpl.searchTweets(tweetSearchDto);
		});
	}

	@Test
	void testPostTweetSuccess() throws TweetServiceException {
		TweetDto tweetDto = getTweetDto();
		String username = TEST;
		when(modelMapper.map(tweetDto, TweetEntity.class)).thenReturn(getTweetEntity());
		when(tweetRepository.save(any())).thenReturn(getTweetEntity());
		assertEquals("Tweet Posted Successfully", tweetServiceImpl.postTweet(username, tweetDto));

	}

	@Test
	void testPostTweetFailure() throws TweetServiceException {
		TweetDto tweetDto = getTweetDto();
		String username = TEST;
		when(modelMapper.map(tweetDto, TweetEntity.class)).thenReturn(getTweetEntity());
		TweetEntity tweetEntity = getTweetEntity();
		tweetEntity.setId(null);
		when(tweetRepository.save(any())).thenReturn(tweetEntity);
		assertEquals("Tweet Post Failed", tweetServiceImpl.postTweet(username, tweetDto));

	}

	@Test
	void testPostTweetLongTagTweet() throws TweetServiceException {
		TweetDto tweetDto = getTweetDto();
		String longTweet = "#";
		for (int i = 0; i < 55; i++) {
			longTweet += "REALLYLONGTWEET";
		}
		tweetDto.setTweetMessage(longTweet);
		String username = TEST;
		when(modelMapper.map(tweetDto, TweetEntity.class)).thenReturn(getTweetEntity());
		TweetEntity tweetEntity = getTweetEntity();
		tweetEntity.setTweetMessage(longTweet);
		when(tweetRepository.save(any())).thenReturn(tweetEntity);
		assertEquals("Tweet Posted Successfully", tweetServiceImpl.postTweet(username, tweetDto));

	}

	@Test
	void testPostTweetValidTagTweet() throws TweetServiceException {
		TweetDto tweetDto = getTweetDto();
		tweetDto.setTweetMessage("#TEST");
		String username = TEST;
		when(modelMapper.map(tweetDto, TweetEntity.class)).thenReturn(getTweetEntity());
		TweetEntity tweetEntity = getTweetEntity();
		tweetEntity.setTweetMessage("#TEST");
		when(tweetRepository.save(any())).thenReturn(tweetEntity);
		assertEquals("Tweet Posted Successfully", tweetServiceImpl.postTweet(username, tweetDto));

	}

	@Test
	void testPostTweetException() throws TweetServiceException {
		TweetDto tweetDto = getTweetDto();
		String username = TEST;
		when(modelMapper.map(tweetDto, TweetEntity.class)).thenThrow(new RuntimeException());
		assertThrows(TweetServiceException.class, () -> {
			tweetServiceImpl.postTweet(username, tweetDto);
		});
	}

	@Test
	void testUpdateTweetSuccess() throws TweetServiceException {
		TweetDto tweetDto = getTweetDto();
		String tweetId = TEST;
		when(modelMapper.map(tweetDto, TweetEntity.class)).thenReturn(getTweetEntity());
		when(tweetRepository.findById(any())).thenReturn(Optional.of(getTweetEntity()));
		when(tweetRepository.save(any())).thenReturn(getTweetEntity());

		assertEquals("Tweet Updated Successfully", tweetServiceImpl.updateTweet(tweetDto, tweetId));

	}

	@Test
	void testUpdateTweetFailure() throws TweetServiceException {
		TweetDto tweetDto = getTweetDto();
		String tweetId = TEST;
		when(modelMapper.map(tweetDto, TweetEntity.class)).thenReturn(getTweetEntity());
		when(tweetRepository.findById(any())).thenReturn(Optional.of(getTweetEntity()));
		TweetEntity tweetEntity = getTweetEntity();
		tweetEntity.setId(null);
		when(tweetRepository.save(any())).thenReturn(tweetEntity);
		assertEquals("Tweet Update Failed", tweetServiceImpl.updateTweet(tweetDto, tweetId));

	}

	@Test
	void testUpdateTweetNotFoundFailure() throws TweetServiceException {
		TweetDto tweetDto = getTweetDto();
		String tweetId = TEST;
		when(modelMapper.map(tweetDto, TweetEntity.class)).thenReturn(getTweetEntity());

		assertEquals("Tweet Not Found", tweetServiceImpl.updateTweet(tweetDto, tweetId));

	}

	@Test
	void testUpdateTweetException() throws TweetServiceException {
		TweetDto tweetDto = getTweetDto();
		String tweetId = TEST;
		when(modelMapper.map(tweetDto, TweetEntity.class)).thenThrow(new RuntimeException());
		assertThrows(TweetServiceException.class, () -> {
			tweetServiceImpl.updateTweet(tweetDto, tweetId);
		});
	}

	@Test
	void testDeleteTweetSuccess() throws TweetServiceException {
		String tweetId = TEST;
		when(tweetRepository.findById(any())).thenReturn(Optional.of(getTweetEntity()));

		assertEquals("Tweet Deleted Successfully", tweetServiceImpl.deleteTweet(tweetId));

	}

	@Test
	void testDeleteTweetNotFoundFailure() throws TweetServiceException {
		String tweetId = TEST;

		assertEquals("Tweet Not Found", tweetServiceImpl.deleteTweet(tweetId));

	}

	@Test
	void testDeleteTweetException() throws TweetServiceException {
		String tweetId = TEST;
		when(tweetRepository.findById(any())).thenThrow(new RuntimeException());
		assertThrows(TweetServiceException.class, () -> {
			tweetServiceImpl.deleteTweet(tweetId);
		});
	}

	@Test
	void testLikeTweetSuccess() throws TweetServiceException {
		String tweetId = TEST;
		String username = TEST;
		when(tweetRepository.findById(any())).thenReturn(Optional.of(getTweetEntity()));
		when(tweetRepository.save(any())).thenReturn(getTweetEntity());

		assertEquals("Tweet Liked Successfully", tweetServiceImpl.likeTweet(tweetId, username));

	}

	@Test
	void testLikeTweetFailure() throws TweetServiceException {
		String tweetId = TEST;
		String username = TEST;
		when(tweetRepository.findById(any())).thenReturn(Optional.of(getTweetEntity()));
		TweetEntity tweetEntity = getTweetEntity();
		tweetEntity.setId(null);
		when(tweetRepository.save(any())).thenReturn(tweetEntity);
		assertEquals("Tweet Like Failed", tweetServiceImpl.likeTweet(tweetId, username));

	}

	@Test
	void testLikeTweetNotFoundFailure() throws TweetServiceException {
		String tweetId = TEST;
		String username = TEST;

		assertEquals("Tweet Not Found", tweetServiceImpl.likeTweet(tweetId, username));

	}

	@Test
	void testLikeTweetException() throws TweetServiceException {
		String tweetId = TEST;
		String username = TEST;
		when(tweetRepository.findById(any())).thenThrow(new RuntimeException());

		assertThrows(TweetServiceException.class, () -> {
			tweetServiceImpl.likeTweet(tweetId, username);
		});
	}

	@Test
	void testReplyToTweetSuccess() throws TweetServiceException {
		TweetDto tweetDto = getTweetDto();
		String username = TEST;
		String tweetId = TEST;

		when(modelMapper.map(tweetDto, TweetEntity.class)).thenReturn(getTweetEntity());
		when(tweetRepository.save(any())).thenReturn(getTweetEntity());

		assertEquals("Replied to TweetId: TEST Successfully",
				tweetServiceImpl.replyToTweet(username, tweetDto, tweetId));

	}

	@Test
	void testReplyToTweetFailure() throws TweetServiceException {
		TweetDto tweetDto = getTweetDto();
		String username = TEST;
		String tweetId = TEST;

		when(modelMapper.map(tweetDto, TweetEntity.class)).thenReturn(getTweetEntity());
		TweetEntity tweetEntity = getTweetEntity();
		tweetEntity.setId(null);
		when(tweetRepository.save(any())).thenReturn(tweetEntity);
		assertEquals("Replied to TweetId: TEST Failed", tweetServiceImpl.replyToTweet(username, tweetDto, tweetId));

	}

	@Test
	void testReplyToTweetException() throws TweetServiceException {
		TweetDto tweetDto = getTweetDto();
		String username = TEST;
		String tweetId = TEST;

		when(modelMapper.map(tweetDto, TweetEntity.class)).thenThrow(new RuntimeException());

		assertThrows(TweetServiceException.class, () -> {
			tweetServiceImpl.replyToTweet(username, tweetDto, tweetId);
		});
	}

	@Test
	void testGetTrendingTopicsSuccess() throws TweetServiceException {

		when(tweetRepository.getTrendingTopics(any())).thenReturn(getTrendingTopics());

		assertEquals("TEST2", tweetServiceImpl.getTrendingTopics(new TweetSearchDto()).get(1).getTweetTopic());

	}

	@Test
	void testGetTrendingTopicsException() throws TweetServiceException {

		when(tweetRepository.getTrendingTopics(any())).thenThrow(new RuntimeException());

		assertThrows(TweetServiceException.class, () -> {
			tweetServiceImpl.getTrendingTopics(new TweetSearchDto());
		});
	}

	@Test
	void testGetHashtagsSuccess() throws TweetServiceException {

		when(tweetRepository.getHashtags()).thenReturn(getHashtags());

		assertEquals(TEST, tweetServiceImpl.getHashtags().get(0));

	}

	@Test
	void testGetHashtagsException() throws TweetServiceException {

		List<String> hashtags = null;
		List<List<String>> hashtagList = new ArrayList<>();
		hashtagList.add(hashtags);

		when(tweetRepository.getHashtags()).thenReturn(hashtagList);

		assertThrows(TweetServiceException.class, () -> {
			tweetServiceImpl.getHashtags();
		});

	}

	@Test
	void testGetExportDataSuccess() throws TweetServiceException {
		when(tweetRepository.getExportData(anyString())).thenReturn(getTweetEntityList());
		when(tweetRepository.findById(anyString())).thenReturn(Optional.of(getTweetEntity()));
		assertEquals(VALID_TWEET_MESSAGE, tweetServiceImpl.getExportData(TEST).get(0).getTweetMessage());

	}

	@Test
	void testGetExportDataNullValues() throws TweetServiceException {
		List<TweetEntity> tweetEntityList = getTweetEntityList();
		tweetEntityList.get(0).setRepliedToTweet(null);
		tweetEntityList.get(0).setLikedBy(null);

		when(tweetRepository.getExportData(anyString())).thenReturn(tweetEntityList);
		assertEquals(VALID_TWEET_MESSAGE, tweetServiceImpl.getExportData(TEST).get(0).getTweetMessage());

	}

	@Test
	void testGetExportDataRepliedToNotFound() throws TweetServiceException {
		when(tweetRepository.getExportData(anyString())).thenReturn(getTweetEntityList());
		when(tweetRepository.findById(anyString())).thenReturn(Optional.empty());
		assertEquals(VALID_TWEET_MESSAGE, tweetServiceImpl.getExportData(TEST).get(0).getTweetMessage());

	}

	@Test
	void testGetExportDataNotFound() throws TweetServiceException {
		when(tweetRepository.getExportData(anyString())).thenReturn(new ArrayList<>());
		assertEquals(0, tweetServiceImpl.getExportData(TEST).size());

	}

	@Test
	void testGetExportDataException() throws TweetServiceException {

		when(tweetRepository.getExportData(anyString())).thenReturn(null);

		assertThrows(TweetServiceException.class, () -> {
			tweetServiceImpl.getExportData(TEST);
		});

	}

}
