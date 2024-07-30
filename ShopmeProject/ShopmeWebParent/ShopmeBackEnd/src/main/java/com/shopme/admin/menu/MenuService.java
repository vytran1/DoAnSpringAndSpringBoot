package com.shopme.admin.menu;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.MenuMoveDirection;
import com.shopme.admin.MenuUtil;
import com.shopme.common.entity.Menu;
import com.shopme.common.exception.MenuNotFoundException;
import com.shopme.common.exception.MenuUnmoveAbleException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MenuService {
    
	@Autowired
	private MenuRepository menuRepository;
	
	private static final int MENU_PER_PAGE = 5;
	
	public List<Menu> findAll(){
		
		return menuRepository.findAllByOrderByTypeAscPositionAsc();
	}
	
	
	public void save(Menu menu) {
		MenuUtil.setDefaultAlias(menu);
		if(menu.getId() == null) {
			MenuUtil.setPositionForNewMenu(menuRepository,menu);
		}else {
			MenuUtil.setPositionForEditedMenu(menuRepository,menu);
		}
		
		menuRepository.save(menu);
	}
	
	
	
	public Menu get(Integer id) throws MenuNotFoundException {
		try {
			return menuRepository.findById(id).get();
		}catch(NoSuchElementException e) {
			throw new MenuNotFoundException("Could not find menu with id: " + id);
		}
	}
	
	public void updateEnabledStatus(Integer id,boolean status) throws MenuNotFoundException {
		if(!menuRepository.existsById(id)) {
			throw new MenuNotFoundException("Could not find menu with id: " + id);
		}
		menuRepository.updateEnabledStatus(id, status);
	}
	
	public void delete(Integer id) throws MenuNotFoundException {
		if(!menuRepository.existsById(id)) {
			throw new MenuNotFoundException("Could not find menu with id: " + id);
		}
		menuRepository.deleteById(id);
	}
	
	public void moveMenu(Integer id, MenuMoveDirection direction) throws MenuNotFoundException, MenuUnmoveAbleException {
		if(direction.equals(MenuMoveDirection.UP)) {
			MenuUtil.moveUp(menuRepository,id);
		}else {
			MenuUtil.moveDown(menuRepository,id);
		}
	}
}
