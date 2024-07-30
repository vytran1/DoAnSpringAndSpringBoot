package com.shopme.common.entity.section;

import com.shopme.common.entity.Article;
import com.shopme.common.entity.IdBasedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sections_articles")
public class ArticleSection extends IdBasedEntity {
     
	@Column(name = "article_order")
	private int articleOrder;

	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;
	
	
	public ArticleSection() {
		
	}
	
	public ArticleSection(Integer id) {
		this.id = id;
	}

	public ArticleSection(int articleOrder, Article article) {
		this.articleOrder = articleOrder;
		this.article = article;
	}
	
	public ArticleSection(int id,int articleOrder, Article article) {
		this.id = id;
		this.articleOrder = articleOrder;
		this.article = article;
	}

	public int getArticleOrder() {
		return articleOrder;
	}

	public void setArticleOrder(int articleOrder) {
		this.articleOrder = articleOrder;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}
	
	
}
