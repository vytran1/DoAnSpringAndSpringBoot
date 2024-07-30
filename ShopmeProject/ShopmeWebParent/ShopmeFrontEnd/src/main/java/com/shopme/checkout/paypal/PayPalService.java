package com.shopme.checkout.paypal;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.shopme.setting.PaymentSettingBag;
import com.shopme.setting.SettingService;

@Component
public class PayPalService {
    @Autowired
    private SettingService settingService;
    
    //@Autowired
    //private RestTemplate restTemplate;
    private static final String GET_ORDER_URL = "/v2/checkout/orders/";
    
    public boolean validate(String orderId) throws PayPalApiException {
    	
    	PayPalOrderResponse responseBody = getOrderDetails(orderId);
    	return responseBody.validate(orderId);
    }

	private PayPalOrderResponse getOrderDetails(String orderId) throws PayPalApiException {
		ResponseEntity<PayPalOrderResponse> response = makeRequest(orderId);
    	
    	HttpStatus status = (HttpStatus)response.getStatusCode();
    	if(!status.equals(HttpStatus.OK)) {
    		throwExceptionForNoneOkResponse(status);
    	}
    	PayPalOrderResponse responseBody = response.getBody();
		return responseBody;
	}

	private ResponseEntity<PayPalOrderResponse> makeRequest(String orderId) {
		PaymentSettingBag paymentSettingBag = settingService.getPaymentSettingBag();
    	String baseURL = paymentSettingBag.getURL();
    	String requestURL = baseURL + GET_ORDER_URL + orderId;
    	String clientId = paymentSettingBag.getClientId();
    	String clientSecret = paymentSettingBag.getClientSecret();
    	
    	HttpHeaders header = new HttpHeaders();
    	header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    	header.add("Accept-language","en_US");
    	header.setBasicAuth(clientId,clientSecret);
    	
    	HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(header);
    	
    	RestTemplate restTemplate = new RestTemplate();
    	
    	ResponseEntity<PayPalOrderResponse> response =  restTemplate.exchange(requestURL,HttpMethod.GET,request,PayPalOrderResponse.class);
		return response;
	}

	private void throwExceptionForNoneOkResponse(HttpStatus status) throws PayPalApiException {
		String message = null;
		switch(status) {
		       case NOT_FOUND:{
			       message ="Order id not found";
		       }
		       case BAD_REQUEST:{
		    	   message ="Bad request to Paypal checkout API";
		       }
		       case INTERNAL_SERVER_ERROR:{
		    	   message ="Paypal Server Error";
		       }
		       default: {
			       message ="PayPal returned non-OK status code";
		       }
		       
		}
		throw new PayPalApiException(message);
	}
}
