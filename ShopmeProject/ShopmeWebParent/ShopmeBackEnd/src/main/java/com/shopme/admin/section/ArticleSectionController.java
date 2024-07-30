package com.shopme.admin.section;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.SectionUtil;
import com.shopme.admin.article.ArticleService;
import com.shopme.common.entity.Article;
import com.shopme.common.entity.section.Section;
import com.shopme.common.entity.section.SectionType;
import com.shopme.common.exception.SectionNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ArticleSectionController {
   
	
	@Autowired
	private SectionService sectionService;
	
	@Autowired
	private ArticleService articleService;
	
	
	@GetMapping("/sections/new/article")
	public String showForm(Model model) {
		Section section = new Section(true, SectionType.ARTICLE);
		List<Article> listArticles = articleService.listAll();
		
		model.addAttribute("listArticles", listArticles);
		model.addAttribute("section", section);
		model.addAttribute("pageTitle", "Add Article Section");
		
		return "sections/article_section_form";
	}
	
	@PostMapping("/sections/save/article")
	public String saveSection(Section section, RedirectAttributes ra, HttpServletRequest request) {
		SectionUtil.addArticlesToSection(section, request);
		sectionService.saveSection(section);
		ra.addFlashAttribute("message","The section of type Article has been saved successfully.");
		return "redirect:/sections";
	}
	
	@GetMapping("/sections/edit/Article/{id}")
	public String editSection(@PathVariable(name="id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Section section = sectionService.getSection(id);
			List<Article> listArticles = articleService.listAll();
			model.addAttribute("listArticles", listArticles);
			model.addAttribute("section", section);
			model.addAttribute("pageTitle", "Edit Article Section (ID: " + id + ")");
			return "sections/article_section_form";
		} catch (SectionNotFoundException e) {
			// TODO Auto-generated catch block
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/sections";
		}
	}
}
