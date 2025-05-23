package com.shopme.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "address")
public class Address extends AbstractAddressWithCountry{
//	 @Id
//	 @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Integer id;
	
//	 @Column(name = "first_name",nullable = false,length = 45)
//     private String firstName;
//	 
//	 @Column(name = "last_name",nullable = false,length = 45)
//     private String lastName;
//	 
//	 @Column(name = "phone_number",nullable = false,length = 15)
//     private String phoneNumber;
//	 
//	 @Column(name = "address_line1",nullable = false,length = 64)
//     private String addressLine1;
//	 
//	 @Column(name = "address_line2",length = 64)
//     private String addressLine2;
//	 
//	 @Column(nullable = false,length = 45)
//     private String city;
//	 
//	 @Column(nullable = false,length = 45)
//     private String state;
//	 
//	 @Column(name = "postal_code",nullable = false,length = 10)
//     private String postalCode;
//	 
	 
//	 @ManyToOne
//	 @JoinColumn(name = "country_id")
//	 private Country country;
//	 
	 @ManyToOne
	 @JoinColumn(name = "customer_id")
     private Customer customer;
     
	 @Column(name="default_address")
     private boolean defaultForShipping;

	public Address(Integer id) {
		super();
		this.id = id;
	}

	public Address() {
		
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

//	public Country getCountry() {
//		return country;
//	}
//
//	public void setCountry(Country country) {
//		this.country = country;
//	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public boolean isDefaultForShipping() {
		return defaultForShipping;
	}

	public void setDefaultForShipping(boolean defaultForShipping) {
		this.defaultForShipping = defaultForShipping;
	}

	@Override
	public String toString() {
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
		address+=this.country.getName()+".";
		if(!this.postalCode.isEmpty()) {
			address+="Postal Code: "+this.postalCode;
		}
		if(!this.phoneNumber.isEmpty()) {
			address+=",Phone Number: "+this.phoneNumber;
		}
		
		return address;
	}
	 
	 
     
}
