package com.tweetapp.tweetservice.constants;

public class TweetAppConstants {
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String EXPORT_HEADER = "Tweet Message,Tweet Topic,Tag,Liked By,Replied To Message,Replied To User,Created On,Last Modified On";
	public static final String EXCEPTION = "Exception - {}";

	private TweetAppConstants() {
		throw new IllegalStateException("Utility class");
	}

}
