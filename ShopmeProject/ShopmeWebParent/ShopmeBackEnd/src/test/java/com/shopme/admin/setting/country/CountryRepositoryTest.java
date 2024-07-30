package com.shopme.admin.setting.country;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CountryRepositoryTest {
     @Autowired 
     private CountryRepository countryRepository;
     
     @Test
     public void testCreateCountry() {
//    	 Country country = countryRepository.save(new Country("Vietnam","VN"));
//    	 assertThat(country).isNotNull();
//    	 assertThat(country.getId()).isGreaterThan(0);
    	 
    	 
    	 Country country = countryRepository.save(new Country("Australia","AU"));
    	 assertThat(country).isNotNull();
    	 assertThat(country.getId()).isGreaterThan(0);
     }
     
     @Test
     public void testListCountry() {
    	 List<Country> listCountries = countryRepository.findAllByOrderByNameAsc();
    	 listCountries.forEach((x)->System.out.println(x.getName()));
    	 assertThat(listCountries).size().isGreaterThan(0);
     }
     
     
    
}
