package com.boostmytool.services.orders;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boostmytool.model.orders.Order;

public interface OrdersRepository extends JpaRepository<Order, String>{
	
}
