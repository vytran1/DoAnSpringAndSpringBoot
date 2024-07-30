package com.shopme.admin.shippingrate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.admin.product.ProductRepository;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ShippingRateNotFoundException;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ShippingRateServiceTest {
    
	@MockBean private ShippingRateRepository shippingRateRepository;
	@MockBean private ProductRepository productRepository;
	@InjectMocks
	private ShippingRateService shippingRateService;
	
	
	
	@Test
	public void testCalculateShippingCostNotFound() {
		Integer productId = 5;
		Integer countryId = 1;
		String state = "TPHCMM";
		
        Mockito.when(shippingRateRepository.findByCountryAndState(countryId, state)).thenReturn(null);
        
        assertThrows(ShippingRateNotFoundException.class, new Executable() {
			
			@Override
			public void execute() throws Throwable {
				shippingRateService.calculateShippingCost(productId, countryId, state);
				
			}
		});
	}
	
	@Test
	public void testCalculateShippingRateFound() throws ShippingRateNotFoundException {
		Integer productId = 5;
		Integer countryId = 1;
		String state = "TPHCM";
		
		ShippingRate shippingRate = new ShippingRate();
		shippingRate.setRate(10.00f);
		Mockito.when(shippingRateRepository.findByCountryAndState(countryId, state)).thenReturn(shippingRate);
		Product product = new Product();
		product.setWeight(5);
		product.setWidth(4);
		product.setHeight(3);
		product.setLength(8);
		Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
		
		float shippingCost = shippingRateService.calculateShippingCost(productId, countryId, state);
		
		assertEquals(5,shippingCost);
	}
}
