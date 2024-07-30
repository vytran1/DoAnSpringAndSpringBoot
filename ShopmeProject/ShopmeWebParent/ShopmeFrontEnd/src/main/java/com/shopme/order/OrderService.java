package com.shopme.order;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.checkout.CheckoutInfo;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.OrderTrack;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.OrderNotFoundException;

@Service
public class OrderService {
    @Autowired
	private OrderRepository orderRepository;
	
    public static final int ORDERS_PER_PAGE = 3;
    
	public Order createOrder(Customer customer, Address address,List<CartItem> cartItems,PaymentMethod paymentMethod,CheckoutInfo checkout) {
		   Order newOrder = new Order();
		   newOrder.setOrderTime(new Date());
		   
		   if(paymentMethod.equals(PaymentMethod.PAYPAL)) {
			   newOrder.setOrderStatus(OrderStatus.PAID);
		   }else {
			   newOrder.setOrderStatus(OrderStatus.NEW);
		   }
           newOrder.setCustomer(customer);
           
           
           newOrder.setProductCost(checkout.getProductCost());
           newOrder.setSubtotal(checkout.getProductTotal());
           newOrder.setShippingCost(checkout.getShippingCostTotal());
           newOrder.setTax(0.0f);
           newOrder.setTotal(checkout.getPaymentTotal());
           newOrder.setPaymentMethod(paymentMethod);
           newOrder.setDeliveryDays(checkout.getDeliverDays());
           newOrder.setDeliveryDate(checkout.getDeliverDate());
           
           if(address == null) {
        	   newOrder.copyAddressFromCustomer();
           }else {
        	   newOrder.copyShippingAddressFromAddressObject(address);
           }
           
           Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
           
           for(CartItem item : cartItems) {
        	   Product product = item.getProduct();
        	   
        	   OrderDetail detail = new OrderDetail();
        	   detail.setOrder(newOrder);
        	   detail.setProduct(product);
        	   detail.setQuantity(item.getQuantity());
        	   detail.setUnitPrice(product.getDiscountPrice());
        	   detail.setProductCost(product.getCost() * item.getQuantity());
        	   detail.setSubtotal(item.getSubTotal());
        	   detail.setShippingCost(item.getShippingCost());
        	   orderDetails.add(detail);
           }
           
           OrderTrack track = new OrderTrack();
           track.setOrder(newOrder);
           track.setOrderStatus(OrderStatus.NEW);
           track.setNotes(OrderStatus.NEW.defaultDescription());
           track.setUpdateTime(new Date());
           
           newOrder.getOrderTracks().add(track);
           
		   return orderRepository.save(newOrder);
	}
	
	public Page<Order> listForCustomerByPage(Customer customer,int pageNum,String sortField,String sortDir, String keyWord){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable =PageRequest.of(pageNum - 1,ORDERS_PER_PAGE,sort);
		
		if(keyWord != null) {
			return orderRepository.findAll(keyWord,customer.getId(),pageable);
		}
		
		return orderRepository.findAll(customer.getId(), pageable);
	
	}
	
	public Order getOrder(Integer orderId, Customer customer) {
		return orderRepository.findByIdAndCustomer(orderId, customer);
	}
	
	public void setOrderReturnRequested(OrderReturnRequest request,Customer customer) throws OrderNotFoundException {
		Order order = orderRepository.findByIdAndCustomer(request.getOrderId(), customer);
		if(order == null) {
		    throw new OrderNotFoundException("Order not found with order id: " + request.getOrderId());
		}
		if(order.isReturnRequested()) {
			return;
		}
		OrderTrack orderTrack = new OrderTrack();
		orderTrack.setOrder(order);
		orderTrack.setUpdateTime(new Date());
		orderTrack.setOrderStatus(OrderStatus.RETURN_REQUESTED);
		String notes ="Reason" +  request.getReason();
		if(!"".equals(request.getNote())) {
			notes += "." +request.getNote();
		}
		orderTrack.setNotes(notes);
		
		order.getOrderTracks().add(orderTrack);
		order.setOrderStatus(OrderStatus.RETURN_REQUESTED);
		orderRepository.save(order);
	}
}
