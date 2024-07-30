package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
   @Autowired
   private RoleRepository roleRepository;
   
   @Test
   public void testCreateFirstRole() {
	   Role role = new Role("Admin","manage everything");
	   Role savedRole = roleRepository.save(role);
	   assertThat(savedRole.getId()).isGreaterThan(0);
   }
   
   @Test
   public void testCreateRole() {
	   Role role2 = new Role("Salesperson","manage product price,customers,shipping,orders and sales report");
	   Role roleEditor = new Role("Editor","manage categories,brand,products,article and menus");
	   Role roleshipper = new Role("Shipper","view products,view orders, and update order status");
       Role roleAssistant = new Role("Assistant","manage questions and product reviews");
       roleRepository.saveAll(List.of(role2,roleEditor,roleshipper,roleAssistant));
   }
}
