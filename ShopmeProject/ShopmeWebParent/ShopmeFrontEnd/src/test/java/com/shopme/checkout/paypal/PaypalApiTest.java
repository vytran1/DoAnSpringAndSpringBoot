package com.shopme.checkout.paypal;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PaypalApiTest {
    private static final String BASE_URL ="https://api-m.sandbox.paypal.com";
    private static final String GET_ORDER_URL = "/v2/checkout/orders/";
    private static final String CLIENT_ID = "Ad4Rw6lNSHGqyKwcHKb_Wzr_01ko2v8Q0mzSqJW947kadatGG1chyrRZoEcRD0TRrLYQTP9metSkvIW7";
    private static final String CLIENT_SECRET_CODE = "EMTxGyuFKI1Cy6ID7U6-v8zsCbVNSeVoTlPQ8HF_dvetBn7gFUmJpyaqQY6wtViJV2UG2_0fWn9_oDY5";
    
    @Test
    public void testGetOrderDetails() {
    	String orderId = "9L913960W7872744N";
    	String requestURL = BASE_URL + GET_ORDER_URL + orderId;
    	
    	HttpHeaders header = new HttpHeaders();
    	header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    	header.add("Accept-language","en_US");
    	header.setBasicAuth(CLIENT_ID,CLIENT_SECRET_CODE);
    	
    	HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(header);
    	RestTemplate restTemplate = new RestTemplate();
    	
    	ResponseEntity<PayPalOrderResponse> response =  restTemplate.exchange(requestURL,HttpMethod.GET,request,PayPalOrderResponse.class);
    	PayPalOrderResponse responseBody = response.getBody();
    	System.out.println("Order id: " + responseBody.getId());
    	System.out.println("Validated: " + responseBody.validate(orderId));
    }
}
