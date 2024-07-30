package com.shopme.admin.setting.country;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
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
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.Country;

import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTest {
   
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	CountryRepository countryRepository;
//	EntityManager entityManager;
	
	@Test
	@WithMockUser(username = "vy.tn171003@gmail.com",password = "123456789ASD",roles = "ADMIN")
	public void listCountries() throws Exception {
		String url ="/countries/list";
		MvcResult result =  mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print()).andReturn();
		
		//lấy Response Json
		String  jsonResponse = result.getResponse().getContentAsString();
//		System.out.println(jsonResponse);
		
		//inject json into Java Object
		Country[] list = objectMapper.readValue(jsonResponse,Country[].class);
//		for(Country country : list) {
//			System.out.println(country);
//		}
		assertThat(list).hasSizeGreaterThan(0);
	}
	@Test
	@WithMockUser(username = "vy.tn171003@gmail.com",password = "123456789ASD",roles = "ADMIN")
	public void testCreateCountry() throws Exception, Exception {
		String url = "/countries/save";
		String countryName = "Chinese";
		String countryCode = "CN";
		Country countryObject = new Country(countryName,countryCode);
		
		MvcResult result = mockMvc
				.perform(post(url).contentType("application/json")
						.content(objectMapper.writeValueAsString(countryObject))
						.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
		String resultContent = result.getResponse().getContentAsString();
		Integer countryId = Integer.parseInt(resultContent);
//		Country savedCountry = entityManager.find(Country.class, countryId);
		Optional<Country> findById = countryRepository.findById(countryId);
		assertThat(findById).isPresent();
		Country savedCountry = findById.get();
		assertThat(savedCountry.getName()).isEqualTo(countryName);
		
	}
	
	@Test
	@WithMockUser(username = "vy.tn171003@gmail.com",password = "123456789ASD",roles = "ADMIN")
	public void testUpdateCountry() throws Exception, Exception {
		String url = "/countries/save";
		
		Integer countryId = 5;
		String countryName = "United State";
		String countryCode = "USA";
		Country countryObject = new Country(countryId,countryName,countryCode);
		
		mockMvc
				.perform(post(url).contentType("application/json")
						.content(objectMapper.writeValueAsString(countryObject))
						.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect((ResultMatcher) content().string(String.valueOf(countryId)));
		
		
//		Country savedCountry = entityManager.find(Country.class, countryId);
		Optional<Country> findById = countryRepository.findById(countryId);
		assertThat(findById).isPresent();
		
		
		Country savedCountry = findById.get();
		assertThat(savedCountry.getName()).isEqualTo(countryName);
		
	}
}
