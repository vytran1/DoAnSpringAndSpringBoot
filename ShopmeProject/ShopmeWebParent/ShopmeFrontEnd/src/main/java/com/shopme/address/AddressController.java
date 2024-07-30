package com.shopme.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.Utility;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private CustomerService customerService;
    
    @GetMapping("/address_book")
    public String showAddressBook(Model model, HttpServletRequest request) {
    	Customer customer = getAuthenticatedCustomer(request);
    	List<Address> addresses = addressService.listAll(customer);
    	
    	boolean usePrimaryAddressAsDefault = true;
    	for(Address address: addresses) {
    		if(address.isDefaultForShipping()) {
    			usePrimaryAddressAsDefault = false;
    			break;
    		}
    	}
    	
    	model.addAttribute("listAddresses",addresses);
    	model.addAttribute("customer",customer);
    	model.addAttribute("usePrimaryAddressAsDefault",usePrimaryAddressAsDefault);
    	
    	return "address_book/addresses";
    }
    
    @GetMapping("/address_book/new")
    public String newAddress(Model model) {
    	Address address = new Address();
    	List<Country> listCountries = customerService.listAllCountries();
    	
    	model.addAttribute("address",address);
    	model.addAttribute("listCountries",listCountries);
    	model.addAttribute("pageTitle","Add new Address");
    	
    	return "address_book/address_form";
    }
    
    @PostMapping("/address_book/save")
    public String saveAddress(Address address,HttpServletRequest request,RedirectAttributes ra ) {
    	Customer customer = getAuthenticatedCustomer(request);
    	address.setCustomer(customer);
    	addressService.save(address);
    	
    	String redirectOption = request.getParameter("redirect");
     	String redirectURL = "redirect:/address_book";
     	if("checkout".equals(redirectOption)) {
     		redirectURL += "?redirect=checkout";
     	}
    	
    	
    	ra.addFlashAttribute("message","Successfully save Address to database");
    	return redirectURL;
    }
    
    @GetMapping("/address_book/edit/{addressId}")
    public String editAddress(@PathVariable("addressId") Integer addressId,Model model,HttpServletRequest request) {
    	Customer customer = getAuthenticatedCustomer(request);
    	List<Country> listCountries = customerService.listAllCountries();
    	Address address = addressService.get(addressId,customer.getId());
    	
    	model.addAttribute("address",address);
    	model.addAttribute("listCountries",listCountries);
    	model.addAttribute("pageTitle","Edit address with id:" +addressId);
    	
    	return "address_book/address_form";
    }
    
    @GetMapping("/address_book/delete/{addressId}")
    public String deleteAddress(@PathVariable("addressId") Integer addressId,HttpServletRequest request, RedirectAttributes ra) {
    	Customer customer = getAuthenticatedCustomer(request);
    	addressService.deleteByIdAndCustomer(addressId,customer.getId());
    	ra.addFlashAttribute("message","The address with ID:" + addressId +"has been delete success");
        return "redirect:/address_book";
    } 
    private Customer getAuthenticatedCustomer(HttpServletRequest request)  {
   	 String customerEmail = Utility.getEmailOfAuthenticationCustomer(request);
   	 return customerService.getCustomerByEmail(customerEmail);
    }
    
    @GetMapping("/address_book/default/{addressId}")
    public String setDefault(@PathVariable("addressId") Integer addressId, HttpServletRequest request) {
    	Customer customer = getAuthenticatedCustomer(request);
        addressService.setDefaultAddress(addressId, customer.getId());
        String redirectOption = request.getParameter("redirect");
    	String redirectURL = "redirect:/address_book";
    	if("cart".equals(redirectOption)) {
    		redirectURL = "redirect:/cart";
    	}else if("checkout".equals(redirectOption)) {
    		redirectURL = "redirect:/checkout";
    	}
    	return redirectURL;
    }
}
