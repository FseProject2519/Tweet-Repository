package com.tweetapp.tweetservice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tweetapp.tweetservice.dto.UserSearchDto;
import com.tweetapp.tweetservice.entity.UserEntity;

public interface UserCriteriaRepository {

	Page<UserEntity> searchUsersPaged(UserSearchDto userSearchDto, Pageable pageable);

	Page<UserEntity> findAll(Pageable pageable);

	List<UserEntity> searchUsers(UserSearchDto userSearchDto);

	List<String> getUserIds();

}
