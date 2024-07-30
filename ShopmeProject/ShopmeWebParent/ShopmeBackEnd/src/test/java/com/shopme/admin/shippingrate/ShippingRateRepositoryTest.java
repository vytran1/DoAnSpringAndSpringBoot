package com.shopme.admin.shippingrate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ShippingRateRepositoryTest {
     
	@Autowired
	private ShippingRateRepository shippingRateRepository;
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Test
	public void testCreateRate() {
		Integer countryID = 1;//VietName
		Country VietName = new Country(1);
		ShippingRate shippingRate = new ShippingRate();
		shippingRate.setCountry(VietName);
		shippingRate.setState("VUNGTAU");
		shippingRate.setRate(7.0f);
		shippingRate.setCodSupported(false);
		shippingRate.setDays(3);
		
		ShippingRate savedObject = shippingRateRepository.save(shippingRate);
		assertThat(savedObject).isNotNull();
		assertThat(savedObject.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdate() {
		Integer rateId = 1;
		ShippingRate rate = testEntityManager.find(ShippingRate.class, rateId);
		rate.setRate(9.0f);
		rate.setDays(2);
		ShippingRate updatedRate = shippingRateRepository.save(rate);
		
		assertThat(updatedRate.getRate()).isEqualTo(9.0f);
		assertThat(updatedRate.getDays()).isEqualTo(2);
	}
	
	@Test
	public void testFindAll() {
		List<ShippingRate> rates = (List<ShippingRate>) shippingRateRepository.findAll();
		assertThat(rates.size()).isGreaterThan(0);
		
		rates.forEach(System.out::println);
	}
	
	@Test
	public void testFindByCountryAndState() {
		Integer countryId = 1;
		String state = "VUNGTAU";
		ShippingRate rate = shippingRateRepository.findByCountryAndState(countryId, state);
		
		assertThat(rate).isNotNull();
		System.out.println(rate);
	}
	
	@Test
	public void testUpdateCODSupport() {
		Integer rateId = 1;
		shippingRateRepository.updateCODSupported(rateId,true);
		
		ShippingRate rate = testEntityManager.find(ShippingRate.class, rateId);
		assertThat(rate.isCodSupported()).isTrue();
	}
	
}
