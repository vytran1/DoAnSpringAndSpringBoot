package com.shopme.admin.product;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;
import com.shopme.common.exception.ProductNotFoundException;

@Controller
public class ProductController {
	
    @Autowired
	private ProductService productService;
    
    @Autowired
    private BrandService brandService;
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/products")
    public String listFirstPage(Model model) {
    	return listByPage(1, model, "name", "asc", null,0);
    }
    
    
    @GetMapping("/products/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") int pageNum, Model model, @Param("sortField") String sortField,
 		   @Param("sortDir") String sortDir, @Param("keyWord") String keyWord, @Param("categoryID") Integer categoryID) {
 	   
       System.out.println("Selected "+categoryID);	
 	   Page<Product> pageProduct = productService.listByPage(pageNum, sortField, sortDir, keyWord,categoryID);
 	   List<Product> listProducts = pageProduct.getContent();
 	   List<Category> listCategories = categoryService.listOfCategoriesUsedInForm();
 	   
 	   long startCount = (pageNum-1) * productService.PRODUCTS_PER_PAGE +1 ;
 	   long endCount = startCount + productService.PRODUCTS_PER_PAGE -1 ;
 	   if(endCount > pageProduct.getTotalElements() ) {
 		   endCount = pageProduct.getTotalElements();
 	   }
 	   if(categoryID != null) model.addAttribute("categoryID",categoryID);
 	   String reverseSorDir = sortDir.equals("asc") ? "desc" : "asc";
 	   model.addAttribute("totalPages",pageProduct.getTotalPages());
 	   model.addAttribute("totalItems",pageProduct.getTotalElements());
 	   model.addAttribute("currentPage",pageNum);
 	   model.addAttribute("sortField",sortField);
 	   model.addAttribute("sortDir",sortDir);
 	   model.addAttribute("keyWord",keyWord);
 	   model.addAttribute("listProducts",listProducts );
 	   model.addAttribute("reverseSortDir",reverseSorDir);
 	   model.addAttribute("startCount",startCount);
 	   model.addAttribute("endCount",endCount);
 	   model.addAttribute("listCategories",listCategories);
 	   model.addAttribute("moduleURL","/products");
 	  return "products/products";
    }
    
    
    
    
    
    @GetMapping("/products/new")
    public String newProduct(Model model) {
    	
    	List<Brand> listBrands = brandService.listAll();
    	Product product = new Product();
    	product.setEnabled(true);
    	product.setInStock(true);
    	
    	model.addAttribute("product",product);
    	model.addAttribute("listBrands",listBrands);
    	model.addAttribute("pageTitle","Create New Product");
    	model.addAttribute("numberOfExistingImage", 0);
    	return "products/product_form";
    }
    
    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes ra,
    		@RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
    		@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
    		@RequestParam(name = "detailIDs",required = false)String[] detailIDs,
    		@RequestParam(name = "detailNames",required = false)String[] detailNames,
    		@RequestParam(name = "detailValues",required = false) String [] detailValues, 
            @RequestParam(name="imageIDs",required = false) String[] imageIDs,
            @RequestParam(name="imageNames",required = false) String[] imageNames,
            @AuthenticationPrincipal ShopmeUserDetails loggerUser
            )
    				throws IOException {
    	   if(!loggerUser.hasRole("Admin") && !loggerUser.hasRole("Editor") ) {
    		    if(loggerUser.hasRole("Salesperson")) {
    			  productService.saveProductPrice(product);
    			  ra.addFlashAttribute("message","The product have been save successfully");
    			  return "redirect:/products";
    		    }
    	   }
    	   ProductSaveHelper.setMainImageName(mainImageMultipart,product);
    	   ProductSaveHelper.setExistingExtraImageName(imageIDs,imageNames,product);
    	   ProductSaveHelper.setNewExtraImageName(extraImageMultiparts,product);
    	   ProductSaveHelper.setProductDetail(detailIDs,detailNames,detailValues,product);
  		   Product saveProduct =  productService.saveProduct(product);
  		   ProductSaveHelper.savedUploadImages(mainImageMultipart,extraImageMultiparts,saveProduct);
  		   ProductSaveHelper.deletExtraImageWasRemovedFromForm(product);
  	    
    	   ra.addFlashAttribute("message","The product have been save successfully");
    	   return "redirect:/products";
    }
    
//    private void deletExtraImageWasRemovedFromForm(Product product) {
//		// TODO Auto-generated method stub
//		String extraImageDir = "../products-images/" + product.getId() + "/extras";
//		Path dirPath = Paths.get(extraImageDir);
//		try {
//			Files.list(dirPath).forEach(file -> {
//				String fileName = file.toFile().getName();
//				if(!product.containsImageName(fileName)) {
//					try {
//						Files.delete(file);
//						LOGGER.info("Deleted extra image: " + fileName);
//					}catch(IOException ex) {
//						LOGGER.error("Could not delete extra image " + fileName);
//					}
//				}
//			});
//		}catch(IOException ex) {
//			LOGGER.error("Could not list directory " + dirPath);
//		}
//	}

//	private void setExistingExtraImageName(String[] imageIDs, String[] imageNames, Product product) {
//		// TODO Auto-generated method stub
//		if(imageIDs == null || imageIDs.length == 0) return;
//		Set<ProductImage> images = new HashSet<>(); 
//		for(int count =0; count < imageIDs.length;count ++) {
//			Integer id = Integer.parseInt(imageIDs[count]);
//			String name = imageNames[count];
//			images.add(new ProductImage(id,name,product));
//		}
//		product.setImages(images);
//	}

//	private void setProductDetail(String[] detailIDs,String[] detailNames, String[] detailValues, Product product) {
//		// TODO Auto-generated method stub
//		if(detailNames == null || detailNames.length == 0) {
//			return;
//		}else {
//			for(int count =0; count < detailNames.length; count ++) {
//				String name = detailNames[count];
//				String value = detailValues[count];
//				Integer id = Integer.parseInt(detailIDs[count]);
//				if(id != 0) {
//					product.addDetail(id,name,value);
//				}else if(!name.isEmpty() && !value.isEmpty()) {
//					product.addDetail(name, value);
//				}
//			}
//		}
//	}

//	private void savedUploadImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
//			Product saveProduct) throws IOException {
//		// TODO Auto-generated method stub
//    	   if(!mainImageMultipart.isEmpty()) {
// 		     String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
// 		     String uploadDir = "../products-images/" + saveProduct.getId();
// 		     FileUploadUltil.cleanDir(uploadDir);
//		     FileUploadUltil.saveFile(uploadDir, fileName, mainImageMultipart);
//    	   }
//    	   if(extraImageMultiparts.length > 0) {
//    		String uploadDir = "../products-images/" + saveProduct.getId() +"/extras";   
//   			for(MultipartFile multipart : extraImageMultiparts) {
//   				if(multipart.isEmpty()) continue; 
//   			    String fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
//   			    FileUploadUltil.saveFile(uploadDir, fileName, multipart);
//   			}
//    	 }
//	}

//	private void setNewExtraImageName(MultipartFile[] extraImageMultiparts, Product product) {
//		// TODO Auto-generated method stub
//		if(extraImageMultiparts.length > 0) {
//			for(MultipartFile multipart : extraImageMultiparts) {
//				if(!multipart.isEmpty()) {
//					 String fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
//					 if(!product.containsImageName(fileName)) {
//						 product.addExtraImage(fileName);
//					 }
//				}
//			}
//		}
//	}

//	private void setMainImageName(MultipartFile mainImageMultipart, Product product) {
//		// TODO Auto-generated method stub
//    	 if(!mainImageMultipart.isEmpty()) {
//    		   String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
//    		   product.setMainImage(fileName);
//    	 }
//	}

	@GetMapping("/products/{id}/enabled/{status}")
    public String updateEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean status, RedirectAttributes ra) {
    	productService.updateProductEnabledStatus(id, status);
    	String statusMessage = status ? "enabled" : "disabled";
    	String message = "The product with id " + id + "has beean" + statusMessage;
    	ra.addFlashAttribute("message",message);
    	return "redirect:/products";
    }
    
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
    	try {
    		productService.deleteProduct(id);
    		String productExtraImagesDir = "../products-images/" + id +"/extras";
    		String productMainImagesDir = "../products-images/" + id;
    		
    		FileUploadUltil.removeDir(productExtraImagesDir);
    		FileUploadUltil.removeDir(productMainImagesDir);
    		
    		ra.addFlashAttribute("message","The Product with ID " + id + "has been delete successfully");
    	}catch(ProductNotFoundException e) {
    		ra.addFlashAttribute("message",e.getMessage());
    	}
    	return "redirect:/products";
    }
    
    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model,RedirectAttributes ra,@AuthenticationPrincipal ShopmeUserDetails loggerUser ) {
    	try {
    		Product productById = productService.get(id);
    		List<Brand> listBrands = brandService.listAll();
    		boolean isReadOnlyForSalesPerson = false;
    	    if(!loggerUser.hasRole("Admin") && !loggerUser.hasRole("Editor") ) {
      		    if(loggerUser.hasRole("Salesperson")) {
      		    	isReadOnlyForSalesPerson = true;
      		    }
      	    }
    		model.addAttribute("product",productById);
    		model.addAttribute("isReadOnlyForSalesPerson",isReadOnlyForSalesPerson);
    		model.addAttribute("listBrands",listBrands);
    		model.addAttribute("pageTitle","Edit Product with ID " + id);
    		Integer numberOfExistingImage = productById.getImages().size();
    		model.addAttribute("numberOfExistingImage",numberOfExistingImage);
    		return "products/product_form";
    	}catch(ProductNotFoundException ex) {
    		ra.addFlashAttribute("message",ex.getMessage());
    		return "redirect:/products";
    	}
    }
    
    @GetMapping("/products/detail/{id}")
    public String viewProductDetail(@PathVariable("id") Integer id, Model model,RedirectAttributes ra ) {
    	try {
    		Product productById = productService.get(id);
    		
    		model.addAttribute("product",productById);
    		
    		
    		
    		return "products/product_detail_modal";
    	}catch(ProductNotFoundException ex) {
    		ra.addFlashAttribute("message",ex.getMessage());
    		return "redirect:/products";
    	}
    }
}
