package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTest {
    @Autowired
    private SettingRepository settingRepository;
    
    @Test
    public void createSetting() {
    	//Setting setting = new Setting("SITE_NAME","Shopme",SettingCategory.GENERAL);
    	Setting siteLogo = new Setting("SITE_LOGO","Shopme.png",SettingCategory.GENERAL);
    	Setting copyRight = new Setting("COPYRIGHT","Copyright (C) 2023 Shopme LTD",SettingCategory.GENERAL);

    	settingRepository.saveAll(List.of(siteLogo,copyRight));
    	Iterable<Setting> iterator = settingRepository.findAll();
    	assertThat(iterator).size().isGreaterThan(0);
    }
    
    @Test
    public void createCurrencySettings() {
    	Setting currencyId = new Setting("CURRENCY_ID","1",SettingCategory.CURRENCY);
    	Setting symbol = new Setting("CURRENCY_SYMBOL","$",SettingCategory.CURRENCY);
    	Setting symbolPosition= new Setting("CURRENCY_SYMBOL_POSITION","before",SettingCategory.CURRENCY);
    	Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE","POINT",SettingCategory.CURRENCY);
    	Setting decimalDigits = new Setting("DECIMAL_DIGITS","2",SettingCategory.CURRENCY);
    	Setting thousandPointType = new Setting("THOUSANDS_POINT_TYPE","COMMA",SettingCategory.CURRENCY);


        settingRepository.saveAll(List.of(currencyId,symbol,symbolPosition,decimalPointType,decimalDigits,thousandPointType));
    }
    
    @Test
    public void testListingsByCategory() {
    	List<Setting> settings = settingRepository.findByCategory(SettingCategory.GENERAL);
    	settings.forEach((x)->System.out.println(x));
    }
}
