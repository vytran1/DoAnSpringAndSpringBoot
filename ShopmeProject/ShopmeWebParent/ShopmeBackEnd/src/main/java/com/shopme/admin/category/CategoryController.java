package com.shopme.admin.category;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUltil;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	  @GetMapping("/categories")
      public String listFirstPage( @Param("sortDir") String sortDir ,Model model) {
		  return listByPage(1,sortDir,model,null);
      }
	  
	  @GetMapping("/categories/page/{pageNum}")
	  public String listByPage(@PathVariable("pageNum") int pageNum,
			  @Param("sortDir") String sortDir ,
			  Model model
			  ,@Param("keyWord") String keyWord
			  ) {
		  if(sortDir == null || sortDir.isEmpty()) {
	      		sortDir = "asc";
	      	  }
//		  if(keyWord == null) {
//			  keyWord = "";
//		  }
		  CategoryPageInfo categoryInfo = new CategoryPageInfo();
		  List<Category> list = categoryService.listByPage(categoryInfo,pageNum,sortDir,keyWord);
		  
		  long startCount = (pageNum -1)*CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
			 long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
			 if(endCount > categoryInfo.getTotalElements()) {
				 endCount = categoryInfo.getTotalElements();
			 }
		  
  	      String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
  	      model.addAttribute("totalPages",categoryInfo.getTotalPages());
  	      model.addAttribute("totalItems",categoryInfo.getTotalElements());
  	      model.addAttribute("currentPage",pageNum);
  	      model.addAttribute("sortField","name");
  	      model.addAttribute("sortDir",sortDir);
  	      model.addAttribute("keyWord",keyWord);
  	      model.addAttribute("listCategories",list );
  	      model.addAttribute("reverseSortDir",reverseSortDir);
  	      model.addAttribute("startCount",startCount);
		  model.addAttribute("endCount",endCount);
		  model.addAttribute("moduleURL","/categories");
  	      return "categories/categories";
	  }
	  
	  
	  @GetMapping("/categories/new")
	  public String newCategory(Model model) {
		  Category newCategory = new Category();
		  List<Category> listOfCategoriesUsedInForm = categoryService.listOfCategoriesUsedInForm();
		  model.addAttribute("category", newCategory);
		  model.addAttribute("listCategories",listOfCategoriesUsedInForm);
		  model.addAttribute("pageTitle","Create New Category");
		  return "categories/category_form";
	  }
	  
	  @PostMapping("/categories/save")
	  public String saveCategory(Category category,@RequestParam("fileImage") MultipartFile multipartFile,RedirectAttributes redirectAttributes) throws IOException {
		  if(!multipartFile.isEmpty()) {
			  String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()); 
			  category.setImage(fileName);  
			  Category saveCategory =  categoryService.saveCategory(category);
			  String uploadDir = "../category-image/" + saveCategory.getId();
			  FileUploadUltil.cleanDir(uploadDir);
			  FileUploadUltil.saveFile(uploadDir, fileName, multipartFile);
		  }else {
			  categoryService.saveCategory(category);
		  }
		  redirectAttributes.addFlashAttribute("message","Saving category have been success");
		  return "redirect:/categories";
	  }
	  
	  @GetMapping("/categories/edit/{id}")
	  public String editCategory(@PathVariable("id") Integer Id, Model model, RedirectAttributes ra) throws CategoryNotFoundException {
		  //find by ID
		  try {
			  Category category = categoryService.getCategoryByID(Id);
			  List<Category> list = categoryService.listOfCategoriesUsedInForm();
			  
			  model.addAttribute("category",category);
			  model.addAttribute("listCategories",list);
			  model.addAttribute("pageTitle","Edit Category with ID " + Id);
			  return "categories/category_form";
		  }catch(CategoryNotFoundException e) {
			  ra.addFlashAttribute("message",e.getMessage());
			  return "redirect:/categories";
		  }
		  
	  }
	  
	  @GetMapping("/categories/{id}/enabled/{status}")
	  public String updateCategoryStatus(@PathVariable("id") Integer Id, @PathVariable("status") boolean status, RedirectAttributes ra) {
		  
		  categoryService.updateCategoryEnabledStatus(Id, status);
		  String messageStatus = status ? "enabled" : "disabled"; 
		  String message = "The category with ID" + Id + "Has beean " + messageStatus;
		  ra.addFlashAttribute("message",message);
		  return "redirect:/categories";
	  }
	  
	  @GetMapping("/categories/delete/{id}")
	  public String deleteCategory(@PathVariable("id") Integer id,RedirectAttributes ra) {
		  try {
			  categoryService.deleteCategory(id);
			  String categoryDir = "../category-image/" + id;
			  FileUploadUltil.cleanDir(categoryDir);
			  String message = "Delete successfull category with ID" + id;
			  ra.addFlashAttribute("message",message);
		  }catch(CategoryNotFoundException e) {
			  ra.addFlashAttribute("message",e.getMessage());
		  }
		  return "redirect:/categories";
		  
	  }
	  
	  @GetMapping("/categories/export/csv")
	  public void exportToCSV(HttpServletResponse response) throws IOException {
		  List<Category> listCategories = categoryService.listOfCategoriesUsedInForm();
		  CategoryCSVExporter exporter = new CategoryCSVExporter();
		  exporter.export(listCategories, response);
	  }
}
