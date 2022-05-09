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
public class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userServiceImpl;

	@Mock
	UserRepository userRepository;

	@Test
	public void testGetAllUsersSuccess() throws TweetServiceException {
		UserSearchDto userSearchDto = buildUserSearchDto("firstName", "asc");
		when(userRepository.findAll(isA(Pageable.class))).thenReturn(getUserEntity());
		assertEquals("TEST", userServiceImpl.getAllUsers(userSearchDto, 0, 1).getContent().get(0).getUserId());

	}

	@Test
	public void testGetAllUsersSuccessWithNullSortParams() throws TweetServiceException {
		UserSearchDto userSearchDto = buildUserSearchDto(null, null);
		when(userRepository.findAll(isA(Pageable.class))).thenReturn(getUserEntity());
		assertEquals("TEST", userServiceImpl.getAllUsers(userSearchDto, null, null).getContent().get(0).getUserId());

	}

	@Test
	public void testGetAllUsersException() throws TweetServiceException {
		UserSearchDto userSearchDto = null;
		assertThrows(TweetServiceException.class, () -> {
			userServiceImpl.getAllUsers(userSearchDto, null, null);
		});
	}

	@Test
	public void testSearchUsersSuccess() throws TweetServiceException {
		UserSearchDto userSearchDto = buildUserSearchDto("createdBy", "desc");
		when(userRepository.searchUsers(any(), isA(Pageable.class))).thenReturn(getUserEntity());
		assertEquals("TEST", userServiceImpl.searchUsers(userSearchDto, 0, 1).getContent().get(0).getUserId());

	}

	@Test
	public void testSearchUsersSuccessWithNullSortField() throws TweetServiceException {
		UserSearchDto userSearchDto = buildUserSearchDto(null, "desc");
		when(userRepository.searchUsers(any(), isA(Pageable.class))).thenReturn(getUserEntity());
		assertEquals("TEST", userServiceImpl.searchUsers(userSearchDto, 0, 1).getContent().get(0).getUserId());

	}

	@Test
	public void testSearchUsersSuccessWithNullSortOrder() throws TweetServiceException {
		UserSearchDto userSearchDto = buildUserSearchDto("firstName", null);
		when(userRepository.searchUsers(any(), isA(Pageable.class))).thenReturn(getUserEntity());
		assertEquals("TEST", userServiceImpl.searchUsers(userSearchDto, null, null).getContent().get(0).getUserId());

	}

	@Test
	public void testSearchUsersException() throws TweetServiceException {
		UserSearchDto userSearchDto = null;
		assertThrows(TweetServiceException.class, () -> {
			userServiceImpl.searchUsers(userSearchDto, null, null);
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
