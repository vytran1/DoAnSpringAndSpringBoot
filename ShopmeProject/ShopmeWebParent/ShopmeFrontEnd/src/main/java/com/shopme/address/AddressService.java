package com.shopme.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AddressService {
	 @Autowired
     private AddressRepository addressRepository;
	 
	 public List<Address> listAll(Customer customer){
		 return addressRepository.findByCustomer(customer);
	 }
	 
	 public void save(Address address) {
		 addressRepository.save(address);
	 }
	 
	 public Address get(Integer addressId,Integer customerId) {
		 return addressRepository.findByIdAndCustomer(addressId, customerId);
	 }
	 
	 public void deleteByIdAndCustomer(Integer addressId, Integer customerId) {
		 addressRepository.deleteByIdAndCustomer(addressId, customerId);
	 }
	 
	 public void setDefaultAddress(Integer addressId, Integer customerId) {
		 if(addressId > 0) {
			 addressRepository.setDefaultAddress(addressId);
		 }
		 addressRepository.setNonDefaultForOthers(addressId, customerId);
	 }
	 
	 public Address getDefaultShippingAddress(Customer customer) {
		 return addressRepository.findDefaultByCustomer(customer.getId());
	 }
}
