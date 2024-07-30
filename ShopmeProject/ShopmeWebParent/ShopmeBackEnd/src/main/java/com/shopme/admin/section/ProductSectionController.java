package com.shopme.admin.section;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.SectionUtil;
import com.shopme.common.entity.section.Section;
import com.shopme.common.entity.section.SectionType;
import com.shopme.common.exception.SectionNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ProductSectionController {
	@Autowired
    private SectionService sectionService;
	
	@GetMapping("/sections/new/product")
	public String showForm(Model model) {
		Section section = new Section(true, SectionType.PRODUCT);
		model.addAttribute("section", section);
		model.addAttribute("pageTitle", "Add Product Section");
		return "sections/product_section_form";
	}
	
	@PostMapping("/sections/save/product")
	public String saveSection(Section section,HttpServletRequest request,RedirectAttributes ra) {
		SectionUtil.addProductsToSection(section, request);
		sectionService.saveSection(section);
		ra.addFlashAttribute("messageSuccess", "The section of type Product has been saved successfully.");
		return "redirect:/sections";
	}
	
	@GetMapping("/sections/edit/Product/{id}")
	public String editSection(@PathVariable("id") Integer id, RedirectAttributes ra, Model model) {
		try {
			Section section = sectionService.getSection(id);
			model.addAttribute("section", section);
			model.addAttribute("pageTitle", "Edit Product Section (ID: " + id + ")");
			return "sections/product_section_form";
		}catch(SectionNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/sections";
		}
	}
}
