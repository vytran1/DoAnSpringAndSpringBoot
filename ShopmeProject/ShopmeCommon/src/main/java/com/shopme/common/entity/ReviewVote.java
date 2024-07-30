package com.shopme.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "reviews_votes")
public class ReviewVote extends IdBasedEntity {
     private static final int VOTE_UP_POINT =1;
     private static final int VOTE_DOWN_POINT =-1;
     
     
     private int votes;
     
     @ManyToOne
     @JoinColumn(name = "customer_id")
     private Customer customer;
     
     
     
     @ManyToOne
     @JoinColumn(name = "review_id")
     private Review review;



	public int getVotes() {
		return votes;
	}



	public void setVotes(int votes) {
		this.votes = votes;
	}



	public Customer getCustomer() {
		return customer;
	}



	public void setCustomer(Customer customer) {
		this.customer = customer;
	}



	public Review getReview() {
		return review;
	}



	public void setReview(Review review) {
		this.review = review;
	}

    

	public void voteUp() {
		this.votes =  VOTE_UP_POINT;
	}
	
	
	public void voteDown() {
		this.votes = VOTE_DOWN_POINT;
	}



	@Override
	public String toString() {
		return "ReviewVote [votes=" + votes + ", customer=" + customer.getFullName() + ", review=" + review + "]";
	}
     
    
	@Transient
	public boolean isUpVoted() {
		return this.votes == VOTE_UP_POINT;
	}
    
	
	@Transient
	public boolean isDownVoted() {
		return this.votes == VOTE_DOWN_POINT;
	} 
}
