package com.boostmytool.services.customers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.boostmytool.model.customers.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String>{
	@Query("SELECT c FROM Customer c WHERE " +
		"LOWER(c.customerID) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		"LOWER(c.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		"LOWER(c.customerEmail) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		"LOWER(c.customerAddress) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		"c.customerPhone LIKE CONCAT('%', :keyword, '%')")
	List<Customer> findByKeyword(@Param("keyword") String keyword);
	
	
	// Thống kê theo nhóm tuổi
    @Query("SELECT " +
           "CASE " +
           "  WHEN YEAR(CURRENT_DATE) - YEAR(c.customerDOB) < 18 THEN '< 18 tuổi' " +
           "  WHEN YEAR(CURRENT_DATE) - YEAR(c.customerDOB) BETWEEN 18 AND 30 THEN '18 - 30 tuổi' " +
           "  WHEN YEAR(CURRENT_DATE) - YEAR(c.customerDOB) BETWEEN 31 AND 50 THEN '31 - 50 tuổi' " +
           "  ELSE 'Trên 50' " +
           "END AS ageGroup, " +
           "COUNT(c) " +
           "FROM Customer c " +
           "GROUP BY ageGroup")
    List<Object[]> findStatisticsByAgeGroup();
    
    // Thống kê theo loại khách hàng
    @Query("SELECT c.customerType, COUNT(c) FROM Customer c GROUP BY c.customerType")
    List<Object[]> findStatisticsByCustomerType();
    
    // Thống kê theo địa chỉ (lấy phần tỉnh từ địa chỉ)
    @Query("SELECT SUBSTRING_INDEX(c.customerAddress, ',', -1) AS province, COUNT(c) " +
           "FROM Customer c " +
           "GROUP BY SUBSTRING_INDEX(c.customerAddress, ',', -1)")
    List<Object[]> findStatisticsByLocation();
    
    // Thống kê khách hàng mới (đăng ký hàng tháng trong năm hiện tại)
    @Query("SELECT MONTH(c.customerDateCreated), COUNT(c) " +
           "FROM Customer c " +
           "WHERE YEAR(c.customerDateCreated) = YEAR(CURRENT_DATE) " +
           "GROUP BY MONTH(c.customerDateCreated)")
    List<Object[]> findStatisticsByNewCustomers();
    
    // Thống kê khách hàng theo tổng số tiền đã chi tiêu (đã trả + nợ)
    @Query("SELECT " +
    	       "CASE " +
    	       "  WHEN (c.customerPaidAmount + c.customerSumDebt) < 1000000 THEN '< 1 triệu' " +
    	       "  WHEN (c.customerPaidAmount + c.customerSumDebt) BETWEEN 1000000 AND 4999999 THEN '1 - 5 triệu' " +
    	       "  WHEN (c.customerPaidAmount + c.customerSumDebt) BETWEEN 5000000 AND 9999999 THEN '5 - 10 triệu' " +
    	       "  ELSE '10 triệu trở lên' " +
    	       "END AS spendingGroup, COUNT(c) " +
    	       "FROM Customer c " +
    	       "GROUP BY spendingGroup")
    List<Object[]> findStatisticsByTotalSpending();
    
    // Thống kê khách hàng theo số tiền nợ
    @Query("SELECT " +
    	       "CASE " +
    	       "  WHEN c.customerSumDebt < 1000000 THEN '< 1 triệu' " +
    	       "  WHEN c.customerSumDebt BETWEEN 1000000 AND 4999999 THEN '1 - 5 triệu' " +
    	       "  WHEN c.customerSumDebt BETWEEN 5000000 AND 9999999 THEN '5 - 10 triệu' " +
    	       "  ELSE '10 triệu trở lên' " +
    	       "END AS debtGroup, COUNT(c) " +
    	       "FROM Customer c " +
    	       "GROUP BY debtGroup")
    	List<Object[]> findStatisticsByDebt();
    	
}
