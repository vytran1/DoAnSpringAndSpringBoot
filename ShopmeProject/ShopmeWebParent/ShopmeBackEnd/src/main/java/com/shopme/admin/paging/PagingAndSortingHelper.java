package com.shopme.admin.paging;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.ui.Model;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.shopme.admin.user.UserService;
import com.shopme.common.entity.User;

public class PagingAndSortingHelper {
	private ModelAndViewContainer model;
	private String moduleURL;
	private String listName;
	private String sortField;
	private String sortDir;
	private String keyWord;
   
	public PagingAndSortingHelper(ModelAndViewContainer model,String moduleURL, String listName, String sortField, String sortDir,String keyWord) {
		this.model = model;
		this.moduleURL = moduleURL;
		this.listName = listName;
		this.sortField = sortField;
		this.sortDir = sortDir;
		this.keyWord = keyWord;
	}
	
	
	
	public String getSortField() {
		return sortField;
	}



	public void setSortField(String sortField) {
		this.sortField = sortField;
	}



	public String getSortDir() {
		return sortDir;
	}



	public void setSortDir(String sortDir) {
		this.sortDir = sortDir;
	}

    

	public String getKeyWord() {
		return keyWord;
	}



	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}



	public void updateModelAttribute(int pageNum,Page<?> page) {
		 List<?> listItems = page.getContent();
         int pageSize = page.getSize();
		 
		 long startCount = (pageNum -1)* pageSize + 1;
		 long endCount = startCount + pageSize - 1;
		 if(endCount > page.getTotalElements()) {
			 endCount = page.getTotalElements();
		 }

		 model.addAttribute("currentPage",pageNum);
		 model.addAttribute("totalPages",page.getTotalPages());
		 model.addAttribute("startCount",startCount);
		 model.addAttribute("endCount",endCount);
		 model.addAttribute("totalItems",page.getTotalElements()); 
		 model.addAttribute(this.listName,listItems);
		 model.addAttribute("moduleURL",this.moduleURL);

	}
	
	public void listEntities(int pageNum, int pageSize, SearchRepository<?, Integer> repo ) {
		 Sort sort = Sort.by(this.sortField);
		 sort = this.sortDir.equals("asc") ? sort.ascending():sort.descending();
		 org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNum - 1,pageSize,sort);
		 
		 Page<?> users = null;
		 if(this.keyWord != null) {
			 users = repo.findAll(this.keyWord, pageable);
		 }
		 else {
			 users =  repo.findAll(pageable);	 
		 }
		 
		
		 updateModelAttribute(pageNum, users);
	}
	
	public Pageable createPageable(int pageSize,int pageNum) {
		Sort sort = Sort.by(this.sortField);
		sort = this.sortDir.equals("asc") ? sort.ascending() : sort.descending();
		return PageRequest.of(pageNum-1, pageSize,sort);
	}
}
