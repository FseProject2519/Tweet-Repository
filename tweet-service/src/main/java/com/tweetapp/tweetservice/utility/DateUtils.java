package com.tweetapp.tweetservice.utility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.tweetapp.tweetservice.constants.TweetAppConstants;

public class DateUtils {

	private DateUtils() {
		throw new IllegalStateException("Utility class");
	}

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

	public static String userFriendlyFormat(LocalDateTime now) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		return now.format(formatter);
	}
}
