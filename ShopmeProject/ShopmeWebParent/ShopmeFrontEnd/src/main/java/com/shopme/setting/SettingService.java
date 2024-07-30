package com.shopme.setting;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;

@Service
public class SettingService {
    @Autowired
    private SettingRepository settingRepository;
    
    
    @Autowired
    private CurrencyRepository currencyRepository;
   
    
    public List<Setting> getGeneralSetting() {
    	return settingRepository.findByTwoCategory(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }
    
    public EmailSettingBag getEmailSettingBag() {
    	List<Setting> settings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
    	settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));
    	return new EmailSettingBag(settings);
    }
    
    public CurrencySettingBag getCurrencySetting() {
    	List<Setting> settings = settingRepository.findByCategory(SettingCategory.CURRENCY);
    	return new CurrencySettingBag(settings);
    }
    
    public PaymentSettingBag getPaymentSettingBag() {
    	List<Setting> settings = settingRepository.findByCategory(SettingCategory.PAYMENT);
    	return new PaymentSettingBag(settings);
    }
    
    public String getCurrencyCode() {
    	Setting setting = settingRepository.findByKey("CURRENCY_ID");
    	Integer currencyId = Integer.parseInt(setting.getValue());
    	com.shopme.common.entity.Currency currencyObject = currencyRepository.findById(currencyId).get();
    	return currencyObject.getCode();
    	
    }
}
