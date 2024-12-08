package com.boostmytool.services.customers;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boostmytool.model.customers.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String>{

}
