package com.boostmytool.services.suppliers;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boostmytool.model.suppliers.Supplier;

public interface SuppliersRepository extends JpaRepository<Supplier, String>{


}
