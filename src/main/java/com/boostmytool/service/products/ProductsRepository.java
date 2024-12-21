package com.boostmytool.service.products;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.boostmytool.model.products.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameContaining(String name);
    
    @Query(value = "SELECT * FROM products WHERE " +
            "LOWER(name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(category) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(brand) LIKE LOWER(CONCAT('%', :keyword, '%'))", 
            nativeQuery = true)
     List<Product> findByKeyword(@Param("keyword") String keyword);
    
    @Query(nativeQuery = true, value = "SELECT * FROM search_inf_products(:category, :minPrice, :maxPrice)")
    List<Object[]> findProductsByCustomSearch(
        @Param("category") String category, 
        @Param("minPrice") Float minPrice, // Changed to Float
        @Param("maxPrice") Float maxPrice  // Changed to Float
    );

    default List<Product> searchProducts(String category, Float minPrice, Float maxPrice) {
        List<Object[]> results = findProductsByCustomSearch(category, minPrice, maxPrice);
        
        if (results == null || results.isEmpty()) {
            return Collections.emptyList(); 
        }
//        System.out.println("Raw Results:");
//        for (Object[] row : results) {
//            System.out.println(Arrays.toString(row));
//        }
     // Map results to Product objects
        List<Product> products = results.stream()
                      .map(this::mapToProduct)
                      .collect(Collectors.toList());

        // Print the mapped Product objects
//        System.out.println("Mapped Products:");
//        for (Product product : products) {
//            System.out.println(product); // Assuming toString is implemented in Product
//        }

        return products;
    }

    private Product mapToProduct(Object[] result) {
        Product product = new Product();
        product.setId(result[0] != null ? ((Number) result[0]).intValue() : null);
        product.setName(result[1] != null ? (String) result[1] : "");
        product.setCategory(result[2] != null ? (String) result[2] : "");
        product.setSupplierID(result[3] != null ? (String) result[3] : "");
        product.setPrice(result[4] != null ? ((Number) result[4]).floatValue() : null);
        product.setBaseprice(result[5] != null ? ((Number) result[5]).floatValue() : null);
        product.setDiscount(result[6] != null ? ((Number) result[6]).intValue() : null);
        product.setQuantity(result[7] != null ? ((Number) result[7]).intValue() : null);
        product.setImageFileName(result[8] != null ? (String) result[8] : "");
        product.setBrand(result[9] != null ? (String) result[9] : "");
        product.setCreatedAt(result[10] != null ? (Date) result[10] : null);
        product.setUpdatedAt(result[11] != null ? (Date) result[11] : null);
        return product;
    }
    
}