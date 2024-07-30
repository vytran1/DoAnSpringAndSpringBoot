package com.shopme.checkout;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.checkout.paypal.PayPalApiException;
import com.shopme.checkout.paypal.PayPalService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.customer.CustomerService;
import com.shopme.order.OrderService;
import com.shopme.setting.CurrencySettingBag;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.PaymentSettingBag;
import com.shopme.setting.SettingService;
import com.shopme.shipping.ShippingRateService;
import com.shopme.shoppingcart.ShoppingCartService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CheckoutController {
     @Autowired
     private CheckoutService checkoutService;
     
     @Autowired
     private CustomerService customerService;
     
     @Autowired
     private AddressService addressService;
     
     @Autowired
     private ShippingRateService shippingRateService;
     
     
     @Autowired
     private ShoppingCartService shoppingCartService;
     
     @Autowired
     private OrderService orderService;
     
     @Autowired
     private SettingService settingService;
     
     @Autowired
     private PayPalService payPalService;
     
     @GetMapping("/checkout")
     public String showCheckoutPage(Model model, HttpServletRequest request) {
    	Customer customer = getAuthenticatedCustomer(request);
     	 
    	Address defaultAddressCustomer = addressService.getDefaultShippingAddress(customer);
    	ShippingRate shippingRate = null;
    	
    	if(defaultAddressCustomer != null) {
    		model.addAttribute("shippingAddress",defaultAddressCustomer.toString());
    		shippingRate = shippingRateService.getShippingRateForAddress(defaultAddressCustomer);
    	}
    	else {
    		model.addAttribute("shippingAddress",customer.getAddress());
    		shippingRate = shippingRateService.getShippingRateForCustomer(customer);
        }
     	
    	if(shippingRate == null) {
    		return "redirect:/cart";
    	}
    	
    	List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
    	CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
    	System.out.println(checkoutInfo.getDeliverDate());
    	
    	String currencyCode = settingService.getCurrencyCode();
    	PaymentSettingBag paymentSettingBag =  settingService.getPaymentSettingBag();
    	String paypalClientId = paymentSettingBag.getClientId();
    	
    	//Setup for paypal
    	model.addAttribute("currencyCode",currencyCode);
    	model.addAttribute("customer",customer);
    	model.addAttribute("paypalClientId", paypalClientId);
    	//****
    	
    	model.addAttribute("checkoutInfo",checkoutInfo);
    	model.addAttribute("cartItems",cartItems);
    	
    	return "checkout/checkout";
     }
     
     private Customer getAuthenticatedCustomer(HttpServletRequest request)  {
    	 String customerEmail = Utility.getEmailOfAuthenticationCustomer(request);
    	 return customerService.getCustomerByEmail(customerEmail);
     }
     
     @PostMapping("/place_order")
     public String placeOrder(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
    	String paymentMethod = request.getParameter("paymentMethod");
    	System.out.println(paymentMethod);
    	PaymentMethod paymentMethod2 = PaymentMethod.valueOf(paymentMethod);
    	Customer customer = getAuthenticatedCustomer(request);
     	 
     	Address defaultAddressCustomer = addressService.getDefaultShippingAddress(customer);
     	ShippingRate shippingRate = null;
     	
     	if(defaultAddressCustomer != null) {
     		
     		shippingRate = shippingRateService.getShippingRateForAddress(defaultAddressCustomer);
     	}
     	else {
     		
     		shippingRate = shippingRateService.getShippingRateForCustomer(customer);
         }
      	
     	
     	
     	List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
     	CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
     	
     	Order order =  orderService.createOrder(customer, defaultAddressCustomer, cartItems, paymentMethod2, checkoutInfo);
        shoppingCartService.deleteByCustomer(customer);
        sendOrderConfirmationMail(request,order);
        
    	 return "checkout/order_completed";
     }

	private void sendOrderConfirmationMail(HttpServletRequest request, Order order) throws MessagingException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		EmailSettingBag emailSettingBag = settingService.getEmailSettingBag();
		JavaMailSenderImpl senderImpl = Utility.preJavaMailSenderImpl(emailSettingBag);
		senderImpl.setDefaultEncoding("utf-8");
		String toAddessString = order.getCustomer().getEmail();
		String subject = emailSettingBag.getOrderConfirmationSubject();
		String content = emailSettingBag.getOrderConfirmationContent();
		
		subject.replace("[[orderId",String.valueOf(order.getId()));
		
		MimeMessage message =  senderImpl.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		
		helper.setFrom(emailSettingBag.getFromAddress(),emailSettingBag.getSenderName());
		helper.setTo(toAddessString);
		helper.setSubject(subject);
		
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss E, dd MM yyyy ");
		String orderTime = dateFormat.format(order.getOrderTime());
		
		CurrencySettingBag currencySettingBag = settingService.getCurrencySetting();
		String totalAmount = Utility.formatCurrency(order.getTotal(),currencySettingBag);
		
		
		content = content.replace("[[name]]",order.getCustomer().getFullName());
		content = content.replace("[[orderId]]",String.valueOf(order.getId()));
		content = content.replace("[[orderTime]]",orderTime);
		content = content.replace("[[shippingAddress",order.getShippingAddress());
		content = content.replace("[[total]]",totalAmount);
		content = content.replace("[[paymentMethod]]",order.getPaymentMethod().toString());
		
	    helper.setText(content,true);
	    senderImpl.send(message);
	}
	
	@PostMapping("/process_paypal_order")
	public String processPayPalOrder(HttpServletRequest request, Model model) throws UnsupportedEncodingException, MessagingException {
		String orderId = request.getParameter("orderId");
		String pageTitle = "Checkout Failure";
		String message = null;
		try {
			if(payPalService.validate(orderId)) {
				return placeOrder(request);
			}else {
				pageTitle = "Checkout Failure";
				message = "Error Transaction could not be completed because order information is invalid";
			}
		}catch (PayPalApiException e) {
			// TODO Auto-generated catch block
			message = "Error: Transaction due to error: " + e.getMessage();
			
		}
		model.addAttribute("title",pageTitle);
		model.addAttribute("message",message);
		return "message";
	}
}
