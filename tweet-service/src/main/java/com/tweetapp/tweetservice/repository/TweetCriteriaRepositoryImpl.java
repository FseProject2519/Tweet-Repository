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

import com.tweetapp.tweetservice.dto.TweetSearchDto;
import com.tweetapp.tweetservice.entity.TweetEntity;

public class TweetCriteriaRepositoryImpl implements TweetCriteriaRepository {
	private final MongoTemplate mongoTemplate;

	public TweetCriteriaRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Page<TweetEntity> searchTweets(TweetSearchDto tweetSearchDto, Pageable pageable) {
		Query query = new Query().with(pageable);
		final List<Criteria> criteria = new ArrayList<>();

		if (StringUtils.hasLength(tweetSearchDto.getCreatedBy()))
			criteria.add(Criteria.where("createdBy").regex(tweetSearchDto.getCreatedBy(), "i"));

		if (StringUtils.hasLength(tweetSearchDto.getTweetMessage()))
			criteria.add(Criteria.where("tweetMessage").regex(tweetSearchDto.getTweetMessage(), "i"));

		if (StringUtils.hasLength(tweetSearchDto.getTweetTopic()))
			criteria.add(Criteria.where("tweetTopic").regex(tweetSearchDto.getTweetTopic(), "i"));

		if (StringUtils.hasLength(tweetSearchDto.getTag()))
			criteria.add(Criteria.where("tag").regex(tweetSearchDto.getTag(), "i"));

		if (tweetSearchDto.getStartDateTime() != null && tweetSearchDto.getEndDateTime() != null)
			criteria.add(Criteria.where("createdDateTime").gte(tweetSearchDto.getStartDateTime())
					.lte(tweetSearchDto.getEndDateTime()));

		if (tweetSearchDto.getStartDateTime() == null && tweetSearchDto.getEndDateTime() != null)
			criteria.add(Criteria.where("createdDateTime").lte(tweetSearchDto.getEndDateTime()));

		if (tweetSearchDto.getStartDateTime() != null && tweetSearchDto.getEndDateTime() == null)
			criteria.add(Criteria.where("createdDateTime").gte(tweetSearchDto.getStartDateTime()));

		if (tweetSearchDto.getLikedBy() != null && !tweetSearchDto.getLikedBy().isEmpty())
			criteria.add(Criteria.where("likedBy").all(tweetSearchDto.getLikedBy()));

		if (!criteria.isEmpty()) {
			query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
		}

		return PageableExecutionUtils.getPage(mongoTemplate.find(query, TweetEntity.class), pageable,
				() -> mongoTemplate.count(query.skip(0).limit(0), TweetEntity.class));

	}

	@Override
	public Page<TweetEntity> findAll(Pageable pageable) {
		Query query = new Query().with(pageable);

		return PageableExecutionUtils.getPage(mongoTemplate.find(query, TweetEntity.class), pageable,
				() -> mongoTemplate.count(query.skip(0).limit(0), TweetEntity.class));

	}
}
