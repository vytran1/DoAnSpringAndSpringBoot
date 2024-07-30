package com.shopme.admin.shippingrate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.ShippingRateAlreadyExistsException;
import com.shopme.common.exception.ShippingRateNotFoundException;

@Controller
public class ShippingRateController {
	private String defaultRedirectURL = "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";
	@Autowired
	private ShippingRateService shippingRateService;
	
	@GetMapping("/shipping_rates/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(listName = "shippingRates", moduleURL = "/shipping_rates") PagingAndSortingHelper helper,
			    @PathVariable("pageNum") Integer pageNum
			){
		
		shippingRateService.listByPage(pageNum, helper);
		return "shipping_rates/shipping_rate";
	}
	
	@GetMapping("/shipping_rates")
	public String listFirstPage() {
		return defaultRedirectURL;
	}
	
	@GetMapping("/shipping_rates/new")
	public String viewForm(Model model) {
		List<Country> listCountries = shippingRateService.listCountries();
		ShippingRate shippingRate = new ShippingRate();
		model.addAttribute("shippingRate",shippingRate);
		model.addAttribute("listCountries",listCountries);
		model.addAttribute("pageTitle","new Rate");
		
		return "shipping_rates/shipping_rate_form";
	}
	
	@PostMapping("/shipping_rates/save")
	public String save(ShippingRate shippingRate,RedirectAttributes ra) {
		try {
			shippingRateService.save(shippingRate);
			ra.addFlashAttribute("message","The shipping rate has been save successfully");
		} catch (ShippingRateAlreadyExistsException e) {
			// TODO Auto-generated catch block
			ra.addFlashAttribute("message",e.getMessage());
		}
		return defaultRedirectURL;
	}
	
	@GetMapping("/shipping_rates/edit/{id}")
	public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			ShippingRate shippingRate = shippingRateService.getById(id);
			List<Country> listCountries = shippingRateService.listCountries();
			
			model.addAttribute("listCountries",listCountries);
			model.addAttribute("shippingRate",shippingRate);
			model.addAttribute("pageTitle","Edit ShippingRate With ID: " + id);
			
			return "shipping_rates/shipping_rate_form";
		} catch (ShippingRateNotFoundException e) {
			// TODO Auto-generated catch block
			ra.addFlashAttribute("message",e.getMessage());
			return "redirect:/shipping_rates";
		}
	}
	
	@GetMapping("/shipping_rates/code/{id}/enabled/{supported}")
	public String updateCODStatus(@PathVariable("id") Integer id, @PathVariable("supported") boolean supported, Model model, RedirectAttributes ra) {
		try {
			shippingRateService.updateCODStatus(id, supported);
			ra.addFlashAttribute("message","COD suppor with shipping rate ID " + id + "has been updated");
		} catch (ShippingRateNotFoundException e) {
			// TODO Auto-generated catch block
			ra.addFlashAttribute("message",e.getMessage());
		}
		return defaultRedirectURL;
	}
	
	@GetMapping("/shipping_rates/delete/{id}")
	public String deleteShippingRate(@PathVariable("id") Integer id,RedirectAttributes ra) {
		try {
			shippingRateService.delete(id);
			ra.addFlashAttribute("message","The shipping rate with id" + id +"has been deleted successfully");
		} catch (ShippingRateNotFoundException e) {
			// TODO Auto-generated catch block
			ra.addFlashAttribute("message", e.getMessage());
		}
		return defaultRedirectURL;
	}
}
