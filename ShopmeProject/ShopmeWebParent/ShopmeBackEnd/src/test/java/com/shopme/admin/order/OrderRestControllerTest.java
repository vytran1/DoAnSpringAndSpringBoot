package com.shopme.admin.order;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc

public class OrderRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@WithMockUser(username = "chowsude@gmail.com",password = "123456789",authorities = "Shipper")
	public void testUpdateOrderStatus() throws Exception {
		Integer orderId = 9;
		String orderStatus = "DELIVERED";
		String requestURL = "/orders_shipper/update/" + orderId +"/"+orderStatus;
		
		mockMvc.perform(post(requestURL).with(csrf())).andExpect(MockMvcResultMatchers.status().isOk()).andDo(print());
		
	}

}
