package com.shopme.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.common.exception.UnableAddToCart;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {
	
	public static final int PRODUCT_PER_PAGE = 3;
	public static final int SEARCH_RESULTS_PER_PAGE = 3;
    @Autowired
    private ProductRepository productRepository;
    
    public Page<Product> listByCategory(int pageNum, int categoryId){
    	String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
    	Pageable pageable = PageRequest.of(pageNum-1,PRODUCT_PER_PAGE);
    	
    	
    	return productRepository.listByCategory(categoryId,categoryIdMatch,pageable);
    }
    
    public Product getProduct(String alias) throws ProductNotFoundException {
    	Product product = productRepository.findByAlias(alias);
    	if(product == null) {
    		throw new ProductNotFoundException("Not exist product with alias " + alias);
    	}
    	return product;
    }
    
    public Page<Product> search(String keyWord,int pageNumber){
    	Pageable pageable = PageRequest.of(pageNumber-1,SEARCH_RESULTS_PER_PAGE);
    	return productRepository.search(keyWord, pageable);
    }
    
    public Product getProduct(Integer productId) throws ProductNotFoundException {
    	Product product = productRepository.findById(productId).get();
    	if(product == null) {
    		throw new ProductNotFoundException("Not exist product with product ID " + productId);
    	}
    	return product;
    }
    
    public Page<Product> listByBrand(int pageNum, Integer brandId){
    	Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE);
    	return productRepository.listByBrand(brandId, pageable);
    }
    
    public boolean checkQuantityAvailbel(Product product,int quantityInForm) {
    	int quantityProduct = product.getQuantity();
    	if(quantityProduct >= quantityInForm) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    public void updateQuantityAddToCart(Product product, int quantity) throws UnableAddToCart {
         int newQuantity = product.getQuantity() - quantity;
         if(newQuantity < 0) {
        	 throw new UnableAddToCart("Please add to cart quantity product less than " + product.getQuantity());
         }
         product.setQuantity(newQuantity);
         productRepository.save(product);
    }
    
    public void updateQuantityRemoveFromCart(Product product, Integer quantity) {
    	int newQuantity = product.getQuantity() + quantity;
    	product.setQuantity(newQuantity);
    	productRepository.save(product);
    }
    
    public void updateQuantityInCart(Integer productId,String type) throws ProductNotFoundException, UnableAddToCart {
    	if(!productRepository.existsById(productId)) {
    		throw new ProductNotFoundException("Product with id" + productId   + "not exist");
    	}
    	Integer quantityOfProduct = productRepository.findQuantityByProduct(productId);
    	if(quantityOfProduct == null) {
    		throw new ProductNotFoundException("Product with id" + productId   + "not exist");
    	}
    	if(type.equals("INCREASE")) {
    		if(quantityOfProduct == 0) {
    			throw new UnableAddToCart("Can not increase product quantity because it is out stock");
    		}
    	    int newQuantity = quantityOfProduct - 1;
    	    System.out.println(newQuantity);
    	    if(newQuantity  < 0) {
    	    	throw new UnableAddToCart("We do not have enough number of products you need. Please wait");
    	    }
    	    productRepository.updateQuantityProduct(productId,-1);
    	}
    	else if(type.equals("DECREASE")) {
    		productRepository.updateQuantityProduct(productId,1);
    	}else {
    		throw new UnableAddToCart("Error When Update Shopping Cart");
    	}
    }
}
