package com.boostmytool.service.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;


import com.boostmytool.model.orders.Order;
import com.boostmytool.model.orders.OrderDto;
import com.boostmytool.model.products.Product;

import com.boostmytool.service.FileStorageService;
import com.boostmytool.service.products.ProductsRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository repo;
    @Autowired
	private ProductsRepository repoP;
    
    @Autowired
    private FileStorageService fileStorageService;

    public String createNewOrder(OrderDto orderDto, BindingResult result){
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
			order.setCreatedAt(orderDto.getCreatedAt());
			order.setUpdatedAt(orderDto.getUpdatedAt());
			order.setEstimatedDeliveryDate(orderDto.getEstimatedDeliveryDate());
			order.setPaymentMethod(orderDto.getPaymentMethod());
			order.setPaymentStatus(orderDto.getPaymentStatus());
			order.setOrderStatus(orderDto.getOrderStatus());
			order.setNote(orderDto.getNote());

			repo.save(order);
		} else {
			result.reject("product", "Product not found.");
			return "admin/orders/CreateOrder";
		}
		return "redirect:/orders";
	}

    public Order editOrder(String id, OrderDto orderDto, Order order) {
    	
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

		return repo.save(order);
    }
    
    public void deleteOrders(String id) {
    	Order order = repo.findById(id).get();
    	// Delete the orders
    	repo.delete(order);
    }
    
    public String searchOrders(String keyword, Model model) {
    	List<Order> orders = repo.findByKeyword(keyword);
		model.addAttribute("orders", orders);
		model.addAttribute("keyword", keyword); // Truyền từ khóa về view
		return "admin/orders/SearchOrder"; // Tên file HTML
	}
    
    public String reportOrders(String statType, Model model) {
    	List<Object[]> chartData;
		switch (statType) {
		case "reneuveDay":
			chartData = repo.findTotalValueByDay();
			break;
		case "reneuveMonth":
			chartData = repo.findTotalValueByMonth();
			break;
		case "reneuveYear":
			chartData = repo.findTotalValueByYear();
			break;
		case "quantityDay":
			chartData = repo.findOrderCountByDay();
			break;
		case "quantityMonth":
			chartData = repo.findOrderCountByMonth();
			break;
		case "quantityYear":
			chartData = repo.findOrderCountByYear();
			break;
		default:
			chartData = null;
		}

		model.addAttribute("chartData", chartData);
		model.addAttribute("statType", statType);
		return "admin/orders/ReportOrder";
	}
}