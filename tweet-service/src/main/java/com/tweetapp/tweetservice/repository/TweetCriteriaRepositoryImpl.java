package com.tweetapp.tweetservice.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import com.tweetapp.tweetservice.dto.TweetSearchDto;
import com.tweetapp.tweetservice.entity.TweetEntity;
import com.tweetapp.tweetservice.entity.TweetTrendEntity;
import com.tweetapp.tweetservice.utility.DateUtils;

public class TweetCriteriaRepositoryImpl implements TweetCriteriaRepository {
	private static final String CREATED_DATE_TIME_SPACED = "created_date_time";
	private static final String CREATED_DATE_TIME = "createdDateTime";
	private static final String COUNT = "count";
	private final MongoTemplate mongoTemplate;

	public TweetCriteriaRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Page<TweetEntity> searchTweetsPaged(TweetSearchDto tweetSearchDto, Pageable pageable) {
		Query query = new Query().with(pageable);
		final List<Criteria> criteria = buildCriteria(tweetSearchDto);

		if (!criteria.isEmpty()) {
			query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
		}

		return PageableExecutionUtils.getPage(mongoTemplate.find(query, TweetEntity.class), pageable,
				() -> mongoTemplate.count(query.skip(0).limit(0), TweetEntity.class));

	}

	@Override
	public List<TweetEntity> searchTweets(TweetSearchDto tweetSearchDto) {
		Query query = new Query();
		final List<Criteria> criteria = buildCriteria(tweetSearchDto);

		if (!criteria.isEmpty()) {
			query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
		}

		return mongoTemplate.find(query, TweetEntity.class);

	}

	@Override
	public Page<TweetEntity> findAll(Pageable pageable) {
		Query query = new Query().with(pageable);

		return PageableExecutionUtils.getPage(mongoTemplate.find(query, TweetEntity.class), pageable,
				() -> mongoTemplate.count(query.skip(0).limit(0), TweetEntity.class));

	}

	@Override
	public List<TweetTrendEntity> getTrendingTopics(TweetSearchDto tweetSearchDto) {

		Criteria criteria = new Criteria();

		if (tweetSearchDto.getStartDateTime() != null && tweetSearchDto.getEndDateTime() != null)
			criteria = Criteria.where(CREATED_DATE_TIME_SPACED)
					.gte(DateUtils.getDate(tweetSearchDto.getStartDateTime()))
					.lte(DateUtils.getDate(tweetSearchDto.getEndDateTime()));

		if (tweetSearchDto.getStartDateTime() == null && tweetSearchDto.getEndDateTime() != null)
			criteria = Criteria.where(CREATED_DATE_TIME_SPACED).lte(DateUtils.getDate(tweetSearchDto.getEndDateTime()));

		if (tweetSearchDto.getStartDateTime() != null && tweetSearchDto.getEndDateTime() == null)
			criteria = Criteria.where(CREATED_DATE_TIME_SPACED)
					.gte(DateUtils.getDate(tweetSearchDto.getStartDateTime()));

		MatchOperation matchOperation = Aggregation.match(criteria);
		AggregationOperation unwind = Aggregation.unwind("hashtags");
		GroupOperation groupOperation = Aggregation.group("hashtags").count().as(COUNT);
		ProjectionOperation projectionOperation = Aggregation.project(COUNT).and("hashtags").previousOperation();
		SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.DESC, COUNT)).and(Sort.Direction.ASC,"hashtags");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, unwind, groupOperation, projectionOperation,
				sortOperation);
		AggregationResults<TweetTrendEntity> result = mongoTemplate.aggregate(aggregation, "TweetCollection",
				TweetTrendEntity.class);
		return result.getMappedResults();
	}

	@Override
	public List<List<String>> getHashtags() {
		Query query = new Query();

		List<TweetEntity> resultList = mongoTemplate.find(query, TweetEntity.class);
		return resultList.stream().filter(result -> result.getHashtags() != null)
				.map(result -> new ArrayList<>(result.getHashtags())).collect(Collectors.toList());
	}

	private List<Criteria> buildCriteria(TweetSearchDto tweetSearchDto) {
		final List<Criteria> criteria = new ArrayList<>();

		if (StringUtils.hasLength(tweetSearchDto.getCreatedBy()))
			criteria.add(Criteria.where("createdBy").regex(tweetSearchDto.getCreatedBy(), "i"));

		if (StringUtils.hasLength(tweetSearchDto.getTweetMessage()))
			criteria.add(Criteria.where("tweetMessage").regex(tweetSearchDto.getTweetMessage(), "i"));

		if (tweetSearchDto.getTag() != null && !tweetSearchDto.getTag().isEmpty())
			criteria.add(Criteria.where("hashtags").all(tweetSearchDto.getTag()));

		if (tweetSearchDto.getStartDateTime() != null && tweetSearchDto.getEndDateTime() != null)
			criteria.add(Criteria.where(CREATED_DATE_TIME).gte(tweetSearchDto.getStartDateTime())
					.lte(tweetSearchDto.getEndDateTime()));

		if (tweetSearchDto.getStartDateTime() == null && tweetSearchDto.getEndDateTime() != null)
			criteria.add(Criteria.where(CREATED_DATE_TIME).lte(tweetSearchDto.getEndDateTime()));

		if (tweetSearchDto.getStartDateTime() != null && tweetSearchDto.getEndDateTime() == null)
			criteria.add(Criteria.where(CREATED_DATE_TIME).gte(tweetSearchDto.getStartDateTime()));

		if (tweetSearchDto.getLikedBy() != null && !tweetSearchDto.getLikedBy().isEmpty())
			criteria.add(Criteria.where("likedBy").all(tweetSearchDto.getLikedBy()));
		return criteria;
	}

}
