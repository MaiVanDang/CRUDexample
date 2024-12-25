package com.boostmytool.service.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.boostmytool.model.products.Product;
import com.boostmytool.model.products.ProductDto;
import com.boostmytool.model.suppliers.Supplier;
import com.boostmytool.service.FileStorageService;
import com.boostmytool.service.suppliers.SuppliersRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductsRepository repo;
    
    @Autowired
    private SuppliersRepository repoS;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    public Product createProduct(ProductDto productDto) throws IOException {
        // Kiểm tra và validate file ảnh
        if (productDto.getImageFile() == null || productDto.getImageFile().isEmpty()) {
            throw new IllegalArgumentException("image:The image file is required");
        }

        MultipartFile image = productDto.getImageFile();

        // Kiểm tra sự tồn tại của nhà cung cấp trước khi lưu file
        int supplierID;
        try {
            supplierID = Integer.parseInt(productDto.getSupplierID().replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("supplierID:Mã nhà cung cấp không hợp lệ");
        }

        Optional<Supplier> supplierOptional = repoS.findById(supplierID);
        if (!supplierOptional.isPresent()) {
            throw new IllegalArgumentException("supplierID:Mã nhà cung cấp không tồn tại");
        }

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
    
    public Product updateProduct(int id, ProductDto productDto) throws Exception,IOException {
        Product product = repo.findById(id)
            .orElseThrow(() -> new Exception("Product not found with id: " + id));

        if (!productDto.getImageFile().isEmpty()) {
            fileStorageService.deleteFile(product.getImageFileName());

            String newImageFileName = fileStorageService.saveFile(productDto.getImageFile());
            product.setImageFileName(newImageFileName);
        }
        
     // Kiểm tra sự tồn tại của nhà cung cấp trước khi lưu file
        int supplierID;
        try {
            supplierID = Integer.parseInt(productDto.getSupplierID().replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("supplierID:Mã nhà cung cấp không hợp lệ");
        }

        Optional<Supplier> supplierOptional = repoS.findById(supplierID);
        if (!supplierOptional.isPresent()) {
            throw new IllegalArgumentException("supplierID:Mã nhà cung cấp không tồn tại");
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
    
    public String searchByKeyword(String keyword, Model model) {
        try {
            int number = Integer.parseInt(keyword);
            Optional<Product> productOptional = repo.findById(number);
            if (productOptional.isPresent()) {
                List<Product> products = List.of(productOptional.get());
                model.addAttribute("products", products);
            } else {
                model.addAttribute("error", "Sản phẩm không tồn tại!");
                return "admin/Error";
            }
        } catch (NumberFormatException e) {
            List<Product> products = repo.findByKeyword(keyword);
            model.addAttribute("products", products);
        }
        
        model.addAttribute("keyword", keyword);
        return "admin/products/showListProduct";
    }
    
    public int totalNumberProduct() {
    	return repo.totalNumberProduct();
    }
    
    public Product[] topSelling() {
        List<Product> products = repo.findTop3BestSellingProductsWithSoldCount();
        return products.toArray(new Product[0]);
    }
}