package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTest {
      
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Test
	public void testCreateCurrency() {
		List<Currency> currencies = Arrays.asList(
				new Currency("United States Dollar","$","USD"),
				new Currency("British Pound","£","GPB"),
				new Currency("Japanese Yen","¥","JPY"),
				new Currency("Euro","€","EUR"),
				new Currency("Russian Ruble","₽","RUB"),
				new Currency("South Korean Won","₩","KRW"),
				new Currency("Chinese Yuan","¥","CNY"),
				new Currency("Brazilian Real","R$","BRL"),
				new Currency("Australian Dollar","$","AUD"),
				new Currency("Canadian Dollar","$","CAD"),
				new Currency("Vietnamese Đồng","đ","VND"),
				new Currency("Indian Rupee","₹","INR")
	    );
		currencyRepository.saveAll(currencies);
	}
	
	@Test
	public void testFindAllCurrencyOrderByNameASC() {
		List<Currency> list = currencyRepository.findAllByOrderByNameAsc();
		list.forEach((x)->System.out.println(x));
		assertThat(list).size().isGreaterThan(0);
	}
}
