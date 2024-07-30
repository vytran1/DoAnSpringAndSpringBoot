package com.shopme.review.vote;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.ReviewVote;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ReviewVoteRepositoryTest {
     
	@Autowired
	private ReviewVoteRepository reviewVoteRepository;
	
	
	@Test
	public void testSavedVote() {
		Integer customerId = 2;
		Integer reviewId = 3;
		
		ReviewVote newVote = new ReviewVote();
		newVote.setCustomer(new Customer(customerId));
		newVote.setReview(new Review(reviewId));
		newVote.voteUp();
		
		ReviewVote savedVote = reviewVoteRepository.save(newVote);
		assertThat(savedVote.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindByReviewAndCustomer() {
		Integer customerId =2;
		Integer reviewId = 3;
		
		ReviewVote vote = reviewVoteRepository.findByReviewAndCustomer(reviewId, customerId);
		assertThat(vote).isNotNull();
		System.out.println(vote);
	}
	
	
	@Test
	public void testFindByProductAndCustomer() {
		Integer customerId =2;
		Integer productId = 21;
		
		List<ReviewVote> vote = reviewVoteRepository.findByProductAndCustomer(productId, customerId);
		assertThat(vote.size()).isGreaterThan(0);
		vote.forEach(item -> System.out.println(item));
	}
}
