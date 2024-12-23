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
import com.boostmytool.service.orders.OrdersService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/orders")
public class OrdersController {

	@Autowired
	private OrdersRepository repo;
	@Autowired
	
	private OrdersService ordersService;

	@GetMapping({ "", "/" })
	public String showOrdersList(Model model) {
		List<Order> orders = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("orders", orders);
		return "admin/orders/index";
	}

	@GetMapping({ "/create" })
	public String showCreateOrderPage(Model model) {
		OrderDto orderDto = new OrderDto();
		model.addAttribute("orderDto", orderDto);
		return "admin/orders/CreateOrder";
	}

	@PostMapping("/create")
	public String createOrder(@Valid @ModelAttribute OrderDto orderDto, BindingResult result) {
		if (result.hasErrors()) {
			return "admin/orders/CreateOrder";
		}

		return ordersService.createNewOrder(orderDto, result);
	}

	@GetMapping("/edit")
	public String showEditPage(Model model, @RequestParam int id) {

		try {
			Order order = repo.findById(id).get();
			model.addAttribute("order", order);

			OrderDto orderDto = new OrderDto();
			orderDto.setCustomerID(order.getCustomerID());
			orderDto.setProductID(order.getProductID());
			orderDto.setQuantity(order.getQuantity());
			orderDto.setPrice(order.getPrice());
			orderDto.setCost(order.getCost());
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

			model.addAttribute("orderDto", orderDto);
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/orders";
		}

		return "admin/orders/EditOrder";
	}

	@PostMapping("/edit")
	public String updateOrder(Model model, @RequestParam int id, @Valid @ModelAttribute OrderDto orderDto,
			BindingResult result) {

		try {
			Order order = repo.findById(id).get();
			model.addAttribute("order", order);

			if (result.hasErrors()) {
				return "admin/orders/EditOrder";
			}

			ordersService.editOrder(orderDto, order);

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/orders";
		}

		return "redirect:/orders";
	}

	@GetMapping("/delete")
	public String deleteOrder(@RequestParam int id) {
		try {

			// xoa order
			ordersService.deleteOrders(id);

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/orders";
		}
		return "redirect:/orders";
	}

	@GetMapping("/search")
	public String searchOrder(@RequestParam("keyword") String keyword, Model model) {
		return ordersService.searchOrders(keyword, model);
	}

	@GetMapping("/report")
	public String viewOrderReport(
			@RequestParam(value = "statType", required = false, defaultValue = "") String statType, Model model) {

		return ordersService.reportOrders(statType, model);
	}

}