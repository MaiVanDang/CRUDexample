package com.boostmytool.services.products;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boostmytool.model.products.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer>{

}
