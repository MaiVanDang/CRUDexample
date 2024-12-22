package com.boostmytool.service.customers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.boostmytool.model.customers.Customer;
import com.boostmytool.model.customers.CustomerDto;
import com.boostmytool.model.products.Product;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository repo;

	public String createCustomer(CustomerDto customerDto) {
		

		Customer customer = new Customer();
		customer.setCustomerName(customerDto.getCustomerName());
		customer.setCustomerDOB(customerDto.getCustomerDOB());
		customer.setCustomerGender(customerDto.getCustomerGender());
		customer.setCustomerAddress(customerDto.getCustomerAddress());
		customer.setCustomerPhone(customerDto.getCustomerPhone());
		customer.setCustomerEmail(customerDto.getCustomerEmail());
		customer.setCustomerDateCreated(customerDto.getCustomerDateCreated());

		customer.setCustomerDateUpdated(customerDto.getCustomerDateUpdated());

		repo.save(customer);

		return "redirect:/customers";
	}

	public String updateCustomer(CustomerDto customerDto, int id, Model model) {
		try {
			Customer customer = repo.findById(id).get();
			model.addAttribute("customer", customer);
			
			customer.setCustomerName(customerDto.getCustomerName());
			customer.setCustomerDOB(customerDto.getCustomerDOB());
			customer.setCustomerGender(customerDto.getCustomerGender());
			customer.setCustomerAddress(customerDto.getCustomerAddress());
			customer.setCustomerPhone(customerDto.getCustomerPhone());
			customer.setCustomerEmail(customerDto.getCustomerEmail());
			customer.setCustomerDateCreated(customerDto.getCustomerDateCreated());
			customer.setCustomerDateUpdated(customerDto.getCustomerDateUpdated());

			repo.save(customer);
		}
		catch(Exception ex){
			System.out.println("Exception: " + ex.getMessage());
		}
		
		return "redirect:/customers";
	}
	
	public String deleteCustomer(int id) {
		try {
			Customer product = repo.findById(id).get();
			repo.delete(product);
		}catch(Exception ex){
			System.out.println("Exception: " + ex.getMessage());
		}
		
		return "redirect:/customers";
	}
	
	public String searchCustomers(String keyword, Model model) {
        try {
            int number = Integer.parseInt(keyword);
            Optional<Customer> customerOptional = repo.findById(number);
            if (customerOptional.isPresent()) {
            	List<Customer> customers = List.of(customerOptional.get());
                model.addAttribute("customers", customers);
            } else {
                model.addAttribute("error", "Khánh hàng không tồn tại!");
                return "admin/Error";
            }
        } catch (NumberFormatException e) {
        	List<Customer> customers = repo.findByKeyword(keyword);
        	model.addAttribute("customers", customers);
        }
        
        model.addAttribute("keyword", keyword);
        return "admin/customers/SearchCustomer";
    }
}
