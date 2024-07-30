package com.shopme.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.product.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
   
	
	//check unique
	public Product findByName(String name);
	
	//enabled status
	@Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
	@Modifying
	public void updatedEnabledStatus(Integer id, boolean enabled);
	
	//delete function check whether this product has exist
	public Long countById(Integer id);
	//search by keyWord
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%" + "OR p.shortDescription LIKE %?1%" + 
	        "OR p.fullDescription LIKE %?1%" +
			"OR p.brand.name LIKE %?1%"+
	        "OR p.category.name LIKE %?1%")
	public Page<Product> findAll(String keyword,Pageable pageable);
	
	//search by Category
	@Query("SELECT p FROM Product p WHERE p.category.id = ?1 " + "OR p.category.allParentIDs LIKE %?2%")
	public Page<Product> findAllInCategory(Integer categoryId, String categoryMatch, Pageable pageable);
	
	//search by category associate with keyWord
	@Query("SELECT p FROM Product p WHERE (p.category.id = ?1 " 
	         + "OR p.category.allParentIDs LIKE %?2%) AND" 
	         + "(p.name LIKE %?3%" 
			 + "OR p.shortDescription LIKE %?3%" 
	         + "OR p.fullDescription LIKE %?3%"
			 + "OR p.brand.name LIKE %?3%"
	         + "OR p.category.name LIKE %?3%)")
	public Page<Product> searchInCategory(Integer categoryId, String categoryMatch,String keyWord, Pageable pageable);
	
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
	public Page<Product> searchProductByName(String keyword,Pageable page);
	
	 @Query("Update Product p SET p.averageRating = COALESCE((SELECT avg(r.rating) FROM Review r WHERE r.product.id = ?1),0), " +
	           " p.reviewCount = (SELECT  COUNT(r.id) FROM Review r WHERE r.product.id = ?1) "+
	    		"WHERE p.id = ?1"
	       )
	 @Modifying
	 public void updateReviewCountAndAverageRating(Integer productId);
}
