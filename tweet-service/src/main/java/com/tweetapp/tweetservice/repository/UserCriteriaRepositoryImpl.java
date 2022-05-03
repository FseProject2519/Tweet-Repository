package com.tweetapp.tweetservice.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import com.tweetapp.tweetservice.dto.UserSearchDto;
import com.tweetapp.tweetservice.entity.UserEntity;

public class UserCriteriaRepositoryImpl implements UserCriteriaRepository {
	private final MongoTemplate mongoTemplate;

	public UserCriteriaRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Page<UserEntity> searchUsers(UserSearchDto userSearchDto, Pageable pageable) {
		Query query = new Query().with(pageable);
		final List<Criteria> criteria = new ArrayList<>();

		if (StringUtils.hasLength(userSearchDto.getUserId()))
			criteria.add(Criteria.where("userId").regex(userSearchDto.getUserId(), "i"));
		if (StringUtils.hasLength(userSearchDto.getFirstName()))
			criteria.add(Criteria.where("firstName").regex(userSearchDto.getFirstName(), "i"));
		if (StringUtils.hasLength(userSearchDto.getLastName()))
			criteria.add(Criteria.where("lastName").regex(userSearchDto.getLastName(), "i"));
		if (StringUtils.hasLength(userSearchDto.getEmail()))
			criteria.add(Criteria.where("email").regex(userSearchDto.getEmail(), "i"));
		if (!criteria.isEmpty()) {
			query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
		}

		return PageableExecutionUtils.getPage(mongoTemplate.find(query, UserEntity.class), pageable,
				() -> mongoTemplate.count(query.skip(0).limit(0), UserEntity.class));

	}

	@Override
	public Page<UserEntity> findAll(Pageable pageable) {
		Query query = new Query().with(pageable);

		return PageableExecutionUtils.getPage(mongoTemplate.find(query, UserEntity.class), pageable,
				() -> mongoTemplate.count(query.skip(0).limit(0), UserEntity.class));

	}
}
