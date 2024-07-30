package com.shopme.admin.category;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public List<Category> listRootCategories(Sort sort);
    
    @Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
   	public Page<Category> listRootCategories(Pageable pageable);
    
    @Query("SELECT c FROM Category c WHERE c.name LIKE %?1%")
    public Page<Category> search(String keyWord,Pageable pageable);
    
    public Category findByName(String name);
    
    public Category findByAlias(String alias);
    
    public Long countById(Integer id);
    
    @Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer Id, boolean status);
}
