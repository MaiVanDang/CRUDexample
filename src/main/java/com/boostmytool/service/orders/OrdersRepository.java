package com.boostmytool.service.orders;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.boostmytool.model.orders.*;


@Repository
public interface OrdersRepository extends JpaRepository<Order, String>{
	
	@Query("SELECT o FROM Order o WHERE " + 
		       "LOWER(o.orderID) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +  
			   "LOWER(o.customerID) LIKE LOWER(CONCAT('%',  :keyword, '%')) OR " +  
			   "LOWER(o.productID) LIKE LOWER(CONCAT('%',  :keyword, '%')) OR " +
			   "LOWER(o.orderStatus) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " + 
		       "LOWER(o.paymentStatus) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(o.paymentMethod) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Order> findByKeyword(@Param("keyword") String keyword);
	
	//Report by day
    // Tổng giá trị các đơn hàng theo ngày
    @Query("SELECT DATE(o.createdAt) AS day, SUM(o.price * o.quantity) AS totalValue " +
           "FROM Order o " +
           "GROUP BY DATE(o.createdAt)")
    List<Object[]> findTotalValueByDay();
	
    // Tổng giá trị các đơn hàng theo tháng
    @Query("SELECT MONTH(o.createdAt) AS month,SUM(o.price * o.quantity) AS totalValue, YEAR(o.createdAt) AS year " +
           "FROM Order o " +
           "GROUP BY YEAR(o.createdAt), MONTH(o.createdAt)")
    List<Object[]> findTotalValueByMonth();	
	
    // Tổng giá trị các đơn hàng theo năm
    @Query("SELECT YEAR(o.createdAt) AS year, SUM(o.price * o.quantity) AS totalValue " +
           "FROM Order o " +
           "GROUP BY YEAR(o.createdAt)")
    List<Object[]> findTotalValueByYear();	
    
    // Số lượng đơn hàng theo ngày
    @Query("SELECT DATE(o.createdAt) AS day,  COUNT(o) AS orderCount " +
    	       "FROM Order o " +
    	       "GROUP BY DATE(o.createdAt)")
    List<Object[]> findOrderCountByDay();
	
    @Query("SELECT MONTH(o.createdAt) AS month, COUNT(o) AS orderCount, YEAR(o.createdAt) AS year " +
    	       "FROM Order o " +
    	       "GROUP BY YEAR(o.createdAt), MONTH(o.createdAt)")
    List<Object[]> findOrderCountByMonth();
    
    @Query("SELECT YEAR(o.createdAt) AS year, COUNT(o) AS orderCount " +
    	       "FROM Order o " +
    	       "GROUP BY YEAR(o.createdAt)")
    List<Object[]> findOrderCountByYear();
    
}