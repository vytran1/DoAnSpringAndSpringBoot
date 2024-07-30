package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Controller
public class BrandController {
    @Autowired
	private BrandService brandService;
    
    @Autowired
    private CategoryService categoryService;
    
   @GetMapping("/brands")
   public String listAll() {
//	   List<Brand> brands = brandService.listAll();
//	   model.addAttribute("listBrands",brands);
//	   return "brands/brands";
	   
	   return "redirect:/brands/page/1?sortField=name&sortDir=asc";
   }
   
   @GetMapping("/brands/page/{pageNum}")
   public String listByPage(@PagingAndSortingParam(listName = "listBrands",moduleURL = "/brands") PagingAndSortingHelper helper,@PathVariable("pageNum") int pageNum, Model model) {
	    
//	   Page<Brand> pageBrand = brandService.listByPage(pageNum, sortField, sortDir, keyWord);
//	   List<Brand> brands = pageBrand.getContent();
//	   long startCount = (pageNum-1) * BrandService.BRANDS_PER_PAGE +1 ;
//	   long endCount = startCount + BrandService.BRANDS_PER_PAGE -1 ;
//	   if(endCount > pageBrand.getTotalElements() ) {
//		   endCount = pageBrand.getTotalElements();
//	   }
//	   String reverseSorDir = sortDir.equals("asc") ? "desc" : "asc";
//	   model.addAttribute("totalPages",pageBrand.getTotalPages());
//	   model.addAttribute("totalItems",pageBrand.getTotalElements());
//	   model.addAttribute("currentPage",pageNum);
//	   model.addAttribute("sortField","name");
//	   model.addAttribute("sortDir",sortDir);
//	   model.addAttribute("keyWord",keyWord);
//	   model.addAttribute("listBrands",brands );
//	   model.addAttribute("reverseSortDir",reverseSorDir);
//	   model.addAttribute("startCount",startCount);
//	   model.addAttribute("endCount",endCount);
//	   model.addAttribute("moduleURL","/brands");
	   
	   brandService.listByPage(pageNum,helper);
	   
	   return "brands/brands";
   }
   
   
   
   @GetMapping("/brands/new")
   public String newBrand(Model model) {
	   Brand newBrandObject = new Brand();
	   List<Category> categories = categoryService.listOfCategoriesUsedInForm();
	   model.addAttribute("brand",newBrandObject);
	   model.addAttribute("listCategories",categories);
	   model.addAttribute("pageTitle","Create new Brand");
	   return "brands/brand_form";
   }
   
   @PostMapping("/brands/save")
   public String saveBrand(Brand brand,
		   @RequestParam("fileImage") MultipartFile multipartFile,
		   RedirectAttributes ra) throws IOException {
	   if(!multipartFile.isEmpty()) {
		   String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		   brand.setLogo(fileName);
		   Brand saveBrand =  brandService.saveBrand(brand);
		   String uploadDir = "../brand-logos/" + saveBrand.getId();
		   FileUploadUltil.cleanDir(uploadDir);
		   FileUploadUltil.saveFile(uploadDir, fileName, multipartFile);
	   }else {
		   brandService.saveBrand(brand);
	   }
	   ra.addFlashAttribute("message","The brand has been saved successfully");
	   return "redirect:/brands";
   }
   
   @GetMapping("/brands/edit/{id}")
   public String editBrand(@PathVariable("id") Integer id,
		   Model model,
		   RedirectAttributes ra) {
	   try {
		   Brand brandEdit = brandService.get(id);
		   List<Category> categories = categoryService.listOfCategoriesUsedInForm();
		   model.addAttribute("brand",brandEdit);
		   model.addAttribute("listCategories",categories);
		   model.addAttribute("pageTitle","Edit Brand with ID: " + id);
		   return "brands/brand_form";
	   }catch(BrandNotFoundException e) {
		   ra.addFlashAttribute("message",e.getMessage());
		   return "redirect:/brands";
	   }
   }
   
   @GetMapping("/brands/delete/{id}")
   public String deleteBrand(@PathVariable("id") Integer id, RedirectAttributes ra) {
	   try {
		   brandService.delete(id);
		   String brandDir = "../brand-logos/" + id;
		   FileUploadUltil.removeDir(brandDir);
		   
		   ra.addFlashAttribute("message","The brand with ID: " + id +"has been deleted successfully");
	   }catch(BrandNotFoundException e) {
		   ra.addFlashAttribute("message",e.getMessage());
	   }
	   return "redirect:/brands";
   }
}
