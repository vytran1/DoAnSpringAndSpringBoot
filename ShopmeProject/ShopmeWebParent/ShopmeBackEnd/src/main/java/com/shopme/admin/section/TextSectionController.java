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
public class TextSectionController {
     
	@Autowired
	private SectionService sectionService;
	
	@GetMapping("/sections/new/text")
	public String showForm(Model model) {
		Section section = new Section(true, SectionType.TEXT);
		model.addAttribute("section", section);
		model.addAttribute("pageTitle", "Add Text Section");
		return "sections/text_section_form";
	}
	
	@PostMapping("/sections/save/text")
	public String saveSection(Section section,RedirectAttributes ra) {
		sectionService.saveSection(section);
		ra.addFlashAttribute("messageSuccess", "The section of type Text has been saved successfully.");
		return "redirect:/sections";
	}
	
	@GetMapping("/sections/edit/Text/{id}")
	public String editSection(@PathVariable("id") Integer id,RedirectAttributes ra,Model model) {
		try {
			Section section = sectionService.getSection(id);
			model.addAttribute("section", section);
			model.addAttribute("pageTitle", "Edit Text Section (ID: " + id + ")");
			return "sections/text_section_form";
		} catch (SectionNotFoundException e) {
			// TODO Auto-generated catch block
			ra.addFlashAttribute("messageError", e.getMessage());
			return "redirect:/sections";
		}
	}
	
	
}
