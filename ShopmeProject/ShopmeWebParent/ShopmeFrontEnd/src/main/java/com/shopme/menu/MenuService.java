package com.shopme.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Article;
import com.shopme.common.entity.Menu;
import com.shopme.common.entity.MenuType;
import com.shopme.common.exception.MenuNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MenuService {
    
	@Autowired
	private MenuRepository menuRepository;
	
	public List<Menu> getHeaderMenuItems() {
		// TODO Auto-generated method stub
		return menuRepository.findByTypeAndEnabledOrderByPositionAsc(MenuType.HEADER, true);
	}
	
	public List<Menu> getFooterMenuItems() {
		// TODO Auto-generated method stub
		return menuRepository.findByTypeAndEnabledOrderByPositionAsc(MenuType.FOOTER, true);
	}
	
	public Article getArticleBoundToMenu(String menuAlias) throws MenuNotFoundException {
		// TODO Auto-generated method stub
		Menu menu = menuRepository.findByAlias(menuAlias);
		if (menu == null) {
			throw new MenuNotFoundException("No menu found with alias " + menuAlias);
		}

		return menu.getArticle();
	}
}
