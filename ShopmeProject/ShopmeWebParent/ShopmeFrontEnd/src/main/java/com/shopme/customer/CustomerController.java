package com.shopme.customer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.Utility;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.security.CustomerUserDetails;
import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class CustomerController {
    
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private SettingService settingService;
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		List<Country> countries = customerService.listAllCountries();
		model.addAttribute("listCountries",countries);
		model.addAttribute("pageTitle","Customer Registration");
		model.addAttribute("customer",new Customer());
		return "register/register_form";
	}
	
	@PostMapping("/create_customer")
	public String createCustomer(@Valid Customer customer,Errors errors,Model model,HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		 if(errors.hasErrors()) {
			 return "register/register_form";
		 }
		
		 customerService.registerCustomer(customer);
		 sendVerificationEmail(request,customer);
		 
		 
		 model.addAttribute("pageTitle","Registration Succeeded!");
		 
		 
		 return "register/register_success";
	}

	private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag settingsBag =  settingService.getEmailSettingBag();
		JavaMailSenderImpl mailSender =  Utility.preJavaMailSenderImpl(settingsBag);
		mailSender.setDefaultEncoding("utf-8");
		String toAddress = customer.getEmail();
		String subject = settingsBag.getCustomerVerifySubject();
		String content = settingsBag.getCustomerVerifyContent();
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message);
		
		messageHelper.setFrom(settingsBag.getFromAddress(), settingsBag.getSenderName());
		messageHelper.setTo(toAddress);
		messageHelper.setSubject(subject);
		
		content = content.replace("[[customer]]", customer.getFullName());
		String verifyURL = Utility.getSiteUrl(request) + "/verify?code=" + customer.getVerificationCode();
		content = content.replace("[[URL]]", verifyURL);
		
		//Đối số thứ 2 là HTML form -> true 
		messageHelper.setText(content, true);
		mailSender.send(message);
		
		System.out.println("to Address: " + toAddress);
		System.out.println("Verify URL:" + verifyURL);
	}
	
	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code,Model model) {
		boolean isVerify =  customerService.verify(code);
		return "register/" + (isVerify ? "verify_success":"verify_fail");
	}
	
	@GetMapping("/account_details")
	public String viewAccountDetails(Model model, HttpServletRequest request) {
		String emailAuthentication = Utility.getEmailOfAuthenticationCustomer(request);
		Customer customerByEmail = customerService.getCustomerByEmail(emailAuthentication);
		List<Country> listCountries = customerService.listAllCountries();
		model.addAttribute("customer",customerByEmail);
		model.addAttribute("listCountries",listCountries);
		
		return "customers/account_form";
	}
//	private String getEmailOfAuthenticationCustomer( HttpServletRequest request) {
//		Object principal = request.getUserPrincipal();
//		String customerEmail = null;
//		if( principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken ) {
//			customerEmail = request.getUserPrincipal().getName();
//		}else if(principal instanceof OAuth2AuthenticationToken) {
//			OAuth2AuthenticationToken OAuthToekn  = (OAuth2AuthenticationToken) principal;
//			CustomerOAuth2User oauthUser = (CustomerOAuth2User)OAuthToekn.getPrincipal();
//			customerEmail = oauthUser.getMail();
//		}
//		return customerEmail;
//	}
	
    @PostMapping("/update_account_details")
    public String updateAccountDetails(Model model, Customer customerInForm,RedirectAttributes ra,HttpServletRequest request) {
    	customerService.update(customerInForm);
    	ra.addFlashAttribute("message","Your details account have been updated ");
    	
    	updateNameForAuthenticatedCustomer(request,customerInForm);
    	String redirectOption = request.getParameter("redirect");
    	String redirectURL = "redirect:/account_details";
    	if("address_book".equals(redirectOption)) {
    		redirectURL = "redirect:/address_book";
    	}else if("cart".equals(redirectOption)) {
    		redirectURL = "redirect:/cart";
    	}else if("checkout".equals(redirectOption)) {
    		redirectURL = "redirect:/address_book?redirect=checkout";
    	}
    	
    	return redirectURL;
    }

	private void updateNameForAuthenticatedCustomer(HttpServletRequest request,Customer customerInForm) {
		// TODO Auto-generated method stub
		Object principal = request.getUserPrincipal();
		String fullName = customerInForm.getFirstName() + " " + customerInForm.getFullName(); 
		if( principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken ) {
			CustomerUserDetails customerDetails =  getCustomerDetailsObject(principal);
			Customer authenticatedCustomer = customerDetails.getCustomer();
			authenticatedCustomer.setFirstName(customerInForm.getFirstName());
			authenticatedCustomer.setLastName(customerInForm.getLastName());
		}else if(principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken OAuthToekn  = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauthUser = (CustomerOAuth2User)OAuthToekn.getPrincipal();
			oauthUser.setFullName(fullName);
		}
	}
	
	private CustomerUserDetails getCustomerDetailsObject(Object principal) {
		CustomerUserDetails userDetails = null;
		if(principal instanceof UsernamePasswordAuthenticationToken ) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			userDetails = (CustomerUserDetails)token.getPrincipal();
		}else if(principal instanceof RememberMeAuthenticationToken) {
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
			userDetails = (CustomerUserDetails)token.getPrincipal();
		}
		return userDetails;
	}
}
