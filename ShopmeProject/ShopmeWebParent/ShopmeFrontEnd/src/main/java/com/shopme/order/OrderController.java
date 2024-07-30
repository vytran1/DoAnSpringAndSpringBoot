package com.shopme.order;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.product.Product;
import com.shopme.customer.CustomerService;
import com.shopme.review.ReviewService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OrderController {
    @Autowired
	private OrderService orderService;
    
    @Autowired 
    private CustomerService customerService;
    
    @Autowired
    private ReviewService reviewService;
    
    @GetMapping("/orders")
    public String listFirstPage(Model model, HttpServletRequest request) {
    	return listOrdersByPage(model,request,1,"orderTime","desc",null);
    }
    
    @GetMapping("/orders/page/{pageNum}")
    public String listOrdersByPage(Model model, 
    		                       HttpServletRequest request,
    		                       @PathVariable("pageNum") int pageNum,
    		                       @RequestParam("sortField") String sortField,
    		                       @RequestParam("sortDir") String sortDir,
    		                       @RequestParam(name = "keyWord",required = false) String keyWord) {
    	Customer customer = getAuthenticatedCustomer(request);
    	Page<Order> pages = orderService.listForCustomerByPage(customer, pageNum, sortField, sortDir, keyWord);
    	
    	List<Order> listOrders = pages.getContent();
    	long startCount = (pageNum -1) * orderService.ORDERS_PER_PAGE + 1;
    	long endCount = startCount + orderService.ORDERS_PER_PAGE -1;
    	if(endCount > pages.getTotalElements()) {
    		endCount = pages.getTotalElements();
    	}
       model.addAttribute("totalPages",pages.getTotalPages());
  	   model.addAttribute("totalItems",pages.getTotalElements());
  	   model.addAttribute("currentPage",pageNum);
  	   model.addAttribute("sortField",sortField);
  	   model.addAttribute("sortDir",sortDir);
  	   model.addAttribute("keyWord",keyWord);
  	   model.addAttribute("listOrders",listOrders);
  	   model.addAttribute("reverseSortDir",sortDir.equals("asc") ? "desc" : "asc"  );
  	   model.addAttribute("startCount",startCount);
  	   model.addAttribute("endCount",endCount);
  	   model.addAttribute("moduleURL","/orders");
  	   
  	   return "orders/orders_customer";
    }
    
    private Customer getAuthenticatedCustomer(HttpServletRequest request)  {
   	 String customerEmail = Utility.getEmailOfAuthenticationCustomer(request);
   	 return customerService.getCustomerByEmail(customerEmail);
    }
    
    @GetMapping("/orders/detail/{id}")
    public String getViewOrderDetail(Model model,@PathVariable("id") Integer orderId, HttpServletRequest request) {
    	Customer customer = getAuthenticatedCustomer(request);
    	
    	Order order = orderService.getOrder(orderId, customer);
    	setProductReviewableStatus(customer,order);
    	model.addAttribute("order",order);
    	
    	return "orders/orders_details_modal";
    }

	private void setProductReviewableStatus(Customer customer, Order order) {
		// TODO Auto-generated method stub
		Iterator<OrderDetail> listOrderDetails = order.getOrderDetails().iterator();
		while(listOrderDetails.hasNext()) {
			OrderDetail orderDetail = listOrderDetails.next();
			Product product = orderDetail.getProduct();
			Integer productId = product.getId();
			
			boolean disCustomerReviewThisProduct = reviewService.didCustomerReviewProduct(customer, productId);
			product.setReviewedByCustomer(disCustomerReviewThisProduct);
			
			if(!disCustomerReviewThisProduct) {
				boolean canCustomerReviewThisProduct =reviewService.canCustomerReviewProduct(customer, productId);
				product.setCustomerCanReview(canCustomerReviewThisProduct);
			}
		}
	}
}
