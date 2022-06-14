package com.tweetapp.tweetservice.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.tweetservice.dto.TweetDto;
import com.tweetapp.tweetservice.dto.TweetSearchDto;
import com.tweetapp.tweetservice.dto.UserSearchDto;
import com.tweetapp.tweetservice.entity.TweetEntity;
import com.tweetapp.tweetservice.entity.TweetTrendEntity;
import com.tweetapp.tweetservice.entity.UserEntity;
import com.tweetapp.tweetservice.exception.TweetServiceException;
import com.tweetapp.tweetservice.exception.handler.AuthHandlerInterceptor;
import com.tweetapp.tweetservice.repository.TweetRepository;
import com.tweetapp.tweetservice.repository.UserRepository;
import com.tweetapp.tweetservice.service.TweetService;
import com.tweetapp.tweetservice.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(TweetAppController.class)
@ImportAutoConfiguration({ FeignAutoConfiguration.class })
class TweetAppControllerTest {

	private static final String TWEET_ID = "6288e82a2b6c4c684f0e2033";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TweetService tweetService;

	@MockBean
	private UserService userService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private TweetRepository tweetRepository;

	@MockBean
	private MongoTemplate mongoTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	AuthHandlerInterceptor authHandlerInterceptor;

	private static String PATH = "/api/v1.0/tweets";

	private static final String TEST = "test";

	private static final String VALID_TWEET_MESSAGE = "TEST #TEST";

	private static final String TOKEN = "TEST";

	private List<String> getUsertags() {
		List<String> usertags = new ArrayList<>();
		usertags.add(TEST);
		return usertags;
	}

	private Page<UserEntity> getPagedUserEntity() {
		return new PageImpl<>(getUserEntityList());

	}

	private List<UserEntity> getUserEntityList() {
		List<UserEntity> userEntityList = new ArrayList<>();
		userEntityList.add(getUserEntity());
		return userEntityList;
	}

	private UserEntity getUserEntity() {
		UserEntity userEntity = UserEntity.builder().userId(TEST).firstName(TEST).lastName(TEST).email(TEST)
				.contactNumber(TEST).build();
		return userEntity;
	}

	private UserSearchDto buildUserSearchDto(String sortField, String sortOrder) {
		return UserSearchDto.builder().userId("TEST").firstName("TEST").lastName("TEST").email("TEST")
				.sortField(sortField).sortOrder(sortOrder).build();

	}

	private List<TweetTrendEntity> getTrendingTopics() {
		List<TweetTrendEntity> trendingTopicsList = new ArrayList<>();
		trendingTopicsList.add(TweetTrendEntity.builder().tweetTopic(TEST).count(10L).build());
		return trendingTopicsList;
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

	private static TweetDto getTweetDto() {
		Set<String> likedBy = new HashSet<>();
		likedBy.add(TEST);
		TweetDto tweetDto = TweetDto.builder().createdBy(TEST).tweetMessage(VALID_TWEET_MESSAGE).repliedToTweet(TEST)
				.createdDateTime(LocalDateTime.now()).likedBy(likedBy).tweetTopic(TEST).build();
		return tweetDto;
	}

	private List<String> getHashtags() {
		List<String> hashtags = new ArrayList<>();
		hashtags.add(TEST);
		return hashtags;
	}

//check page size
	@Test
	void testGetAllTweetsPaged() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.getAllTweetsPaged(any(), anyInt(), anyInt())).thenReturn(getTweetEntityPage());
		this.mockMvc.perform(get(PATH + "/all?isPaged=true&page=0&size=5").header("Authorization", TOKEN))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(TEST)));
	}

	@Test
	void testGetAllTweets() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.getAllTweets()).thenReturn(getTweetEntityList());
		this.mockMvc.perform(get(PATH + "/all").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().string(containsString(TEST)));
	}

	@Test
	void testGetAllTweetsPagedException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.getAllTweetsPaged(any(), anyInt(), anyInt())).thenThrow(new TweetServiceException("TEST"));
		this.mockMvc.perform(get(PATH + "/all?isPaged=true&page=0&size=5").header("Authorization", TOKEN))
				.andDo(print()).andExpect(status().isInternalServerError());
	}

	@Test
	void testGetUserTweetsPaged() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.searchTweetsPaged(any(), anyInt(), anyInt())).thenReturn(getTweetEntityPage());
		this.mockMvc.perform(get(PATH + "/sam?isPaged=true&page=0&size=5").header("Authorization", TOKEN))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(TEST)));
	}

	@Test
	void testGetUserTweets() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.searchTweets(any())).thenReturn(getTweetEntityList());
		this.mockMvc.perform(get(PATH + "/sam").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().string(containsString(TEST)));
	}

	@Test
	void testGetUserTweetsException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.searchTweetsPaged(any(), anyInt(), anyInt())).thenThrow(new TweetServiceException("TEST"));
		this.mockMvc.perform(get(PATH + "/sam?isPaged=true&page=0&size=5").header("Authorization", TOKEN))
				.andDo(print()).andExpect(status().isInternalServerError());
	}

	@Test
	void testSearchTweetsPaged() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.searchTweetsPaged(any(), anyInt(), anyInt())).thenReturn(getTweetEntityPage());
		this.mockMvc.perform(get(PATH + "/search?isPaged=true&page=0&size=5").header("Authorization", TOKEN))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(TEST)));
	}

	@Test
	void testSearchTweets() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.searchTweets(any())).thenReturn(getTweetEntityList());
		this.mockMvc.perform(get(PATH + "/search").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().string(containsString(TEST)));
	}

	@Test
	void testSearchTweetsException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.searchTweetsPaged(any(), anyInt(), anyInt())).thenThrow(new TweetServiceException("TEST"));
		this.mockMvc.perform(get(PATH + "/search?isPaged=true&page=0&size=5").header("Authorization", TOKEN))
				.andDo(print()).andExpect(status().isInternalServerError());
	}

	@Test
	void testSearchTweetsDateException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		this.mockMvc.perform(get(PATH + "/search?startDateTime=TEST").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	void testGetTrendingTopics() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.getTrendingTopics(any())).thenReturn(getTrendingTopics());
		this.mockMvc.perform(get(PATH + "/trend").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().string(containsString(TEST)));
	}

	@Test
	void testGetTrendingTopicsDateException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		this.mockMvc.perform(get(PATH + "/trend?startDateTime=TEST").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	void testGetTrendingTopicsException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.getTrendingTopics(any())).thenThrow(new TweetServiceException("TEST"));
		this.mockMvc.perform(get(PATH + "/trend").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isInternalServerError());
	}

	@Test
	void testGetHashtags() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.getHashtags()).thenReturn(getHashtags());
		this.mockMvc.perform(get(PATH + "/all/hashtags").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().string(containsString(TEST)));
	}

	@Test
	void testGetHashtagsException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.getHashtags()).thenThrow(new TweetServiceException("TEST"));
		this.mockMvc.perform(get(PATH + "/all/hashtags").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isInternalServerError());
	}

	@Test
	void testPostNewTweetSuccess() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.postTweet(anyString(), any())).thenReturn("Tweet Posted Successfully");
		this.mockMvc
				.perform(post(PATH + "/TEST/add").content(objectMapper.writeValueAsString(getTweetDto()))
						.contentType(MediaType.APPLICATION_JSON).header("Authorization", TOKEN))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(content().string(containsString("Tweet Posted Successfully")));
	}

	@Test
	void testPostNewTweetBadRequest() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		TweetDto tweetDto = getTweetDto();
		tweetDto.setTweetMessage(null);
		this.mockMvc
				.perform(post(PATH + "/TEST/add").content(objectMapper.writeValueAsString(tweetDto))
						.contentType(MediaType.APPLICATION_JSON).header("Authorization", TOKEN))
				.andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	void testPostNewTweetException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.postTweet(anyString(), any())).thenThrow(new TweetServiceException("TEST"));
		this.mockMvc.perform(post(PATH + "/TEST/add").header("Authorization", TOKEN)
				.content(objectMapper.writeValueAsString(getTweetDto())).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isInternalServerError());
	}

	@Test
	void testUpdateTweetSuccess() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.updateTweet(any(), anyString())).thenReturn("Tweet Updated Successfully");
		this.mockMvc
				.perform(put(PATH + "/TEST/update/" + TWEET_ID).header("Authorization", TOKEN)
						.content(objectMapper.writeValueAsString(getTweetDto()))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(content().string(containsString("Tweet Updated Successfully")));
	}

	@Test
	void testUpdateTweetBadRequest() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		TweetDto tweetDto = getTweetDto();
		tweetDto.setTweetMessage(null);
		this.mockMvc
				.perform(put(PATH + "/TEST/update/" + TWEET_ID).header("Authorization", TOKEN)
						.content(objectMapper.writeValueAsString(tweetDto)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	void testUpdateTweetException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.updateTweet(any(), anyString())).thenThrow(new TweetServiceException("TEST"));
		this.mockMvc.perform(put(PATH + "/TEST/update/" + TWEET_ID).header("Authorization", TOKEN)
				.content(objectMapper.writeValueAsString(getTweetDto())).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isInternalServerError());
	}

	@Test
	void testLikeTweetSuccess() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.likeTweet(any(), anyString())).thenReturn("Tweet Liked Successfully");
		this.mockMvc
				.perform(patch(PATH + "/TEST/like/" + TWEET_ID).header("Authorization", TOKEN)
						.content(objectMapper.writeValueAsString(getTweetDto()))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Tweet Liked Successfully")));
	}

	@Test
	void testLikeTweetException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.likeTweet(any(), anyString())).thenThrow(new TweetServiceException("TEST"));
		this.mockMvc.perform(patch(PATH + "/TEST/like/" + TWEET_ID).header("Authorization", TOKEN)
				.content(objectMapper.writeValueAsString(getTweetDto())).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isInternalServerError());
	}

	@Test
	void testReplyTweetSuccess() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.replyToTweet(anyString(), any(), anyString()))
				.thenReturn("Replied to TweetId: " + TWEET_ID + " Successfully");
		this.mockMvc
				.perform(post(PATH + "/TEST/reply/" + TWEET_ID).header("Authorization", TOKEN)
						.content(objectMapper.writeValueAsString(getTweetDto()))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(content().string(containsString("Replied to TweetId: " + TWEET_ID + " Successfully")));
	}

	@Test
	void testReplyTweetException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.replyToTweet(anyString(), any(), anyString())).thenThrow(new TweetServiceException("TEST"));
		this.mockMvc.perform(post(PATH + "/TEST/reply/" + TWEET_ID).header("Authorization", TOKEN)
				.content(objectMapper.writeValueAsString(getTweetDto())).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isInternalServerError());
	}

	@Test
	void testDeleteTweetSuccess() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.deleteTweet(any())).thenReturn("Tweet Deleted Successfully");
		this.mockMvc
				.perform(delete(PATH + "/TEST/delete/" + TWEET_ID).header("Authorization", TOKEN)
						.content(objectMapper.writeValueAsString(getTweetDto()))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Tweet Deleted Successfully")));
	}

	@Test
	void testDeleteTweetException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.deleteTweet(any())).thenThrow(new TweetServiceException("TEST"));
		this.mockMvc.perform(delete(PATH + "/TEST/delete/" + TWEET_ID).header("Authorization", TOKEN)
				.content(objectMapper.writeValueAsString(getTweetDto())).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isInternalServerError());
	}

	@Test
	void testGetAllUsersPaged() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(userService.getAllUsersPaged(any(), anyInt(), anyInt())).thenReturn(getPagedUserEntity());
		this.mockMvc.perform(get(PATH + "/users/all?isPaged=true&page=0&size=5").header("Authorization", TOKEN))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(TEST)));
	}

	@Test
	void testGetAllUsers() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(userService.getAllUsers()).thenReturn(getUserEntityList());
		this.mockMvc.perform(get(PATH + "/users/all").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().string(containsString(TEST)));
	}

	@Test
	void testGetAllUsersException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(userService.getAllUsers()).thenThrow(new TweetServiceException(TEST));
		this.mockMvc.perform(get(PATH + "/users/all").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isInternalServerError());
	}

	@Test
	void testGetUserPaged() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(userService.searchUsersPaged(any(), anyInt(), anyInt())).thenReturn(getPagedUserEntity());
		this.mockMvc.perform(get(PATH + "/users/search/test?isPaged=true&page=0&size=5").header("Authorization", TOKEN))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(TEST)));
	}

	@Test
	void testGetUser() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(userService.searchUsers(any())).thenReturn(getUserEntityList());
		this.mockMvc.perform(get(PATH + "/users/search/test").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().string(containsString(TEST)));
	}

	@Test
	void testGetUserException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(userService.searchUsers(any())).thenThrow(new TweetServiceException(TEST));
		this.mockMvc.perform(get(PATH + "/users/search/test").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isInternalServerError());
	}

	@Test
	void testSearchUsersPaged() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(userService.searchUsersPaged(any(), anyInt(), anyInt())).thenReturn(getPagedUserEntity());
		this.mockMvc.perform(get(PATH + "/users/search?isPaged=true&page=0&size=5").header("Authorization", TOKEN))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString(TEST)));
	}

	@Test
	void testSearchUsers() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(userService.searchUsers(any())).thenReturn(getUserEntityList());
		this.mockMvc.perform(get(PATH + "/users/search").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().string(containsString(TEST)));
	}

	@Test
	void testSearchUsersException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(userService.searchUsers(any())).thenThrow(new TweetServiceException(TEST));
		this.mockMvc.perform(get(PATH + "/users/search").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isInternalServerError());
	}

	@Test
	void testGetUsertags() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(userService.getUsertags()).thenReturn(getUsertags());
		this.mockMvc.perform(get(PATH + "/users/usertags").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().string(containsString(TEST)));
	}

	@Test
	void testGetUsertagsException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(userService.getUsertags()).thenThrow(new TweetServiceException(TEST));
		this.mockMvc.perform(get(PATH + "/users/usertags").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isInternalServerError());
	}

	@Test
	void testExportCSV() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.getExportData(anyString())).thenReturn(new ArrayList<>());
		this.mockMvc.perform(get(PATH + "/test/export").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void testExportCSVException() throws Exception {
		when(authHandlerInterceptor.preHandle(any(), any(), any())).thenReturn(true);
		when(tweetService.getExportData(anyString())).thenThrow(new TweetServiceException(TEST));
		this.mockMvc.perform(get(PATH + "/test/export").header("Authorization", TOKEN)).andDo(print())
				.andExpect(status().isInternalServerError());
	}

}