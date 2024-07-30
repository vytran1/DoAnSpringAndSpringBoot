package com.shopme.customer;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.setting.CountryRepository;

import jakarta.transaction.Transactional;
import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class CustomerService {
    
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CountryRepository coutrnCountryRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<Country> listAllCountries(){
		return coutrnCountryRepository.findAllByOrderByNameAsc();
	}
	
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepository.findByEmail(email);
		return customer == null;
	}
	
	
	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(AuthenticationType.DATABASE);
		String randomCodeVerify = RandomString.make(64);
		customer.setVerificationCode(randomCodeVerify);
//		System.out.println("Verification code" + randomCodeVerify);
//		System.out.println("Password Encoded" + customer.getPassword());
		customerRepository.save(customer);
	}
	
	public Customer getCustomerByEmail(String mail)  {
		 
		
		return customerRepository.findByEmail(mail);
	}
	
	
	
	private void encodePassword(Customer customer) {
		String passwordEncoded = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(passwordEncoded);
	}
	
	public boolean verify(String verificationCode) {
		Customer customer = customerRepository.findByVerificationCode(verificationCode);
		if(customer == null || customer.isEnabled()) {
			return false;
		}
		else {
			customerRepository.enable(customer.getId());
			return true;
		}
	}
	
	public void updateAuthenticationType(Customer customer, AuthenticationType type) {
		if(!customer.getAuthenticationType().equals(type)) {
			customerRepository.updateAuthenticationType(customer.getId(), type);
	 	}
		
	}

	public void addNewCustomerUponOAuthLogin(String name, String mail,String countryCode,AuthenticationType type) {
		// TODO Auto-generated method stub
		Customer newCustomer = new Customer();
		setName(name,newCustomer);
		newCustomer.setEmail(mail);
		newCustomer.setFirstName(name);
		newCustomer.setEnabled(true);
		newCustomer.setCreatedTime(new Date());
		newCustomer.setAuthenticationType(type);
		newCustomer.setPassword("");
		newCustomer.setAddressLine1("");
		newCustomer.setCity("");
		newCustomer.setState("");
		newCustomer.setPhoneNumber("");
		newCustomer.setPostalCode("");
		newCustomer.setCountry(coutrnCountryRepository.findByCode(countryCode));
		
		customerRepository.save(newCustomer);
	}
	
	private void setName(String name,Customer customer) {
		String[] nameArray = name.split(" ");
		if(nameArray.length < 2) {
			customer.setFirstName(name);
			customer.setLastName("");
		}else {
			String firstName = nameArray[0];
			customer.setFirstName(firstName);
			
			String lastName = name.replace(firstName + " "," ");
			customer.setLastName(lastName);
		}
	}
	
	public void update(Customer customerInform) {
		Customer customerInDB = customerRepository.findById(customerInform.getId()).get();
		
		if(customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
			if(!customerInform.getPassword().isEmpty()) {
				String passwordEncoded = passwordEncoder.encode(customerInform.getPassword());
				customerInform.setPassword(passwordEncoded);
			}else {
				customerInform.setPassword(customerInDB.getPassword());
			}
		}else {
			customerInform.setPassword(customerInDB.getPassword());
		}
		
		customerInform.setEnabled(customerInDB.isEnabled());
		customerInform.setCreatedTime(customerInDB.getCreatedTime());
		customerInform.setVerificationCode(customerInDB.getVerificationCode());
		customerInform.setAuthenticationType(customerInDB.getAuthenticationType());
		customerInform.setResetPasswordToekn(customerInDB.getResetPasswordToekn());
		customerRepository.save(customerInform);
	}
    //Change Password
	public String updatePasswordToken(String email) throws CustomerNotFoundException {
		// TODO Auto-generated method stub
		Customer customerByEmail = customerRepository.findByEmail(email);
		if(customerByEmail != null) {
			String token = RandomString.make(30);
			customerByEmail.setResetPasswordToekn(token);
			customerRepository.save(customerByEmail);
			return token;
		}else {
			throw new CustomerNotFoundException("Could not find any customer with the email: " + email);
		}
		
	}
	
	public Customer getCustomerByResetPasswordToken(String token) {
		return customerRepository.findByResetPasswordToken(token);
	}
	
	public void updatePassword(String token,String newPassword) throws CustomerNotFoundException {
		Customer customer = customerRepository.findByResetPasswordToken(token);
		if(customer == null) {
			throw new CustomerNotFoundException("Invalid Token No customer found");
		}else {
			customer.setPassword(newPassword);
			customer.setResetPasswordToekn(null);
		    this.encodePassword(customer);
		    customerRepository.save(customer);
		}
	}
}
