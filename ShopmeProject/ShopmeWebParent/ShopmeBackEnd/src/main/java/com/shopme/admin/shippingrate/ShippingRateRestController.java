package com.shopme.admin.shippingrate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.exception.ShippingRateNotFoundException;

@RestController
public class ShippingRateRestController {
    
	@Autowired
	private ShippingRateService shippingRateService;
	
	
	@PostMapping("/get_shipping_cost")
	public String getShippingCost(Integer productId, Integer countryId, String stateId) throws ShippingRateNotFoundException {
		float shippingCost = shippingRateService.calculateShippingCost(productId, countryId, stateId);
		return String.valueOf(shippingCost);
	}
}
