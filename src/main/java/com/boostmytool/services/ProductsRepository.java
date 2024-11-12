package com.boostmytool.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boostmytool.model.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer>{

}
