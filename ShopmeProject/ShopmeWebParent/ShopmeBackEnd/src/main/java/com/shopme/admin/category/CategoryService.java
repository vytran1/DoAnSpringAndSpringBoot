package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {
    @Autowired
	private CategoryRepository categoryRepository;
    
    public static final int ROOT_CATEGORIES_PER_PAGE = 4;
    
    public List<Category> listByPage(CategoryPageInfo pageInfo,int pageNumber,String sortDir,String keyWord){
    	Sort sort = Sort.by("name");
    	
    	if(sortDir.equals("asc")) {
    		sort = sort.ascending();
    	}
    	else if(sortDir.equals("desc")) {
    		sort = sort.descending();
    	}
    	Page<Category> pageCategories = null;
    	Pageable pageable = PageRequest.of(pageNumber - 1, ROOT_CATEGORIES_PER_PAGE,sort);
    	if(keyWord != null && !keyWord.isEmpty()) {
    		 pageCategories = categoryRepository.search(keyWord, pageable);
    	}else {
    		 pageCategories = categoryRepository.listRootCategories(pageable);	
    	}
        List<Category> rootCategories = pageCategories.getContent();
        pageInfo.setTotalElements(pageCategories.getTotalElements());
        pageInfo.setTotalPages(pageCategories.getTotalPages());
        
        if(keyWord != null && !keyWord.isEmpty()) {
        	 List<Category> searchResults = pageCategories.getContent();
        	 for(Category x : searchResults) {
        		 x.setHasChildren(x.getChildren().size()>0);
        	 }
        	 return searchResults;
        }
        else {
        	return listHierarchicalCategories(rootCategories,sortDir);	
        }
    	
    }
    
    
    private  List<Category> listHierarchicalCategories(List<Category> rootCategories,String sortDir){
    	 List<Category> HierarchicalCategories = new ArrayList<>();
    	 
    	 for(Category rootCategory : rootCategories) {
    		 HierarchicalCategories.add(Category.copyFull(rootCategory));
    		 
    		 Set<Category> children = sortSubCategories(rootCategory.getChildren(),sortDir);
    		 for(Category subCategory : children) {
    			 String name = "--" + subCategory.getName();
    			 HierarchicalCategories.add(Category.copyFull(subCategory, name));
    			 listSubHierarchicalCategories(HierarchicalCategories,subCategory,1,sortDir);
    		 }
    	 }
    	 return HierarchicalCategories;
    }
    
    private void listSubHierarchicalCategories(List<Category> HierarchicalCategories,Category parent, int subLevel,String sortDir) {
    	Set<Category> children = sortSubCategories(parent.getChildren(),sortDir);
    	int newSubLevel = subLevel  + 1;
    	for(Category sub : children) {
    		String name = "";
			for(int i =0; i<newSubLevel;i++) {
				name += "--";
			}
			name += sub.getName();
			HierarchicalCategories.add(Category.copyFull(sub, name));
			listSubHierarchicalCategories(HierarchicalCategories, sub, newSubLevel,sortDir);
    	}
    	
    }
    
    public Category getCategoryByID(Integer Id) throws CategoryNotFoundException {
    	try {
    		return categoryRepository.findById(Id).get();
    	}
    	catch(Exception e){
    		throw new CategoryNotFoundException("Could not find category with ID  " + Id );
    	}
    }
    
    public List<Category> listOfCategoriesUsedInForm(){
    	List<Category> listCategoryUsedInForm = new ArrayList<>();
    	
    	Iterable<Category> categoriesDB = categoryRepository.listRootCategories(Sort.by("name").ascending());
    	
    	for(Category rootCategories : categoriesDB) {
    		if(rootCategories.getParent() == null) {
    			
    			listCategoryUsedInForm.add(Category.copyIdAndName(rootCategories));
    			Set<Category> children = sortSubCategories(rootCategories.getChildren());
    			for(Category subCatgegory : children) {
    				String name = "--" + subCatgegory.getName();
    				listCategoryUsedInForm.add(Category.copyIdAndName(subCatgegory.getId(), name));
    				listSubCategoriesUsedInForm(listCategoryUsedInForm,subCatgegory,1);
    			}
    		}
    	}
    	return listCategoryUsedInForm;
    }
    
    public Category saveCategory(Category category) {
    	Category parent = category.getParent();
    	if(parent != null) {
    		String allParentIDs = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
    		allParentIDs += String.valueOf(parent.getId())+"-";
    		category.setAllParentIDs(allParentIDs);
    	}
    	return categoryRepository.save(category);
    }
    
    private void listSubCategoriesUsedInForm(List<Category> listCategoryUsedInForm,Category parent, int subLevel) {
    	int newSubLevel = subLevel  + 1;
    	Set<Category> children = sortSubCategories(parent.getChildren());
    	for(Category subCatgegory : children) {
    		String name = "";
			for(int i =0; i<newSubLevel;i++) {
				name += "--";
			}
			name += subCatgegory.getName();
			listCategoryUsedInForm.add(Category.copyIdAndName(subCatgegory.getId(), name));
			listSubCategoriesUsedInForm( listCategoryUsedInForm,subCatgegory,newSubLevel);
		}
    }
    
    public String checkUnique(Integer id,String name,String alias) {
    	boolean isCreating = (id == null || id == 0);
    	
    	Category categoryByName = categoryRepository.findByName(name);
    	
    	if(isCreating) {
    		if(categoryByName != null) {
    			return "DupplicatedName";
    		}else {
    			Category categoryByAlias = categoryRepository.findByAlias(alias);
    			if(categoryByAlias != null) {
    				return "DupplicatedAlias";
    			}
    		}
    	}else {
    		if(categoryByName != null && categoryByName.getId() != id) {
    			return "DupplicatedName";
    		}
    		Category categoryByAlias = categoryRepository.findByAlias(alias);
    		if(categoryByAlias != null && categoryByAlias.getId() != id) {
    			return "DupplicatedAlias";
    		}
    	}
    	return "OK";
    }
    private SortedSet<Category> sortSubCategories(Set<Category> children){
    	return sortSubCategories(children,"asc");
    }
    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir){
    	SortedSet<Category> sortedChildren = new TreeSet(new Comparator<Category>() {

			@Override
			public int compare(Category o1, Category o2) {
				if(sortDir.equals("asc")) {
					return o1.getName().compareTo(o2.getName());
				}else {
					return o2.getName().compareTo(o1.getName());
				}
				// TODO Auto-generated method stub
				
			}
    		
		});
    	sortedChildren.addAll(children);
    	return sortedChildren;
    }
    
    public void updateCategoryEnabledStatus(Integer id, boolean status) {
    	//findById
//    	Category categoryById = categoryRepository.findById(id).get();
    	
    	categoryRepository.updateEnabledStatus(id, status);
    }
    
    public void deleteCategory(Integer Id) throws CategoryNotFoundException {
    	Long countByID = categoryRepository.countById(Id);
    	if(countByID == null || countByID == 0) {
    		throw new CategoryNotFoundException("No category exist with ID" + Id);
    	}
    	categoryRepository.deleteById(Id);
    }
}
