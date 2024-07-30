package com.shopme.admin.shippingrate;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.ShippingRate;

public interface ShippingRateRepository extends SearchRepository<ShippingRate,Integer> {
    
	@Query("SELECT s FROM ShippingRate s WHERE s.country.name LIKE %?1% OR s.state LIKE %?1%")
	public Page<ShippingRate> findAll(String keyWord,Pageable pageable);
	
	@Query("SELECT s FROM ShippingRate s WHERE s.country.id = ?1 AND s.state = ?2")
	public ShippingRate findByCountryAndState(Integer countryId, String state);
	
	@Query("UPDATE ShippingRate s SET s.codSupported = ?2 WHERE s.id = ?1")
	@Modifying
	public void updateCODSupported(Integer shippingRateId,boolean status);
	
	public Long countById(Integer id);
}
