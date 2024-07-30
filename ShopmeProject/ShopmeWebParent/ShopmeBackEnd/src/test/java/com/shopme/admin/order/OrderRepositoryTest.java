package com.shopme.admin.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.intThat;

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
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.OrderTrack;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private TestEntityManager testEntityManager;
    
//    @Test
//    public void testCreateNewOrderWithSingleProduct() {
//    	Customer customer =testEntityManager.find(Customer.class,2);
//    	Product product = testEntityManager.find(Product.class,1);
//    	Order mainOrder = new Order();
//    	
//    	
//    	mainOrder.setOrderTime(new Date());
//    	
//    	
//    	mainOrder.setCustomer(customer);
//    	mainOrder.setFirstName(customer.getFirstName());
//    	mainOrder.setLastName(customer.getLastName());
//    	mainOrder.setPhoneNumber(customer.getPhoneNumber());
//        mainOrder.setAddressLine1(customer.getAddressLine1());
//        mainOrder.setAddressLine2(customer.getAddressLine2());
//        mainOrder.setCity(customer.getCity());
//        mainOrder.setCountry(customer.getCountry().getName());
//        mainOrder.setPostalCode(customer.getPostalCode());
//        mainOrder.setState(customer.getState());
//        
//        mainOrder.setShippingCost(100);
//        mainOrder.setProductCost(product.getCost());
//        mainOrder.setTax(0);
//        mainOrder.setSubtotal(product.getPrice());
//        mainOrder.setTotal(product.getPrice()+ 100);
//        
//        
//        mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
//        mainOrder.setOrderStatus(OrderStatus.NEW);
//        mainOrder.setDeliveryDate(new Date());
//        mainOrder.setDeliveryDays(1);
//        
//        OrderDetail orderDetail = new OrderDetail();
//        orderDetail.setProduct(product);
//        orderDetail.setOrder(mainOrder);
//        orderDetail.setProductCost(product.getCost());
//        orderDetail.setShippingCost(100);
//        orderDetail.setQuantity(1);
//        orderDetail.setSubtotal(product.getPrice() * 1);
//        orderDetail.setUnitPrice(product.getPrice());
//    	
//        mainOrder.getOrderDetails().add(orderDetail);
//        
//        Order savedOrder = orderRepository.save(mainOrder);
//        assertThat(savedOrder.getId()).isGreaterThan(0);
//    }
    
//    @Test
//    public void testCreateNewOrderWithMultipleProduct() {
//    	Customer customer =testEntityManager.find(Customer.class,9);
//    	Product product12 = testEntityManager.find(Product.class,12);
//    	Product product5 = testEntityManager.find(Product.class,5);
//    	
//    	Order mainOrder = new Order();
//    	mainOrder.setOrderTime(new Date());
//    	mainOrder.setCustomer(customer);
//    	mainOrder.copyAddressFromCustomer();
//    	
//    	
//    	OrderDetail orderDetail1 = new OrderDetail();
//        orderDetail1.setProduct(product12);
//        orderDetail1.setOrder(mainOrder);
//        orderDetail1.setProductCost(product12.getCost());
//        orderDetail1.setShippingCost(10);
//        orderDetail1.setQuantity(1);
//        orderDetail1.setSubtotal(product12.getPrice() * 1);
//        orderDetail1.setUnitPrice(product12.getPrice());
//        
//        OrderDetail orderDetail2 = new OrderDetail();
//        orderDetail2.setProduct(product5);
//        orderDetail2.setOrder(mainOrder);
//        orderDetail2.setProductCost(product5.getCost());
//        orderDetail2.setShippingCost(20);
//        orderDetail2.setQuantity(2);
//        orderDetail2.setSubtotal(product5.getPrice() * 2);
//        orderDetail2.setUnitPrice(product5.getPrice());
//        
//        mainOrder.getOrderDetails().add(orderDetail2);
//        mainOrder.getOrderDetails().add(orderDetail1);
//        
//        mainOrder.setShippingCost(30);
//        mainOrder.setProductCost(product5.getCost() + product12.getCost());
//        mainOrder.setTax(0);
//        float subTotal = product5.getPrice() + product12.getPrice() * 2;
//        mainOrder.setSubtotal(subTotal);
//        mainOrder.setTotal(subTotal + 30);
//        
//        
//        mainOrder.setPaymentMethod(PaymentMethod.COD);
//        mainOrder.setOrderStatus(OrderStatus.PROCESSING);
//        mainOrder.setDeliveryDate(new Date());
//        mainOrder.setDeliveryDays(3);
//        
//        
//        Order savedOrder = orderRepository.save(mainOrder);
//        assertThat(savedOrder.getId()).isGreaterThan(0);
//    }
//    
    @Test
    public void testListOrder() {
    	Iterable<Order> listOrder = orderRepository.findAll();
    	assertThat(listOrder).hasSizeGreaterThan(0);
    	listOrder.forEach(x -> System.out.println(x));
    }
    
//    @Test
//    public void testUpdateOrder() {
//    	Integer orderID = 3;
//    	Order order = orderRepository.findById(orderID).get();
//    	order.setOrderStatus(OrderStatus.SHIPPING);
//    	order.setPaymentMethod(PaymentMethod.COD);
//    	order.setOrderTime(new Date());
//    	order.setDeliveryDays(4);
//    	
//    	Order updatedObject = orderRepository.save(order);
//    	
//    	assertThat(updatedObject.getOrderStatus()).isEqualTo(OrderStatus.SHIPPING);
//    }
    
    @Test
    public void testDeleteOrder() {
    	Integer orderID =4;
    	Order order = orderRepository.findById(orderID).get();
    	orderRepository.delete(order);
    	
    	Order deleteOrder = orderRepository.findById(orderID).get();
    	assertThat(deleteOrder).isNull();
    }
    
    @Test
    public void testFindAll() {
    	String sortField = "firstName";
    	String sortDir = "asc";
    	String keyWord = "Phương";
    	
    	Sort sort = Sort.by(sortField);
    	sort = sort.ascending();
    	Pageable pageable = PageRequest.of(0,2,sort);
    	Page<Order> page = orderRepository.findAll(keyWord,pageable);
    	List<Order> orders = page.getContent();
    	assertThat(orders.size()).isGreaterThan(0);
    	for(Order x : orders) {
    		System.out.println(x.getCustomer().getFullName());
    	}
    }
    
    @Test
    public void testUpdateOrderTrack() {
    	Integer orderId = 9;
    	Order order = orderRepository.findById(orderId).get();
    	
    	OrderTrack orderTrack = new OrderTrack();
    	orderTrack.setOrder(order);
    	orderTrack.setUpdateTime(new Date());
    	orderTrack.setOrderStatus(OrderStatus.NEW);
    	orderTrack.setNotes(OrderStatus.NEW.defaultDescription());
    	
    	OrderTrack processingTrack = new OrderTrack();
    	processingTrack.setOrder(order);
    	processingTrack.setUpdateTime(new Date());
    	processingTrack.setOrderStatus(OrderStatus.PROCESSING);
    	processingTrack.setNotes(OrderStatus.PROCESSING.defaultDescription());
    	
    	List<OrderTrack> orderTracks = order.getOrderTracks();
        orderTracks.add(processingTrack);
        
        Order updatedOrder = orderRepository.save(order);
        
        assertThat(updatedOrder.getOrderTracks()).hasSizeGreaterThan(1);
    }
    
    @Test
    public void testFindOrderTimeBetween() throws ParseException {
    	DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    	Date startTime = dateFormatter.parse("2024-02-01");
    	Date endTime = dateFormatter.parse("2024-03-20");
    	
    	List<Order> listOrders = orderRepository.findByOrderTimeBetween(startTime, endTime);
    	
    	
    	assertThat(listOrders.size()).isGreaterThan(0);
        for(Order order : listOrders) {
    		System.out.printf("%s | %s | %.2f | %.2f | %.2f \n", 
    				order.getId(),
    				order.getOrderTime(),
    				order.getProductCost(),
    				order.getSubtotal(),
    				order.getTotal()
    				);
    	}
    }
    
    @Test
    public void testCheckWhetherCustomerHaveOrder() {
    	Integer customerId = 3;
    	Long count = orderRepository.countByCustomerId(customerId);
    	assertThat(count).isGreaterThan(0);
    }
}
