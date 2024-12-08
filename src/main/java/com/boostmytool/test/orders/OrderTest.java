package com.boostmytool.test.orders;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.boostmytool.model.orders.Order;
import com.boostmytool.services.orders.OrderReport;

public class OrderTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Tạo danh sách các đối tượng Order
        List<Order> orders = new ArrayList<>();
        
        // Thêm dữ liệu giả lập
        orders.add(new Order("O001", "C001", "P001", 5, 100.0f, "Promo1",
                Date.valueOf("2024-12-01"), null, null, "Credit Card", "Paid", "Delivered", ""));
        orders.add(new Order("O002", "C002", "P002", 3, 200.0f, "Promo2",
                Date.valueOf("2024-12-02"), null, null, "Paypal", "Pending", "Processing", ""));
        orders.add(new Order("O003", "C003", "P003", 10, 50.0f, null,
                Date.valueOf("2024-12-01"), null, null, "Bank Transfer", "Paid", "Delivered", ""));
        orders.add(new Order("O004", "C004", "P004", 8, 75.0f, "Promo3",
                Date.valueOf("2024-11-30"), null, null, "Credit Card", "Paid", "Shipped", ""));
        orders.add(new Order("O005", "C005", "P005", 12, 120.0f, "Promo4",
                Date.valueOf("2024-12-01"), null, null, "Cash", "Paid", "Delivered", ""));
        orders.add(new Order("O006", "C006", "P006", 2, 500.0f, null,
                Date.valueOf("2024-12-03"), null, null, "Paypal", "Pending", "Processing", ""));
        orders.add(new Order("O007", "C007", "P007", 7, 90.0f, "Promo5",
                Date.valueOf("2024-12-02"), null, null, "Credit Card", "Cancelled", "Cancelled", ""));
        orders.add(new Order("O008", "C008", "P008", 15, 30.0f, null,
                Date.valueOf("2024-12-01"), null, null, "Cash", "Paid", "Delivered", ""));
        orders.add(new Order("O009", "C009", "P009", 6, 45.0f, "Promo6",
                Date.valueOf("2024-12-02"), null, null, "Bank Transfer", "Paid", "Shipped", ""));
        orders.add(new Order("O010", "C010", "P010", 4, 80.0f, null,
                Date.valueOf("2024-11-29"), null, null, "Credit Card", "Paid", "Delivered", ""));

        // Tạo một dịch vụ báo cáo
        OrderReport report = new OrderReport(orders);
        
        report.dailyRevenueReport();
        report.monthlyRevenueReport(2024, 11);
        
        
	}

}
