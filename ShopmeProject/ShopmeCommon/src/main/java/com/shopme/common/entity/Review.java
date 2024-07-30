package com.shopme.common.entity;

import java.util.Date;
import java.util.Objects;

import com.shopme.common.entity.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "reviews")
public class Review extends IdBasedEntity{ 
    
	@Column(length = 128, nullable = false)
	private String headline;
	
	@Column(length = 300, nullable = false)
	private String comment;
	
	private int rating;
	
	@Column(nullable = false)
	private Date reviewTime;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	
	@Transient
	private boolean upvotedByCurrentCustomer;
	
	@Transient
	private boolean downvotedByCurrentCustomer;
	
	
	public Review() {
		
	}


	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

    //Vote count
	private int votes;
	
	public Review(Integer reviewId) {
		// TODO Auto-generated constructor stub
		this.id = reviewId;
	}


	public String getHeadline() {
		return headline;
	}


	public void setHeadline(String headline) {
		this.headline = headline;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public int getRating() {
		return rating;
	}


	public void setRating(int rating) {
		this.rating = rating;
	}


	public Date getReviewTime() {
		return reviewTime;
	}


	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}


	public Product getProduct() {
		return product;
	}


	public void setProduct(Product product) {
		this.product = product;
	}


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

    
	
	
	public int getVotes() {
		return votes;
	}


	public void setVotes(int votes) {
		this.votes = votes;
	}


	@Override
	public String toString() {
		return "Review [headline=" + headline + ", comment=" + comment + ", rating=" + rating + ", reviewTime="
				+ reviewTime + ", product=" + product.getShortName() + ", customer=" + customer.getFullName() + "]";
	}


	public boolean isUpvotedByCurrentCustomer() {
		return upvotedByCurrentCustomer;
	}


	public void setUpvotedByCurrentCustomer(boolean upvotedByCurrentCustomer) {
		this.upvotedByCurrentCustomer = upvotedByCurrentCustomer;
	}


	public boolean isDownvotedByCurrentCustomer() {
		return downvotedByCurrentCustomer;
	}


	public void setDownvotedByCurrentCustomer(boolean downvotedByCurrentCustomer) {
		this.downvotedByCurrentCustomer = downvotedByCurrentCustomer;
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
		Review other = (Review) obj;
		return Objects.equals(id, other.id);
	}
}
