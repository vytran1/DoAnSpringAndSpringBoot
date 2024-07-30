package com.shopme.admin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.shopme.admin.menu.MenuRepository;
import com.shopme.common.entity.Menu;
import com.shopme.common.exception.MenuNotFoundException;
import com.shopme.common.exception.MenuUnmoveAbleException;

public class MenuUtil {
    public static void setDefaultAlias(Menu menu) {
    	if(menu.getAlias() == null || menu.getAlias().isEmpty()) {
    		menu.setAlias(menu.getTitle().replaceAll(" ","-"));
    	}
    }

	public static void setPositionForNewMenu(MenuRepository menuRepository, Menu menu) {
		// TODO Auto-generated method stub
		Long newPosition = menuRepository.countByType(menu.getType()) + 1;
		
		menu.setPosition(newPosition.intValue());
	}

	public static void setPositionForEditedMenu(MenuRepository menuRepository, Menu menu) {
		// TODO Auto-generated method stub
		Menu existMenu = menuRepository.findById(menu.getId()).get();
		// if the menu type changed, then set its position at the last
		if(!existMenu.getType().equals(menu.getType())) {
			setPositionForNewMenu(menuRepository,menu);
		}
	}

	public static void moveUp(MenuRepository menuRepository, Integer id) throws MenuNotFoundException, MenuUnmoveAbleException {
		// TODO Auto-generated method stub
		Optional<Menu> menu = menuRepository.findById(id);
		if(!menu.isPresent()) {
			throw new MenuNotFoundException("Could not find menu with id: " + id);
		}
		
		Menu currentMenu = menu.get();
		
		List<Menu> listAllMenuOfSameType = menuRepository.findByTypeOrderByPositionAsc(currentMenu.getType());
		
		int currentMenuIndex  = listAllMenuOfSameType.indexOf(currentMenu);
		
		if(currentMenuIndex == 0) {
			throw new MenuUnmoveAbleException("The menu with id " + id + " is already in the first position");
		}
		
		int previousMenuIndex = currentMenuIndex - 1;
		
		Menu previousMenu = listAllMenuOfSameType.get(previousMenuIndex);
		
		currentMenu.setPosition(previousMenuIndex + 1);
		listAllMenuOfSameType.set(previousMenuIndex, currentMenu);
		
		previousMenu.setPosition(currentMenuIndex + 1);
		listAllMenuOfSameType.set(currentMenuIndex, previousMenu);
		
		
		menuRepository.saveAll(Arrays.asList(currentMenu,previousMenu));
		
		
	}

	public static void moveDown(MenuRepository menuRepository, Integer id) throws MenuNotFoundException, MenuUnmoveAbleException {
		// TODO Auto-generated method stub
		Optional<Menu> menu = menuRepository.findById(id);
		if(!menu.isPresent()) {
			throw new MenuNotFoundException("Could not find menu with id: " + id);
		}
		
		Menu currentMenu = menu.get();
		List<Menu> listAllMenuOfSameType = menuRepository.findByTypeOrderByPositionAsc(currentMenu.getType());
		int currentMenuIndex = listAllMenuOfSameType.indexOf(currentMenu);
		if (currentMenuIndex == listAllMenuOfSameType.size() - 1) {
			throw new MenuUnmoveAbleException("The menu with id " + id + " is already in the last position");
		}
		int nextMenuIndex = currentMenuIndex + 1;
		Menu nextMenu = listAllMenuOfSameType.get(nextMenuIndex);
		
		currentMenu.setPosition(nextMenuIndex + 1);
		listAllMenuOfSameType.set(nextMenuIndex, currentMenu);
		
		nextMenu.setPosition(currentMenuIndex + 1);
		listAllMenuOfSameType.set(currentMenuIndex, nextMenu);
		
		menuRepository.saveAll(Arrays.asList(currentMenu, nextMenu));
	}
}
