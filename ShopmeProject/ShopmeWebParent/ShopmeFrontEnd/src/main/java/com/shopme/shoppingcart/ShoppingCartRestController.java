package com.shopme.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.Utility;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.common.exception.ShoppingCartException;
import com.shopme.common.exception.UnableAddToCart;
import com.shopme.customer.CustomerService;
import com.shopme.product.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {
     @Autowired
     private ShoppingCartService shoppingCartService;
     
     @Autowired
     private CustomerService customerService;
     
     @Autowired
     private ProductService productService;
     
     @PostMapping("/cart/add/{productId}/{quantity}")
     public ResponseEntity<String> addProductToCart(@PathVariable("productId") Integer productId,@PathVariable("quantity")Integer quantity,HttpServletRequest request) {
    	 try {
    		Product product = productService.getProduct(productId);
    		boolean checkQuantity = productService.checkQuantityAvailbel(product,quantity);
    		if(!checkQuantity) {
    			return ResponseEntity.badRequest().body("The quantity you want must less than: " + product.getQuantity());
    		}
    		productService.updateQuantityAddToCart(product,quantity);
			Customer customer = getAuthenticatedCustomer(request);
			Integer updateQuantity = shoppingCartService.addProduct(productId, quantity, customer);
			return ResponseEntity.ok(updateQuantity +"item(s) of this product were added to your shopping cart");
		} catch (CustomerNotFoundException e) {
			return ResponseEntity.badRequest().body("You must loggin to add product to cart");
		} catch(ShoppingCartException e){
			return ResponseEntity.badRequest().body(e.getMessage()); 
		} catch (ProductNotFoundException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.badRequest().body(e.getMessage()); 
		} catch (UnableAddToCart e) {
			// TODO Auto-generated catch block
			return ResponseEntity.badRequest().body(e.getMessage()); 
		}
     }
     
     private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
    	 String customerEmail = Utility.getEmailOfAuthenticationCustomer(request);
    	 if(customerEmail == null) {
    		 throw new CustomerNotFoundException("No Authenticated Customer");
    	 }
    	 return customerService.getCustomerByEmail(customerEmail);
     }
     
     @PostMapping("/cart/update/{productId}/{quantity}/{type}")
     public ResponseEntity<String> updateQuantity(@PathVariable("productId") Integer productId,@PathVariable("quantity")Integer quantity,@PathVariable("type") String type,HttpServletRequest request) {
    	 try {
			Customer customer = getAuthenticatedCustomer(request);
			productService.updateQuantityInCart(productId, type);
			float subToTal = shoppingCartService.updateQuantity(productId, quantity, customer);
			return ResponseEntity.ok(String.valueOf(subToTal));
		} catch (CustomerNotFoundException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must login to change product quantity");
		} catch (ProductNotFoundException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (UnableAddToCart e) {
			// TODO Auto-generated catch block
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    	 
     }
     
     @DeleteMapping("/cart/remove/{productId}")
     public ResponseEntity<String> removeProductCartItem(@PathVariable("productId") Integer productId,HttpServletRequest request) {
    	 try {
			Customer customer = getAuthenticatedCustomer(request);
			Product product = productService.getProduct(productId);
			CartItem cartItem = shoppingCartService.getCartItemWithProductAndCustomer(product, customer);
			if(cartItem == null) {
				return ResponseEntity.badRequest().body("No cart item found with customer name " + customer.getFullName() + " and product id" + product.getId());
			}
			productService.updateQuantityRemoveFromCart(product,cartItem.getQuantity());
			shoppingCartService.removeProductCartItem(productId, customer);
			return ResponseEntity.ok("The product cartItem have been deleted successfully from shopping cart");
		} catch (CustomerNotFoundException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.badRequest().body("You must login to delete product quantity");
		} catch (ProductNotFoundException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.badRequest().body(e.getMessage());
		} 
     }
}
