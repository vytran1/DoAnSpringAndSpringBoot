package com.shopme.customer;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ForgotPasswordController {
    @Autowired CustomerService customerService;
    @Autowired SettingService settingService;
    @GetMapping("/forgot_password")
    public String showRequestForm() {
    	return "customers/forgot_password_form";
    }
    
    @PostMapping("/forgot_password")
    public String processRequest(HttpServletRequest request, Model model) {
    	String email = request.getParameter("email");
    	try {
    		
          String token = customerService.updatePasswordToken(email);
          System.out.println("Token" + token);
          String link = Utility.getSiteUrl(request) + "/reset_password?token=" + token;
          sendEmail(link, email);
          model.addAttribute("message","We have send a resetLink To your email Please check");
    	}catch(CustomerNotFoundException ex) {
    	  model.addAttribute("error",ex.getMessage());
    	}catch (UnsupportedEncodingException  |  MessagingException e) {
    	  model.addAttribute("error","Could Not Send Email");
		}
    	
    	return "customers/forgot_password_form";
    }
    
    private void sendEmail(String link,String email) throws UnsupportedEncodingException, MessagingException {
    	EmailSettingBag settingsBag =  settingService.getEmailSettingBag();
		JavaMailSenderImpl mailSender =  Utility.preJavaMailSenderImpl(settingsBag);
		
		String toAddress = email;
		String subject = "Here's the link to reset your password";
		
		String content = "<p>Hello,</p>" 
		+ "<p>You have requested to reset your password</p" 
		+ "Click The Link below to change your password" 
		+"<p><a href=\"" + link +  "\">Change My Password</a></p>"
		+"<br>"
		+"<p>Ignore it if you have remember your password</p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message);
		
		
		messageHelper.setFrom(settingsBag.getFromAddress(), settingsBag.getSenderName());
		messageHelper.setTo(toAddress);
		messageHelper.setSubject(subject);
		
		messageHelper.setText(content,true);
		mailSender.send(message);
		
    }
    
    @GetMapping("/reset_password")
    public String showResetForm(@RequestParam("token") String token,Model model) {
    	Customer customer = customerService.getCustomerByResetPasswordToken(token);
    	if(customer != null) {
    		model.addAttribute("token",token);
    	}else {
    		model.addAttribute("message","Invalid Token");
    		return "message";
    	}
    	return "customers/reset_password_form";
    }
    
    @PostMapping("/reset_password")
    public String processResetForm(HttpServletRequest request, Model model) {
    	String token = request.getParameter("token");
    	String newPassword = request.getParameter("password");
    	try {
			customerService.updatePassword(token, newPassword);
			model.addAttribute("pageTitle","Reset Your Password");
			model.addAttribute("message","You have successfully change your password");
			return "message";
		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle","ResetYourPassword");
			model.addAttribute("message",e.getMessage());
    		return "message";
		}
    	
    }
}
