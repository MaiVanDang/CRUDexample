package com.boostmytool.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.boostmytool.model.products.Product;
import com.boostmytool.service.products.ProductService;

@Controller
@RequestMapping("/admin")
public class HomeController {
	
	@Autowired
    private ProductService productService;
	
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
}
