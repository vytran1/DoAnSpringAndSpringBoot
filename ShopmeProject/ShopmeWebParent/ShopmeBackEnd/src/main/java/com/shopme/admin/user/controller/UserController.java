package com.shopme.admin.user.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUltil;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.admin.user.export.UserExcelExporter;
import com.shopme.admin.user.export.UserPDFExporter;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UserController {
	 @Autowired
     private UserService userService;
	 
	 @GetMapping("/users")
	 public String listAll() {
//		 List<User> users = userService.listAll();
//		 model.addAttribute("listUsers",users);
//		 return "users";
//		 return listByPage(paHelper,1, model,"firstName","asc",null);
		 return "redirect:/users/page/1?sortField=firstName&sortDir=asc";
		 
	 }
	 @GetMapping("/users/page/{pageNum}")
	 public String listByPage(@PagingAndSortingParam(listName = "listUsers",moduleURL = "/users") PagingAndSortingHelper paHelper,
			 @PathVariable("pageNum") int pageNum, Model model
			 ) {
		 
		 userService.listByPage(pageNum,paHelper);

		 
		 return "users/users";
	 }
	 @GetMapping("/users/new")
	 public String createUserForm(Model model) {
		 User user = new User();
		 user.setEnabled(true);
		 List<Role> roles = userService.listRoles();
		 model.addAttribute("user",user);
		 model.addAttribute("listRoles",roles);
		 model.addAttribute("pageTitle","Create New User");
		 return "users/user_form";
	 }
	 
	 @PostMapping("/users/save")
	 public String saveUser(User user, RedirectAttributes redirectAttributes,@RequestParam("image") MultipartFile multipartFile) throws IOException {
		//System.out.println(user);
		//System.out.println(multipartFile.getOriginalFilename());
		
		 
		 
		if(!multipartFile.isEmpty()) {
			System.out.println(multipartFile.getOriginalFilename());
			//Screen Shot......
			String fileName = org.springframework.util.StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = userService.saveUser(user);
			String uploadDir = "user-photos/" + savedUser.getId();
			FileUploadUltil.cleanDir(uploadDir);
			FileUploadUltil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if(user.getPhotos().isEmpty()) {
				user.setPhotos(null);
			}
			userService.saveUser(user);
		}
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
		return getRedirectURLtoAffectedUser(user);
	 }
	private String getRedirectURLtoAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyWord=" + firstPartOfEmail;
	}
	 
	 @GetMapping("/users/edit/{id}")
	 public String editUser(@PathVariable("id") Integer id, 
			 RedirectAttributes redirectAttributes, 
			 Model model) {
		 try {
			 User user = userService.get(id);
			 List<Role> roles = userService.listRoles();
			 model.addAttribute("user",user);
			 model.addAttribute("pageTitle","Edit User By Id");
			 model.addAttribute("listRoles",roles);
			 return "users/user_form";
		 }catch(UserNotFoundException ex) {
			 redirectAttributes.addFlashAttribute("message", ex.getMessage());
			 return "redirect:/users";
		 }
	 }
	 @GetMapping("/users/delete/{id}")
	 public String deleteUser(@PathVariable("id") Integer id, Model model,RedirectAttributes redirectAttributes) {
		 try {
			 userService.deleteUserById(id);
			 redirectAttributes.addFlashAttribute("message", "Delete Successfull User with Id" + id);
			 return "redirect:/users";
		 }catch(UserNotFoundException ex) {
			 redirectAttributes.addFlashAttribute("message", ex.getMessage());
			 return "redirect:/users";
		 }
	 }
	 
	 @GetMapping("/users/{id}/enabled/{status}")
	 public String updateUserEnabledStatus(@PathVariable("id") Integer Id, 
			 @PathVariable("status") boolean status,RedirectAttributes redirectAttributes) {
		 userService.updateUserEnabledStatus(Id, status);
		 String messageStatus = status ? "Enabled":"Disabled";
		 String message = "The user with ID" + Id + "has been" + messageStatus + "success"; 
		 redirectAttributes.addFlashAttribute("message",message);
		 return "redirect:/users";
	 }
	 
	 @GetMapping("/users/export/csv")
	 public void exportToCSV(HttpServletResponse response) throws IOException {
		 List<User> listUser =  userService.listAll();
		 UserCsvExporter exporter = new UserCsvExporter();
		 exporter.export(listUser, response);
	 }
	 @GetMapping("/users/export/excel")
	 public void exportToExcel(HttpServletResponse response) throws IOException {
		 List<User> listUser =  userService.listAll();
		 UserExcelExporter exporter = new UserExcelExporter();
		 exporter.export(listUser, response);
	 }
	 @GetMapping("/users/export/pdf")
	 public void exportToPDF(HttpServletResponse response) throws IOException {
		 List<User> listUser =  userService.listAll();
		 UserPDFExporter exporter = new UserPDFExporter();
		 exporter.export(listUser, response);
	 }
}
