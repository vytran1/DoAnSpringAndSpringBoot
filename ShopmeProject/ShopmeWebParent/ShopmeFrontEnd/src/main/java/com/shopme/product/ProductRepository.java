package com.shopme.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.product.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p WHERE p.enabled = true " + "AND(p.category.id = ?1 OR p.category.allParentIDs LIKE %?2% )"
            + "ORDER BY p.name ASC"
    		)
	public Page<Product> listByCategory(Integer categoryID,String categoryIDMatch,Pageable pageable);
    
    public Product findByAlias(String alias);
    @Query(value = "SELECT * FROM products p WHERE p.enabled = true AND  MATCH(name,short_description,full_description) AGAINST (?1)",
    		nativeQuery = true)
    public Page<Product> search(String keyWord,Pageable pageable);
    
    @Query("Update Product p SET p.averageRating = COALESCE((SELECT avg(r.rating) FROM Review r WHERE r.product.id = ?1),0), " +
           " p.reviewCount = (SELECT  COUNT(r.id) FROM Review r WHERE r.product.id = ?1) "+
    		"WHERE p.id = ?1"
    		)
    @Modifying
    public void updateReviewCountAndAverageRating(Integer productId);
    
    @Query("SELECT p FROM Product p WHERE p.enabled=true AND p.brand.id=?1")
	public Page<Product> listByBrand(Integer brandId, Pageable pageable);
    
    @Query("UPDATE Product p SET p.quantity = p.quantity + ?2 WHERE p.id = ?1")
    @Modifying
    public void updateQuantityProduct(Integer productId, Integer quantity);
    
    @Query("SELECT p.quantity FROM Product p WHERE p.id = ?1")
    public Integer findQuantityByProduct(Integer productId);
}
