package com.shopme.admin.question;

import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Question;
import com.shopme.common.entity.User;
import com.shopme.common.exception.QuestionNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class QuestionService {
    
	
	private static final int QUESTION_PER_PAGE = 10;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		// TODO Auto-generated method stub
		helper.listEntities(pageNum, QUESTION_PER_PAGE, questionRepository);
	}
	
	public Question getQuestionById(Integer id) throws QuestionNotFoundException {
		// TODO Auto-generated method stub
		try {
			return questionRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new QuestionNotFoundException("Could not find question with ID " + id);
		}
	}
	
	public void savedQuestionByUser(Question questionInForm,User user) throws QuestionNotFoundException {
		try {
			Question questionInDB = questionRepository.findById(questionInForm.getId()).get();
			questionInDB.setQuestionContent(questionInForm.getQuestionContent());
			questionInDB.setAnswer(questionInForm.getAnswer());
			questionInDB.setApproved(questionInForm.isApproved());;
			if(questionInDB.isAnswered()) {
				questionInDB.setAnswerTime(new Date());
				questionInDB.setAnswerer(user);
			}
			questionRepository.save(questionInDB);
		}catch(NoSuchElementException e) {
			throw new QuestionNotFoundException("Could not find question with ID " + questionInForm.getId());
		}
	}
	
	
	public void approve(Integer id) {
		// TODO Auto-generated method stub
		questionRepository.updateApprovalStatus(id, true);
	}
	
	
	public void disapprove(Integer id) {
		// TODO Auto-generated method stub
		questionRepository.updateApprovalStatus(id, false);
	}
	
	
	public void delete(Integer id) throws QuestionNotFoundException {
		// TODO Auto-generated method stub
		if (!questionRepository.existsById(id)) {
			throw new QuestionNotFoundException("Could not find any question with ID " + id);
		}
		questionRepository.deleteById(id);
	}
	
	
}
