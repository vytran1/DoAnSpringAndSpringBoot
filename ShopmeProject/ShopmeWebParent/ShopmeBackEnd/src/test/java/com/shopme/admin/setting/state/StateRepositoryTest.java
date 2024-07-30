package com.shopme.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTest {
    @Autowired
    StateRepository stateRepository;
    
    @Autowired
    TestEntityManager testEntityManager;
    
    @Test
    public void testCreateStatesInVietName() {
    	Integer countryID = 1;
    	Country country = testEntityManager.find(Country.class, countryID);
    	
    	
    	//State state1 = stateRepository.save(new State("TPHCM",country));
    	//State state2 = stateRepository.save(new State("DONGNAI",country));
    	State state3 = stateRepository.save(new State("BINHDUONG",country));
    	assertThat(state3).isNotNull();
    	assertThat(state3.getId()).isGreaterThan(0);
    }
}
