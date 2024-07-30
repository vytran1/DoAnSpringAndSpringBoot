package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "categories")
public class Category extends IdBasedEntity {
    
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;
	
	@Column(length = 128, unique = true, nullable = false)
	private String name;
	
	@Column(length = 64, unique = true, nullable = false)
	private String alias;
	
	@Column(length = 128, nullable = false)
	private String image;
	
	
	private boolean enabled;
	
	@Column(name = "all_parent_ids",length = 256, nullable = true)
	private String allParentIDs;
	
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	
	@OneToMany(mappedBy = "parent")
	@OrderBy("name asc")
	private Set<Category> children = new HashSet<>();

	
	
	
    
	
	public Category() {
		
	}


	public Category(Integer id) {
		this.id = id;
	}

    
	
	public Category(Integer id, String name, String alias) {
		super();
		this.id = id;
		this.name = name;
		this.alias = alias;
	}


	public static Category copyIdAndName(Category category) {
		Category newCategory = new Category();
		newCategory.setId(category.getId());
		newCategory.setName(category.getName());
		return newCategory;
	}

	public static Category copyIdAndName(Integer id, String name) {
		Category newCategory = new Category();
		newCategory.setId(id);
		newCategory.setName(name);
		return newCategory;
	}
	//avoid save new information parent to database, learn more about Cascade and Fetch of JPA
	public static Category copyFull(Category category) {
		Category newCategory = new Category();
		newCategory.setId(category.getId());
		newCategory.setName(category.getName());
		newCategory.setImage(category.getImage());
		newCategory.setAlias(category.getAlias());
		newCategory.setEnabled(category.isEnabled());
		newCategory.setHasChildren(category.getChildren().size()>0);
		return newCategory;
	}
	public static Category copyFull(Category category,String name) {
		Category newCategory = Category.copyFull(category);
		newCategory.setName(name);
        return newCategory;		
	}
	
	
	public Category(String name) {
		this.name = name;
		this.alias = name;
		this.image = "default.png";
	}

	
	public Category(String name,Category parent) {
		this(name);
		this.parent = parent;
		
	}

//	public Integer getId() {
//		return id;
//	}
//
//
//	public void setId(Integer id) {
//		this.id = id;
//	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAlias() {
		return alias;
	}


	public void setAlias(String alias) {
		this.alias = alias;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public Category getParent() {
		return parent;
	}


	public void setParent(Category parent) {
		this.parent = parent;
	}


	public Set<Category> getChildren() {
		return children;
	}


	public void setChildren(Set<Category> children) {
		this.children = children;
	}
	
	@Transient
	public String getImagePath() {
		if(this.id == null) return "images/image-thumbnail.png";
		return "/category-image/" + this.id + "/" + this.image;
	}
	
	public boolean isHasChildren() {
		return this.hasChildren;
	}
	
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	
	@Transient
	private boolean hasChildren;






	@Override
	public String toString() {
		return "Category [name=" + name + "]";
	}


	public String getAllParentIDs() {
		return allParentIDs;
	}


	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
	}
	
	
	
}
