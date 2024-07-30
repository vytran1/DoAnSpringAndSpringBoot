package com.shopme.common.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractAddressWithCountry extends AbstractAddress {
	 @ManyToOne
	 @JoinColumn(name = "country_id")
	 protected Country country;
	 
	 
	 public Country getCountry() {
			return country;
		}

		public void setCountry(Country country) {
			this.country = country;
		}
		
//		@Override
//		public String toString() {
//			String address = this.firstName;
//			if(this.lastName != null && !this.lastName.isEmpty()) {
//				address+=this.lastName;
//			}
//			if(!this.addressLine1.isEmpty()) {
//				address+=", "+this.addressLine1;
//			}
//			if(this.addressLine2 != null && !this.addressLine2.isEmpty()) {
//				address+=", "+this.addressLine2;
//			}
//			if(!city.isEmpty()) {
//				address+=", "+this.city;
//			}
//			if(state!= null && !this.state.isEmpty()) {
//				address+=","+this.state;
//			}
//			address+=this.country.getName()+".";
//			if(!this.postalCode.isEmpty()) {
//				address+="Postal Code: "+this.postalCode;
//			}
//			if(!this.phoneNumber.isEmpty()) {
//				address+=",Phone Number: "+this.phoneNumber;
//			}
//			
//			return address;
//		}	
}
