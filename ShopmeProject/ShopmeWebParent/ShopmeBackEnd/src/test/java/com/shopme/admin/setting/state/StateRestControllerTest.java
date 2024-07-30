package com.shopme.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTest {
    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    ObjectMapper objectMapper;
    
    @Autowired
    CountryRepository countryRepository;
    
    @Autowired
    StateRepository stateRepository;
    
    @Test
    @WithMockUser(username = "vy.tn171003@gmail.com",password = "123456789ASD",roles = "ADMIN")
    public void testListStateByCountries() throws Exception {
    	Integer id = 1;
    	String url = "/states/list_by_country/" + id;
    	
    	
    	MvcResult mvcResult = mockMvc.perform(get(url))
    			.andExpect(status()
    		    .isOk())
    			.andDo(print())
    			.andReturn();
    	String jasonResponse = mvcResult.getResponse().getContentAsString();
    	State[] states = objectMapper.readValue(jasonResponse,State[].class); 
    	assertThat(states).hasSizeGreaterThan(1);
    }
    
    @Test
    @WithMockUser(username = "vy.tn171003@gmail.com",password = "123456789ASD",roles = "ADMIN")
    public void testCreateState() throws JsonProcessingException, Exception {
    	String url = "/states/save";
    	Integer countryID = 2;
    	Country country = countryRepository.findById(countryID).get();
    	State state = new State("VUNGTAU",country);
    	
    	MvcResult result = mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(state)).with(csrf()))
    	
    	.andDo(print())
    	.andExpect(status().isOk())
		.andReturn();;
    	
    	String response = result.getResponse().getContentAsString();
    	Integer stateId = Integer.parseInt(response);
    	Optional<State> findByID = stateRepository.findById(stateId);
    	assertThat(findByID.isPresent());
    }
    
    @Test
    @WithMockUser(username = "vy.tn171003@gmail.com",password = "123456789ASD",roles = "ADMIN")
    public void testUpdateState() throws JsonProcessingException, Exception {
    	String url = "/states/save";
    	Integer stateID = 2;
    	String stateName = "DONGNAI2";
    	
    	State state = stateRepository.findById(stateID).get();
    	state.setName(stateName);
    	MvcResult result = mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(state)).with(csrf()))
    	
    	.andDo(print())
    	.andExpect(status().isOk())
		.andReturn();;
    	
    	Optional<State> findByID = stateRepository.findById(stateID);
    	assertThat(findByID.isPresent());
    	
    	State updateState = findByID.get();
    	assertThat(updateState.getName()).isEqualTo(stateName);
    }
    
    
    @Test
    @WithMockUser(username = "vy.tn171003@gmail.com",password = "123456789ASD",roles = "ADMIN")
    public void testDeleteState() throws Exception {
    	Integer stateID = 3;
    	String url = "/states/delete/" + stateID;
    	
    	mockMvc.perform(get(url)).andExpect(status().isOk());
    	
    	Optional<State> findByID = stateRepository.findById(stateID);
    	
    	assertThat(findByID).isNotPresent();
    }
    
}
