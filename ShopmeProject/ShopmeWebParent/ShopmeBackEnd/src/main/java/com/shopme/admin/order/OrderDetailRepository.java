package com.shopme.admin.order;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.order.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer>  {
	
	
	@Query("SELECT NEW com.shopme.common.entity.order.OrderDetail(od.product.category.name,od.quantity,od.productCost,od.shippingCost,od.subtotal) FROM OrderDetail od WHERE od.order.orderTime between ?1 AND ?2")
	public List<OrderDetail> findWithCategoryAndTimeBetween(Date startTime, Date endTime); 
	
	@Query("SELECT NEW com.shopme.common.entity.order.OrderDetail(od.quantity,od.product.name,od.productCost,od.shippingCost,od.subtotal) FROM OrderDetail od WHERE od.order.orderTime between ?1 AND ?2")
	public List<OrderDetail> findWithProductAndTimeBetween(Date startTime, Date endTime); 

}
