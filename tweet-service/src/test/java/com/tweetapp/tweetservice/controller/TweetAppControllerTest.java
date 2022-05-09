//package com.tweetapp.tweetservice.controller;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class TweetAppControllerTest {
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	private static String PATH = "/api/v1.0/tweets";
//
//	@Test
//	public void testGetAllTweets() throws Exception {
//		this.mockMvc.perform(get(PATH + "/all")).andDo(print()).andExpect(status().isOk())
//				.andExpect(content().string(containsString("TEST")));
//	}
//
//}