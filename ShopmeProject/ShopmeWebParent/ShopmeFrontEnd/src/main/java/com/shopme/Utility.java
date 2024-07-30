package com.shopme;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.CurrencySettingBag;
import com.shopme.setting.EmailSettingBag;

import jakarta.servlet.http.HttpServletRequest;

public class Utility {
   public static String getSiteUrl(HttpServletRequest request) {
	   String siteURL = request.getRequestURL().toString();
	   return siteURL.replace(request.getServletPath(),"");
   }
   
   
   public static JavaMailSenderImpl preJavaMailSenderImpl(EmailSettingBag emailSettingBag) {
	   JavaMailSenderImpl mailSender =  new JavaMailSenderImpl();
	   
	   mailSender.setHost(emailSettingBag.getHost());
	   mailSender.setPort(emailSettingBag.getPort());
	   mailSender.setUsername(emailSettingBag.getUsername());
	   mailSender.setPassword(emailSettingBag.getPassword());
	   
	   Properties mailProperties = new Properties();
	   mailProperties.setProperty("mail.smtp.auth", emailSettingBag.getSmtpAuth());
	   mailProperties.setProperty("mail.smtp.starttls.enable", emailSettingBag.getSmtpSecured());
	   
	   mailSender.setJavaMailProperties(mailProperties);
	   return mailSender;
   }
   
   public static String getEmailOfAuthenticationCustomer( HttpServletRequest request) {
		Object principal = request.getUserPrincipal();
	
		if(principal == null) {
			return null;
		}
		String customerEmail = null;
		if( principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken ) {
			customerEmail = request.getUserPrincipal().getName();
			
		}else if(principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken OAuthToekn  = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauthUser = (CustomerOAuth2User)OAuthToekn.getPrincipal();
			customerEmail = oauthUser.getMail();
		}
		return customerEmail;
	}
	
   public static String formatCurrency(float amount, CurrencySettingBag settingBag) {
	   String symbol = settingBag.getSymbol();
	   String symbolPosition = settingBag.getSymbolPosition();
	   String decimalPointType = settingBag.getDecimalPointType();
	   String thousandPointType = settingBag.getThousandPointType();
	   int decimalDigits = settingBag.getDecimalDigits();
	   
	   
	   
	   String pattern = symbolPosition.equals("Before Price")? symbol : "";
	   pattern += "###,###";
	   
	   if(decimalDigits >0) {
		   pattern += ".";
		   for (int i = 1; i < decimalDigits; i++) {
			    pattern += "#";
		   }
	   }
	   pattern += symbolPosition.equals("After Price")? symbol : "";
	   
	   char thousandSeparator =  thousandPointType.equals("POINT") ? '.' : ',';
	   char decimalSeparator = decimalPointType.equals("POINT") ? '.' : ',';
	   
	   DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
	   decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
	   decimalFormatSymbols.setGroupingSeparator(thousandSeparator);
	   
	   DecimalFormat formatter = new DecimalFormat(pattern,decimalFormatSymbols);
	   return formatter.format(amount);
   }
   
//    public static void main(String[] args) {
//    	float amount = 5555555.55555555f;
//    	String formatted = formatCurrency(amount);
//    	System.out.println(formatted);
//    }
}
