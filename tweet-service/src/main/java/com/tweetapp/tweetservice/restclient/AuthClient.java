package com.tweetapp.tweetservice.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.tweetapp.tweetservice.dto.AuthResponse;

@FeignClient(name = "${auth.client.name}", url = "${auth.client.url}")
public interface AuthClient {

	@GetMapping("/uservalidate")
	public AuthResponse getUserValidity(@RequestHeader("Authorization") String token);

}
