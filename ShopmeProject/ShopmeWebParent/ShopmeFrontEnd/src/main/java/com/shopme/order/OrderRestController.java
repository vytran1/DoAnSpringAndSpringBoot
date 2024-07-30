package com.shopme.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.OrderNotFoundException;
import com.shopme.customer.CustomerService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class OrderRestController {
     @Autowired
     private OrderService orderService;
     
     @Autowired
     private CustomerService customerService;
     
     
     @PostMapping("/orders/return")
     public ResponseEntity<?> handleOrderReturnRequest(@RequestBody OrderReturnRequest returnRequest, HttpServletRequest request) {
    	 System.out.println("Order id" + returnRequest.getOrderId());
    	 System.out.println("Reason " + returnRequest.getReason());
    	 System.out.println("Note " + returnRequest.getNote());
    	 Customer customer = getAuthenticatedCustomer(request);
    	 if(customer == null) {
    		 return new ResponseEntity<>("Authentication required",HttpStatus.BAD_REQUEST);
    	 }
    	 try {
			orderService.setOrderReturnRequested(returnRequest, customer);
		 } catch (OrderNotFoundException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		 }
    	 return new ResponseEntity<>(new OrderReturnResponse(returnRequest.getOrderId()),HttpStatus.OK);
     }
     
     
     private Customer getAuthenticatedCustomer(HttpServletRequest request)  {
       	 String customerEmail = Utility.getEmailOfAuthenticationCustomer(request);
       	 return customerService.getCustomerByEmail(customerEmail);
     }
}
