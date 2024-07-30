package com.shopme;

import java.util.List;

import com.shopme.common.entity.section.Section;
import com.shopme.common.entity.section.SectionType;

public class SectionUtil {
     public static boolean hasAllCategoriesSection(List<Section> listSections) {
    	 for(Section section : listSections) {
    		 if(section.getType().equals(SectionType.ALL_CATEGORIES)) {
    			 return true;
    		 }
    	 }
    	 return false;
     }
}
