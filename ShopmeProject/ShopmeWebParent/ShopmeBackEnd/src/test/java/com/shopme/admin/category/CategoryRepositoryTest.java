package com.shopme.admin.category;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {
    @Autowired
	private CategoryRepository categoryRepository;
	
    
    @Test
    public void createRootCategory() {
    	Category root = new Category("Electronics");
    	Category saveCategory = categoryRepository.save(root);
    	assertThat(saveCategory.getId()).isGreaterThan(0);
    }
	
    @Test
    public void createSubCategory() {
    	//get Parent Category by ID
    	Category parentCate = new Category(5);
    	//create subCategory 
    	Category laptops = new Category("Memory",parentCate);
    	//Category components = new Category("",parentCate);
        //categoryRepository.saveAll(List.of(laptops,components));
    	categoryRepository.save(laptops);
    }
    
    @Test
    public void getCategoriesAndItsChild() {
    	Category computers = categoryRepository.findById(1).get();
    	System.out.println(computers.getName());
    	Set<Category> children = computers.getChildren();
    	for(Category x : children) {
    		System.out.println(x.getName());
    	}
    	assertThat(children.size()).isGreaterThan(0);
    }
    
    @Test
    public void testPrintHierarchicalCategories() {
    	//find All
    	Iterable<Category> categories = categoryRepository.findAll(); 
    	for(Category rootCategories : categories) {
    		if(rootCategories.getParent() == null) {
    			System.out.println(rootCategories.getName());
    			Set<Category> children = rootCategories.getChildren();
    			for(Category subCatgegory : children) {
    				System.out.println("--" + subCatgegory.getName());
    				printChildren(subCatgegory,1);
    			}
    		}
    	}
    }
    
    private void printChildren(Category parent, int subLevel) {
    	int newSubLevel = subLevel  + 1;
    	Set<Category> children = parent.getChildren();
    	for(Category subCatgegory : children) {
			for(int i =0; i<newSubLevel;i++) {
				System.out.print("--");
			}
    		System.out.println(subCatgegory.getName());
    		printChildren( subCatgegory,newSubLevel);
		}
    }
    
//    @Test
//    public void listRootCategory() {
//    	List<Category> rootCategories = categoryRepository.listRootCategories();
//    	rootCategories.forEach(x -> System.out.println(x.getName()));
//    }
    
    @Test
    public void testFindByName() {
    	String name = "Computers";
    	Category find = categoryRepository.findByName(name);
    	if(find == null) {
    		System.out.println("No exist Category with name  " + name);
    	}
    	else {
    		System.out.println("Has exist");
    	}
    }
    @Test
    public void testFindByAlias() {
    	String alias = "something";
    	Category find = categoryRepository.findByAlias(alias);
    	if(find == null) {
    		System.out.println("No exist Category with name  " + alias);
    	}
    	else {
    		System.out.println("Has exist");
    	}
    }
    
}
