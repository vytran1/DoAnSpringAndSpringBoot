package com.shopme.shoppingcart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ShoppingCartException;
import com.shopme.product.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShoppingCartService {
     @Autowired
     private CartItemRepository cartItemRepository;
     
     @Autowired
     private ProductRepository productRepository;
     //Add thì ta thêm 1 số lượng mới cần mua
     public Integer addProduct(Integer productID, Integer quantityNumber,Customer customer) throws ShoppingCartException {
    	 Integer updateQuantity = quantityNumber;
    	 Product product = new Product(productID);
    	 
    	 CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);
    	 
    	 if(cartItem != null) {
    		 updateQuantity = cartItem.getQuantity() + quantityNumber;
    		 if(updateQuantity > 5) {
    			 throw new ShoppingCartException("Could not add more " + updateQuantity + "items because maximum products order is 5" );
    		 }
    	 }else {
    		 cartItem = new CartItem();
    		 cartItem.setCustomer(customer);
    		 cartItem.setProduct(product);
    	 }
    	 cartItem.setQuantity(updateQuantity);
    	 
    	 cartItemRepository.save(cartItem);
    	 
    	 return updateQuantity;
     }
     
     
     //Lấy dữ liệu từ database
     public List<CartItem> listCartItems(Customer customer){
    	 return cartItemRepository.findByCustomer(customer);
     }
     
     
     //Cập nhật thì ta sẽ thay đổi số lượng chúng ta cần mua hiện giờ
     public float updateQuantity(Integer productID, Integer quantityNumber, Customer customer) {
    	 cartItemRepository.updateQuantity(quantityNumber,customer.getId(), productID);
    	 Product productObject = productRepository.findById(productID).get();
    	 float subToTal = productObject.getDiscountPrice() * quantityNumber;
    	 return subToTal;
     }
     
     public void removeProductCartItem(Integer productID,Customer customer) {
    	 cartItemRepository.deleteByCustomerAndProduct(customer.getId(), productID);
     }
     
     public void deleteByCustomer(Customer customer) {
    	 cartItemRepository.deleteByCustomer(customer.getId());
     }
     
     public CartItem getCartItemWithProductAndCustomer(Product product, Customer customer) {
    	 return cartItemRepository.findByCustomerAndProduct(customer,product);
     }
}
