package com.shopme.admin.report;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportRestControllerTest {
       @Autowired
       private MockMvc mockMvc;
       
       @Test
       @WithMockUser(username = "vy.tn171003@gmail.com",password = "123456789ASD",authorities = {"Admin"})
       public void testGetReportDataLast7Days() throws Exception {
    	   String requestURL = "/reports/sales_by_date/last_7_days";
    	   mockMvc.perform(get(requestURL)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(print());
       }
       
       @Test
       @WithMockUser(username = "vy.tn171003@gmail.com",password = "123456789ASD",authorities = {"Admin"})
       public void testGetReportDataLast6Months() throws Exception {
    	   String requestURL = "/reports/sales_by_date/last_6_months";
    	   mockMvc.perform(get(requestURL)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(print());
       }
       
       @Test
       @WithMockUser(username = "vy.tn171003@gmail.com",password = "123456789ASD",authorities = {"Admin"})
       public void testGetReportDataByDateRange() throws Exception {
    	   String endDate = "2024-04-23";
    	   String startDate = "2024-03-30";
    	   String requestURL = "/reports/sales_by_date/" + startDate + "/" + endDate;
    	   mockMvc.perform(get(requestURL)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(print());
       }
       
       @Test
       @WithMockUser(username = "vy.tn171003@gmail.com",password = "123456789ASD",authorities = {"Admin"})
       public void testGetReportDateByCategory() throws Exception {
    	   String requestURL = "/reports/category/last_7_days";
    	   mockMvc.perform(get(requestURL)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(print());
       }
       
       @Test
       @WithMockUser(username = "vy.tn171003@gmail.com",password = "123456789ASD",authorities = {"Admin"})
       public void testGetReportDateByProduct() throws Exception {
    	   String requestURL = "/reports/product/last_7_days";
    	   mockMvc.perform(get(requestURL)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(print());
       }
}
