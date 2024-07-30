package com.shopme.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Article;
import com.shopme.common.exception.ArticleNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ArticleService {
   
	@Autowired
	private ArticleRepository articleRepository;
	
	public Article findByAlias(String alias) throws ArticleNotFoundException {
		Article article = articleRepository.findByAlias(alias);
		if(article == null) {
			throw new ArticleNotFoundException("No article exist with alias " + alias);
		}
		return article;
	}
}
