package com.shopme.admin.brand;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Brand;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BrandService {
    
    public static final int BRANDS_PER_PAGE = 4;
	
	@Autowired
	private BrandRepository brandRepository;
	
	public List<Brand> listAll(){
		return (List<Brand>)brandRepository.findAll();
	}
	
	public void listByPage(int pageNum,PagingAndSortingHelper helper){
		Sort sort = Sort.by(helper.getSortField());
		
		sort = helper.getSortDir().equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum -1, BRANDS_PER_PAGE,sort);
		
		Page<Brand> page = null;
		
		if(helper.getKeyWord() != null) {
			page =  brandRepository.findAll(helper.getKeyWord(),  pageable);
		}
		else {
			
			page = brandRepository.findAll(pageable);
		}
		
		helper.updateModelAttribute(pageNum, page);
	}
	
	public Brand saveBrand(Brand brand) {
		return brandRepository.save(brand);
	}
	
	public Brand get(Integer ID) throws BrandNotFoundException {
		try {
			Brand brand  = brandRepository.findById(ID).get();
		    return brand;
		}catch(Exception e) {
			throw new BrandNotFoundException("No existing brand with ID: " + ID);
		}
	}
	
	public void delete(Integer id) throws BrandNotFoundException {
		Long countByID = brandRepository.countById(id);
		if(countByID == null || countByID == 0) {
			throw new BrandNotFoundException("No existing brand with ID: " + id);
		}
		brandRepository.deleteById(id);
	}
	
	public String checkUnique(Integer id,String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Brand brandByName = brandRepository.findByName(name);
		if(isCreatingNew) {
			if(brandByName != null) return "Dupplicated";
		}else {
			if(brandByName != null && brandByName.getId() != id) {
				return "Dupplicated";
			}
		}
		return "OK";
	}
}
