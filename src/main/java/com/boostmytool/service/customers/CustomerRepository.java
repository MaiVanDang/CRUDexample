package com.boostmytool.service.customers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.boostmytool.model.customers.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	@Query("SELECT c FROM Customer c WHERE " +
		"LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		"LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		"LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		"c.phone LIKE CONCAT('%', :keyword, '%')")
	List<Customer> findByKeyword(@Param("keyword") String keyword);
	
	
	// Thống kê theo nhóm tuổi
    @Query("SELECT " +
           "CASE " +
           "  WHEN YEAR(CURRENT_DATE) - YEAR(c.dob) < 18 THEN '< 18 tuổi' " +
           "  WHEN YEAR(CURRENT_DATE) - YEAR(c.dob) BETWEEN 18 AND 30 THEN '18 - 30 tuổi' " +
           "  WHEN YEAR(CURRENT_DATE) - YEAR(c.dob) BETWEEN 31 AND 50 THEN '31 - 50 tuổi' " +
           "  ELSE 'Trên 50' " +
           "END AS ageGroup, " +
           "COUNT(c) " +
           "FROM Customer c " +
           "GROUP BY ageGroup")
    List<Object[]> findStatisticsByAgeGroup();
    
    // Thống kê theo địa chỉ (lấy phần tỉnh từ địa chỉ)
    @Query("SELECT SUBSTRING_INDEX(c.address, ',', -1) AS province, COUNT(c) " +
           "FROM Customer c " +
           "GROUP BY SUBSTRING_INDEX(c.address, ',', -1)")
    List<Object[]> findStatisticsByLocation();
    
    // Thống kê khách hàng mới (đăng ký hàng tháng trong năm hiện tại)
    @Query("SELECT MONTH(c.customerDateCreated), COUNT(c) " +
           "FROM Customer c " +
           "WHERE YEAR(c.customerDateCreated) = YEAR(CURRENT_DATE) " +
           "GROUP BY MONTH(c.customerDateCreated)")
    List<Object[]> findStatisticsByNewCustomers();
    	
}