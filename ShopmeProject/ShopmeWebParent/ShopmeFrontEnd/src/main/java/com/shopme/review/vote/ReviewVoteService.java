package com.shopme.review.vote;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.ReviewVote;
import com.shopme.review.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReviewVoteService {
    
	@Autowired
	private ReviewVoteRepository reviewVoteRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	
	public VoteResult undoVote(ReviewVote vote, Integer reviewId, VoteType voteType) {
		reviewVoteRepository.delete(vote);
		reviewRepository.updateVoteCount(reviewId);
		Integer voteCount = reviewRepository.getVoteCount(reviewId);
		
		return VoteResult.success("You have unvoted " + voteType + " that review",voteCount);
	}
	
	public VoteResult doVote(Integer reviewId,Customer customer,VoteType voteType) {
		Review review = null;
		try {
			review = reviewRepository.findById(reviewId).get();
		}catch(NoSuchElementException e) {
			return VoteResult.fail("The review with ID " + reviewId + " have not found");
		}
		
		ReviewVote voteObject = reviewVoteRepository.findByReviewAndCustomer(reviewId,customer.getId());
		if(voteObject != null) {
			if(voteObject.isUpVoted() && voteType.equals(VoteType.UP) ||  voteObject.isDownVoted() && voteType.equals(VoteType.DOWN)) {
				return undoVote(voteObject, reviewId, voteType);
			}else if(voteObject.isUpVoted() && voteType.equals(VoteType.DOWN)) {
				voteObject.voteDown();
			}else if(voteObject.isDownVoted() && voteType.equals(VoteType.UP)) {
				voteObject.voteUp();
			}
		}else {
			voteObject = new ReviewVote();
			voteObject.setReview(review);
			voteObject.setCustomer(customer);
			if(voteType.equals(VoteType.UP)) {
				voteObject.voteUp();
			}else {
				voteObject.voteDown();
			}
			
		}
		reviewVoteRepository.save(voteObject);
		reviewRepository.updateVoteCount(reviewId);
		Integer voteCount =reviewRepository.getVoteCount(reviewId);
		return VoteResult.success("You have successfully voted  " + voteType + " that review",voteCount);
	}
    
	
	public void markReviewsVotedByCurrentCustomer(List<Review> listOfReviews,Integer productId,Integer customerId) {
		List<ReviewVote> listOfReviewVotes = reviewVoteRepository.findByProductAndCustomer(productId, customerId);
		for(ReviewVote vote : listOfReviewVotes) {
			Review votedReview = vote.getReview();
			if(listOfReviews.contains(votedReview)) {
				int indexOfVote = listOfReviews.indexOf(votedReview);
				Review review = listOfReviews.get(indexOfVote);
				if(vote.isUpVoted()) {
					review.setUpvotedByCurrentCustomer(true);
				}else {
					review.setDownvotedByCurrentCustomer(true);
				}
			}
		}
	}
}
