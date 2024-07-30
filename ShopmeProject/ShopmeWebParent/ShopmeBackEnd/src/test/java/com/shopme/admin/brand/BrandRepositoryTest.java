package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {
    @Autowired
	private BrandRepository brandRepository;
    
    
    @Test
    public void testRong() {
    	
    }
    
    
    @Test
    public void createNewBrand() {
    	String name = "Test";
    	String logo = "Default.png";
    	Set<Category> categories = new HashSet<>();
    	//Laptops
    	Category laptops = new Category(1);
    	//Desktop
    	Category desktop = new Category(3);
    	//Computer Components
    	Category component = new Category(5);
    	Brand brand = new Brand();
    	brand.setName(name);
    	brand.setLogo(logo);
    	categories.add(desktop);
//    	categories.add(laptops);
    	brand.setCategories(categories);
    	Brand savedBrand = brandRepository.save(brand);
    	assertThat(savedBrand.getId()).isGreaterThan(0);
    }
    
    @Test
    public void findAllBrand() {
    	Iterable<Brand> brands = brandRepository.findAll();
    	brands.forEach((x)->System.out.println(x.getName()));
    }
    
    @Test
    public void updateBrand() {
    	Integer brandId = 2;
    	Brand acer = brandRepository.findById(brandId).get();
    	acer.setName("Acer Electronics");
    	brandRepository.save(acer);
    }
    
    @Test
    public void deleteBrand() {
    	Integer brandId = 3;
    	Brand test = brandRepository.findById(brandId).get();
    	brandRepository.delete(test);    	
    }
}
