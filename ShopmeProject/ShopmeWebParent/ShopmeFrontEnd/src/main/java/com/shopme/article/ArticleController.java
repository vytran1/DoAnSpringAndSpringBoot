package com.shopme.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.common.entity.Article;
import com.shopme.common.exception.ArticleNotFoundException;

@Controller
public class ArticleController {
    
	@Autowired
	private ArticleService articleService;
	
	@GetMapping("/a/{alias}")
	public String viewArticle(@PathVariable("alias") String alias, Model model) {
		
		try {
			Article article = articleService.findByAlias(alias);
			model.addAttribute("pageTitle",article.getTitle());
			model.addAttribute("article",article);
			return "article";
		} catch (ArticleNotFoundException e) {
			// TODO Auto-generated catch block
			return "error/404";
		}
	}
}
