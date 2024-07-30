package com.shopme.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.ControllerHelper;
import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.common.exception.ReviewNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.product.ProductService;
import com.shopme.review.vote.ReviewVoteService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ReviewController {
    @Autowired
	private ReviewService reviewService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ReviewVoteService reviewVoteService;
    
    @Autowired
    private ControllerHelper controllerHelper;
    
    private String defaultRedirectURL ="redirect:/reviews/page/1?sortField=reviewTime&sortDir=asc";
    
    
    @GetMapping("/reviews")
    public String listFirstPage(Model model) {
    	return defaultRedirectURL;
    }
    
    
    @GetMapping("/reviews/page/{pageNum}")
    public String listReviewByCustomerByPage(Model model, HttpServletRequest request,
    		                                 @PathVariable("pageNum") int pageNum, 
    		                                 String keyWord, String sortField, String sortDir
    		) {
    	Customer authenticatedCustomer = getCustomerAuthenticated(request);
    	Page<Review> pages = reviewService.listByCustomerByPage(authenticatedCustomer, keyWord, pageNum, sortField, sortDir);
    	List<Review> listReviews = pages.getContent();
    	
    	model.addAttribute("totalPages", pages.getTotalPages());
		model.addAttribute("totalItems", pages.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyWord", keyWord);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("moduleURL", "/reviews");
		model.addAttribute("listReviews",listReviews);
		
		long startCount = (pageNum - 1) * ReviewService.REVIEW_PER_PAGE+ 1;
		model.addAttribute("startCount", startCount);
		long endCount = startCount + ReviewService.REVIEW_PER_PAGE - 1;
		if (endCount > pages.getTotalElements()) {
			endCount = pages.getTotalElements();
		}
		model.addAttribute("endCount", endCount);
		return "reviews/reviews_customer";
    }
    
    private Customer getCustomerAuthenticated(HttpServletRequest request) {
    	String customerEmail = Utility.getEmailOfAuthenticationCustomer(request);
    	Customer customer = customerService.getCustomerByEmail(customerEmail);
    	return customer;
    }
    
    
    @GetMapping("/reviews/detail/{id}")
    public String viewReview(@PathVariable("id") Integer id, Model model,RedirectAttributes ra, HttpServletRequest request) {
    	Customer customer = getCustomerAuthenticated(request);
    	try {
    		Review review = reviewService.getByCustomerAndId(customer, id);
    		model.addAttribute("review",review);
    		return "reviews/review_detail_modal";
    	}catch(ReviewNotFoundException e) {
    		ra.addFlashAttribute("message",e.getMessage());
    		return defaultRedirectURL;
    	}
    }
    
    @GetMapping("/ratings/{productAlias}/page/{pageNum}")
    public String listByProductByPage(Model model,@PathVariable("productAlias") String productAlias, @PathVariable("pageNum") int pageNum,String sortField, String sortDir,HttpServletRequest request) {
    	Product product = null;
    	try {
    		product = productService.getProduct(productAlias);
    	}catch(ProductNotFoundException e) {
    		return "error/404";
    	}
    	
    	Page<Review> page = reviewService.listByProduct(product, pageNum, sortField, sortDir);
    	List<Review> listReviews = page.getContent();
    	
    	Customer customer = controllerHelper.getAuthenticatedCustomer(request);
    	if(customer != null) {
    		reviewVoteService.markReviewsVotedByCurrentCustomer(listReviews,product.getId(),customer.getId());
    	}
    	
    	long startCount = (pageNum -1) * reviewService.REVIEW_PER_PAGE + 1;
    	long endCount = startCount + reviewService.REVIEW_PER_PAGE -1;
    	if(endCount > page.getTotalElements()) {
    		endCount = page.getTotalElements();
    	}
       model.addAttribute("totalPages",page.getTotalPages());
  	   model.addAttribute("totalItems",page.getTotalElements());
  	   model.addAttribute("currentPage",pageNum);
  	   model.addAttribute("sortField",sortField);
  	   model.addAttribute("sortDir",sortDir);
  	   
  	   model.addAttribute("listReviews",listReviews);
  	   model.addAttribute("reverseSortDir",sortDir.equals("asc") ? "desc" : "asc"  );
  	   model.addAttribute("startCount",startCount);
  	   model.addAttribute("endCount",endCount);
  	   
  	   model.addAttribute("product",product);
  	   model.addAttribute("pageTitle","Reviews for " + product.getShortName());
  	   
  	   return "reviews/reviews_product";
    } 
    
    @GetMapping("/ratings/{productAlias}")
    public String listByProductFirstPage(@PathVariable("productAlias") String alias, Model model,HttpServletRequest request) {
    	return listByProductByPage(model, alias, 1,"reviewTime","desc",request);
    }
    
    @GetMapping("/write_review/product/{productId}")
    public String showReviewForm(@PathVariable("productId") Integer productId,Model model,HttpServletRequest request) {
    	Review review = new Review();
    	Product productById = null;
    	try {
			 productById = productService.getProduct(productId);
		} catch (ProductNotFoundException e) {
			// TODO Auto-generated catch block
			return "error/404";
		}
    	Customer customerAuthenticated = getCustomerAuthenticated(request);
    	boolean didCustomerReviewThisProduct = reviewService.didCustomerReviewProduct(customerAuthenticated,productById.getId());
    	if(didCustomerReviewThisProduct) {
    		model.addAttribute("customerReviewed",didCustomerReviewThisProduct);
    	}
    	else {
    		boolean canCustomerReviewThisProduct = reviewService.canCustomerReviewProduct(customerAuthenticated,productById.getId());
    		if(canCustomerReviewThisProduct) {
    			model.addAttribute("customerCanReviewed",canCustomerReviewThisProduct);
    		}else {
    			model.addAttribute("noReviewPermission",true);
    		}
    	}
    	model.addAttribute("product",productById);
    	model.addAttribute("review",review);
    	return "reviews/review_form";
    }
    
    @PostMapping("/post_review")
    public String savedReview(Model model,Review review,Integer productId, HttpServletRequest request) {
    	Customer customer = getCustomerAuthenticated(request);
    	Product productById = null;
    	try {
			 productById = productService.getProduct(productId);
		} catch (ProductNotFoundException e) {
			// TODO Auto-generated catch block
			return "error/404";
		}
    	review.setProduct(productById);
    	review.setCustomer(customer);
    	Review savedReview = reviewService.save(review);
    	
    	model.addAttribute("review",savedReview);
    	return "reviews/review_done";
    }
}
