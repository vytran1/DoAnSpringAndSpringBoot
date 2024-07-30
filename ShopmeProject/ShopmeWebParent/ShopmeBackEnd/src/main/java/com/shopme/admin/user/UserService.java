package com.shopme.admin.user;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
	
	 public static final int USER_PER_PAGE =4;
	 @Autowired
     private UserRepository useRepository;
	 
	 @Autowired
	 private PasswordEncoder passwordEncoder;
	 
	 @Autowired
	 private RoleRepository roleRepository;
	 public List<User> listAll(){
		 return (List<User>)useRepository.findAll();
	 }
	 
	 public List<Role> listRoles(){
		 return (List<Role>)roleRepository.findAll();
	 }
	 
	 public User getByEmail(String email) {
		 return useRepository.getUserByEmail(email);
	 }
	 public void listByPage(int pageNumber, PagingAndSortingHelper helper){
		 helper.listEntities(pageNumber, USER_PER_PAGE, useRepository);
	 }
	 public User saveUser(User user) {
		 boolean isUpdatingUser = (user.getId()!= null);
		 if(isUpdatingUser) {
			 User existingUser = useRepository.findById(user.getId()).get();
			 if(user.getPassword().isEmpty()) {
				 user.setPassword(existingUser.getPassword());
			 }else {
				 encodePassword(user);
			 }
		 }else {
			 encodePassword(user);
		 }
		 
		 return useRepository.save(user);
	 }
	 //only password, photos, name
	 public User updateAccount(User userInForm) {
		User userInDB = useRepository.findById(userInForm.getId()).get();
		if(!userInForm.getPassword().isEmpty()) {
			userInDB.setPassword(userInForm.getPassword());
			encodePassword(userInDB);
		}
		if(userInForm.getPhotos() != null) {
			userInDB.setPhotos(userInForm.getPhotos());
		} 
		userInDB.setFirstName(userInForm.getFirstName());
		userInDB.setLastName(userInForm.getLastName());
		return useRepository.save(userInDB);
	 }
	 private void encodePassword(User user) {
		 String encodePassword = passwordEncoder.encode(user.getPassword());
		 user.setPassword(encodePassword);
	 }
	 
	 public boolean emailIsUnique(Integer Id, String email) {
//		 User findUserByEmail = useRepository.getUserByEmail(email);
//		 if(findUserByEmail == null) {
//			 return true;
//		 }
		 User userByEmail = useRepository.getUserByEmail(email);
			
			if (userByEmail == null) return true;
			
			boolean isCreatingNew = (Id == null);
			
			if (isCreatingNew) {
				if (userByEmail != null) return false;
			} else {
				if (!userByEmail.getId().equals(Id)) {
					return false;
				}
			}
			
			return true;
//		 if(findUserByEmail == null) return true;
//		 if(findUserByEmail.getId() == Id) return true;
//		 return false;
	 }

	public User get(Integer id) throws UserNotFoundException {
		// TODO Auto-generated method stub
		//SELECT u FROM User u WHERE u.id = id
		try {
			return useRepository.findById(id).get();
		}catch(Exception ex){
			throw new UserNotFoundException("Could not find User with id" + id);
		}
	}
	
	public void deleteUserById(Integer Id) throws UserNotFoundException {
		//Is Exist Account
		//SELECT u FROM User u WHERE id = id;
		//SELECT COUNT(*) FROM User WHERE id = id;
		Long count = useRepository.countById(Id);
		if(count == null | count == 0) {
			throw new UserNotFoundException("Could not find User with id" + Id);
		}
		useRepository.deleteById(Id);
	}
	
	public void updateUserEnabledStatus(Integer Id, boolean status) {
		useRepository.updateEnabledStatus(Id, status);
	}
}
