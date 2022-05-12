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
	void testGetAllUsersSuccess() throws TweetServiceException {
		UserSearchDto userSearchDto = buildUserSearchDto("firstName", "asc");
		when(userRepository.findAll(isA(Pageable.class))).thenReturn(getUserEntity());
		assertEquals("TEST", userServiceImpl.getAllUsersPaged(userSearchDto, 0, 1).getContent().get(0).getUserId());

	}

	@Test
	void testGetAllUsersSuccessWithNullSortParams() throws TweetServiceException {
		UserSearchDto userSearchDto = buildUserSearchDto(null, null);
		when(userRepository.findAll(isA(Pageable.class))).thenReturn(getUserEntity());
		assertEquals("TEST", userServiceImpl.getAllUsersPaged(userSearchDto, null, null).getContent().get(0).getUserId());

	}

	@Test
	void testGetAllUsersException() throws TweetServiceException {
		UserSearchDto userSearchDto = null;
		assertThrows(TweetServiceException.class, () -> {
			userServiceImpl.getAllUsersPaged(userSearchDto, null, null);
		});
	}

	@Test
	void testSearchUsersSuccess() throws TweetServiceException {
		UserSearchDto userSearchDto = buildUserSearchDto("createdBy", "desc");
		when(userRepository.searchUsersPaged(any(), isA(Pageable.class))).thenReturn(getUserEntity());
		assertEquals("TEST", userServiceImpl.searchUsersPaged(userSearchDto, 0, 1).getContent().get(0).getUserId());

	}

	@Test
	void testSearchUsersSuccessWithNullSortField() throws TweetServiceException {
		UserSearchDto userSearchDto = buildUserSearchDto(null, "desc");
		when(userRepository.searchUsersPaged(any(), isA(Pageable.class))).thenReturn(getUserEntity());
		assertEquals("TEST", userServiceImpl.searchUsersPaged(userSearchDto, 0, 1).getContent().get(0).getUserId());

	}

	@Test
	void testSearchUsersSuccessWithNullSortOrder() throws TweetServiceException {
		UserSearchDto userSearchDto = buildUserSearchDto("firstName", null);
		when(userRepository.searchUsersPaged(any(), isA(Pageable.class))).thenReturn(getUserEntity());
		assertEquals("TEST", userServiceImpl.searchUsersPaged(userSearchDto, null, null).getContent().get(0).getUserId());

	}

	@Test
	void testSearchUsersException() throws TweetServiceException {
		UserSearchDto userSearchDto = null;
		assertThrows(TweetServiceException.class, () -> {
			userServiceImpl.searchUsersPaged(userSearchDto, null, null);
		});
	}

	private Page<UserEntity> getUserEntity() {
		UserEntity userEntity = UserEntity.builder().userId("TEST").firstName("TEST").lastName("TEST").email("TEST")
				.contactNumber("1234567890").build();

		List<UserEntity> userEntityList = new ArrayList<>();
		userEntityList.add(userEntity);
		return new PageImpl<>(userEntityList);

	}

	private UserSearchDto buildUserSearchDto(String sortField, String sortOrder) {
		return UserSearchDto.builder().userId("TEST").firstName("TEST").lastName("TEST").email("TEST")
				.sortField(sortField).sortOrder(sortOrder).build();

	}
}
