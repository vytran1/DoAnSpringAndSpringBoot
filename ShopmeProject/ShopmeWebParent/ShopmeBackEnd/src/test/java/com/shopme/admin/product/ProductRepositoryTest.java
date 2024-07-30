package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {
    @Autowired
	private ProductRepository productRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    
    
    @Test
    public void createProduct() {
    	//Lenovo
    	//Apple
    	//Gigabyte
    	Brand brands = entityManager.find(Brand.class, 8);
    	
    	//Electronics
    	Category categories = entityManager.find(Category.class, 2);
    	Product products = new Product();
    	products.setName("GigaByte Pro 2023");
    	products.setAlias("gigabyte_2022");
    	products.setShortDescription("PowerFull Laptop for Studying and Listening");
    	products.setFullDescription("Wonderful with many new features");
    	
    	products.setBrand(brands);
    	products.setCategory(categories);
    	
    	products.setPrice(2400);
    	products.setCost(1400);
    	products.setEnabled(true);
    	products.setInStock(true);
    	
    	products.setCreatedTime(new Date());
        products.setUpdatedTime(new Date());    	
    	
        Product savedProduct = productRepository.save(products);
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }
    
    @Test
    public void listAllProduct() {
    	Iterable<Product> products = productRepository.findAll();
    	
    	products.forEach((x)-> System.out.println(x));
    }
    
    @Test
    public void getProductById() {
    	Integer id = 2;
    	Product product = productRepository.findById(id).get();
    	System.out.println(product.getName());
    	assertThat(product).isNotNull();
    }
    
    @Test
    public void updatePridceProduct() {
    	Integer id = 2;
    	Product product = productRepository.findById(id).get();
    	product.setPrice(2400);
    	Product afterProduct = productRepository.save(product);
    	assertThat(afterProduct.getPrice()).isEqualTo(2400);
    }
    
    @Test
    public void deleteProductById() {
    	Integer id = 3;
    	productRepository.deleteById(id);
    	
    	Optional<Product> productDelete = productRepository.findById(id);
    	assertThat(productDelete).isNotPresent();
    }
    
    @Test void saveProductWithImage() {
    	Integer productid = 1;
    	Product productById = productRepository.findById(productid).get();
    	productById.setMainImage("legion5.jpg");
    	productById.addExtraImage("extra images1.png");
    	productById.addExtraImage("extra images2.png");
    	productById.addExtraImage("extra images3.png");
        
    	
    	Product savedProduct = productRepository.save(productById);
    	assertThat(savedProduct.getImages().size()).isEqualTo(3);
    }
    
    @Test
    public void testAddDetail() {
    	Integer productId = 1;
    	Product productById = productRepository.findById(productId).get();
    	//details1
    	String name = "SSD";
    	String value = "512GB";
    	//details2
    	String name1 = "RAM";
    	String value1 = "8GB";
    	//add details
    	productById.addDetail(name,value);
    	productById.addDetail(name1, value1);
    	//save 
    	productRepository.save(productById);
    	
    	
    }
}
