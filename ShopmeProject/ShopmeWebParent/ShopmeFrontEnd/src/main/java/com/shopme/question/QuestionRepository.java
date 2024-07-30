package com.shopme.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Question;

public interface QuestionRepository extends JpaRepository<Question,Integer> {
	//Show Question in Product Details
	@Query("SELECT q FROM Question q WHERE q.approved = true AND q.product.id = ?1")
	Page<Question> findAll(Integer productId, Pageable pageable);
	
	//Finding
	@Query("SELECT q FROM Question q WHERE q.approved = true AND q.product.alias = ?1")
	Page<Question> findByAlias(String alias, Pageable pageable);
	
	@Query("SELECT COUNT (q) FROM Question q WHERE q.approved = true and q.product.id =?1")
	int countApprovedQuestions(Integer productId);
	
	//Show Question in Customer Page
	@Query("SELECT q FROM Question q WHERE q.asker.id = ?1")
	Page<Question> findByCustomer(Integer customerId, Pageable pageable);
	
	
	@Query("SELECT q FROM Question q WHERE q.asker.id = ?1 AND ("
			+ "q.questionContent LIKE %?2% OR "
			+ "q.answer LIKE %?2% OR q.product.name LIKE %?2%)")
	Page<Question> findByCustomer(Integer customerId, String keyWord, Pageable pageable);
	
	@Query("SELECT q FROM Question q WHERE q.asker.id = ?1 AND q.id = ?2")
	Question findByCustomerAndId(Integer customerId, Integer questionId);
	
	@Query("SELECT q.votes FROM Question q WHERE q.id = ?1")
	public Integer getVoteCount(Integer questionId);
	
	@Query("SELECT COUNT (q) FROM Question q WHERE q.approved = true AND q.answer IS NOT NULL and q.product.id =?1")
	int countAnsweredQuestions(Integer productId);
}
