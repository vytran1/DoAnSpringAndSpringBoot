package com.shopme.admin.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

@RestController
public class ProductRestController {
    @Autowired
	private ProductService productService;
    
    @PostMapping("/products/check_unique")
    public String checkUnique(@Param("id")Integer id,@Param("name") String name) {
    	return productService.checkUnique(id, name);
    }
    
    @GetMapping("/products/get/{id}")
    public ProductDTO getProductInfo(@PathVariable("id") Integer id) throws ProductNotFoundException {
    	Product product = productService.get(id);
    	return new ProductDTO(product.getName(),product.getMainImagePath(),product.getDiscountPrice(),product.getCost());
    }
}
