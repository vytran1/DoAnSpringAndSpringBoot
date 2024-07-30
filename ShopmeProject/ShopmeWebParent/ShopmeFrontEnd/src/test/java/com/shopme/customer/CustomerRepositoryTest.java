package com.shopme.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTest {
    
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Test
	public void testCreateCustomer() {
		Integer countryID = 1; // VietNam
		Country country = testEntityManager.find(Country.class, countryID);
		
		Customer customer  = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Trần Nguyễn");
		customer.setLastName("Vỹ");
		customer.setPassword("123456789ASD");
		customer.setEmail("tn.vy171003@gmail.com");
		customer.setPhoneNumber("0979847481");
		customer.setAddressLine1("99/9 Khu Phố 1 Phường Tân Biên");
        customer.setCity("Thành Phố Biên Hòa");
        customer.setState("Đồng Nai");
        customer.setPostalCode("0979");
        customer.setCreatedTime(new Date());
        
        Customer savedCustomer = customerRepository.save(customer);
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	@Test
	public void testCreateCustomer2() {
		Integer countryID = 1; // VietNam
		Country country = testEntityManager.find(Country.class, countryID);
		
		Customer customer  = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Lê Phương Minh");
		customer.setLastName("Khoa");
		customer.setPassword("123456789ASD");
		customer.setEmail("lpm.khoa03@gmail.com");
		customer.setPhoneNumber("0966272357");
		customer.setAddressLine1("99/9 Khu Phố 1 Phường Tân Biên");
        customer.setCity("Thành Phố Biên Hòa");
        customer.setState("Đồng Nai");
        customer.setPostalCode("0979");
        customer.setCreatedTime(new Date());
        
        Customer savedCustomer = customerRepository.save(customer);
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	
	
	@Test
	public void testUpdateCustomer() {
		Integer customerID = 1; //VyTran;
//		String lastName = "Vĩ";
		
	    Customer customer = customerRepository.findById(customerID).get();
//	    customer.setLastName(lastName);
//	    customer.setEnabled(true);
	    customer.setVerificationCode("171003");
	    
	    Customer saveCustomer =  customerRepository.save(customer);
	    System.out.println(saveCustomer);
	    
	}
	
	@Test
	public void testGetCustomer() {
		Integer customerID = 1; //ViTran
		Customer customer = customerRepository.findById(customerID).get();
	}
	
	@Test
	public void testDeleteCustomer() {
		Integer customerID = 3; //NgocHao
		Customer customer = customerRepository.findById(customerID).get();
		customerRepository.delete(customer);
		
		Optional<Customer> findById = customerRepository.findById(customerID);
		
		assertThat(findById).isNotPresent();
	}
	
	@Test
	public void testFindByEmail() {
		String email ="tn.vy171003@gmail.com";
		Customer customer = customerRepository.findByEmail(email);
		System.out.println(customer);
	}
	
	@Test
	public void testFindByVerificationCode() {
		String verificationCode = "QSF84PwoEozJiwejnQWVpkOc1eQJdyV97EJDkQmMZqvG673EMml8RRdxy8xcTShj";
		Customer customer = customerRepository.findByVerificationCode(verificationCode);
		System.out.println(customer);
	}
	
	@Test
	public void testEnabledCustomer() {
		Integer customerID = 2; //Khoa
		customerRepository.enable(customerID);
		Customer customer = customerRepository.findById(customerID).get();
		assertThat(customer.isEnabled()).isTrue();
	}
	
	@Test
	public void testUpdateAuthenticationType() {
		Integer id = 2;
		customerRepository.updateAuthenticationType(id,AuthenticationType.FACEBOOK);
		Customer customer = customerRepository.findById(id).get();
		assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.FACEBOOK);
	} 
}
