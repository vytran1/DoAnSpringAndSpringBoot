package com.shopme.admin.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUltil;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.User;

@Controller
public class AccountController {
   @Autowired
   private UserService userService;
   
   
   @GetMapping("/account")
   public String viewDetail(@AuthenticationPrincipal ShopmeUserDetails loggedUser,Model model) {
	  String email =  loggedUser.getUsername();
	  User  user = userService.getByEmail(email);
	  model.addAttribute("user",user);
	  return "users/account_form";
   }
   
   @PostMapping("/account/update")
	 public String saveDetails(User user,
			 @AuthenticationPrincipal ShopmeUserDetails loggedUser,
			 RedirectAttributes redirectAttributes,@RequestParam("image") MultipartFile multipartFile) throws IOException {
		//System.out.println(user);
		//System.out.println(multipartFile.getOriginalFilename());
		if(!multipartFile.isEmpty()) {
			String fileName = org.springframework.util.StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = userService.updateAccount(user);
			String uploadDir = "user-photos/" + savedUser.getId();
			FileUploadUltil.cleanDir(uploadDir);
			FileUploadUltil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if(user.getPhotos().isEmpty()) {
				user.setPhotos(null);
			}
			userService.updateAccount(user);
		}
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
		redirectAttributes.addFlashAttribute("message", "Your account details have been updated");
		return "redirect:/account";
	 }
}
