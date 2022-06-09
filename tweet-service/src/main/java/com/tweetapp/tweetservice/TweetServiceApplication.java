package com.tweetapp.tweetservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.handler.MappedInterceptor;

import com.tweetapp.tweetservice.exception.handler.MyHandlerInterceptor;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
@ComponentScan(basePackages = { "com.tweetapp.tweetservice" })
@EnableMongoRepositories
@EnableFeignClients
public class TweetServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetServiceApplication.class, args);
	}

	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Tweet Service API").description("Tweet application").version("v0.0.1"));
	}

	@Bean
	@Autowired
	public MappedInterceptor getMappedInterceptor(MyHandlerInterceptor myHandlerInterceptor) {
		return new MappedInterceptor(new String[] { "/api/v1.0/tweets/**" }, myHandlerInterceptor);
	}
}
