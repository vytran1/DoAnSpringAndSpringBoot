package com.shopme.checkout;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CheckoutService {
     
	private static final int DIM_DIVISOR = 139;
	
	public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRates) {
		CheckoutInfo checkoutInfo = new CheckoutInfo();
		
		float productCost = calculateProductCost(cartItems);
		float productTotal = calculateProductTotal(cartItems);
		float shippingCostTotal = calculateShippingCost(cartItems,shippingRates);
		float paymentTotal = productTotal + shippingCostTotal;
		
		checkoutInfo.setProductCost(productCost);
		checkoutInfo.setProductTotal(productTotal);
		checkoutInfo.setDeliverDays(shippingRates.getDays());
		checkoutInfo.setCodSupported(shippingRates.isCodSupported());
		checkoutInfo.setShippingCostTotal(shippingCostTotal);
		checkoutInfo.setPaymentTotal(paymentTotal);
		 
		return checkoutInfo;
	}

	private float calculateShippingCost(List<CartItem> cartItems, ShippingRate shippingRates) {
		float shippingCostTotal = 0.0f;
		
		for(CartItem item : cartItems) {
			Product itemProduct = item.getProduct();
			float dimWeight = (itemProduct.getLength() * itemProduct.getWidth() * itemProduct.getHeight()) / DIM_DIVISOR;
			float finalWeight = itemProduct.getWeight() > dimWeight ? itemProduct.getWeight() : dimWeight;
			float shippingCost = finalWeight * item.getQuantity() * shippingRates.getRate();
			item.setShippingCost(shippingCost);
			shippingCostTotal += shippingCost;
		}
		
		return shippingCostTotal;
	}

	private float calculateProductTotal(List<CartItem> cartItems) {
		float total = 0.0f;
		
		for(CartItem item : cartItems) {
	        total += item.getSubTotal();
	    }
		
		return total;
	}

	private float calculateProductCost(List<CartItem> cartItems) {
        float cost = 0.0f;
		
        for(CartItem item : cartItems) {
        	cost += item.getQuantity() * item.getProduct().getCost();
        }
		return cost;
	}
}
