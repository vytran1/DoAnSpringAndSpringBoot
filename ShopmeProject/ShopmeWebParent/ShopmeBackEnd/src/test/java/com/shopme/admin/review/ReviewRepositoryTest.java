package com.shopme.admin.review;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ReviewRepositoryTest {
     
	@Autowired
	private ReviewRepository repository;
	
	
	@Test
	public void firstTest() {
	    Integer customerId = 9;
	    Customer customer = new Customer(customerId);
	    Integer productId = 12;
	    Product product = new Product(productId);
	    
	    Review review = new Review();
	    review.setProduct(product);
	    review.setCustomer(customer);
	    review.setReviewTime(new Date());
	    review.setHeadline("Perfect Lapto for my career.I love it");
	    review.setComment("Nice to have: laptop mạnh mẽ và tiện lợi");
	    review.setRating(4);
	    
	    Review savedReview = repository.save(review);
	    
	    assertThat(savedReview).isNotNull();
	    assertThat(savedReview.getId()).isGreaterThan(0);
	}
	
	@Test
	public void getAllReview() {
		List<Review> listReviews = repository.findAll();
		assertThat(listReviews.size()).isGreaterThan(0);
		listReviews.forEach(item -> System.out.println(item.toString()));
	}
	
	@Test
	public void updateReview() {
		Integer reviewId = 1;
		String headline = "An awesome bluetooh device";
		String command = "Price is affordable";
		
		Review reviewById = repository.findById(reviewId).get();
		reviewById.setHeadline(headline);
		reviewById.setComment(command);
		
		Review updateReview = repository.save(reviewById);
		
		assertThat(updateReview.getHeadline()).isEqualTo(headline);
		assertThat(updateReview.getComment()).isEqualTo(command);

	}
	
	@Test
	public void deleteReview() {
		Integer reviewId = 2;
		repository.deleteById(reviewId);
		
		Optional<Review> reviewDelete = repository.findById(reviewId);
		
		assertThat(reviewDelete).isNotPresent();
	}
}
