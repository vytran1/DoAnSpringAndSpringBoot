package com.shopme.shoppingcart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.shipping.ShippingRateService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ShoppingCartController {
     @Autowired
     private ShoppingCartService shoppingCartService;
     
     @Autowired
     private CustomerService customerService;
     
     @Autowired
     private AddressService addressService;
     
     @Autowired
     private ShippingRateService shippingRateService;
     
     @GetMapping("/cart")
     public String viewCart(Model model,HttpServletRequest request) {
    	Customer customer = getAuthenticatedCustomer(request);
    	List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
        System.out.println(cartItems);
    	float estimateTotal = 0.0F;
    
    	for(CartItem item : cartItems) {
    		estimateTotal += item.getSubTotal();
    	}
    	
    	boolean usePrimaryAddress = false;
    	Address defaultAddressCustomer = addressService.getDefaultShippingAddress(customer);
    	ShippingRate shippingRate = null;
    	
    	if(defaultAddressCustomer != null) {
    		shippingRate = shippingRateService.getShippingRateForAddress(defaultAddressCustomer);
    	}else {
    		usePrimaryAddress = true;
    		shippingRate = shippingRateService.getShippingRateForCustomer(customer);
    	}
    	model.addAttribute("shippingSupported",shippingRate != null);
    	model.addAttribute("usePrimaryAddress",usePrimaryAddress);
    	
    	model.addAttribute("cartItems",cartItems);
    	model.addAttribute("estimateTotal",estimateTotal);
    	
    	return "cart/shopping_cart";
     }
     
     
     private Customer getAuthenticatedCustomer(HttpServletRequest request)  {
    	 String customerEmail = Utility.getEmailOfAuthenticationCustomer(request);
    	 return customerService.getCustomerByEmail(customerEmail);
     }
}
