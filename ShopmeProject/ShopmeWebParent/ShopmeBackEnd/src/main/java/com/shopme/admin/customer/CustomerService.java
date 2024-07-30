package com.shopme.admin.customer;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.order.OrderRepository;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CanNotDeleteException;
import com.shopme.common.exception.CustomerNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMER_PER_PAGE = 3;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	
	public List<Customer> findAll(){
		return (List<Customer>) customerRepository.findAll();
	}
	
	public Page<Customer> listByPage(int pageNum,String sortField,String sortDir,String keyWord){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc")? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum-1,CUSTOMER_PER_PAGE,sort);
		if(keyWord != null) {
			return customerRepository.findAll(keyWord, pageable);
		}
		return customerRepository.findAll(pageable);
	}
	
	public void updateCustomerEnabledStatus(Integer id, boolean status) {
		customerRepository.updateEnabledStatus(id, status);
	}
	
	public Customer getByID(Integer ID) throws CustomerNotFoundException {
		try {
			return customerRepository.findById(ID).get();
		}catch(Exception ex) {
			throw new CustomerNotFoundException("No exist Customer with ID " + ID);
		}
	}
	
	public boolean isEmailUnique(Integer id,String email) {
		Customer customerByID = customerRepository.findByEmail(email);
		if(customerByID != null && customerByID.getId() != id) {
			return false;
		}
		return true;
	}
	
	public Long isHaveOrder(Integer customerId) {
		return orderRepository.countByCustomerId(customerId);
	}
	
	
	public void saveCustomer(Customer customerInform) {
		Customer customerInDB = customerRepository.findById(customerInform.getId()).get();
		if(!customerInform.getPassword().isEmpty()) {
			String passwordEncoded = passwordEncoder.encode(customerInform.getPassword());
			customerInform.setPassword(passwordEncoded);
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
	
	public void delete(Integer id) throws CustomerNotFoundException, CanNotDeleteException {
		Long count = customerRepository.countById(id);
		if(count == null || count == 0) {
			throw new CustomerNotFoundException("Not exist Customer With ID " + id);
		}
		Long countOrder = isHaveOrder(id);
		if(countOrder > 0) {
			throw new CanNotDeleteException("Customer with id" + id  + "can not be delete because this person have been order");
		}
		customerRepository.deleteById(id);
	}
	public List<Country> listAllCountries(){
		return countryRepository.findAllByOrderByNameAsc();
	}
}
