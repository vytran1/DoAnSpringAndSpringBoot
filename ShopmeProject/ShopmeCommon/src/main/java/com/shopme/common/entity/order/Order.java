package com.shopme.common.entity.order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.shopme.common.entity.AbstractAddress;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.IdBasedEntity;
import com.shopme.common.entity.OrderTrack;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "orders")
public class Order extends AbstractAddress {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
	
//	@Column(name = "first_name",nullable = false,length = 45)
//    private String firstName;
//	 
//	@Column(name = "last_name",nullable = false,length = 45)
//    private String lastName;
//	 
//	@Column(name = "phone_number",nullable = false,length = 15)
//    private String phoneNumber;
//	 
//	@Column(name = "address_line1",nullable = false,length = 64)
//    private String addressLine1;
//	 
//	@Column(name = "address_line2",length = 64)
//    private String addressLine2;
//	 
//	@Column(nullable = false,length = 45)
//    private String city;
//	 
//	@Column(nullable = false,length = 45)
//    private String state;
//	 
//	@Column(name = "postal_code",nullable = false,length = 10)
//    private String postalCode;
	
	@Column(nullable = false,length = 45)
	private String country;
	 
	private Date orderTime;
	
	private float shippingCost;
	private float productCost;
	
	private float subtotal;
	
	private float tax;
	
	private float total;
	
	private int deliveryDays;
	private Date deliveryDate;
	
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
	private Set<OrderDetail> orderDetails = new HashSet<>();
	
	@OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
	@OrderBy("updateTime ASC")
	private List<OrderTrack> orderTracks = new ArrayList<>();

	public Order() {
		
	}

	
	
	public Order(Integer id) {
		super();
		this.id = id;
	}

    

    public Order(Integer id,Date orderTime, float productCost, float subtotal, float total) {
		this.id = id;
		this.orderTime = orderTime;
		this.productCost = productCost;
		this.subtotal = subtotal;
		this.total = total;
	}



//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}

//	public String getFirstName() {
//		return firstName;
//	}
//
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//
//	public String getLastName() {
//		return lastName;
//	}
//
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//
//	public String getPhoneNumber() {
//		return phoneNumber;
//	}
//
//	public void setPhoneNumber(String phoneNumber) {
//		this.phoneNumber = phoneNumber;
//	}
//
//	public String getAddressLine1() {
//		return addressLine1;
//	}
//
//	public void setAddressLine1(String addressLine1) {
//		this.addressLine1 = addressLine1;
//	}
//
//	public String getAddressLine2() {
//		return addressLine2;
//	}
//
//	public void setAddressLine2(String addressLine2) {
//		this.addressLine2 = addressLine2;
//	}
//
//	public String getCity() {
//		return city;
//	}
//
//	public void setCity(String city) {
//		this.city = city;
//	}
//
//	public String getState() {
//		return state;
//	}
//
//	public void setState(String state) {
//		this.state = state;
//	}
//
//	public String getPostalCode() {
//		return postalCode;
//	}
//
//	public void setPostalCode(String postalCode) {
//		this.postalCode = postalCode;
//	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}

	public float getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}

	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public int getDeliveryDays() {
		return deliveryDays;
	}

	public void setDeliveryDays(int deliveryDays) {
		this.deliveryDays = deliveryDays;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}



	public float getProductCost() {
		return productCost;
	}



	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}
	
	public void copyAddressFromCustomer() {
		
	   this.setFirstName(customer.getFirstName());
       this.setLastName(customer.getLastName());
       this.setPhoneNumber(customer.getPhoneNumber());
       this.setAddressLine1(customer.getAddressLine1());
       this.setAddressLine2(customer.getAddressLine2());
       this.setCity(customer.getCity());
       this.setCountry(customer.getCountry().getName());
       this.setPostalCode(customer.getPostalCode());
       this.setState(customer.getState());
	}



	@Override
	public String toString() {
		return "Order [id=" + id + ", subtotal=" + subtotal + ", paymentMethod=" + paymentMethod + ", orderStatus="
				+ orderStatus + ", customer=" + customer.getFullName() + "]";
	}
	
	
	public String getDestination() {
		String destination = this.city + ",";
		if(this.state != null && !this.state.isEmpty()) {
			destination += this.state + ", ";
		}
        destination += this.country;
        return destination;
	}



	public void copyShippingAddressFromAddressObject(Address address) {
		// TODO Auto-generated method stub
		   this.setFirstName(address.getFirstName());
	       this.setLastName(address.getLastName());
	       this.setPhoneNumber(address.getPhoneNumber());
	       this.setAddressLine1(address.getAddressLine1());
	       this.setAddressLine2(address.getAddressLine2());
	       this.setCity(address.getCity());
	       this.setCountry(address.getCountry().getName());
	       this.setPostalCode(address.getPostalCode());
	       this.setState(address.getState());
		
	}
	
	@Transient
	public String getShippingAddress() {
		String address = this.firstName;
		if(this.lastName != null && !this.lastName.isEmpty()) {
			address+=this.lastName;
		}
		if(!this.addressLine1.isEmpty()) {
			address+=", "+this.addressLine1;
		}
		if(this.addressLine2 != null && !this.addressLine2.isEmpty()) {
			address+=", "+this.addressLine2;
		}
		if(!city.isEmpty()) {
			address+=", "+this.city;
		}
		if(state!= null && !this.state.isEmpty()) {
			address+=","+this.state;
		}
		address+=this.country+".";
		if(!this.postalCode.isEmpty()) {
			address+="Postal Code: "+this.postalCode;
		}
		if(!this.phoneNumber.isEmpty()) {
			address+=",Phone Number: "+this.phoneNumber;
		}
		
		return address;
	}



	public List<OrderTrack> getOrderTracks() {
		return orderTracks;
	}



	public void setOrderTracks(List<OrderTrack> orderTracks) {
		this.orderTracks = orderTracks;
	}
	
	@Transient
	public String getDeliverDateOnForm() {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormatter.format(this.deliveryDate);
	}
	
	public void setDeliverDateOnForm(String date) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.deliveryDate = dateFormatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Transient
	public String getRecipentName() {
		String name = this.firstName;
		if(this.lastName != null && !this.lastName.isEmpty()) {
			name+=this.lastName;
		}
		return name;
	}
	
	
	@Transient
	public String getRecipentAddress() {
		String address = this.addressLine1;
		
		
		if(this.addressLine2 != null && !this.addressLine2.isEmpty()) {
			address+=", "+this.addressLine2;
		}
		if(!city.isEmpty()) {
			address+=", "+this.city;
		}
		if(state!= null && !this.state.isEmpty()) {
			address+=","+this.state;
		}
		
		address+=this.country+".";
		
		
		if(!this.postalCode.isEmpty()) {
			address+="Postal Code: "+this.postalCode;
		}
		
		return address;
	}
	
	@Transient
	public boolean isCOD() {
		return this.paymentMethod.equals(PaymentMethod.COD);
	}
	
	@Transient
	public boolean isPicked() {
		return hasStatus(OrderStatus.PICKED);
	}
	
	@Transient
	public boolean isShipping() {
		return hasStatus(OrderStatus.SHIPPING);
	}
	
	@Transient
	public boolean isDelivery() {
		return hasStatus(OrderStatus.DELIVERED);
	}
	
	@Transient
	public boolean isReturned() {
		return hasStatus(OrderStatus.RETURNED);
	}
	
	public boolean isProcessing() {
		return hasStatus(OrderStatus.PROCESSING);
	}
	
	
	@Transient
	public boolean isReturnRequested() {
		return hasStatus(OrderStatus.RETURN_REQUESTED);
	}
	public boolean hasStatus(OrderStatus orderStatus) {
		for(OrderTrack track : this.orderTracks) {
			if(track.getOrderStatus().equals(orderStatus)) {
				return true;
			}
		}
		return false;
	}
	
	@Transient
	public String getProductNames() {
		String productNames = "";
		productNames = "<ul>";
		for(OrderDetail detail : this.orderDetails) {
			productNames += "<li>" + detail.getProduct().getShortName() + "</li>";
		}
		productNames += "</ul>";
		return productNames;
	}
	 
}
