package com.tweetapp.tweetservice.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	public Page<UserEntity> searchUsersPaged(UserSearchDto userSearchDto, Pageable pageable) {
		Query query = new Query().with(pageable);
		buildCriteria(userSearchDto, query);

		return PageableExecutionUtils.getPage(mongoTemplate.find(query, UserEntity.class), pageable,
				() -> mongoTemplate.count(query.skip(0).limit(0), UserEntity.class));

	}

	@Override
	public List<UserEntity> searchUsers(UserSearchDto userSearchDto) {
		Query query = new Query();
		buildCriteria(userSearchDto, query);

		return mongoTemplate.find(query, UserEntity.class);

	}

	@Override
	public Page<UserEntity> findAll(Pageable pageable) {
		Query query = new Query().with(pageable);

		return PageableExecutionUtils.getPage(mongoTemplate.find(query, UserEntity.class), pageable,
				() -> mongoTemplate.count(query.skip(0).limit(0), UserEntity.class));

	}

	@Override
	public List<String> getUserIds() {
		Query query = new Query();

		List<UserEntity> resultList = mongoTemplate.find(query, UserEntity.class);
		return resultList.stream().map(UserEntity::getUserId).collect(Collectors.toList());
	}

	private void buildCriteria(UserSearchDto userSearchDto, Query query) {
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
	}

}
