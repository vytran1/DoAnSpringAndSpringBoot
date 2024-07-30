package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
   @Autowired
   private UserRepository userRepository;
   
   @Autowired
   private TestEntityManager entityManager;
   
   @Test
   public void testCreateNewUserWithOneRole() {
	   Role roleAdmin =  entityManager.find(Role.class, 1);
	   User vytran = new User("vy.tn171003@gmail.com","123456","Tran Nguyen","Vy");
	   vytran.addRole(roleAdmin);
	   
	   User userSaved = userRepository.save(vytran);
	   assertThat(userSaved.getId()).isGreaterThan(0); 
   }
   @Test
   public void testCreateNewUserWithMultiRole() {
	   User userNgocHao = new User("ngochao180303@gmail.com","123456","Tran Ngoc","Hao");
	   Role roleEditor = new Role(3);
	   Role roleAssistant = new Role(5);
	   
	   userNgocHao.addRole(roleEditor);
	   userNgocHao.addRole(roleAssistant);
	   
	   User userSaved = userRepository.save(userNgocHao);
	   
	   assertThat(userSaved.getId()).isGreaterThan(0);
   }
   
   @Test
   public void testListAllUser() {
	  List<User> users =  userRepository.findAll();
	  users.forEach((x)->System.out.println(x));
//	  assertThat(users.size()).isGreaterThan(0);
   }
   @Test
   public void testGetUserById() {
	   User user = userRepository.findById(1).get();
	   System.out.println(user);
	   assertThat(user).isNotNull();
   }
   
   @Test
   public void testUpdateUserDetails() {
	   User user = userRepository.findById(1).get();
	   user.setEnabled(true);
	   userRepository.save(user);
   }
   @Test
   public void testUpdateUserRole() {
	   User userNgocHao = userRepository.findById(2).get();
	   Role roleEditor = new Role(3);
	   Role roleSalesPerson = new Role(2);
	   
	   
	   userNgocHao.getRoles().remove(roleEditor);
	   userNgocHao.addRole(roleSalesPerson);
	   
	   userRepository.save(userNgocHao);
   }
   
   @Test
   public void testGetUserByEmail() {
	   String email = "vy.tn171003@gmail.com";
	   User existUser = userRepository.getUserByEmail(email);
	   assertThat(existUser).isNotNull();
   }
   
   @Test
   public void testCountByID() {
	   Integer id = 1;
	   Long count = userRepository.countById(id);
	   assertThat(count).isNotNull().isGreaterThan(0);
   }
   @Test
   public void testDisabledUser() {
	   Integer id = 4;
	   userRepository.updateEnabledStatus(id, false);	   
   }
   @Test
   public void testEnabledUser() {
	   Integer id = 4;
	   userRepository.updateEnabledStatus(id, true);
   }
   
   @Test
   public void testListFirstPage() {
	   int pageNumber = 0;
	   int pageSize = 4;
	   org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNumber, pageSize);
	   Page<User> listPage = userRepository.findAll(pageable);
	   List<User> users = listPage.getContent();
	   System.out.println(listPage.getTotalPages());
	   System.out.println(listPage.getNumberOfElements());
   }
   
   @Test
   public void testSearchUserByKeyWord() {
	   String keyword = "vy";
	   int pageNumber = 0;
	   int pageSize = 4;
	   org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNumber, pageSize);
	   Page<User> listPage = userRepository.findAll(keyword,pageable);
	   List<User> users = listPage.getContent();
	   System.out.println(listPage.getTotalPages());
	   System.out.println(listPage.getNumberOfElements());
	   users.forEach((x)->System.out.println(x));
//	   assertThat(listPage.getSize()).isGreaterThan(0);
   }
}
