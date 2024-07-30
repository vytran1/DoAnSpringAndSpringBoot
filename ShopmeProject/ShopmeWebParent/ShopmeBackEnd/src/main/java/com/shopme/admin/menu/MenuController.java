package com.shopme.admin.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.MenuMoveDirection;
import com.shopme.admin.article.ArticleService;
import com.shopme.common.entity.Article;
import com.shopme.common.entity.Menu;
import com.shopme.common.exception.MenuNotFoundException;
import com.shopme.common.exception.MenuUnmoveAbleException;

@Controller
public class MenuController {
	private final String defaultRedirectURL = "redirect:/menus";
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private ArticleService articleService;
	
	
	@GetMapping("/menus")
	public String listAll(Model model) {
		List<Menu> listMenuItems = menuService.findAll();
		model.addAttribute("listMenuItems",listMenuItems);
		return "menus/menus_items";
	}
	
	
	@GetMapping("/menus/new")
	public String newMenu(Model model) {
		List<Article> listArticle = articleService.listArticlesForMenu();
		model.addAttribute("menu",new Menu());
		model.addAttribute("listArticles",listArticle);
		model.addAttribute("pageTitle","Create new Menu");
		return "menus/menu_form";
	}
	
	
	@PostMapping("/menus/save")
	public String saveMenu(Menu menu, RedirectAttributes ra) {
		menuService.save(menu);
		ra.addFlashAttribute("message","The menu item has been saved successfully.");
		return defaultRedirectURL;
	}
	
	
	@GetMapping("/menus/edit/{id}")
	public String editMenu(@PathVariable("id") Integer id,Model model, RedirectAttributes ra) {
		try {
			Menu menu = menuService.get(id);
			List<Article> listArticle = articleService.listArticlesForMenu();
			
			model.addAttribute("menu",menu);
			model.addAttribute("listArticles",listArticle);
			model.addAttribute("pageTitle", "Edit Menu Item (ID: " + id + ")");
			
			return "menus/menu_form";
		} catch (MenuNotFoundException e) {
			// TODO Auto-generated catch block
			ra.addFlashAttribute("message",e.getMessage());
			return defaultRedirectURL;
		}
	}
	
	@GetMapping("/menus/{id}/enabled/{enabledStatus}")
	public String updateEnabledStatus(@PathVariable("id") Integer id, @PathVariable("enabledStatus") String enabledStatus, RedirectAttributes ra ) {
		try {
			boolean status = Boolean.parseBoolean(enabledStatus);
			menuService.updateEnabledStatus(id, status);
			String updateResult = status ? "enabled." : "disabled.";
			ra.addFlashAttribute("messageSuccess", "The menu item ID " + id + " has been " + updateResult);
			
		}catch(MenuNotFoundException e) {
			ra.addFlashAttribute("message",e.getMessage());
		}
		
		return defaultRedirectURL;
	}
	
	@GetMapping("/menus/delete/{id}")
	public String delete(@PathVariable("id") Integer id,RedirectAttributes ra) {
		try {
			menuService.delete(id);
			ra.addFlashAttribute("message","The menu item ID " + id + " has been deleted.");
		} catch (MenuNotFoundException e) {
			// TODO Auto-generated catch block
			ra.addFlashAttribute("message", e.getMessage());
		}
		return defaultRedirectURL;
	}
	
	@GetMapping("/menus/{direction}/{id}")
	public String moveMenu(@PathVariable("direction")String direction, @PathVariable("id") Integer id, RedirectAttributes ra) {
		try {
			MenuMoveDirection moveDirection = MenuMoveDirection.valueOf(direction.toUpperCase());
			menuService.moveMenu(id, moveDirection);
			ra.addFlashAttribute("message","The menu item ID " + id + " has been moved up by one position.");
		} catch (MenuNotFoundException | MenuUnmoveAbleException e) {
			// TODO Auto-generated catch block
			ra.addFlashAttribute("message", e.getMessage());
		}
		return defaultRedirectURL;
		
	}
}
