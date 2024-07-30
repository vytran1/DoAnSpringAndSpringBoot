package com.shopme.admin.setting.country;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import com.shopme.common.entity.Country;

@RestController
public class CountryRestController {
    @Autowired 
    private CountryRepository countryRepository;
    
    
    @GetMapping("/countries/list")
    public List<Country> listAllCountry(){
    	return countryRepository.findAllByOrderByNameAsc();
    }
    
    @PostMapping("/countries/save")
    public String saveCountry(@RequestBody Country country) {
    	Country saveCountry = countryRepository.save(country);
    	return String.valueOf(saveCountry.getId());
    }
    
    @DeleteMapping("/countries/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
    	countryRepository.deleteById(id);
    }
    
}
