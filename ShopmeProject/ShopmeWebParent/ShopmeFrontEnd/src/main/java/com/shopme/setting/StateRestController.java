package com.shopme.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.entity.StateDTO;

@RestController
public class StateRestController {
    
	
	@Autowired 
	private StateRepository stateRepository;
	
	@GetMapping("/settings/list_states_by_country/{id}")
	public List<StateDTO> listByCountry(@PathVariable("id") Integer countryID){
		List<State> states = stateRepository.findByCountryOrderByNameAsc(new Country(countryID));
		List<StateDTO> response = new ArrayList<>();
		for(State state : states) {
			response.add(new StateDTO(state.getId(),state.getName()));
		}
		return response;
	}
	
	
}
