package com.shopme.review.vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.ControllerHelper;
import com.shopme.common.entity.Customer;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ReviewVoteRestController {
    
	@Autowired
	private ReviewVoteService reviewVoteService;
	
	@Autowired
	private ControllerHelper helper;
	
	@PostMapping("/vote_review/{id}/{type}")
	public VoteResult voteReview(@PathVariable("id") Integer reviewId,@PathVariable("type") String voteType, HttpServletRequest request) {
		Customer authenticatedCustomer = helper.getAuthenticatedCustomer(request);
		if(authenticatedCustomer == null) {
			return VoteResult.fail("You must login to vote the review");
		}
		VoteType tyepVote = VoteType.valueOf(voteType.toUpperCase());
		return reviewVoteService.doVote(reviewId, authenticatedCustomer, tyepVote);
	}
	
	
}
