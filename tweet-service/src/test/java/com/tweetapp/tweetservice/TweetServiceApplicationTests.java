package com.tweetapp.tweetservice;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({ "com.tweetapp.tweetservice.controller", "com.tweetapp.tweetservice.service.impl" })
class TweetServiceApplicationTests {
}
