package com.shopme.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@RestController
public class BrandRestController {
    
	@Autowired
	private BrandService brandService;
	
	@PostMapping("/brands/check_unique")
	public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
		return brandService.checkUnique(id, name);
	}
	
	@GetMapping("/brands/{id}/categories")
	public List<CategoryDTO> listCategoriesByBrand(@PathVariable("id") Integer id) throws BrandNotFoundRestException{
		List<CategoryDTO> categoriesDTO = new ArrayList<>();
		try {
			Brand brand = brandService.get(id);
		    Set<Category> categories =	brand.getCategories();
		    for(Category x : categories) {
		    	CategoryDTO categoryDTO = new  CategoryDTO(x.getId(),x.getName());
		    	categoriesDTO.add(categoryDTO);
		    }
		    return categoriesDTO;
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
		}
	}
}
