package com.tweetapp.tweetservice.utility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.tweetapp.tweetservice.constants.TweetAppConstants;

public class DateUtils {

	public static LocalDateTime processDateTime(String startDateTime) {
		return startDateTime != null
				? convertToUtcDateTime(LocalDateTime.parse(startDateTime,
						DateTimeFormatter.ofPattern(TweetAppConstants.DATE_TIME_PATTERN)))
				: null;
	}

	public static LocalDateTime convertToUtcDateTime(LocalDateTime localDateTime) {
		return localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
	}

	public static Date getDate(LocalDateTime localDateTime) {

		return Date.from(localDateTime.atZone(ZoneId.of("UTC")).toInstant());
	}
}
