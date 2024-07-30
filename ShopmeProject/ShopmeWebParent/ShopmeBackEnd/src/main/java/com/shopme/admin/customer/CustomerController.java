package com.shopme.admin.customer;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.user.export.CustomerCsvExporter;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.User;
import com.shopme.common.exception.CanNotDeleteException;
import com.shopme.common.exception.CustomerNotFoundException;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CustomerController {
    
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/customers")
	public String listFirstPage(Model model) {
		//model,pageNum,sortField,sortDir,keyWord
		return listByPage(model,1,"firstName","asc",null);
	}
	
	
	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(Model model,
			@PathVariable("pageNum") int pageNum,
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			@RequestParam("keyWord") String keyWord
			) {
		Page<Customer> page = customerService.listByPage(pageNum, sortField, sortDir, keyWord);
		List<Customer> listCustomers = page.getContent();
		long startCount = (pageNum-1) * customerService.CUSTOMER_PER_PAGE ;
	 	long endCount = startCount + customerService.CUSTOMER_PER_PAGE -1 ;
	 	if(endCount > page.getTotalElements() ) {
	 		   endCount = page.getTotalElements();
	 	}
	 	String reverseSorDir = sortDir.equals("asc") ? "desc" : "asc";
	 	model.addAttribute("totalPages",page.getTotalPages());
	 	model.addAttribute("totalItems",page.getTotalElements());
	 	model.addAttribute("currentPage",pageNum);
	 	model.addAttribute("sortField",sortField);
	 	model.addAttribute("sortDir",sortDir);
	 	model.addAttribute("keyWord",keyWord);
	 	model.addAttribute("listCustomers",listCustomers );
	 	model.addAttribute("reverseSortDir",reverseSorDir);
	 	model.addAttribute("startCount",startCount);
	 	model.addAttribute("endCount",endCount);
	 	model.addAttribute("moduleURL","/customers");
	 	return "customers/customers";
	}
	
	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomerEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean status,RedirectAttributes ra) {
		customerService.updateCustomerEnabledStatus(id, status);
		String statusMessage = status ? "enabled" : "disabled";
		String message = "Customer with ID:" + id + "has benn " + statusMessage;
		ra.addFlashAttribute("message",message);
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable("id") Integer id,Model model,RedirectAttributes ra) {
		try {
			Customer customerById = customerService.getByID(id);
			List<Country> countries = customerService.listAllCountries(); 
			model.addAttribute("customer",customerById);
			model.addAttribute("listCountries",countries);
			model.addAttribute("pageTitle","Edit user with ID: " +id);
			return "customers/customer_form";
		}catch(CustomerNotFoundException ex) {
			ra.addFlashAttribute("message",ex.getMessage());
			return "redirect:/customers";
		}
	}
	
	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer, Model model, RedirectAttributes ra) {
		customerService.saveCustomer(customer);
		ra.addFlashAttribute("message", "The customer with ID:  " + customer.getId()+"have been updated successfully");
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer(@PathVariable("id") Integer id, RedirectAttributes ra) {
		try {
			customerService.delete(id);
			ra.addFlashAttribute("message","The Customer with ID: " + id + "have been deleted successfully");
		}catch(CustomerNotFoundException ex) {
			ra.addFlashAttribute("message",ex.getMessage());
		} catch (CanNotDeleteException e) {
			// TODO Auto-generated catch block
			ra.addFlashAttribute("message",e.getMessage());
		}
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/detail/{id}")
	public String viewCustomer(@PathVariable("id") Integer id ,Model model, RedirectAttributes ra) {
		try {
			Customer customer = customerService.getByID(id);
			model.addAttribute("customer",customer);
			return "customers/customer_detail_modal";
		}catch(CustomerNotFoundException ex) {
			ra.addFlashAttribute("message",ex.getMessage());
			return "redirect:/customers";
		}
	}
	
	@GetMapping("/customers/export/csv")
	 public void exportToCSV(HttpServletResponse response) throws IOException {
		 List<Customer> listUser =  customerService.findAll();
		 CustomerCsvExporter exporter = new CustomerCsvExporter();
		 exporter.export(listUser, response);
	 }
}
