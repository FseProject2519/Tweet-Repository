package com.tweetapp.tweetservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.tweetapp.tweetservice.dto.UserDto;
import com.tweetapp.tweetservice.dto.UserSearchDto;
import com.tweetapp.tweetservice.entity.UserEntity;
import com.tweetapp.tweetservice.exception.TweetServiceException;
import com.tweetapp.tweetservice.repository.UserRepository;
import com.tweetapp.tweetservice.service.UserService;

@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public String createUser(UserDto userDto) {
		return "";
	}

	@Override
	public ResponseEntity<Page<UserEntity>> getAllUsers(UserSearchDto userSearchDto, Integer page, Integer size)
			throws TweetServiceException {
		try {
			Sort sort = getUserSort(userSearchDto);
			Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10, sort);
			return new ResponseEntity<>(userRepository.findAll(pageable), HttpStatus.OK);

		} catch (Exception e) {
			throw new TweetServiceException(e.getMessage());

		}
	}

	@Override
	public ResponseEntity<Page<UserEntity>> searchUsers(UserSearchDto userSearchDto, Integer page, Integer size)
			throws TweetServiceException {
		try {
			Sort sort = getUserSort(userSearchDto);
			Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10, sort);
			return new ResponseEntity<>(userRepository.searchUsers(userSearchDto, pageable), HttpStatus.OK);
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
}
