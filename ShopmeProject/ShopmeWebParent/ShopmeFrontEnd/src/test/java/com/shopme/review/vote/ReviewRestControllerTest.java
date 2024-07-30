package com.shopme.review.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.Review;
import com.shopme.review.ReviewRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewRestControllerTest {
     
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	
	@Test
	public void testVoteNotLogin() throws Exception {
		String requestURL = "/vote_review/3/up";
		
		MvcResult result = mockMvc.perform(post(requestURL).with(csrf()))
		       .andExpect(status().isOk())
		       .andDo(print())
		       .andReturn();
		
		String json = result.getResponse().getContentAsString();
		VoteResult voteResultObject = mapper.readValue(json,VoteResult.class);
		
		assertThat(voteResultObject.isSuccessfull());
		assertThat(voteResultObject.getMessage()).contains("You must login to vote the review");
	}
	
	@Test
	@WithMockUser(username = "n21dcvt128@student.ptithcm.edu.vn",password = "987654321")
	public void testVoteNonExistReview() throws Exception {
		String requestURL = "/vote_review/123/up";
		
		MvcResult result = mockMvc.perform(post(requestURL).with(csrf()))
		       .andExpect(status().isOk())
		       .andDo(print())
		       .andReturn();
		
		String json = result.getResponse().getContentAsString();
		VoteResult voteResultObject = mapper.readValue(json,VoteResult.class);
		
		assertThat(voteResultObject.isSuccessfull());
		assertThat(voteResultObject.getMessage()).contains("have not found");
	}
	
	
	@Test
	@WithMockUser(username = "n21dcvt128@student.ptithcm.edu.vn",password = "987654321")
	public void testVoteUp() throws Exception {
		Integer reviewId = 3;
		String requestURL = "/vote_review/3/up";
		
		Review reviewInDb = reviewRepository.findById(reviewId).get();
		int voteCountBefore = reviewInDb.getVotes();
		MvcResult result = mockMvc.perform(post(requestURL).with(csrf()))
		       .andExpect(status().isOk())
		       .andDo(print())
		       .andReturn();
		
		String json = result.getResponse().getContentAsString();
		VoteResult voteResultObject = mapper.readValue(json,VoteResult.class);
		
		assertTrue(voteResultObject.isSuccessfull());
		assertThat(voteResultObject.getMessage()).contains("You have successfully voted");
		
		int voteCountAfter = voteResultObject.getVoteCount();
		assertEquals(voteCountAfter,voteCountBefore +1);
	}
	
	@Test
	@WithMockUser(username = "n21dcvt128@student.ptithcm.edu.vn",password = "987654321")
	public void testUndoVoteUp() throws Exception {
		Integer reviewId = 3;
		String requestURL = "/vote_review/3/up";
		
		Review reviewInDb = reviewRepository.findById(reviewId).get();
		int voteCountBefore = reviewInDb.getVotes();
		MvcResult result = mockMvc.perform(post(requestURL).with(csrf()))
		       .andExpect(status().isOk())
		       .andDo(print())
		       .andReturn();
		
		String json = result.getResponse().getContentAsString();
		VoteResult voteResultObject = mapper.readValue(json,VoteResult.class);
		
		assertTrue(voteResultObject.isSuccessfull());
		assertThat(voteResultObject.getMessage()).contains("You have unvoted");
		
		int voteCountAfter = voteResultObject.getVoteCount();
		assertEquals(voteCountAfter ,voteCountBefore -1);
	}
	
	
}
