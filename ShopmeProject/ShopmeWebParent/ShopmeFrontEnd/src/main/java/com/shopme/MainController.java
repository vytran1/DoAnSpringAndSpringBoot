package com.shopme;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.section.Section;
import com.shopme.section.SectionService;

@Controller
public class MainController {
   
	@Autowired
	private CategoryService categoryService;
	
	
	@Autowired
	private SectionService sectionService;
	
	@GetMapping("")
	public String viewHomePage(Model model) {
		List<Section> listSections = sectionService.listEnabledSection();
		model.addAttribute("listSections", listSections);
		if(SectionUtil.hasAllCategoriesSection(listSections)) {
			List<Category> listCategory = categoryService.listNoChildrenCategories();
			model.addAttribute("listCategories",listCategory);
		}
		return "index";
	}
	
	
	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null  || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		return "redirect:/";
	}
}
