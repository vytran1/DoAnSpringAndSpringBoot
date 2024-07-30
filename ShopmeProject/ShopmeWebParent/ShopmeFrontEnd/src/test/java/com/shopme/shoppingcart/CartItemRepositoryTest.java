package com.shopme.shoppingcart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CartItemRepositoryTest {
     @Autowired
     private CartItemRepository cartItemRepository;
     
     @Autowired
     private TestEntityManager testEntityManager;
     
     @Test
     public void testSaveItem() {
    	 //Khoa
    	 Integer customerID = 2;
    	 
    	 //AsusTuf
    	 Integer productID = 12;
    	 
    	 
    	 Customer customer  = testEntityManager.find(Customer.class,customerID);
    	 Product product = testEntityManager.find(Product.class,productID);
    	 
    	 CartItem newCartItem = new CartItem();
    	 newCartItem.setCustomer(customer);
    	 newCartItem.setProduct(product);
    	 newCartItem.setQuantity(1);
    	 CartItem savedItem = cartItemRepository.save(newCartItem);
    	 assertThat(savedItem.getId()).isGreaterThan(0);
    	 
    	 
    	 
     }
     @Test
     public void testSave2Item() {
    	 //Khoa
    	 Integer customerID = 9;
    	 
    	 //AsusTuf
    	 Integer productID = 16;
    	 
    	 
    	 Customer customer  = testEntityManager.find(Customer.class,customerID);
    	 Product product = testEntityManager.find(Product.class,productID);
    	 
    	 CartItem newCartItem1 = new CartItem();
    	 newCartItem1.setCustomer(customer);
    	 newCartItem1.setProduct(product);
    	 newCartItem1.setQuantity(2);
    	 
    	 CartItem newCartItem2 = new CartItem();
    	 newCartItem2.setCustomer(new Customer(10));
    	 newCartItem2.setProduct(new  Product(17));
    	 newCartItem2.setQuantity(2);
    	
    	 
    	 Iterable<CartItem> cartItems =   cartItemRepository.saveAll(List.of(newCartItem1,newCartItem2));
    	 assertThat(cartItems).size().isGreaterThan(0);
    	 
    	 
    	 
     }
     
     @Test
     public void testFindByCustomer() {
    	 Integer customerID = 2;
    	 List<CartItem> carts = cartItemRepository.findByCustomer(new Customer(2));
    	 carts.forEach((x)->System.out.println(x));
    	 assertThat(carts).size().isGreaterThan(0);
     }
     
     @Test
     public void testFindByCustomerAndProduct() {
    	 //Khoa
    	 Integer customerID = 9;
    	 
    	 //AsusTuf
    	 Integer productID = 16;
    	 
    	 CartItem cartItem  = cartItemRepository.findByCustomerAndProduct(new Customer(customerID),new Product(productID));
    	 assertThat(cartItem).isNotNull();
    	 System.out.println(cartItem);
     }
     
     @Test
     public void updateQuantityProduct() {
    	 //Khoa
    	 Integer customerID = 9;
    	 
    	 //AsusTuf
    	 Integer productID = 16;
    	 
    	 Integer quantity = 5;
    	 
    	 cartItemRepository.updateQuantity(quantity, customerID, productID);
    	 
    	 CartItem updateCartItem =  cartItemRepository.findByCustomerAndProduct(new Customer(customerID), new Product(productID));
    	 
    	 assertThat(updateCartItem.getQuantity()).isEqualTo(quantity);
     }
     
     @Test
     public void deleteByCustomerAndProduct() {
    	 Integer customerID = 10;
    	 Integer productID = 17;
    	 
    	 cartItemRepository.deleteByCustomerAndProduct(customerID, productID);
    	 
    	 CartItem item = cartItemRepository.findByCustomerAndProduct(new Customer(customerID), new Product(productID));
    	 assertThat(item).isNull();
    	 
     }
}
