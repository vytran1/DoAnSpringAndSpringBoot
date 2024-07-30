package com.shopme.question;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Question;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.product.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class QuestionService {
    public static final int QUESTION_PER_PAGE_FOR_PRODUCT = 10;
    public static final int QUESTION_PER_PAGE_FOR_CUSTOMER = 4;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    
    public List<Question> listTop3RecentQuestions(Integer productId){
    	Pageable pageable = PageRequest.of(0, 3, Sort.by("votes").descending());
    	Page<Question> result = questionRepository.findAll(productId, pageable);
    	return result.getContent();
    }
    
    public Page<Question> listQuestionsOfProduct(String alias, int pageNum, String sortField, String sortDir) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending(); 
		Pageable pageable = PageRequest.of(pageNum - 1, QUESTION_PER_PAGE_FOR_PRODUCT, sort);
		return questionRepository.findByAlias(alias, pageable);
	}
    
    
	public int getNumberOfQuestions(Integer productId) {
		return questionRepository.countApprovedQuestions(productId);
	}
	
	public Page<Question> listQuestionsByCustomer(Customer customer, String keyWord, int pageNum, 
			String sortField, String sortDir) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, QUESTION_PER_PAGE_FOR_CUSTOMER, sort);

		if (keyWord != null) {
			return questionRepository.findByCustomer(customer.getId(), keyWord, pageable);
		}

		return questionRepository.findByCustomer(customer.getId(), pageable);
	}
	
	public Question getByCustomerAndId(Customer customer, Integer questionId) {
		return questionRepository.findByCustomerAndId(customer.getId(), questionId);
	}
	
	public void saveNewQuestion(Question question, Customer asker, 
			Integer productId) throws ProductNotFoundException {
		Optional<Product> productById = productRepository.findById(productId);
		if (!productById.isPresent()) {
			throw new ProductNotFoundException("Could not find product with ID " + productId);
		}
		question.setAskTime(new Date());
		question.setProduct(productById.get());
		question.setAsker(asker);

		questionRepository.save(question);
	}
	
	public int getNumberOfAnsweredQuestions(Integer productId) {
		return questionRepository.countAnsweredQuestions(productId);
	}
	
	public int getVotesForQuestion(Integer questionId) {
		Question question = questionRepository.findById(questionId).get();
		return question.getVotes();
	}
}
