package com.boostmytool.services.orders;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.boostmytool.model.orders.Order;

@Service
public class OrderService {
	
	private OrdersRepository repo;
	
	public String searchByID(Iterable<String> id, Model model) {
		List<Order> orders = repo.findAllById(id);
		model.addAttribute("orders",orders);
		return "admin/orders/showListProduct";
	}
	
}
