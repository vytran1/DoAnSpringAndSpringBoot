package com.shopme.address;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AddressRepositoryTest {
	 @Autowired
     private AddressRepository addressRepository;
	 
	 @Test
	 public void testCreate() {
		 Integer customerId = 2;//Khoa
		 Integer countryId = 1;//VietNam
		 
		 Address address = new Address();
		 address.setCustomer(new Customer(customerId));
		 address.setCountry(new Country(countryId));
		 address.setFirstName("Phương");
		 address.setLastName("Khoa");
		 address.setPhoneNumber("0977765562");
		 address.setAddressLine1("100/100 Khu Pho 10 Phuong Tan Hoa");
		 address.setCity("Biên Hòa");
		 address.setState("DONGNAI");
		 address.setPostalCode("500000");
		 
		 Address saveAddress = addressRepository.save(address);
		 assertThat(saveAddress).isNotNull();
		 assertThat(saveAddress.getId()).isGreaterThan(0);
	 }
	 
	 @Test
	 public void testFindByCustomer() {
		 Integer customerId = 2;//Khoa
		 Customer customer = new Customer(customerId);
		 List<Address> listAddress = addressRepository.findByCustomer(customer);
		 assertThat(listAddress.size()).isGreaterThan(0);
		 listAddress.forEach(x -> System.out.println(x));
	 }
	 
	 @Test
	 public void testFindByIdAndCustomer() {
		 Integer customerId = 2;//Khoa
		 Integer addressId = 1;
		 
		 Address address = addressRepository.findByIdAndCustomer(addressId, customerId);
		 assertThat(address).isNotNull();
		 System.out.println(address);
	 }
	 
	 @Test
	 public void updateAddressStatus() {
		 Integer addressId = 2;
		 Address addressInDb = addressRepository.findById(addressId).get();
		 addressInDb.setDefaultForShipping(true);
		 Address updateAddress = addressRepository.save(addressInDb);
		 assertThat(updateAddress.isDefaultForShipping()).isTrue();		 
	 }
	 
	 @Test
	 public void setDefaultAddres() {
		 Integer addressId = 4;
		 addressRepository.setDefaultAddress(addressId);
		 Address addressObject = addressRepository.findById(addressId).get();
		 assertThat(addressObject.isDefaultForShipping()).isTrue();
		 
	 }
	 
	 @Test
	 public void setNonDefaultForOthersTest() {
		 Integer addressId = 1;
		 Integer customerId = 2;
		 addressRepository.setNonDefaultForOthers(addressId, customerId);
	 }
	 
	 @Test
	 public void getDefaultAddress() {
		 Integer customerId = 2;
		 Address address = addressRepository.findDefaultByCustomer(customerId);
		 assertThat(address).isNotNull();
		 System.out.println(address);
	 }
}
