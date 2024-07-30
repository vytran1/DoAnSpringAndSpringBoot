package com.shopme.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.ControllerHelper;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Question;
import com.shopme.common.exception.ProductNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class QuestionRestController {
    @Autowired
    private QuestionService questionService;
    
    
    @Autowired
    private ControllerHelper controllerHelper;
    
    
    @PostMapping("/post_question/{productId}")
    public ResponseEntity<?> postQuestion(@RequestBody Question question, @PathVariable(name = "productId") Integer productId, HttpServletRequest request){
    	Customer customer = controllerHelper.getAuthenticatedCustomer(request);
    	if (customer == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	try {
			questionService.saveNewQuestion(question, customer, productId);
		} catch (ProductNotFoundException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    	return new ResponseEntity<>(HttpStatus.OK);
    }
}
