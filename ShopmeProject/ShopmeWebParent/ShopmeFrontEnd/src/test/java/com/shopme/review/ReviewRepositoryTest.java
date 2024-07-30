package com.shopme.review;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Review;
import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ReviewRepositoryTest {
    
	@Autowired
	private ReviewRepository repository;
	
	@Test
	public void testListAllByCustomerId() {
		Integer customerId = 10;
		Pageable pageable = PageRequest.of(0,3);
		Page<Review> reviews = repository.findByCustomer(customerId, pageable);
		
		long totalElements = reviews.getTotalElements();
		assertThat(totalElements).isGreaterThan(0);
	}
	
	@Test
	public void testListAllByCustomerIdAndKeyWord() {
		Integer customerId = 10;
		String keyWord = "An awesome";
		Pageable pageable = PageRequest.of(0,3);
		Page<Review> reviews = repository.findByCustomer(customerId,keyWord,pageable);
		
		long totalElements = reviews.getTotalElements();
		assertThat(totalElements).isGreaterThan(0);
	}
	
	@Test
	public void testFindByCustomerAndId() {
		Integer customerId = 10;
		Integer orderId = 1;
		
		Review review = repository.findByCustomerAndId(customerId, orderId);
		assertThat(review).isNotNull();
	}
	
	@Test
	public void testFindByProduct() {
		Integer productId = 22;
		Product product = new Product(productId);
		Pageable pageable = PageRequest.of(0, 3);
		Page<Review> listReview = repository.findByProduct(product, pageable);
		List<Review> reviews = listReview.getContent();
		
		reviews.forEach(item -> System.out.println(item));
	}
	
	@Test
	public void testCountByCustomerAndProduct() {
		Integer customerId = 21;
		Integer productId = 22;
		
		Long count = repository.countByCustomerAndProduct(customerId, productId);
		assertThat(count).isGreaterThan(0);
	}
	
	@Test
	public void updateReviewVoteCount() {
		Integer reviewId = 3;
		
		repository.updateVoteCount(reviewId);
		
		Review review = repository.findById(reviewId).get();
		assertThat(review.getVotes()).isEqualTo(1);
	}
	
	@Test
	public void testGetVoteCount() {
		Integer reviewId = 3;
		
		Integer countVote = repository.getVoteCount(reviewId);
		assertThat(countVote).isEqualTo(1);
	}
}
