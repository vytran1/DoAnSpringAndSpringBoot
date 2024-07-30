package com.shopme.common.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "menus")
public class Menu extends IdBasedEntity {
	@Enumerated(EnumType.ORDINAL) 
	private MenuType type;
	
	@Column(nullable = false, length = 128, unique = true)
	private String title;
	
	@Column(nullable = false, length = 256, unique = true)
	private String alias;
	
	private int position;
	
	private boolean enabled;
	
	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;
    
	
	
	
	public Menu() {
		
	}

    public Menu(Integer id) {
    	this.id = id;
    }
    


	public Menu(MenuType type, String title, String alias, int position, boolean enabled, Article article) {
		super();
		this.type = type;
		this.title = title;
		this.alias = alias;
		this.position = position;
		this.enabled = enabled;
		this.article = article;
	}

	public MenuType getType() {
		return type;
	}

	public void setType(MenuType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	@Override
	public String toString() {
		return "Menu [type=" + type + ", title=" + title + ", position=" + position + ", id=" + id + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Menu other = (Menu) obj;
		return Objects.equals(id, other.id);
	}
}
