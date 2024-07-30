package com.shopme.section;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.section.Section;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SectionService {
    
	@Autowired
	private SectionRepository sectionRepository;
	
	public List<Section> listEnabledSection(){
		 return sectionRepository.findAllByEnabledOrderBySectionOrderAsc(true);
	}
}
