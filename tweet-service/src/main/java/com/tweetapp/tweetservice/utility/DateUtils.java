package com.tweetapp.tweetservice.utility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.tweetapp.tweetservice.constants.TweetAppConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtils {

	private DateUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static LocalDateTime processDateTime(String localDateTime) {

		log.info("processDateTime for - {}", localDateTime);
		return localDateTime != null
				? convertToUtcDateTime(LocalDateTime.parse(localDateTime,
						DateTimeFormatter.ofPattern(TweetAppConstants.DATE_TIME_PATTERN)))
				: null;
	}

	public static LocalDateTime convertToUtcDateTime(LocalDateTime localDateTime) {

		log.info("convertToUtcDateTime for - {}", localDateTime);

		return localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
	}

	public static Date getDate(LocalDateTime localDateTime) {

		log.info("getDate for - {}", localDateTime);

		return Date.from(localDateTime.atZone(ZoneId.of("UTC")).toInstant());
	}

	public static String userFriendlyDateFormat(LocalDateTime now) {

		log.info("userFriendlyDateFormat for - {}", now);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TweetAppConstants.USER_FRIENDLY_DATE_FORMAT);
		return now.format(formatter);
	}
}
