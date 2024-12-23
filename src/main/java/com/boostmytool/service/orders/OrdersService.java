package com.boostmytool.service.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;


import com.boostmytool.model.orders.Order;
import com.boostmytool.model.orders.OrderDto;
import com.boostmytool.model.products.Product;

import com.boostmytool.service.products.ProductsRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository repo;
    @Autowired
	private ProductsRepository repoP;

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
			float cost_product = (float) product.getBaseprice();
			float price_order = price_product * quantity * (1 - discount / 100); // Tính giá sau khi giảm giá
			float cost_order  = cost_product * quantity;

			// Tạo đối tượng Order
			Order order = new Order();
			order.setCustomerID(orderDto.getCustomerID());
			order.setProductID(orderDto.getProductID());
			order.setQuantity(orderDto.getQuantity());
			order.setPrice(price_order);
			order.setCost(cost_order);
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
			result.reject("product", "Product not found.");//Nếu hàng không tồn tại
			return "admin/orders/CreateOrder";
		}
		return "redirect:/orders";
	}

    public Order editOrder(OrderDto orderDto, Order order) {
    	
    	LocalDate currentDate = LocalDate.now();
		Date sqlDate = Date.valueOf(currentDate);
		orderDto.setUpdatedAt(sqlDate);

		order.setCustomerID(orderDto.getCustomerID());
		order.setProductID(orderDto.getProductID());
		order.setQuantity(orderDto.getQuantity());
		order.setPrice(orderDto.getPrice());
		order.setCost(orderDto.getCost());
		order.setPromotion(orderDto.getPromotion());
		order.setUpdatedAt(orderDto.getUpdatedAt());
		order.setEstimatedDeliveryDate(orderDto.getEstimatedDeliveryDate());
		order.setPaymentMethod(orderDto.getPaymentMethod());
		order.setPaymentStatus(orderDto.getPaymentStatus());
		order.setOrderStatus(orderDto.getOrderStatus());
		order.setNote(orderDto.getNote());

		return repo.save(order);
    }
    
    public void deleteOrders(int id) {
    	Order order = repo.findById(id).get();
    	// Delete the orders
    	repo.delete(order);
    }
    
    public String searchOrders(String keyword, Model model) {
    	try {
            int number = Integer.parseInt(keyword);
            Optional<Order> orderOptional = repo.findById(number);
            if (orderOptional.isPresent()) {
                List<Order> orders = List.of(orderOptional.get());
                model.addAttribute("orders", orders);
            } else {
                model.addAttribute("error", "Sản phẩm không tồn tại!");
                return "admin/Error";
            }
        } catch (NumberFormatException e) {
            List<Order> orders = repo.findByKeyword(keyword);
            model.addAttribute("orders", orders);
        }
    	model.addAttribute("keyword", keyword);
        return "admin/orders/SearchOrder";
	}
    
    public String reportOrders(String statType, Model model) {
    	List<Object[]> chartData;
		switch (statType) {
		case "reneuveDay":
			chartData = repo.findTotalValueByDay();
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
    
    public Map<String, double[]> getMonthlyYearlyStats() {
        List<Object[]> resultsMonthly = repo.findTotalValueByMonth();
        List<Object[]> resultsYearly = repo.findTotalValueByYear();

        // Khởi tạo mảng 12 tháng, mặc định giá trị 0
        double[] monthlyCosts = new double[12];
        double[] monthlyPrices = new double[12];

        // Khởi tạo mảng năm, mặc định giá trị 0
        double[] yearlyCosts = new double[5]; // Giả sử bạn có dữ liệu từ 2022 đến 2026
        double[] yearlyPrices = new double[5];

        // Điền dữ liệu vào mảng tháng
        for (Object[] row : resultsMonthly) {
            int month = (Integer) row[0]; // Tháng (1-12)
            double totalMonthlyCost = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0; // Sử dụng row[1]
            double totalMonthlyPrice = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0; // Sử dụng row[2]

            // Đảm bảo tháng hợp lệ
            if (month >= 1 && month <= 12) {
                monthlyCosts[month - 1] = totalMonthlyCost;
                monthlyPrices[month - 1] = totalMonthlyPrice;
            } else {
                System.err.println("Invalid month: " + month);
            }
        }

        // Điền dữ liệu vào mảng năm
        for (Object[] row : resultsYearly) {
            int year = (Integer) row[0]; // Năm
            double totalYearlyCost = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0; // Sử dụng row[1]
            double totalYearlyPrice = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0; // Sử dụng row[2]

            // Đảm bảo năm hợp lệ và không vượt quá kích thước mảng
            if (year >= 2022 && year < 2022 + yearlyCosts.length) {
                yearlyCosts[year - 2022] = totalYearlyCost;
                yearlyPrices[year - 2022] = totalYearlyPrice;
            } else {
                System.err.println("Invalid year: " + year);
            }
        }

        // Ghi log kết quả
        for (int i = 0; i < monthlyCosts.length; i++) {
            System.out.println("Month: " + (i + 1) + ", Total Cost: " + monthlyCosts[i] + ", Total Price: " + monthlyPrices[i]);
        }

        for (int i = 0; i < yearlyCosts.length; i++) {
            System.out.println("Year: " + (2022 + i) + ", Total Cost: " + yearlyCosts[i] + ", Total Price: " + yearlyPrices[i]);
        }

        // Trả về Map chứa cả 4 mảng
        Map<String, double[]> stats = new HashMap<>();
        stats.put("monthlyCosts", monthlyCosts);
        stats.put("monthlyPrices", monthlyPrices);
        stats.put("yearlyCosts", yearlyCosts);
        stats.put("yearlyPrices", yearlyPrices);

        return stats;
    }
}