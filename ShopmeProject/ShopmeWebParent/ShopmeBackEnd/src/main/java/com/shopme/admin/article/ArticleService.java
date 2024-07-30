package com.shopme.admin.article;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Article;
import com.shopme.common.entity.ArticleType;
import com.shopme.common.entity.User;
import com.shopme.common.exception.ArticleNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ArticleService {
    public static final int ARTICLE_PER_PAGE = 5;
    
    @Autowired
    private ArticleRepository articleRepository;
    
    public void listByPage(int pageNum,PagingAndSortingHelper helper) {
    	helper.listEntities(pageNum,ARTICLE_PER_PAGE, articleRepository);
    }
    
    
    public Article get(Integer id) throws ArticleNotFoundException {
    	try {
			Article article = articleRepository.findById(id).get();
			return article;
		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			throw new ArticleNotFoundException("Could not find article with ID: " + id);
		}
    	
    }
    
    public void save(Article articleInForm,User user) {
    	if(articleInForm.getAlias() == null || articleInForm.getAlias().isEmpty()) {
    		articleInForm.setAlias(articleInForm.getTitle().replaceAll(" ","-"));
    	}
    	articleInForm.setUpdatedTime(new Date());
    	articleInForm.setUser(user);
    	articleRepository.save(articleInForm);
    }
    
    public void delete(Integer id) throws ArticleNotFoundException {
    	if(!articleRepository.existsById(id)) {
    		throw new ArticleNotFoundException("Could not find article with ID: " + id);
    	}
    	articleRepository.deleteById(id);
    }
    
    public void updatePublishStatus(Integer id,boolean status) throws ArticleNotFoundException {
    	if(!articleRepository.existsById(id)) {
    		throw new ArticleNotFoundException("Could not find article with ID: " + id);
    	}
    	articleRepository.updatePublishStatus(id, status);
    }
    
    public List<Article> findByTypeOrderByTitle(ArticleType type) {
		// TODO Auto-generated method stub
		return articleRepository.findByTypeOrderByTitle(ArticleType.MENU_BOND);
	}
    
    public List<Article> listArticlesForMenu() {
		return articleRepository.findByTypeOrderByTitle(ArticleType.MENU_BOND);
	}
    
    public List<Article> listAll() {
		return articleRepository.findPublishedArticlesWithIDAndTitleOnly();
	}
}
