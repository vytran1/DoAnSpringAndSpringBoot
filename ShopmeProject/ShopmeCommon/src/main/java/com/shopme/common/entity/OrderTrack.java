package com.shopme.common.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "order_track")
public class OrderTrack extends IdBasedEntity {
	@Column(length = 256)
	private String notes;
	
	
	private Date updateTime;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 45,nullable = false)
	private OrderStatus orderStatus;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	
	

	public OrderTrack() {
		
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	@Transient
	public String getUpdatedTimeOnForm() {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		return dateFormatter.format(this.updateTime);
	}
	
	public void setUpdatedTimeOnForm(String dateString) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		
		try {
			this.updateTime = dateFormatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	} 
}
