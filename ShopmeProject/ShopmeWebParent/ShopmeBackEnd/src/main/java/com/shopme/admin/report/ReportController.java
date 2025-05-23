package com.shopme.admin.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.setting.Setting;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ReportController {
	@Autowired
    private SettingService settingService;
	
	@GetMapping("/reports")
	public String viewSalesReportHome(HttpServletRequest request) {
		loadCurrencySetting(request);
		return "reports/reports";
	}
	
    private void loadCurrencySetting(HttpServletRequest request) {
	   List<Setting> currencySettings = settingService.getCurrencySetting();
	    for(Setting setting : currencySettings) {
	    	request.setAttribute(setting.getKey(),setting.getValue());
	    }
	}
    
   
}
