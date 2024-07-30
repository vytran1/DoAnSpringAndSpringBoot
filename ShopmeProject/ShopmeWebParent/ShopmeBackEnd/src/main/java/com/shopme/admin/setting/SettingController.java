package com.shopme.admin.setting;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUltil;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.setting.Setting;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SettingController {
    @Autowired
    private SettingService settingService;
    
    @Autowired
    private CurrencyRepository currencyRepository;
    
    @GetMapping("/settings")
    public String listAll(Model model) {
    	List<Setting> listSetting = settingService.listAllSetting();
    	List<Currency> listCurrencies = currencyRepository.findAllByOrderByNameAsc();
    	
//    	model.addAttribute("listSettings",listSetting);
    	model.addAttribute("listCurrencies",listCurrencies);
    	
    	
    	for(Setting setting : listSetting) {
    		model.addAttribute(setting.getKey(),setting.getValue());
    	}
    	return "settings/settings";
    }
    
    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,HttpServletRequest request, RedirectAttributes ra) throws IOException {
    	GeneralSettingBag settingBag = settingService.getGeneralSetting();
    	
    	
    	saveSiteLogo(multipartFile, settingBag);
    	saveCurrencySymbol(request,settingBag);
    	updateSettingValuesFromForm(request,settingBag.list());
    	
    	
    	ra.addFlashAttribute("message","General settings have been save");
    	return "redirect:/settings";
    }
     
    
	private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
		if(!multipartFile.isEmpty()) {
    		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
    		String value = "/site-logo/" + fileName;
    		settingBag.updateSiteLogo(value);
    		String uploadDir = "../site-logo/";
    		FileUploadUltil.cleanDir(uploadDir);
    		FileUploadUltil.saveFile(uploadDir, fileName, multipartFile);
    	}
	}
	private void saveCurrencySymbol(HttpServletRequest request,GeneralSettingBag settingBag) {
		Integer  currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Optional<Currency> currencyResult = currencyRepository.findById(currencyId);
		if(currencyResult.isPresent()) {
			Currency currency = currencyResult.get();
			 
			settingBag.updateCurrencySymbol(currency.getSymbol());
		}
	}
	
	private void updateSettingValuesFromForm(HttpServletRequest request,List<Setting> listSettings) {
		 for(Setting x : listSettings) {
			 String value = request.getParameter(x.getKey());
			 if(value != null) {
				 x.setValue(value);
			 }
		 }
		 settingService.saveAll(listSettings);
	}
	 @PostMapping("/settings/save_mail_server")
	 public String saveMailServer(HttpServletRequest request,RedirectAttributes ra) {
		 List<Setting> listSettings = settingService.getMailServerSetting();
		 updateSettingValuesFromForm(request,listSettings);
		 ra.addFlashAttribute("message","Mail server setting have been save successfully to database");
		 return "redirect:/settings";
	 }
	 @PostMapping("/settings/save_mail_templates")
	 public String saveMailTemplates(HttpServletRequest request,RedirectAttributes ra) {
		 List<Setting> listSettings = settingService.getMailTemplatesSetting();
		 updateSettingValuesFromForm(request,listSettings);
		 ra.addFlashAttribute("message","Mail templates setting have been save successfully to database");
		 return "redirect:/settings";
	 }
	 
	 @PostMapping("/settings/save_payment")
	 public String savePaymentSettings(HttpServletRequest request,RedirectAttributes ra) {
		 List<Setting> listSettings = settingService.getPaymentSetting();
		 updateSettingValuesFromForm(request, listSettings);
		 ra.addFlashAttribute("message","Payment setting have been saved");
		 return "redirect:/settings";
	 }
	 
}
