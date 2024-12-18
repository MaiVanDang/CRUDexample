package com.boostmytool.services.orders;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.boostmytool.model.orders.Order;

public class orderReport {
	List<Order> orders = new ArrayList<>();

	public orderReport(List<Order> orders) {
	        this.orders = orders;
	    }
	
	private LocalDate toLocalDate(Date date) {
		Instant instant = date.toInstant();
		ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
		LocalDate currentDate = zdt.toLocalDate();
		return currentDate;
    }

    public void dailyRevenueReport() {
        Map<Date, Double> dailyRevenue = orders.stream()
                .collect(Collectors.groupingBy(Order::getCreatedAt,
                        Collectors.summingDouble(order -> order.getQuantity() * order.getPrice())));
        dailyRevenue.forEach((date, revenue) -> System.out.println("Date: " + date + ", Revenue: " + revenue));
    }
	
    
    public int monthlyRevenueReport(int year, int month) {
        return (int) orders.stream()
                .filter(order -> {
                    LocalDate date = toLocalDate(order.getCreatedAt());
                    return date.getYear() == year && date.getMonthValue() == month;
                })
                .mapToDouble(order -> order.getQuantity() * order.getPrice())
                .sum();
    }
    
    
	
}
