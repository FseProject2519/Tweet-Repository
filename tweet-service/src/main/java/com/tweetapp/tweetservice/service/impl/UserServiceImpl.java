package com.tweetapp.tweetservice.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.tweetservice.dto.UserSearchDto;
import com.tweetapp.tweetservice.entity.TweetEntity;
import com.tweetapp.tweetservice.entity.UserEntity;
import com.tweetapp.tweetservice.exception.TweetServiceException;
import com.tweetapp.tweetservice.repository.TweetRepository;
import com.tweetapp.tweetservice.repository.UserRepository;
import com.tweetapp.tweetservice.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserServiceImpl implements UserService {

	private static final String APPLICATION_JSON = "application/json";

	private static final String AWS_USER_ENDPOINT = "https://5m6zq8a7zh.execute-api.ap-northeast-1.amazonaws.com/users";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TweetRepository tweetRepository;

	@Override
	public Page<UserEntity> getAllUsersPaged(UserSearchDto userSearchDto, Integer page, Integer size)
			throws TweetServiceException {
		try {
			Sort sort = getUserSort(userSearchDto);

			log.info("Finding User With The Criteria - {} and Sort Criteria - {}", userSearchDto, sort);

			Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10, sort);
			return userRepository.findAll(pageable);

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}

	@Override
	public List<UserEntity> getAllUsers() throws TweetServiceException {
		try {

			log.info("Finding All Users");
			return userRepository.findAllByOrderByUserId();

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}

	@Override
	public Page<UserEntity> searchUsersPaged(UserSearchDto userSearchDto, Integer page, Integer size)
			throws TweetServiceException {
		try {
			Sort sort = getUserSort(userSearchDto);

			log.info("Finding User With The Criteria - {} and Sort Criteria - {}", userSearchDto, sort);

			Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10, sort);
			return userRepository.searchUsersPaged(userSearchDto, pageable);
		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}

	@Override
	public List<UserEntity> searchUsers(UserSearchDto userSearchDto) throws TweetServiceException {
		try {

			log.info("Finding User With The Criteria - {}", userSearchDto);

			return userRepository.searchUsers(userSearchDto);
		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}

	@Override
	public List<String> getUsertags() throws TweetServiceException {
		try {
			List<String> userIdList = userRepository.getUserIds();

			Collections.sort(userIdList);

			log.info("Returning User tags - {}", userIdList.toString());

			return userIdList;

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}

	private Sort getUserSort(UserSearchDto userSearchDto) throws TweetServiceException {
		try {
			Sort sort = null;
			if (userSearchDto.getSortField() == null && userSearchDto.getSortOrder() == null) {
				sort = Sort.by("firstName").ascending();
			} else {
				sort = userSearchDto.getSortField() != null ? Sort.by(userSearchDto.getSortField())
						: Sort.by("firstName");
				sort = userSearchDto.getSortOrder() != null && "desc".equalsIgnoreCase(userSearchDto.getSortOrder())
						? sort.descending()
						: sort.ascending();
			}
			return sort;
		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}

	@Override
	public String deleteUser(String username) throws TweetServiceException {
		try {
			Optional<UserEntity> user = userRepository.findByUserId(username);
			if (user.isPresent()) {

				log.info("Deleting User With Id - {}", username);
				userRepository.deleteByUserId(username);
				List<TweetEntity> userTweets = tweetRepository.findByCreatedBy(username);
				for (TweetEntity userTweet : userTweets) {
					tweetRepository.deleteByRepliedToTweet(userTweet.getId());
				}
				deleteUserCloud(username);
				tweetRepository.deleteByCreatedBy(username);
				return "User Deleted Successfully";
			} else {
				return "User Not Found";
			}

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());
		}
	}

	private void deleteUserCloud(String userId) throws TweetServiceException {
		try {
			URL url = new URL(AWS_USER_ENDPOINT + "/" + userId);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", APPLICATION_JSON);
			connection.setRequestMethod("DELETE");
			connection.connect();
			log.info("Response Code = {}", connection.getResponseCode());
		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}

	}

	@Override
	public List<UserEntity> searchUsersCloud(UserSearchDto userSearchDto) throws TweetServiceException {
		try {
			URL url = new URL(AWS_USER_ENDPOINT + "/" + userSearchDto.getUserId());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("accept", APPLICATION_JSON);

			BufferedReader br = null;
			if (100 <= connection.getResponseCode() && connection.getResponseCode() <= 399) {
				br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			}
			StringBuilder sb = new StringBuilder();
			String output;
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			String response = sb.toString();
			JSONObject jsonObj = new JSONObject(response);
			JSONObject responseItem = jsonObj.getJSONObject("Item");
			ObjectMapper mapper = new ObjectMapper();
			UserEntity userEntity = mapper.readValue(responseItem.toString(), UserEntity.class);

			List<UserEntity> userList = new ArrayList<>();
			userList.add(userEntity);

			return userList;
		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}

	@Override
	public List<UserEntity> getAllUsersCloud() throws TweetServiceException {
		try {
			URL url = new URL(AWS_USER_ENDPOINT);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("accept", APPLICATION_JSON);

			BufferedReader br = null;
			if (100 <= connection.getResponseCode() && connection.getResponseCode() <= 399) {
				br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			}
			StringBuilder sb = new StringBuilder();
			String output;
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			String response = sb.toString();
			JSONObject jsonObj = new JSONObject(response);
			JSONArray responseItems = jsonObj.getJSONArray("Items");
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(responseItems.toString(), new TypeReference<List<UserEntity>>() {
			});

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}

}
