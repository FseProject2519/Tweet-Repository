package com.tweetapp.tweetservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tweetapp.tweetservice.dto.UserSearchDto;
import com.tweetapp.tweetservice.entity.UserEntity;

public interface UserCriteriaRepository {

	Page<UserEntity> searchUsers(UserSearchDto userSearchDto, Pageable pageable);

	Page<UserEntity> findAll(Pageable pageable);

}
