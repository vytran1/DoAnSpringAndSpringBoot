package com.shopme.admin.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Menu;
import com.shopme.common.entity.MenuType;

public interface MenuRepository extends JpaRepository<Menu,Integer> {
	public List<Menu> findAllByOrderByTypeAscPositionAsc();
	
	public Long countByType(MenuType type);
	
	@Query("UPDATE Menu m SET m.enabled = ?2 WHERE m.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	public List<Menu> findByTypeOrderByPositionAsc(MenuType type);
}
