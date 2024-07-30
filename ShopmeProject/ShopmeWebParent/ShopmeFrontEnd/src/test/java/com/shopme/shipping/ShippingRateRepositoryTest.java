package com.shopme.shipping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ShippingRateRepositoryTest {
    @Autowired
    private ShippingRateRepository shippingRateRepository;
    
    @Test
    public void findByCountryAndStateTest() {
    	Country vietNam = new Country(1);
    	String state ="TPHCM";
    	ShippingRate result = shippingRateRepository.findByCountryAndState(vietNam, state);
    	assertThat(result).isNotNull();
    	System.out.println(result);
    }
}
