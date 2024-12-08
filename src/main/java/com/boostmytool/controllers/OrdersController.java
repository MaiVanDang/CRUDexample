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
import com.boostmytool.model.orders.Order;
import com.boostmytool.model.orders.OrderDto;
import com.boostmytool.service.orders.OrdersRepository;

import jakarta.validation.Valid;
@Controller
@RequestMapping("/orders")
public class OrdersController {
	
	@Autowired
	private OrdersRepository repo;
	
	 @GetMapping({"", "/"})
	    public String showOrdersList(Model model) {
	    	List<Order> orders = repo.findAll(Sort.by(Sort.Direction.DESC, "orderID"));
	        model.addAttribute("orders", orders);
	        return "admin/orders/index";
	    }
	 
	 @GetMapping({"/create"})
		public String showCreateOrderPage(Model model) {
			OrderDto orderDto = new OrderDto();
			model.addAttribute("orderDto", orderDto);
			return "admin/orders/CreateOrder";
		}
	 @PostMapping("/create")
		public String createOrder(
				@Valid @ModelAttribute OrderDto orderDto,
				BindingResult result
				) {
			
		    if (repo.existsById(orderDto.getOrderID())) {
		        result.rejectValue("orderID", "error.orderDto", "Order ID is exist. Plese try again");
		    }		 
		 
			if (result.hasErrors()) {
				return "admin/orders/CreateOrder";
			}
			
			LocalDate currentDate = LocalDate.now();
		    Date sqlDate = Date.valueOf(currentDate);
		    orderDto.setCreatedAt(sqlDate);    
		    orderDto.setUpdatedAt(sqlDate);
			
			Order order = new Order();
			order.setOrderID(orderDto.getOrderID());
			order.setCustomerID(orderDto.getCustomerID());
			order.setProductID(orderDto.getProductID());
			order.setQuantity(orderDto.getQuantity());
			order.setPrice(orderDto.getPrice());
			order.setPromotion(orderDto.getPromotion());
			order.setCreatedAt(orderDto.getCreatedAt());
			order.setUpdatedAt(orderDto.getUpdatedAt());
			order.setEstimatedDeliveryDate(orderDto.getEstimatedDeliveryDate());
			order.setPaymentMethod(orderDto.getPaymentMethod());
			order.setPaymentStatus(orderDto.getPaymentStatus());
			order.setOrderStatus(orderDto.getOrderStatus());
			order.setNote(orderDto.getNote());
			
			repo.save(order);
			
			
			return "redirect:/orders";
	 	}
	 @GetMapping("/edit")
		public String showEditPage(
					Model model,
					@RequestParam String id
				) {
			
			try {
				Order order = repo.findById(id).get();
				model.addAttribute("order", order);
				
				OrderDto orderDto = new OrderDto();
				orderDto.setOrderID(order.getOrderID());
				orderDto.setCustomerID(order.getCustomerID());
				orderDto.setProductID(order.getProductID());
				orderDto.setQuantity(order.getQuantity());
				orderDto.setPrice(order.getPrice());
				orderDto.setPromotion(order.getPromotion());
				orderDto.setCreatedAt(order.getCreatedAt());
				
				LocalDate currentDate = LocalDate.now();
			    Date sqlDate = Date.valueOf(currentDate);    
			    orderDto.setUpdatedAt(sqlDate);				
				
				orderDto.setEstimatedDeliveryDate(order.getEstimatedDeliveryDate());
				orderDto.setPaymentMethod(order.getPaymentMethod());
				orderDto.setPaymentStatus(order.getPaymentStatus());
				orderDto.setOrderStatus(order.getOrderStatus());
				orderDto.setNote(order.getNote());
				
				model.addAttribute("orderDto",orderDto);
			}
			catch(Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
				return "redirect:/orders";
			}
			
			return "admin/orders/EditOrder";
		}
	 @PostMapping("/edit")
		public String updateOrder(
				Model model,
				@RequestParam String id,
				@Valid @ModelAttribute OrderDto orderDto,
				BindingResult result
				) {
			
			try {
				Order order = repo.findById(id).get();
				model.addAttribute("order", order);
				
				if (result.hasErrors()) {
					return "admin/orders/EditOrder";
				}
				
				LocalDate currentDate = LocalDate.now();
			    Date sqlDate = Date.valueOf(currentDate); 
			    orderDto.setUpdatedAt(sqlDate);
			    
				order.setCustomerID(orderDto.getCustomerID());
				order.setProductID(orderDto.getProductID());
				order.setQuantity(orderDto.getQuantity());
				order.setPrice(orderDto.getPrice());
				order.setPromotion(orderDto.getPromotion());
				order.setUpdatedAt(orderDto.getUpdatedAt());
				order.setEstimatedDeliveryDate(orderDto.getEstimatedDeliveryDate());
				order.setPaymentMethod(orderDto.getPaymentMethod());
				order.setPaymentStatus(orderDto.getPaymentStatus());
				order.setOrderStatus(orderDto.getOrderStatus());
				order.setNote(orderDto.getNote());
				
				repo.save(order);
				
			}
			catch(Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
				return "redirect:/orders";
			}
			
			return "redirect:/orders";
		}	
	 
	 @GetMapping("/delete")
	 public String deleteProduct(
			 @RequestParam String id
			 ) {
		 try {
			 Order order = repo.findById(id).get();
			 
			 //Delete the orders
			 repo.delete(order);
			 
		 }
		 catch(Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
				return "redirect:/orders";
			}
		 return "redirect:/orders";
	 }
	 
	 
	 
}