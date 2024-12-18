package com.boostmytool.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.boostmytool.model.customers.Customer;
import com.boostmytool.model.customers.CustomerDto;
import com.boostmytool.services.customers.CustomerRepository;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/customers")
public class CustomersController {
	
	@Autowired
	private CustomerRepository repo;
	
	@GetMapping({"", "/"})
	public String showCustomerList(Model model) {
		List<Customer> customers = repo.findAll(Sort.by(Sort.Direction.DESC, "customerID"));
		model.addAttribute("customers", customers);
		return "admin/customers/index";
	}
	
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		CustomerDto customerDto = new CustomerDto();
		model.addAttribute("customerDto", customerDto);
		return "customers/CreateCustomer";
	}
	
	@PostMapping("/create")
	public String createCustomer(
			@Valid @ModelAttribute CustomerDto customerDto,
			BindingResult result
			) {
		
		if(result.hasErrors()) {
			return "customers/CreateCustomer";
		}
		
		Customer customer = new Customer(); 
		customer.setCustomerID(customerDto.getCustomerID());
		customer.setCustomerName(customerDto.getCustomerName());
		customer.setCustomerDOB(customerDto.getCustomerDOB());
		customer.setCustomerGender(customerDto.getCustomerGender());
		customer.setCustomerAddress(customerDto.getCustomerAddress());
		customer.setCustomerPhone(customerDto.getCustomerPhone());
		customer.setCustomerEmail(customerDto.getCustomerEmail());
		customer.setCustomerDateCreated(customerDto.getCustomerDateCreated());
		customer.setCustomerDateUpdated(customerDto.getCustomerDateUpdated());
		customer.setCustomerPaidAmount(customerDto.getCustomerPaidAmount());
		customer.setCustomerSumDebt(customerDto.getCustomerSumDebt());
		customer.setCustomerType(customerDto.getCustomerType());
		
		repo.save(customer);
		
		return "redirect:/customers";
	}
	
	@GetMapping("/edit")
	public String showEditPage(
			Model model,
			@RequestParam String id) {
		
		try {
			Customer customer = repo.findById(id).get();
			model.addAttribute("customer", customer);
			
			CustomerDto customerDto = new CustomerDto(); 
			customerDto.setCustomerID(customer.getCustomerID());
			customerDto.setCustomerName(customer.getCustomerName());
			customerDto.setCustomerDOB(customer.getCustomerDOB());
			customerDto.setCustomerGender(customer.getCustomerGender());
			customerDto.setCustomerAddress(customer.getCustomerAddress());
			customerDto.setCustomerPhone(customer.getCustomerPhone());
			customerDto.setCustomerEmail(customer.getCustomerEmail());
			customerDto.setCustomerDateCreated(customer.getCustomerDateCreated());
			customerDto.setCustomerDateUpdated(customer.getCustomerDateUpdated());
			customerDto.setCustomerPaidAmount(customer.getCustomerPaidAmount());
			customerDto.setCustomerSumDebt(customer.getCustomerSumDebt());
			customerDto.setCustomerType(customer.getCustomerType());
			
			model.addAttribute("customerDto",customerDto);
		}
		catch(Exception ex){
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/customers";
		}
			
		
		return "customers/EditCustomer";
	}	
	
	@PostMapping("/edit")
	public String updateCustomer(
			Model model,
			@RequestParam String id,
			@Valid @ModelAttribute CustomerDto customerDto,
			BindingResult result
			) {
		
		try {
			Customer customer = repo.findById(id).get();
			model.addAttribute("customer", customer);
			
			if(result.hasErrors()) {
				return "customers/EditCustomer";
			}
			
			customer.setCustomerID(customerDto.getCustomerID());
			customer.setCustomerName(customerDto.getCustomerName());
			customer.setCustomerDOB(customerDto.getCustomerDOB());
			customer.setCustomerGender(customerDto.getCustomerGender());
			customer.setCustomerAddress(customerDto.getCustomerAddress());
			customer.setCustomerPhone(customerDto.getCustomerPhone());
			customer.setCustomerEmail(customerDto.getCustomerEmail());
			customer.setCustomerDateCreated(customerDto.getCustomerDateCreated());
			customer.setCustomerDateUpdated(customerDto.getCustomerDateUpdated());
			customer.setCustomerPaidAmount(customerDto.getCustomerPaidAmount());
			customer.setCustomerSumDebt(customerDto.getCustomerSumDebt());
			customer.setCustomerType(customerDto.getCustomerType());
			
			repo.save(customer);
		}
		catch(Exception ex){
			System.out.println("Exception: " + ex.getMessage());
		}
		
		return "redirect:/customers";
	}	
	
	@GetMapping("/delete")
	public String deleteCustomer(
			@RequestParam String id
			) {
		
		try {
			Customer product = repo.findById(id).get();
			
			//delete the product
			repo.delete(product);
		}catch(Exception ex){
			System.out.println("Exception: " + ex.getMessage());
		}
		
		return "redirect:/customers";
	}	
		
}
