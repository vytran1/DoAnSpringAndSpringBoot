package com.shopme.admin.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {
	 @Autowired
     private OrderService orderService;
	 
	 @PostMapping("/orders_shipper/update/{id}/{status}")
	 public Response updateOrderStatus(@PathVariable("id") Integer orderId, @PathVariable("status") String orderStatus) {
		 orderService.updateStatus(orderId, orderStatus);
		 return new Response(orderId, orderStatus);
	 }
	 
}

class Response{
	private Integer orderId;
	private String orderStatus;
	
	
	
	public Response(Integer orderId, String orderStatus) {
		super();
		this.orderId = orderId;
		this.orderStatus = orderStatus;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	
}