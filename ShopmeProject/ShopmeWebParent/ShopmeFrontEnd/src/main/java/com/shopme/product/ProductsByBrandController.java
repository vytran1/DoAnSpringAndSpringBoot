package com.shopme.product;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.brand.BrandRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.product.Product;

@Controller
public class ProductsByBrandController {
    @Autowired
    private ProductService productService;
    
    @Autowired
    private BrandRepository brandRepository;
    @GetMapping("/brand/{brand_id}")
    public String listProductsByBrand(@PathVariable("brand_id") Integer brandId, Model model) {
    	return listProductByBrandByPage(brandId,1, model);
    }
    
    @GetMapping("/brand/{brand_id}/page/{pageNum}")
    public String listProductByBrandByPage(@PathVariable("brand_id") Integer brand_id, @PathVariable("pageNum") int pageNum,Model model) {
    	Optional<Brand> brandById = brandRepository.findById(brand_id);
    	if(!brandById.isPresent()) {
    		return "error/404"; 
    	}
    	Brand brand = brandById.get();
    	Page<Product> page = productService.listByBrand(pageNum,brand.getId());
    	List<Product> products = page.getContent();
    	
    	model.addAttribute("sortField", "name");
		model.addAttribute("sortDir", "asc");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		
		long startCount = (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
		model.addAttribute("startCount", startCount);
		long endCount = startCount + ProductService.PRODUCT_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		model.addAttribute("endCount", endCount);
		model.addAttribute("brand", brand);
		model.addAttribute("products", products);
		model.addAttribute("pageTitle", "Products by " + brand.getName());
		return "products/product_by_brand";
    }
}
