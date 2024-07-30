package com.shopme.admin.product;

import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 4;
	
    @Autowired
	private ProductRepository productRepository;
    
    
    public List<Product> listAll(){
    	return (List<Product>)productRepository.findAll();
    } 
    
    public Page<Product> listByPage(int pageNum,String sortField, String sortDir,String keyWord, Integer categoryID){
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum -1, PRODUCTS_PER_PAGE,sort);
		
		if(keyWord != null && !keyWord.isEmpty()) {
			
			if(categoryID != null && categoryID > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryID) + "-";
				return productRepository.searchInCategory(categoryID, categoryIdMatch, keyWord, pageable);
			}
			
			return productRepository.findAll(keyWord,  pageable);
		}
		if(categoryID != null && categoryID > 0) {
			String categoryIdMatch = "-" + String.valueOf(categoryID) + "-";
			return productRepository.findAllInCategory(categoryID, categoryIdMatch, pageable);
		}
		return productRepository.findAll(pageable);
	}
    
    public void searchProduct(int pageNum, PagingAndSortingHelper helper) {
    	Pageable pageable = helper.createPageable(PRODUCTS_PER_PAGE, pageNum);
    	String keyWord = helper.getKeyWord();
    	
    	Page<Product> page = productRepository.searchProductByName(keyWord, pageable);
    	
    	helper.updateModelAttribute(pageNum, page);
    }
    
    public Product saveProduct(Product product) {
    	//first time creatiom
    	if(product.getId() == null) {
    		product.setCreatedTime(new Date());
    	}
    	if(product.getAlias() == null || product.getAlias().isEmpty()) {
    		String defaultAlias = product.getName().replaceAll(" ","-");
    		product.setAlias(defaultAlias);
    	}else {
    		product.setAlias(product.getAlias().replaceAll(" ","-"));
    	}
    	product.setUpdatedTime(new Date());
    	Product updatedProduct = productRepository.save(product);
    	productRepository.updateReviewCountAndAverageRating(product.getId());
    	return updatedProduct;
    }
    
    public void saveProductPrice(Product productInForm) {
    	Product productInDB = productRepository.findById(productInForm.getId()).get();
    	productInDB.setCost(productInForm.getCost());
    	productInDB.setPrice(productInForm.getPrice());
    	productInDB.setDiscountPercent(productInForm.getDiscountPercent());
    	productRepository.save(productInDB);
    }
    
    public String checkUnique(Integer id ,String name) {
    	boolean isCreatingNew = (id == null || id == 0);
    	Product productByName = productRepository.findByName(name);
    	if(isCreatingNew) {
    		if(productByName != null ) return "Dupplicated";
    	}else {
    		if(productByName != null && productByName.getId() != id) {
    			return "Dupplicated";
    		}
    	}
    	return "OK";
    }
    
    public void updateProductEnabledStatus(Integer id, boolean status) {
    	productRepository.updatedEnabledStatus(id, status);
    }
    
    public void deleteProduct(Integer id) throws ProductNotFoundException {
    	Long countByID = productRepository.countById(id);
    	if(countByID == null || countByID == 0) {
    		throw new ProductNotFoundException("Could Not find product with id " + id);
    	}
    	productRepository.deleteById(id);
    }
    public Product get(Integer id) throws ProductNotFoundException {
    	try {
    		return productRepository.findById(id).get();
    	}catch(Exception ex) {
    		throw new ProductNotFoundException("Could not found product with ID " + id);
    	}
    }
    
    
}
