package com.shopme.common.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "customers")
public class Customer extends AbstractAddressWithCountry {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;
//	
    @Column(nullable =  false, unique =  true, length = 45)
	private String email;
    @Column(nullable =  false,  length = 64)
	private String password;
    
//    @Column(name = "first_name",nullable =  false,  length = 45)
//	private String firstName;
//    
//    @Column(name = "last_name",nullable =  false,  length = 45)
//	private String lastName;
//    @Column(name = "phone_number",nullable =  false,  length = 15)
//	private String phoneNumber;
//    
//    @Column(nullable = false,  length = 64)
//	private String addressLine1;
//    
//    @Column(name="address_line_2", length = 64)
//	private String addressLine2;
//    
//    @Column(nullable = false,  length = 45)
//	private String city;
//    
//    @Column(nullable = false,  length = 45)
//	private String state;
//    
//    
//    @Column(name="postal_code",nullable = false,  length = 10)
//	private String postalCode;
    
    @Column(name="verification_code",  length = 64)
    private String verificationCode;
    
	
	private boolean enabled;
    
	@Column(name = "created_time")
	private Date createdTime;
	
//	@ManyToOne
//	@JoinColumn(name = "country_id")
//	private Country country;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "authentication_type",length = 10)
	private AuthenticationType authenticationType;
	
	@Column(name = "reset_password_token",length = 30)
	private String resetPasswordToken;
	public Customer() {
		
	}

	
	
	



	public Customer(Integer id) {
		
		this.id = id;
	}




//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

//	public Country getCountry() {
//		return country;
//	}
//
//	public void setCountry(Country country) {
//		this.country = country;
//	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}

	public String getFullName() {
		// TODO Auto-generated method stub
		return this.firstName + " " + this.lastName;
	}

	public AuthenticationType getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(AuthenticationType authenticationType) {
		this.authenticationType = authenticationType;
	}

	public String getResetPasswordToekn() {
		return resetPasswordToken;
	}

	public void setResetPasswordToekn(String resetPasswordToekn) {
		this.resetPasswordToken = resetPasswordToekn;
	}
	
	
	@Transient
	public String getAddress() {
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
