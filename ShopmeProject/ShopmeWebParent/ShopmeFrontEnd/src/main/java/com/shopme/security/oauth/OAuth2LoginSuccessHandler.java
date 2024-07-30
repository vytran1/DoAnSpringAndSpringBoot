package com.shopme.security.oauth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    
	@Autowired 
	CustomerService customerService;
	
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		
		
		CustomerOAuth2User oAuth2User =  (CustomerOAuth2User)authentication.getPrincipal();
		// TODO Auto-generated method stub
		String name = oAuth2User.getFullName();
		String mail = oAuth2User.getMail();
		String countryCODE = request.getLocale().getCountry();
		System.out.println(countryCODE);
		String clientName = oAuth2User.getClientName();
		System.out.println("OAuth2LoginSuccessHandler with Name: " + name + "and with email: " + mail);
		System.out.println("Client Name" + clientName);
		
		AuthenticationType authenticationType = getAuthenticationType(clientName);
		Customer customerByEmail = customerService.getCustomerByEmail(mail);
		//chưa tạo
		if(customerByEmail == null) {
			customerService.addNewCustomerUponOAuthLogin(name,mail,countryCODE,authenticationType);
		}else {
			oAuth2User.setFullName(customerByEmail.getFullName());
			customerService.updateAuthenticationType(customerByEmail,authenticationType);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	private AuthenticationType getAuthenticationType(String clientName) {
		if(clientName.equals("Google")) {
			return AuthenticationType.GOOGLE;
		}else if(clientName.equals("Facebook")) {
			return AuthenticationType.FACEBOOK;
		}else {
			return AuthenticationType.DATABASE;
		}
	}
    
}
