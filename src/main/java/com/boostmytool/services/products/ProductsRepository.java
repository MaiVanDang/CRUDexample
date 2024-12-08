package com.boostmytool.services.products;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.boostmytool.model.products.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer> {
	List<Product> findByNameContaining(String name);
}