package com.shopme.question;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.ControllerHelper;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Question;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.product.ProductService;
import com.shopme.review.ReviewService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class QuestionController {
    @Autowired 
	private QuestionService questionService;
     
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ControllerHelper controllerHelper;
    
    
    @GetMapping("/questions/{productAlias}") 
    public String listQuestionsOfProduct(@PathVariable(name = "productAlias") String productAlias, Model model, HttpServletRequest request) {
    	return listQuestionsOfProductByPage(model, request, productAlias, 1, "votes", "desc");
    }
    
    @GetMapping("/questions/{productAlias}/page/{pageNum}") 
    public String listQuestionsOfProductByPage(Model model,HttpServletRequest request,@PathVariable("productAlias") String productAlias,
    		@PathVariable("pageNum") int pageNum, String sortField, String sortDir ) {
    	Product product = null;
    	Page<Question> page = questionService.listQuestionsOfProduct(productAlias, pageNum, sortField, sortDir);
    	List<Question> listQuestions = page.getContent();
    	try {
			product = productService.getProduct(productAlias);
		} catch (ProductNotFoundException e) {
			// TODO Auto-generated catch block
			return "error/404";
		}
    	Customer customer = controllerHelper.getAuthenticatedCustomer(request);
    	model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("listQuestions",listQuestions);
		model.addAttribute("product",product);
		long startCount = (pageNum -1) * QuestionService.QUESTION_PER_PAGE_FOR_PRODUCT + 1;
    	long endCount = startCount + QuestionService.QUESTION_PER_PAGE_FOR_PRODUCT -1;
    	if(endCount > page.getTotalElements()) {
    		endCount = page.getTotalElements();
    	}
    	model.addAttribute("startCount",startCount);
    	model.addAttribute("endCount",endCount);
    	model.addAttribute("pageTitle","Question for " + product.getShortName());
    	return "product/product_questions";
    }
    
    @GetMapping("/customer/questions") 
    public String listQuestionsByCustomer(Model model, HttpServletRequest request) {
    	return listQuestionsByCustomerByPage(model, request, 1, null, "askTime", "desc");
    }
    
    @GetMapping("/customer/questions/page/{pageNum}") 
    public String listQuestionsByCustomerByPage(Model model, HttpServletRequest request,
    		@PathVariable(name = "pageNum") int pageNum,
    		String keyWord, String sortField, String sortDir
    		) {
    	Customer customer = controllerHelper.getAuthenticatedCustomer(request);
    	Page<Question> page = questionService.listQuestionsByCustomer(customer, keyWord, pageNum, sortField, sortDir);
    	List<Question> listQuestions = page.getContent();
    	model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyWord", keyWord);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("moduleURL", "/questions");
		model.addAttribute("listQuestions",listQuestions);
		long startCount = (pageNum - 1) * QuestionService.QUESTION_PER_PAGE_FOR_CUSTOMER+ 1;
		model.addAttribute("startCount", startCount);
		long endCount = startCount + QuestionService.QUESTION_PER_PAGE_FOR_CUSTOMER - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		model.addAttribute("endCount", endCount);
		return "questions/customer_questions";
    }
    
    @GetMapping("/customer/questions/detail/{id}")
    public String viewQuestion(@PathVariable("id") Integer id,Model model, RedirectAttributes ra,HttpServletRequest request) {
    	Customer customer = controllerHelper.getAuthenticatedCustomer(request);
    	Question question = questionService.getByCustomerAndId(customer, id);
    	if (question != null) {	
			model.addAttribute("question", question);
			return "questions/question_detail_modal";
		}
    	else {
    		ra.addFlashAttribute("message", "Could not find any question with ID " + id);
    		return "redirect:/customer/questions";
    	}
    }
    
    @GetMapping("/ask_question/{productAlias}")
	public String askQuestion(@PathVariable(name = "productAlias") String productAlias) {
		
		return "redirect:/p/" + productAlias + "#qa";
	}
}
