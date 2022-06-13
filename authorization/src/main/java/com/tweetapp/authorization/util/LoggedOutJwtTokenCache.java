package com.tweetapp.authorization.util;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tweetapp.authorization.event.OnUserLogoutSuccess;
import com.tweetapp.authorization.service.JwtUtil;

import net.jodah.expiringmap.ExpiringMap;

@Component
public class LoggedOutJwtTokenCache {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggedOutJwtTokenCache.class);

	 private ExpiringMap<String, OnUserLogoutSuccess> tokenEventMap;
	 private JwtUtil tokenProvider;
	 
	    @Autowired
	    public LoggedOutJwtTokenCache(JwtUtil tokenProvider) {
	        this.tokenProvider = tokenProvider;
	        this.tokenEventMap = ExpiringMap.builder()
	                .variableExpiration()
	                .maxSize(1000)
	                .build();
	    }
	    
public void markLogoutEventForToken(OnUserLogoutSuccess onUserLogoutSuccess) {
	        String token = onUserLogoutSuccess.getToken();
	        if (tokenEventMap.containsKey(token)) {
	            LOGGER.info(String.format("Log out token for user [%s] is already present in the cache", onUserLogoutSuccess.getUserName()));

	        } else {
	            Date tokenExpiryDate = tokenProvider.getTokenExpiryFromJWT(token);
	            long ttlForToken = getTTLForToken(tokenExpiryDate);
	            LOGGER.info(String.format("Logout token cache set for [%s] with a TTL of [%s] seconds. Token is due expiry at [%s]", onUserLogoutSuccess.getUserName(), ttlForToken, tokenExpiryDate));
	            tokenEventMap.put(token, onUserLogoutSuccess, ttlForToken, TimeUnit.SECONDS);
	        }
	    }
public OnUserLogoutSuccess getLogoutEventForToken(String token) {
    return tokenEventMap.get(token);
}
private long getTTLForToken(Date tokenExpiryDate) {
	long secondAtExpiry = tokenExpiryDate.toInstant().getEpochSecond();
    long secondAtLogout = Instant.now().getEpochSecond();
    return Math.max(0, secondAtExpiry - secondAtLogout);

}

}
