package com.shopme.admin.section;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.section.Section;
import com.shopme.common.entity.section.SectionType;
import com.shopme.common.exception.SectionNotFoundException;

@Controller
public class AllCategoriesSectionController {
     
	@Autowired
	private SectionService sectionService;
	
	
	@GetMapping("/sections/new/all_categories")
	public String showForm(Model model) {
		Section section = new Section(true,SectionType.ALL_CATEGORIES);
		model.addAttribute("section",section);
		model.addAttribute("pageTitle","Add All Categories Section");
		return "sections/all_categories_section_form";
	}
	
	@PostMapping("/sections/save/all_categories")
	public String saveSection(Section section,RedirectAttributes ra) {
		sectionService.saveSection(section);
		ra.addFlashAttribute("message","The section of type All Categories has been saved successfully.");
		return "redirect:/sections";
	}
	
	@GetMapping("/sections/edit/All_Categories/{id}")
	public String editSection(@PathVariable("id") Integer id, RedirectAttributes ra, Model model) {
		try {
			Section section = sectionService.getSection(id);
			model.addAttribute("section",section);
			model.addAttribute("pageTitle","Edit All Categories Section (ID: " + id + ")");
			return "sections/all_categories_section_form";
		} catch (SectionNotFoundException e) {
			// TODO Auto-generated catch block
			ra.addFlashAttribute("message",e.getMessage());
			return "redirect:/sections";
		}
	}
}
