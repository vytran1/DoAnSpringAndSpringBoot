package com.shopme.admin.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.user.UserService;

@RestController
public class UserRestController {
	  @Autowired
      private UserService userService;
	  
	  @PostMapping("/users/check_email")
	  public String checkDupplicatesEmail(@RequestParam(name="id", required = false) Integer id,@RequestParam(name ="email") String email) {
		  return userService.emailIsUnique(id,email) ? "OK" :"Dupplicated";
	  }
}
