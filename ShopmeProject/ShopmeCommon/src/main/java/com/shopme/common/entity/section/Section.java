package com.shopme.common.entity.section;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.shopme.common.entity.IdBasedEntity;
import com.shopme.common.entity.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "sections")
public class Section extends IdBasedEntity {
     
	@Column(length = 256, nullable = false, unique = true)
	private String heading;
	
	@Column(length = 2048, nullable = false)
	private String description;
	
	private boolean enabled;
	
	@Column(name = "section_order")
	private int sectionOrder;
	
	@Enumerated(EnumType.STRING)
	private SectionType type;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "section_id")
	@OrderBy("productOrder ASC")
	private List<ProductSection> productSections = new ArrayList<>();
	
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "section_id")
	@OrderBy("categoryOrder ASC")
	private List<CategorySection> categorySections = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
	@JoinColumn(name = "section_id")
	@OrderBy("brandOrder ASC")
	private List<BrandSection> brandSections = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
	@JoinColumn(name = "section_id")
	@OrderBy("articleOrder ASC")
	private List<ArticleSection> articleSections = new ArrayList<>();
	
	public Section(boolean enabled, SectionType type) {
		this.enabled = enabled;
		this.type = type;
	}
	
	public Section () {
		
	}
	
	public Section(Integer id, String heading, SectionType type, int order, boolean enabled) {
		this.id = id;
		this.heading = heading;
		this.type = type;
		this.sectionOrder = order;
		this.enabled = enabled;
	}
	
	public Section(Integer id) {
		this.id = id;
	}
	public void addProductSection(ProductSection productSection) {
		this.productSections.add(productSection);
	}
	public void addArticleSection(ArticleSection articleSection) {
		// TODO Auto-generated method stub
		this.articleSections.add(articleSection);
	}
	public void addBrandSection(BrandSection brandSection) {
		// TODO Auto-generated method stub
		this.brandSections.add(brandSection);
	}
	public void addCategorySection(CategorySection categorySection) {
		// TODO Auto-generated method stub
		this.categorySections.add(categorySection);
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
		Section other = (Section) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Section [heading=" + heading + ", enabled=" + enabled + ", sectionOrder=" + sectionOrder + ", type="
				+ type + ", id=" + id + "]";
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getSectionOrder() {
		return sectionOrder;
	}

	public void setSectionOrder(int sectionOrder) {
		this.sectionOrder = sectionOrder;
	}

	public SectionType getType() {
		return type;
	}

	public void setType(SectionType type) {
		this.type = type;
	}

	public List<ProductSection> getProductSections() {
		return productSections;
	}

	public void setProductSections(List<ProductSection> productSections) {
		this.productSections = productSections;
	}

	public List<CategorySection> getCategorySections() {
		return categorySections;
	}

	public void setCategorySections(List<CategorySection> categorySections) {
		this.categorySections = categorySections;
	}

	public List<BrandSection> getBrandSections() {
		return brandSections;
	}

	public void setBrandSections(List<BrandSection> brandSections) {
		this.brandSections = brandSections;
	}

	public List<ArticleSection> getArticleSections() {
		return articleSections;
	}

	public void setArticleSections(List<ArticleSection> articleSections) {
		this.articleSections = articleSections;
	}
	
	
}
	