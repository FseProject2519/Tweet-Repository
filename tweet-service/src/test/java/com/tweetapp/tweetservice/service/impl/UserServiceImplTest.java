package com.tweetapp.tweetservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.tweetapp.tweetservice.dto.UserSearchDto;
import com.tweetapp.tweetservice.entity.UserEntity;
import com.tweetapp.tweetservice.exception.TweetServiceException;
import com.tweetapp.tweetservice.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userServiceImpl;

	@Mock
	UserRepository userRepository;

	@Test
	void testGetAllUsersPagedSuccess() throws TweetServiceException {
		UserSearchDto userSearchDto = buildUserSearchDto("firstName", "asc");
		when(userRepository.findAll(isA(Pageable.class))).thenReturn(getPagedUserEntity());
		assertEquals("TEST", userServiceImpl.getAllUsersPaged(userSearchDto, 0, 1).getContent().get(0).getUserId());

	}

	@Test
	void testGetAllUsersPagedSuccessWithNullSortParams() throws TweetServiceException {
		UserSearchDto userSearchDto = buildUserSearchDto(null, null);
		when(userRepository.findAll(isA(Pageable.class))).thenReturn(getPagedUserEntity());
		assertEquals("TEST",
				userServiceImpl.getAllUsersPaged(userSearchDto, null, null).getContent().get(0).getUserId());

	}

	@Test
	void testGetAllUsersPagedException() throws TweetServiceException {
		UserSearchDto userSearchDto = null;
		assertThrows(TweetServiceException.class, () -> {
			userServiceImpl.getAllUsersPaged(userSearchDto, null, null);
		});
	}

	@Test
	void testGetAllUsersSuccess() throws TweetServiceException {
		when(userRepository.findAllByOrderByUserId()).thenReturn(getUserEntityList());
		assertEquals("TEST", userServiceImpl.getAllUsers().get(0).getUserId());

	}

	@Test
	void testGetAllUsersException() throws TweetServiceException {
		when(userRepository.findAllByOrderByUserId()).thenThrow(new RuntimeException());
		assertThrows(TweetServiceException.class, () -> {
			userServiceImpl.getAllUsers();
		});
	}

	@Test
	void testSearchUsersPagedSuccess() throws TweetServiceException {
		UserSearchDto userSearchDto = buildUserSearchDto("createdBy", "desc");
		when(userRepository.searchUsersPaged(any(), isA(Pageable.class))).thenReturn(getPagedUserEntity());
		assertEquals("TEST", userServiceImpl.searchUsersPaged(userSearchDto, 0, 1).getContent().get(0).getUserId());

	}

	@Test
	void testSearchUsersPagedSuccessWithNullSortField() throws TweetServiceException {
		UserSearchDto userSearchDto = buildUserSearchDto(null, "desc");
		when(userRepository.searchUsersPaged(any(), isA(Pageable.class))).thenReturn(getPagedUserEntity());
		assertEquals("TEST", userServiceImpl.searchUsersPaged(userSearchDto, 0, 1).getContent().get(0).getUserId());

	}

	@Test
	void testSearchUsersPagedSuccessWithNullSortOrder() throws TweetServiceException {
		UserSearchDto userSearchDto = buildUserSearchDto("firstName", null);
		when(userRepository.searchUsersPaged(any(), isA(Pageable.class))).thenReturn(getPagedUserEntity());
		assertEquals("TEST",
				userServiceImpl.searchUsersPaged(userSearchDto, null, null).getContent().get(0).getUserId());

	}

	@Test
	void testSearchUsersPagedException() throws TweetServiceException {
		UserSearchDto userSearchDto = null;
		assertThrows(TweetServiceException.class, () -> {
			userServiceImpl.searchUsersPaged(userSearchDto, null, null);
		});
	}

	@Test
	void testSearchUsersSuccess() throws TweetServiceException {
		UserSearchDto userSearchDto = buildUserSearchDto("createdBy", "desc");
		when(userRepository.searchUsers(any())).thenReturn(getUserEntityList());
		assertEquals("TEST", userServiceImpl.searchUsers(userSearchDto).get(0).getUserId());

	}

	@Test
	void testSearchUsersException() throws TweetServiceException {
		UserSearchDto userSearchDto = null;
		when(userRepository.searchUsers(any())).thenThrow(new RuntimeException());
		assertThrows(TweetServiceException.class, () -> {
			userServiceImpl.searchUsers(userSearchDto);
		});
	}

	@Test
	void testGetUsertagsSuccess() throws TweetServiceException {

		List<String> usertags = new ArrayList<>();
		usertags.add("TEST");
		when(userRepository.getUserIds()).thenReturn(usertags);
		assertEquals("TEST", userServiceImpl.getUsertags().get(0));

	}

	@Test
	void testGetUsertagsException() throws TweetServiceException {

		List<String> usertags = new ArrayList<>();
		usertags.add("TEST");
		when(userRepository.getUserIds()).thenThrow(new RuntimeException());
		assertThrows(TweetServiceException.class, () -> {
			userServiceImpl.getUsertags();
		});

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
		UserEntity userEntity = UserEntity.builder().userId("TEST").firstName("TEST").lastName("TEST").email("TEST")
				.contactNumber("1234567890").build();
		return userEntity;
	}

	private UserSearchDto buildUserSearchDto(String sortField, String sortOrder) {
		return UserSearchDto.builder().userId("TEST").firstName("TEST").lastName("TEST").email("TEST")
				.sortField(sortField).sortOrder(sortOrder).build();

	}
}
