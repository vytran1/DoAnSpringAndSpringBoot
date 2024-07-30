package com.shopme.admin.order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import org.hibernate.internal.util.type.PrimitiveWrapperHelper.IntegerDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.OrderTrack;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.exception.OrderNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OrderController {
    private String defaultRedirectURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";
    
    @Autowired 
    private OrderService orderService;
    
    @Autowired
    private SettingService settingService;
    
    @GetMapping("/orders")
    public String listFirstPage() {
    	return defaultRedirectURL;
    }
    
    @GetMapping("/orders/page/{pageNum}")
    public String listByPage(
    		   @PagingAndSortingParam(listName = "listOrders",moduleURL = "/orders") PagingAndSortingHelper helper,
    		   @PathVariable("pageNum") int pageNum,
    		   HttpServletRequest request,
    		   @AuthenticationPrincipal ShopmeUserDetails loggedUser
    		) {
    	orderService.listByPage(pageNum, helper);
    	loadCurrencySetting(request);
    	
    	if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Salesperson") && loggedUser.hasRole("Shipper")) {
    		return "orders/orders_shipper";
    	}
    	
    	
    	return "orders/orders";
    }
    
    
    private void loadCurrencySetting(HttpServletRequest request) {
    	List<Setting> currencySettings = settingService.getCurrencySetting();
    	for(Setting setting : currencySettings) {
    		request.setAttribute(setting.getKey(),setting.getValue());
    	}
    }
    
    @GetMapping("/orders/detail/{orderID}")
    public String viewOrderDetails(@PathVariable("orderID") Integer orderID, Model model, 
    		RedirectAttributes ra,
    		HttpServletRequest request,
    		@AuthenticationPrincipal ShopmeUserDetails loggedUser) {
    	try {
    		Order order = orderService.get(orderID);
    		loadCurrencySetting(request);
    		
    		boolean isVisualForAdminOrSalesperson = false;
    		if(loggedUser.hasRole("Admin") || loggedUser.hasRole("Salesperson")) {
    			isVisualForAdminOrSalesperson = true;
    		}
    		model.addAttribute("order",order);
    		model.addAttribute("isVisualForAdminOrSalesperson",isVisualForAdminOrSalesperson);
    		return "orders/order_details_modal";
    	}catch(OrderNotFoundException ex) {
    		ra.addFlashAttribute("message",ex.getMessage());
    		return defaultRedirectURL;
    	}
    }
    
    @GetMapping("/orders/delete/{orderId}")
    public String deleteOrder(@PathVariable("orderId") Integer orderId, Model model, RedirectAttributes ra) {
    	try {
    		orderService.delete(orderId);
    		ra.addFlashAttribute("message","Order with ID "+orderId +" has been deleted successfully");
    	}catch(OrderNotFoundException ex) {
    		ra.addFlashAttribute("message",ex.getMessage());
    	}
    	return defaultRedirectURL;
    }
    
    @GetMapping("/orders/edit/{orderId}")
    public String editOrder(@PathVariable("orderId") Integer orderId,Model model,RedirectAttributes ra) {
    	try {
    		Order order = orderService.get(orderId);
    		List<Country> listCountries = orderService.listAllCountries();
    		
    		model.addAttribute("order",order);
    		model.addAttribute("listCountries",listCountries);
    		model.addAttribute("pageTitle","Edit order with id: " + orderId);
    		
    		return "orders/order_form";
    	}catch(OrderNotFoundException ex) {
    		ra.addFlashAttribute("message",ex.getMessage());
    		return defaultRedirectURL;
    	}
    }
    
    @PostMapping("/orders/save")
    public String saveOrder(Order order, HttpServletRequest request,RedirectAttributes ra) {
    	String countryName = request.getParameter("countryName");
    	order.setCountry(countryName);
    	
    	updateOrderDetails(order,request);
    	updateOrderTracks(order,request);
    	//System.out.println("Country: " + order.getCountry());
    	//System.out.println("Total: " + order.getTotal());
    	orderService.save(order);
    	ra.addFlashAttribute("message","Order" + order.getId() + " have been updated successfully");
    	return defaultRedirectURL;
    }

	private void updateOrderTracks(Order order, HttpServletRequest request) {
		// TODO Auto-generated method stub
		String[] trackIds = request.getParameterValues("trackId");
		String[] trackStatus = request.getParameterValues("trackStatus");
		String[] trackDates = request.getParameterValues("trackDate");
		String[] trackNotes = request.getParameterValues("trackNotes");
		
		List<OrderTrack> orderTracks = order.getOrderTracks();
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		for(int i = 0; i<trackIds.length;i++) {
			OrderTrack track = new OrderTrack();
			Integer trackId = Integer.parseInt(trackIds[i]);
			if(trackId > 0) {
				track.setId(trackId);
			}
			
			track.setOrder(order);
			track.setOrderStatus(OrderStatus.valueOf(trackStatus[i]));
			track.setNotes(trackNotes[i]);
			try {
				track.setUpdateTime(dateFormatter.parse(trackDates[i]));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			orderTracks.add(track);
		}
	}

	private void updateOrderDetails(Order order, HttpServletRequest request) {
		// TODO Auto-generated method stub
		String[] detailsId = request.getParameterValues("detailId");
		String[] productsId = request.getParameterValues("productId");
		String[] productCost = request.getParameterValues("productDetailCost");
		String[] quantities = request.getParameterValues("quantity");
		String[] productPrice = request.getParameterValues("productPrice");
		String[] productSubtotal = request.getParameterValues("productSubtotal");
		String[] productShipCost = request.getParameterValues("productShippingCost");
		
		Set<OrderDetail> orderDetails = order.getOrderDetails();
		for(int i =0; i<detailsId.length; i++) {
			System.out.println("Detail Id: " + detailsId[i]);
			System.out.println("Product Id: " + productsId[i]);
			System.out.println("Product Cost: " + productCost[i]);
			System.out.println("Quantities: " + quantities[i]);
			System.out.println("Product Price: " + productPrice[i]);
			System.out.println("Product Subtotal" + productSubtotal[i]);
			System.out.println("Ship cost: " + productShipCost[i]);
			
			OrderDetail orderDetail = new OrderDetail();
			Integer detailId = Integer.parseInt(detailsId[i]);
			if(detailId > 0) {
			     orderDetail.setId(detailId);
			}
			orderDetail.setOrder(order);
			orderDetail.setProduct(new Product(Integer.parseInt(productsId[i])));
			orderDetail.setProductCost(Float.parseFloat(productCost[i]));
			orderDetail.setSubtotal(Float.parseFloat(productSubtotal[i]));
			orderDetail.setShippingCost(Float.parseFloat(productShipCost[i]));
			orderDetail.setQuantity(Integer.parseInt(quantities[i]));
			orderDetail.setUnitPrice(Float.parseFloat(productPrice[i]));
			orderDetails.add(orderDetail);
			
			
		}
		
		
		
	}
}
