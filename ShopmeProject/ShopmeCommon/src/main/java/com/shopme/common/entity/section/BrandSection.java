package com.shopme.common.entity.section;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.IdBasedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sections_brands")
public class BrandSection extends IdBasedEntity {
    
	@Column(name = "brand_order")
	private int brandOrder;

	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;
	
	public BrandSection() {
		
	}
	
	public BrandSection(Integer id) {
		this.id = id;
	}

	public BrandSection(int brandOrder, Brand brand) {
		super();
		this.brandOrder = brandOrder;
		this.brand = brand;
	}
	
	public BrandSection(int id,int brandOrder, Brand brand) {
		this.id = id;
		this.brandOrder = brandOrder;
		this.brand = brand;
	}

	public int getBrandOrder() {
		return brandOrder;
	}

	public void setBrandOrder(int brandOrder) {
		this.brandOrder = brandOrder;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
	
}
