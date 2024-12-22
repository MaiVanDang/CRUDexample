package com.boostmytool.controllers;

import java.sql.Date;
import java.time.LocalDate;
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

import com.boostmytool.model.person.Customer;
import com.boostmytool.model.person.CustomerDto;
import com.boostmytool.service.customers.CustomerRepository;
import com.boostmytool.service.customers.CustomerService;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/customers")
public class CustomersController {
	
	@Autowired
	private CustomerRepository repo;
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping({"", "/"})
	public String showCustomerList(Model model) {
		List<Customer> customers = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("customers", customers);
		return "admin/customers/index";
	}
	
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		CustomerDto customerDto = new CustomerDto();
		
		LocalDate currentDate = LocalDate.now();
	    Date sqlDate = Date.valueOf(currentDate);
	    customerDto.setCustomerDateCreated(sqlDate);    
	    customerDto.setCustomerDateUpdated(sqlDate);   
		
		model.addAttribute("customerDto", customerDto);
		return "admin/customers/CreateCustomer";
	}
	
	@PostMapping("/create")
	public String createCustomer(
			@Valid @ModelAttribute CustomerDto customerDto,
			BindingResult result
			) {
		if (result.hasErrors()) {
			return "admin/customers/CreateCustomer";
		}
		return customerService.createCustomer(customerDto);
	}
	
	@GetMapping("/edit")
	public String showEditPage(
			Model model,
			@RequestParam int id) {
		
		try {
			Customer customer = repo.findById(id).get();
			model.addAttribute("customer", customer);
			
			CustomerDto customerDto = new CustomerDto(); 
			customerDto.setCustomerName(customer.getName());
			customerDto.setCustomerDOB(customer.getDOB());
			customerDto.setCustomerGender(customer.getGender());
			customerDto.setCustomerAddress(customer.getAddress());
			customerDto.setCustomerPhone(customer.getPhone());
			customerDto.setCustomerEmail(customer.getEmail());
			customerDto.setCustomerDateCreated(customer.getCustomerDateCreated());
			
			
			LocalDate currentDate = LocalDate.now();
		    Date sqlDate = Date.valueOf(currentDate);    
		    customerDto.setCustomerDateUpdated(sqlDate);
		
			model.addAttribute("customerDto",customerDto);
		}
		catch(Exception ex){
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/customers";
		}
			
		
		return "admin/customers/EditCustomer";
	}	
	
	@PostMapping("/edit")
	public String updateCustomer(
			Model model,
			@RequestParam int id,
			@Valid @ModelAttribute CustomerDto customerDto,
			BindingResult result
			) {
		if(result.hasErrors()) {
			return "admin/customers/EditCustomer";
		}
		
		return customerService.updateCustomer(customerDto, id, model);
	}	
	
	@GetMapping("/delete")
	public String deleteCustomer(
			@RequestParam int id
			) {
		return customerService.deleteCustomer(id);
	}	

	
	@GetMapping("/search")
	public String searchCustomers(@RequestParam("keyword") String keyword, Model model) {
	    return customerService.searchCustomers(keyword, model); // Tên file HTML
	}
	
	@GetMapping("/report")
	public String viewCustomerReport(
	        @RequestParam(value = "statType", required = false, defaultValue = "") String statType,
	        Model model) {
	    
	    List<Object[]> chartData;
	    switch (statType) {
	        case "ageGroup":
	            chartData = repo.findStatisticsByAgeGroup();
	            break;
	        case "customerAddress":
	            chartData = repo.findStatisticsByLocation();
	            break;
	        case "newCustomer":
	            chartData = repo.findStatisticsByNewCustomers();
	            break;
	        default:
	            chartData = null; // Trả về danh sách rỗng
	            break;
	    }
	    
	    model.addAttribute("chartData", chartData);
	    model.addAttribute("statType", statType);
	    return "admin/customers/ReportCustomer";
	}
	
}