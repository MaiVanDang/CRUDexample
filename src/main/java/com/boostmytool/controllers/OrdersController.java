package com.boostmytool.controllers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
import com.boostmytool.model.products.Product;
import com.boostmytool.service.orders.OrdersRepository;
import com.boostmytool.services.products.ProductsRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/orders")
public class OrdersController {

	@Autowired
	private OrdersRepository repo;
	@Autowired
	private ProductsRepository repoP;

	@GetMapping({ "", "/" })
	public String showOrdersList(Model model) {
		List<Order> orders = repo.findAll(Sort.by(Sort.Direction.DESC, "orderID"));
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
	    if (repo.existsById(orderDto.getOrderID())) {
	        result.rejectValue("orderID", "error.orderDto", "Order ID already exists. Please try again.");
	    }

	    if (result.hasErrors()) {
	        return "admin/orders/CreateOrder";
	    }

	    LocalDate currentDate = LocalDate.now();
	    Date sqlDate = Date.valueOf(currentDate);
	    orderDto.setCreatedAt(sqlDate);
	    orderDto.setUpdatedAt(sqlDate);

	    // Cập nhật số lượng sản phẩm
	    int productID = Integer.parseInt(orderDto.getProductID().replaceAll("[^0-9]", ""));
	    Optional<Product> productOptional = repoP.findById(productID);
	    if (productOptional.isPresent()) {
	        Product product = productOptional.get();
	        
	        // Kiểm tra số lượng hàng
	        if (product.getQuantity() < orderDto.getQuantity()) {
	            result.reject("quantity", "Not enough stock available.");
	            return "admin/orders/CreateOrder";
	        }
	        
	        // Trừ số lượng hàng
	        product.setQuantity(product.getQuantity() - orderDto.getQuantity());
//	        System.out.println("productID: " + productID);
//	        System.out.println("Quantity in Order: " + orderDto.getQuantity());
//	        System.out.println("Quantity in Product: " + product.getQuantity());
//	        System.out.println("Updated Quantity in Product: " + (product.getQuantity() - orderDto.getQuantity()));
	        repoP.save(product); // Lưu thay đổi vào cơ sở dữ liệu
	        product = repoP.findById(productID).get();
//	        System.out.println("Quantity in Product: " + product.getQuantity());

	        // Tính giá trị đơn hàng
	        float discount = (float) product.getDiscount();
	        float quantity = (float) orderDto.getQuantity();
	        float price_product = (float) product.getPrice();
	        float price_order = price_product * quantity * (1 - discount / 100); // Tính giá sau khi giảm giá

	        // Tạo đối tượng Order
	        Order order = new Order();
	        order.setOrderID(orderDto.getOrderID());
	        order.setCustomerID(orderDto.getCustomerID());
	        order.setProductID(orderDto.getProductID());
	        order.setQuantity(orderDto.getQuantity());
	        order.setPrice(price_order);
	        order.setPromotion(orderDto.getPromotion());
	        order.setCreatedAt(sqlDate);
	        order.setUpdatedAt(sqlDate);
	        order.setEstimatedDeliveryDate(orderDto.getEstimatedDeliveryDate());
	        order.setPaymentMethod(orderDto.getPaymentMethod());
	        order.setPaymentStatus(orderDto.getPaymentStatus());
	        order.setOrderStatus(orderDto.getOrderStatus());
	        order.setNote(orderDto.getNote());
	        
	        // Lưu đơn hàng vào cơ sở dữ liệu
	        repo.save(order);
	    } else {
	        result.reject("product", "Product not found.");
	        return "admin/orders/CreateOrder";
	    }

	    return "redirect:/orders";
	}

	@GetMapping("/edit")
	public String showEditPage(Model model, @RequestParam String id) {

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

			model.addAttribute("orderDto", orderDto);
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/orders";
		}

		return "admin/orders/EditOrder";
	}

	@PostMapping("/edit")
	public String updateOrder(Model model, @RequestParam String id, @Valid @ModelAttribute OrderDto orderDto,
			BindingResult result) {

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

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/orders";
		}

		return "redirect:/orders";
	}

	@GetMapping("/delete")
	public String deleteProduct(@RequestParam String id) {
		try {
			Order order = repo.findById(id).get();

			// Delete the orders
			repo.delete(order);

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/orders";
		}
		return "redirect:/orders";
	}

}