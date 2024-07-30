package com.shopme.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {
   @Autowired
   private ProductRepository productRepository;
   
   @Test
   public void testFindByAlias() {
	   String alias = "Legion-Series";
	   Product productResult = productRepository.findByAlias(alias);
	   assertThat(productResult).isNotNull();
   }
   
   
   @Test
   public void testUpdateReviewCountAndAverageRating() {
	   Integer productId = 12;
	   productRepository.updateReviewCountAndAverageRating(productId);
	   
   }
}
