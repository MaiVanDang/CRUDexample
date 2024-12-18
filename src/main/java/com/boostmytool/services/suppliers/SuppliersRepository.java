package com.boostmytool.services.suppliers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.boostmytool.model.suppliers.Supplier;

public interface SuppliersRepository extends JpaRepository<Supplier, String>{
    @Query("SELECT s FROM Supplier s WHERE " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.id) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(FUNCTION('DATE_FORMAT', s.createdAt, '%Y-%m-%d')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(FUNCTION('DATE_FORMAT', s.updatedAt, '%Y-%m-%d')) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Supplier> searchSuppliersByKeyword(@Param("keyword") String keyword);	
}
