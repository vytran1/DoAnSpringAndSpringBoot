package com.shopme.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    
    //HomePage
    public List<Category> listNoChildrenCategories(){
    	List<Category> listNoChildren = new ArrayList<>();
    	List<Category> list = categoryRepository.findAllEnabled();
    	list.forEach((x)->{
    		Set<Category> children = x.getChildren();
    		if(children == null || children.size() == 0  ) {
    			listNoChildren.add(x);
    		}
    	});
    	return listNoChildren;
    }
    
    public Category getCategory(String alias) throws CategoryNotFoundException {
    	Category category =  categoryRepository.findByAliasEnabled(alias);
    	if(category == null) {
    		throw new CategoryNotFoundException("Not exist category with alias " + alias);
    	}
    	return category;
    }
    
    
    //Home/Computers/Desktop
    public List<Category> getCategoryParents(Category child){
    	List<Category> listParents = new ArrayList<>();
    	Category  parent = child.getParent();
    	while(parent != null) {
    		listParents.add(parent);
    		parent = parent.getParent();
    	}
    	listParents.add(child);
    	return listParents;
    }
}
