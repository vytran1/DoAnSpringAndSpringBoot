package com.shopme.common.entity.section;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.IdBasedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sections_categories")
public class CategorySection extends IdBasedEntity {
	@Column(name = "category_order") 
	private int categoryOrder;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	public CategorySection() {
		
	}
	
	public CategorySection(Integer id) {
		this.id = id;
	}

	public CategorySection(int categoryOrder, Category category) {
		super();
		this.categoryOrder = categoryOrder;
		this.category = category;
	}
	
	
	public CategorySection(Integer id,int categoryOrder, Category category) {
		this.id = id;
		this.categoryOrder = categoryOrder;
		this.category = category;
	}

	public int getCategoryOrder() {
		return categoryOrder;
	}

	public void setCategoryOrder(int categoryOrder) {
		this.categoryOrder = categoryOrder;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	
}
