package com.shopme.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.common.entity.Article;
import com.shopme.common.exception.MenuNotFoundException;

@Controller
public class MenuController {
     
	@Autowired
	private MenuService menuService;
	
	@GetMapping("/m/{alias}")
	public String viewMenu(@PathVariable("alias")String alias,Model model) {
		try {
			Article article = menuService.getArticleBoundToMenu(alias);
			
			model.addAttribute("pageTitle",article.getTitle());
			model.addAttribute("article",article);
			return "article";
		}catch(MenuNotFoundException e) {
			return "error/404";
		}
	}
}
