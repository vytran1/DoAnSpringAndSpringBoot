package com.shopme.admin.shippingrate;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.product.ProductRepository;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ShippingRateAlreadyExistsException;
import com.shopme.common.exception.ShippingRateNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShippingRateService {
    
	public static final int RATES_PER_PAGE = 3;
	private static final int DIM_DIVISOR = 139;
	
	@Autowired
	private ShippingRateRepository shippingRateRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum,RATES_PER_PAGE,shippingRateRepository);
	}
	
	public List<Country> listCountries(){
		return countryRepository.findAllByOrderByNameAsc();
	}
	
	public void save(ShippingRate shippingRate) throws ShippingRateAlreadyExistsException {
		ShippingRate objectInDB = shippingRateRepository.findByCountryAndState(shippingRate.getCountry().getId(),shippingRate.getState());
		boolean foundExistRateInNewMode  = shippingRate.getId() == null && objectInDB != null;
		boolean foundDifferentExistingRateInEditMode = shippingRate.getId() != null && objectInDB != null && !objectInDB.equals(shippingRate);
		
		if(foundExistRateInNewMode || foundDifferentExistingRateInEditMode) {
			throw new ShippingRateAlreadyExistsException("There is already a rate for the destination " + shippingRate.getCountry().getName() 
					+ " and name of state " + shippingRate.getState());
		}
		shippingRateRepository.save(shippingRate);
	}
	
	
	public ShippingRate getById(Integer shippingRateID) throws ShippingRateNotFoundException {
		try {
			return shippingRateRepository.findById(shippingRateID).get();
		}catch(Exception e) {
			throw new ShippingRateNotFoundException("Could not found shipping rate with id " + shippingRateID);
		}
	}
	
	public void updateCODStatus(Integer id,boolean status) throws ShippingRateNotFoundException {
		Long count = shippingRateRepository.countById(id);
		if(count == 0 || count == null) {
			throw new ShippingRateNotFoundException("Could not found shipping rate with id " + id);
		}
		shippingRateRepository.updateCODSupported(id, status);
	}
	
	public void delete(Integer id) throws ShippingRateNotFoundException {
		Long countByID = shippingRateRepository.countById(id);
		if(countByID == 0 || countByID == null) {
			throw new ShippingRateNotFoundException("Could not found shipping rate with id " + id);
		}
		shippingRateRepository.deleteById(id);
	}
	
	public float calculateShippingCost(Integer productId, Integer countryId,String state) throws ShippingRateNotFoundException {
		ShippingRate shippingRate = shippingRateRepository.findByCountryAndState(countryId, state);
		
		
		if(shippingRate == null) {
			throw new ShippingRateNotFoundException("No shipping rate found for the destinations. You have to enter shipping rate manually");
		}
		
		Product product  = productRepository.findById(productId).get();
        float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;	
        float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
        
        
		return finalWeight * shippingRate.getRate();
	}
	
}
