package com.shopme.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.order.OrderStatus;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderDetailRepositoryTest {
    
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Test
	public void testCountByProductAndCustomerAndOrderStatus() {
		Integer productId = 12;
		Integer customerId = 10;
		
		Long count = orderDetailRepository.countByProductAndCustomerAndOrderStatus(productId, customerId,OrderStatus.DELIVERED);
		assertThat(count).isGreaterThan(0);
	}
}
