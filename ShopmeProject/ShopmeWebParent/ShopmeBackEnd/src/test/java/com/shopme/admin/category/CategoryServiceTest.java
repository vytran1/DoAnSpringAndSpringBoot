package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.common.entity.Category;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {
    @MockBean
	private CategoryRepository categoryRepository;
    
    @InjectMocks
    private CategoryService categoryService;
    
    @Test
    public void testCheckUniqueInNewModeReturnDupplicateName() {
    	Integer id = null;
    	String name = "Computers";
    	String alias = "abc";
    	
    	Category category = new Category(id,name,alias);
    	
    	Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
    	Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);
    	
    	String result = categoryService.checkUnique(id, name, alias);
    	
    	assertThat(result).isEqualTo("Dupplicated");
    }
}
