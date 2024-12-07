package com.boostmytool.services.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.boostmytool.model.products.Product;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductsRepository repo;

    public String searchByName(String name, Model model) {
		List<Product> products = repo.findByNameContaining(name);
		model.addAttribute("products",products);
		return "admin/products/showListProduct";
	}

    
}