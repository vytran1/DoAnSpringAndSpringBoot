package com.shopme.admin.order;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.OrderTrack;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.exception.OrderNotFoundException;

@Service
public class OrderService {
	public static final int ORDER_PER_PAGE = 10;
    
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
    public void listByPage(int pageNum, PagingAndSortingHelper helper) {
    	String sortField = helper.getSortField();
    	String sortDir = helper.getSortDir();
    	String keyWord = helper.getKeyWord();
    	
    	Sort sort = null;
    	
    	if("destination".equals(sortField)) {
    		sort = Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
    	}else {
    		sort = Sort.by(sortField);
    	}
    	
    	sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    	Pageable pageable = PageRequest.of(pageNum-1,ORDER_PER_PAGE,sort);
    	Page<Order> page = null;
    	
    	if(keyWord != null) {
    		page = orderRepository.findAll(keyWord, pageable);
    	}else {
    		page = orderRepository.findAll(pageable);
    	}
    	helper.updateModelAttribute(pageNum, page);
    }
	
    public Order get(Integer orderID) throws OrderNotFoundException {
    	try {
    		return orderRepository.findById(orderID).get();
    	}catch(NoSuchElementException e) {
    		throw new OrderNotFoundException("Could not find any orders with ID " + orderID);
    	}
    }
    
    public void delete(Integer orderID) throws OrderNotFoundException {
    	Long count = orderRepository.countById(orderID);
    	if(count == null || count == 0) {
    		throw new OrderNotFoundException("No exist Order with Id "+orderID);
    	}else {
    		orderRepository.deleteById(orderID);
    	}
    	
    }
    
    public List<Country> listAllCountries(){
    	return countryRepository.findAllByOrderByNameAsc();
    }

	public void save(Order orderInForm) {
		// TODO Auto-generated method stub
		Order orderInDB = orderRepository.findById(orderInForm.getId()).get();
		orderInForm.setOrderTime(orderInDB.getOrderTime());
		orderInForm.setCustomer(orderInDB.getCustomer());
		
		orderRepository.save(orderInForm);
		
	}
	
	public void updateStatus(Integer orderId, String status) {
		Order orderInDB = orderRepository.findById(orderId).get();
		OrderStatus statusToUpdate = OrderStatus.valueOf(status);
		
		if(!orderInDB.hasStatus(statusToUpdate)) {
			List<OrderTrack> tracks = orderInDB.getOrderTracks();
			
			OrderTrack track = new OrderTrack();
			track.setOrder(orderInDB);
			track.setOrderStatus(statusToUpdate);
			track.setUpdateTime(new Date());
			track.setNotes(statusToUpdate.defaultDescription());
			
			tracks.add(track);
			orderInDB.setOrderStatus(statusToUpdate);
			
			orderRepository.save(orderInDB);
		}
	}
}
