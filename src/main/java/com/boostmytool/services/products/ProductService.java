package com.boostmytool.services.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.boostmytool.model.products.Product;
import com.boostmytool.model.products.ProductDto;
import com.boostmytool.service.FileStorageService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductsRepository repo;
    
    @Autowired
    private FileStorageService fileStorageService;

    public String searchByName(String name, Model model) {
		List<Product> products = repo.findByNameContaining(name);
		model.addAttribute("products",products);
		return "admin/products/showListProduct";
	}

    public String searchById(int id, Model model) {
    	try {
	        Optional<Product> productOptional = repo.findById(id);
	        if (productOptional.isPresent()) {
	            model.addAttribute("product", productOptional.get()); // Extract the actual Product
	            return "admin/products/showProductById";
	        } else {
	            model.addAttribute("error", "Sản phẩm không tồn tại!");
	            return "admin/Error";
	        }
	    } catch (Exception ex) {
	        System.out.println("Exception: " + ex.getMessage());
	        return "redirect:/products";
	    }
    }
    
    public Product createProduct(ProductDto productDto) throws IOException {
        // Kiểm tra và validate file ảnh
        if (productDto.getImageFile().isEmpty()) {
            throw new IllegalArgumentException("The image file is required");
        }

        MultipartFile image = productDto.getImageFile();
        
        // Lưu file và lấy tên file
        String storageFileName = fileStorageService.saveFile(image);
        // Tạo đối tượng Product
        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setBaseprice(productDto.getBase_price());
        product.setDiscount(productDto.getDiscount());
        product.setSupplierID(productDto.getSupplierID());
        product.setDescription(productDto.getDescription());
        product.preUpdate();
        product.setImageFileName(storageFileName);

        // Lưu sản phẩm vào database
        return repo.save(product);
    }
    
    public Product updateProduct(int id, ProductDto productDto) throws Exception {
        Product product = repo.findById(id)
            .orElseThrow(() -> new Exception("Product not found with id: " + id));

        if (!productDto.getImageFile().isEmpty()) {
            fileStorageService.deleteFile(product.getImageFileName());

            String newImageFileName = fileStorageService.saveFile(productDto.getImageFile());
            product.setImageFileName(newImageFileName);
        }

        // Cập nhật thông tin sản phẩm
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setBaseprice(productDto.getBase_price());
        product.setDiscount(productDto.getDiscount());
        product.setSupplierID(productDto.getSupplierID());
        product.setDescription(productDto.getDescription());
        product.preUpdate();

        // Lưu và trả về sản phẩm đã cập nhật
        return repo.save(product);
    }
    
    public void deleteProduct(int id) throws Exception{
    	Product product = repo.findById(id)
                .orElseThrow(() -> new Exception("Product not found with id: " + id));
    	fileStorageService.deleteFile(product.getImageFileName());
    	repo.delete(product);
    }
    
    public List<Product> searchProducts(String category, float minPrice, float maxPrice) {
    	
    	List<Product> products = repo.searchProducts(category, minPrice, maxPrice);
//        System.out.println("Mapped Products:");
//        for (Product product : products) {
//            System.out.println(product); // Assuming toString is implemented in Product
//        }

        return products;
    }
}