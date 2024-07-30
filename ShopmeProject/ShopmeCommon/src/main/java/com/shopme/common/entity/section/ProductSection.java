package com.shopme.common.entity.section;

import com.shopme.common.entity.IdBasedEntity;
import com.shopme.common.entity.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sections_products")
public class ProductSection extends IdBasedEntity {
	@Column(name = "product_order")
    private int productOrder;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public ProductSection() {
		
	}
	
	public ProductSection(Integer id) {
		this.id = id;
	}

	public ProductSection(int productOrder, Product product) {
		
		this.productOrder = productOrder;
		this.product = product;
	}
	
	public ProductSection(Integer id,int productOrder, Product product) {
		
		this.id = id;
		this.productOrder = productOrder;
		this.product = product;
	}

	public int getProductOrder() {
		return productOrder;
	}

	public void setProductOrder(int productOrder) {
		this.productOrder = productOrder;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
}
