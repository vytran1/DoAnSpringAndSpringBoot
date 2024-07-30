package com.shopme.admin.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.product.ProductRepository;
import com.shopme.common.entity.Review;
import com.shopme.common.exception.ReviewNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReviewService {
    
	public static final int REVIEWS_PER_PAGE = 3;
	
	@Autowired
	private ReviewRepository repository;
	
	
	@Autowired
	private ProductRepository productRepository;
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, REVIEWS_PER_PAGE, repository);
	}
	
	public Review get(Integer id) throws ReviewNotFoundException {
		try {
			return repository.findById(id).get();
		} catch (Exception e) {
			throw new ReviewNotFoundException("Could not find any reviews with ID " + id);
		}
	}
	
	public void save(Review review) {
		Review reviewInDB = repository.findById(review.getId()).get();
		reviewInDB.setHeadline(review.getHeadline());
		reviewInDB.setComment(review.getComment());
		
		repository.save(reviewInDB);
		productRepository.updateReviewCountAndAverageRating(reviewInDB.getProduct().getId());
	}
	
	public void delete(Integer id) throws ReviewNotFoundException {
		if(!repository.existsById(id)) {
			throw new ReviewNotFoundException("No review exist with id: " + id);
		}
		repository.deleteById(id);
	}
}
