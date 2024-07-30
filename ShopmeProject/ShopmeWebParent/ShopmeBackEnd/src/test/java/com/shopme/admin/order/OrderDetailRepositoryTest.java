package com.shopme.admin.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.order.OrderDetail;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderDetailRepositoryTest {
	  @Autowired
      private OrderDetailRepository orderDetailRepository;
	  
	  
	  @Test
	  public void findWithCategoryAndTimeBetween() throws ParseException {
		    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	    	Date startTime = dateFormatter.parse("2024-04-01");
	    	Date endTime = dateFormatter.parse("2024-04-20");
	    	
	    	List<OrderDetail> listOrderDetail = orderDetailRepository.findWithCategoryAndTimeBetween(startTime, endTime);
	    	assertThat(listOrderDetail.size()).isGreaterThan(0);
	    	for(OrderDetail detail : listOrderDetail) {
	    		System.out.printf("%s | %d | %.2f | %.2f | %.2f \n",
	    				detail.getProduct().getCategory().getName(),detail.getQuantity(),detail.getProductCost(),detail.getShippingCost(),detail.getSubtotal());
	    		
	    	}
	  }
	  
	  @Test
	  public void findWithProductAndTimeBetween() throws ParseException {
		    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	    	Date startTime = dateFormatter.parse("2024-04-01");
	    	Date endTime = dateFormatter.parse("2024-04-20");
	    	
	    	List<OrderDetail> listOrderDetail = orderDetailRepository.findWithProductAndTimeBetween(startTime, endTime);
	    	assertThat(listOrderDetail.size()).isGreaterThan(0);
	    	for(OrderDetail detail : listOrderDetail) {
	    		System.out.printf("%d | %s | %.2f | %.2f | %.2f \n",
	    				detail.getQuantity(),detail.getProduct().getName(),detail.getProductCost(),detail.getShippingCost(),detail.getSubtotal());
	    		
	    	}
	  }
	  
}
