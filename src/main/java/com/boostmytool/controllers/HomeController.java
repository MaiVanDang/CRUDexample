package com.boostmytool.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.boostmytool.model.products.Product;
import com.boostmytool.service.orders.OrdersService;
import com.boostmytool.service.products.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Controller
@RequestMapping("/admin")
public class HomeController {
	
	@Autowired
    private ProductService productService;
	
	@Autowired
    private OrdersService orderService;
	
	@GetMapping({"","/"})
	public String home() {	
		return "/index";
	}
	@GetMapping("/home")
	public String admin() {
		return "admin/index";
	}
	
	@GetMapping("/search")
	public String search() {
		return "admin/AdminSearch";
	}
	
	@GetMapping("/searchcatalogprice")
	public String searchProducts(
	    @RequestParam String category,
	    @RequestParam float minPrice,
	    @RequestParam float maxPrice,
	    Model model
	) {
		List<Product> products = productService.searchProducts(category, minPrice, maxPrice);
//	    System.out.println("Mapped Products:");
//	    for (Product product : products) {
//	        System.out.println(product); // Assuming toString is implemented in Product
//	    }
		model.addAttribute("products", products);
		return "admin/AdminSearch";
	}
	
	@GetMapping("/statistic")
	public String statistic(Model model) throws JsonProcessingException {
	    Map<String, double[]> stats = orderService.getMonthlyYearlyStats();
	    double[] monthlyCosts = stats.get("monthlyCosts");
	    double[] monthlyPrices = stats.get("monthlyPrices");
	    double[] yearlyCosts = stats.get("yearlyCosts");
	    double[] yearlyPrices = stats.get("yearlyPrices");

	    // Tính tổng chi phí và doanh thu
	    double totalMonthlyCosts = 0;
	    double totalMonthlyRevenue = 0;
	    double totalYearlyCosts = 0;
	    double totalYearlyRevenue = 0;

	    for (double cost : monthlyCosts) {
	        totalMonthlyCosts += cost;
	    }

	    System.out.println("Total Monthly Costs: " + totalMonthlyCosts);
	    for (double price : monthlyPrices) {
	        totalMonthlyRevenue += price;
	    }

	    for (double cost : yearlyCosts) {
	        totalYearlyCosts += cost;
	    }

	    for (double price : yearlyPrices) {
	        totalYearlyRevenue += price;
	    }

	    // Tính lợi nhuận
	    double profitMonthly = totalMonthlyRevenue - totalMonthlyCosts;
	    double profitYearly = totalYearlyRevenue - totalYearlyCosts;

	    // Thêm dữ liệu vào model
	    ObjectMapper objectMapper = new ObjectMapper();
	    String monthlyCostsJson = objectMapper.writeValueAsString(monthlyCosts);
	    model.addAttribute("monthlyCosts", monthlyCostsJson);
	    String monthlyPricesJson = objectMapper.writeValueAsString(monthlyPrices);
	    model.addAttribute("monthlyPrices", monthlyPricesJson);
	    model.addAttribute("totalMonthlyCosts", totalMonthlyCosts);
	    model.addAttribute("totalMonthlyRevenue", totalMonthlyRevenue);
	    model.addAttribute("profitMonthly", profitMonthly);

	    String yearlyCostsJson = objectMapper.writeValueAsString(yearlyCosts);
	    model.addAttribute("yearlyCosts", yearlyCostsJson);
	    String yearyPricesJson = objectMapper.writeValueAsString(yearlyPrices);
	    model.addAttribute("yearlyPrices", yearyPricesJson);
	    model.addAttribute("totalYearlyCosts", totalYearlyCosts);
	    model.addAttribute("totalYearlyRevenue", totalYearlyRevenue);
	    model.addAttribute("profitYearly", profitYearly);

	    return "admin/static/index";
	}
}
