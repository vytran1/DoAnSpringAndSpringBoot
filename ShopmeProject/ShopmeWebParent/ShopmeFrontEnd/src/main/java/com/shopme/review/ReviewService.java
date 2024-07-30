package com.shopme.review;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ReviewNotFoundException;
import com.shopme.order.OrderDetailRepository;
import com.shopme.product.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReviewService {
    
	public static final int REVIEW_PER_PAGE =3;
	
	@Autowired
	private ReviewRepository repository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	public Page<Review> listByCustomerByPage(Customer customer,String keyWord, int pageNum,String sortField,String sortDir){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum -1, REVIEW_PER_PAGE, sort);
		
		if(keyWord != null) {
			return repository.findByCustomer(customer.getId(), keyWord, pageable);
			
		}
		return repository.findByCustomer(customer.getId(), pageable);
	}
	
	
	public Review getByCustomerAndId(Customer customer,Integer reviewId) throws ReviewNotFoundException {
		Review review = repository.findByCustomerAndId(customer.getId(), reviewId);
		if(review == null) {
			throw new ReviewNotFoundException("Not exist review with customer id : " + customer.getId() + " and review Id: " + reviewId);
		}
		return review;
	}
	
	public Page<Review> listMostRecentReviewByProduct(Product product){
		Sort sort = Sort.by("votes").descending();
		Pageable pageable = PageRequest.of(0, REVIEW_PER_PAGE,sort);
		
		return repository.findByProduct(product, pageable);
	}
	
	public Page<Review> listByProduct(Product product,int pageNum,String sortField,String sortDir){
		Sort sort = Sort.by(sortField);
		sort = sortField.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum -1 ,REVIEW_PER_PAGE,sort);
		return repository.findByProduct(product, pageable);
	}
	
	public boolean didCustomerReviewProduct(Customer customer,Integer productId) {
		Long count = repository.countByCustomerAndProduct(customer.getId(), productId);
		return count > 0;
	}
	
	public boolean canCustomerReviewProduct(Customer customer,Integer productId) {
		Long count =orderDetailRepository.countByProductAndCustomerAndOrderStatus(productId,customer.getId(),OrderStatus.DELIVERED);
		return count >0;
	}
	
	public Review save(Review review) {
		review.setReviewTime(new Date());
		Review savedReview = repository.save(review);
		
		Integer productId = savedReview.getProduct().getId();
		productRepository.updateReviewCountAndAverageRating(productId);
		
		
		return savedReview;
	}
}
