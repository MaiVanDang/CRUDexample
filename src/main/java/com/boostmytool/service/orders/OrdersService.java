package com.boostmytool.service.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.boostmytool.model.customers.Customer;
import com.boostmytool.model.orders.Order;
import com.boostmytool.model.orders.OrderDto;
import com.boostmytool.model.products.Product;
import com.boostmytool.model.suppliers.Supplier;
import com.boostmytool.service.customers.CustomerRepository;
import com.boostmytool.service.products.ProductsRepository;
import com.boostmytool.service.suppliers.SuppliersRepository;

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
    @Autowired
	private SuppliersRepository repoS;
    @Autowired
	private CustomerRepository repoC;

    public String createNewOrder(Model model,OrderDto orderDto, BindingResult result){
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
				result.rejectValue("quantity", "quantity.notavailable", "Not enough stock available.");
	            model.addAttribute("orderDto", orderDto);
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
			order.setCreatedAt(orderDto.getCreatedAt());
			order.setUpdatedAt(orderDto.getUpdatedAt());
			order.setPaymentMethod(orderDto.getPaymentMethod());
			order.setPaymentStatus(orderDto.getPaymentStatus());
			order.setOrderStatus(orderDto.getOrderStatus());
			order.setNote(orderDto.getNote());

			repo.save(order);
			if("Paid".equals(orderDto.getPaymentStatus())) {
				int supplierID = Integer.parseInt(product.getSupplierID().replaceAll("[^0-9]", ""));
				int customerID = Integer.parseInt(orderDto.getCustomerID().replaceAll("[^0-9]", ""));
				Customer customer = repoC.findById(customerID).get();
				customer.setTotalOrder(customer.getTotalOrder()+1);
				repoC.save(customer);
				Supplier supplier = repoS.findById(supplierID).get();
				supplier.setTotalRevenue(supplier.getTotalRevenue() + price_order);
				product.setTotalSold(product.getTotalSold()+orderDto.getQuantity());
				repoP.save(product);
				repoS.save(supplier);
			}
			
		} else {
			result.rejectValue("productID", "product.notfound", "Product not found.");//Nếu hàng không tồn tại
			model.addAttribute("orderDto", orderDto);
			return "admin/orders/CreateOrder";
		}
		return "redirect:/orders";
	}

    public String editOrder(Model model,OrderDto orderDto, Order order, BindingResult result) {
        // Kiểm tra xem mã sản phẩm mới có tồn tại không
        int newProductID = Integer.parseInt(orderDto.getProductID().replaceAll("[^0-9]", ""));
        Optional<Product> newProductOptional = repoP.findById(newProductID);
        if (!newProductOptional.isPresent()) {
            result.rejectValue("productID", "product.notfound", "Product not found.");
            model.addAttribute("order", order);
            model.addAttribute("orderDto", orderDto);
            return "admin/orders/EditOrder"; // Trả về trang chỉnh sửa với thông tin lỗi
        }

        // Trả lại lượng hàng về mã sản phẩm cũ
        int oldProductID = Integer.parseInt(order.getProductID().replaceAll("[^0-9]", ""));
        Product oldProduct = repoP.findById(oldProductID)
            .orElseThrow(() -> new RuntimeException("Old product not found"));
        int oldQuantity = order.getQuantity();
        oldProduct.setQuantity(oldProduct.getQuantity() + oldQuantity); // Trả lại hàng
        repoP.save(oldProduct);

        // Chỉnh mọi thứ cho loại mới
        Product newProduct = newProductOptional.get();

        // Kiểm tra số lượng hàng
        if (newProduct.getQuantity() < orderDto.getQuantity()) {
            result.rejectValue("quantity", "quantity.notavailable", "Not enough stock available.");
            model.addAttribute("order", order);
            model.addAttribute("orderDto", orderDto);
            return "admin/orders/EditOrder"; // Trả về trang chỉnh sửa với thông tin lỗi
        }

        // Trừ số lượng hàng
        newProduct.setQuantity(newProduct.getQuantity() - orderDto.getQuantity());
        repoP.save(newProduct);

        // Tính tiền sản phẩm
        float discount = (float) newProduct.getDiscount();
        float quantity = (float) orderDto.getQuantity();
        float price_product = (float) newProduct.getPrice();
        float cost_product = (float) newProduct.getBaseprice();
        float price_order = price_product * quantity * (1 - discount / 100); // Tính giá sau khi giảm giá
        float cost_order = cost_product * quantity;

        LocalDate currentDate = LocalDate.now();
        Date sqlDate = Date.valueOf(currentDate);

        order.setCustomerID(orderDto.getCustomerID());
        order.setProductID(orderDto.getProductID());
        order.setQuantity(orderDto.getQuantity());
        order.setPrice(price_order);
        order.setCost(cost_order);
        order.setUpdatedAt(sqlDate);
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setPaymentStatus(orderDto.getPaymentStatus());
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setNote(orderDto.getNote());

        if("Paid".equals(orderDto.getPaymentStatus())) {
			int supplierID = Integer.parseInt(newProduct.getSupplierID().replaceAll("[^0-9]", ""));
			int customerID = Integer.parseInt(orderDto.getCustomerID().replaceAll("[^0-9]", ""));
			Customer customer = repoC.findById(customerID).get();
			customer.setTotalOrder(customer.getTotalOrder()+1);
			repoC.save(customer);
			Supplier supplier = repoS.findById(supplierID).get();
			supplier.setTotalRevenue(supplier.getTotalRevenue() + price_order);
			newProduct.setTotalSold(newProduct.getTotalSold()+orderDto.getQuantity());
			repoP.save(newProduct);
			repoS.save(supplier);
		}
        repo.save(order);
        return "redirect:/orders";
    }

    public void deleteOrders(int id) {
        Optional<Order> optionalOrder = repo.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            // Delete the order
            repo.delete(order);
        } else {
            // Có thể thêm xử lý khi không tìm thấy đơn hàng
            throw new RuntimeException("Order not found for id: " + id);
        }
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
    
    public int totalNumberOrder() {
    	return repo.totalNumberOrder();
    }
    
}