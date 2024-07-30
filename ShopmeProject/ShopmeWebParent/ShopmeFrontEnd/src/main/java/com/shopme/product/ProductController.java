package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopme.Utility;
import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Question;
import com.shopme.common.entity.Review;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.question.QuestionService;
import com.shopme.review.ReviewService;
import com.shopme.review.vote.ReviewVoteService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ProductController {
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ReviewVoteService reviewVoteService;
    
    @Autowired
    private QuestionService questionService;
    
    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String categoryAlias,Model model) {
    	return viewCategoryByPage(categoryAlias,model,1);
    }
    
    
    
    
    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String categoryAlias,Model model, @PathVariable("pageNum")Integer pageNum) {
    	try {
    	Category category = categoryService.getCategory(categoryAlias);
    	List<Category> parents = categoryService.getCategoryParents(category);
    	Page<Product> products = productService.listByCategory(pageNum, category.getId());
    	List<Product> listProducts = products.getContent();
    	
//    	System.out.println(products.getTotalElements());
//    	System.out.println(products.getTotalPages());
//    	listProducts.forEach((x)->System.out.println(x.getName()));
        long startCount = (pageNum-1) * ProductService.PRODUCT_PER_PAGE +1 ;
   	    long endCount = startCount + ProductService.PRODUCT_PER_PAGE - 1 ;
   	    if(endCount > products.getTotalElements() ) {
   		   endCount = products.getTotalElements();
   	    }
   	    
   	    
   	    model.addAttribute("totalPages", products.getTotalPages());
   	    model.addAttribute("totalItems", products.getTotalElements());
   	    model.addAttribute("currentPage",pageNum);
   	    model.addAttribute("startCount",startCount);
	    model.addAttribute("endCount",endCount);
   	   
    	model.addAttribute("pageTitle",category.getName());
    	model.addAttribute("listCategoryParents",parents);
    	model.addAttribute("listProducts",listProducts);
    	model.addAttribute("category",category);
    	return "products/product_by_category";
    	}catch(CategoryNotFoundException e) {
    		return "error/404";
    	}
    }
    
    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias")String alias,Model model,HttpServletRequest request) {
    	try {
    	Product product = productService.getProduct(alias);
    	List<Category> parents = categoryService.getCategoryParents(product.getCategory());
    	Page<Review> listReviews =  reviewService.listMostRecentReviewByProduct(product);
    	List<Question> listQuestions = questionService.listTop3RecentQuestions(product.getId());
    	Customer customerAuthenticated = getAuthenticatedCustomer(request);
    	if(customerAuthenticated != null) {
    		boolean didCustomerReviewThisProduct = reviewService.didCustomerReviewProduct(customerAuthenticated,product.getId());
    		reviewVoteService.markReviewsVotedByCurrentCustomer(listReviews.getContent(),product.getId(),customerAuthenticated.getId());
    		if(didCustomerReviewThisProduct) {
    			model.addAttribute("customerReviewed",didCustomerReviewThisProduct);
    		}
    		else {
    			boolean canCustomerReviewThisProduct = reviewService.canCustomerReviewProduct(customerAuthenticated,product.getId());
    			model.addAttribute("customerCanReviewed",canCustomerReviewThisProduct);
    		}
    	}
    	int numberOfQuestions = questionService.getNumberOfQuestions(product.getId());
    	model.addAttribute("listQuestions", listQuestions);
		model.addAttribute("numberOfQuestions", numberOfQuestions);
    	model.addAttribute("listCategoryParents",parents);
        model.addAttribute("product",product);
        model.addAttribute("listReviews",listReviews);
        model.addAttribute("pageTitle",product.getShortName());
    	return "products/product_detail";
    	}catch(ProductNotFoundException ex) {
    		return "error/404";
    	}
    }
    
    @GetMapping("/search")
    public String searchFirstPage(@RequestParam("keyWord") String keyWord,Model model) {
    	return searchByPage(keyWord, 1, model);
    }
    
    
    
    @GetMapping("/search/{pageNum}")
    public String searchByPage(@RequestParam("keyWord") String keyWord,@PathVariable("pageNum") int pageNum,Model model) {
    	Page<Product> pageProducts = productService.search(keyWord, pageNum);
    	List<Product> products = pageProducts.getContent();
    	
    	 long startCount = (pageNum-1) * ProductService.SEARCH_RESULTS_PER_PAGE +1 ;
    	 long endCount = startCount + ProductService.SEARCH_RESULTS_PER_PAGE - 1 ;
    	 if(endCount > pageProducts.getTotalElements() ) {
    		   endCount = pageProducts.getTotalElements();
    	 }
    	 model.addAttribute("totalPages", pageProducts.getTotalPages());
    	 model.addAttribute("totalItems", pageProducts.getTotalElements());
    	 model.addAttribute("currentPage",pageNum);
         model.addAttribute("startCount",startCount);
 	     model.addAttribute("endCount",endCount);
     	 model.addAttribute("pageTitle",keyWord + "Search Result");
    	
    	
    	
    	
    	model.addAttribute("keyWord",keyWord);
    	model.addAttribute("listProducts",products);
    	return "products/search_result";
    }
    private Customer getAuthenticatedCustomer(HttpServletRequest request)  {
      	 String customerEmail = Utility.getEmailOfAuthenticationCustomer(request);
      	 return customerService.getCustomerByEmail(customerEmail);
       }
}
