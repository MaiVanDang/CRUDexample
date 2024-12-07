package com.boostmytool.controllers;

import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.boostmytool.model.products.Product;
import com.boostmytool.model.products.ProductDto;
import com.boostmytool.services.products.ProductService;
//import com.boostmytool.services.products.ProductService;
import com.boostmytool.services.products.ProductsRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductsController {

	@Autowired
	private ProductsRepository repo;

	@Autowired
	private ProductService productService;
	
	//Show
	@GetMapping({ "", "/" })
	public String showProductList(Model model) {
		List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("products", products);
		return "admin/products/index";
	}

	// Search
	@GetMapping("/ID/{id}")
	public String searchProductById(
	    Model model,
	    @PathVariable int id,
	    @Valid @ModelAttribute ProductDto productDto,
	    BindingResult result
	) {
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
	
	@GetMapping("/search")
    public String searchProducts(@RequestParam("name") String name, Model model) {
        return productService.searchByName(name, model);
    }
	
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		ProductDto productDto = new ProductDto();
		productDto.setCreatedAt(new Date());
		model.addAttribute("productDto",productDto);
		return "admin/products/CreateProduct";
	}
	
	@PostMapping("/create")
	public String createProduct(
			@Valid @ModelAttribute ProductDto productDto,
			BindingResult result
			) {
		
		if (productDto.getImageFile().isEmpty()) {
			result.addError(new FieldError("productDto", "imageFile", "The image file is required"));
		}
		
		if(result.hasErrors()) {
			return "admin/products/CreateProduct";
		}
		
		MultipartFile image = productDto.getImageFile();
		Date updatedAt = new Date();
		String storageFileName = updatedAt.getTime() + "_" + image.getOriginalFilename();
		
		try {
			String uploadDir = "public/images/";
			Path uploadPath = Paths.get(uploadDir);
			
			if(!Files.exists(uploadPath)) { // chỉ là tạo thư mục mới nếu nó chưa tồn tại
				Files.createDirectories(uploadPath);
			}
			
			try (InputStream inputStream = image.getInputStream()){
				Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
						StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		
		Product product = new Product(); 
		product.setName(productDto.getName());
		product.setBrand(productDto.getBrand());
		product.setCategory(productDto.getCategory());
		product.setPrice(productDto.getPrice());
		product.setDescription(productDto.getDescription());
		product.preUpdate();
		product.setImageFileName(storageFileName);
		
		repo.save(product);
		
		return "redirect:/products";
	}
	
	@GetMapping("/edit")
	public String showEditPage(Model model, @RequestParam int id) {

		try {
			Product product = repo.findById(id).get();
			model.addAttribute("product", product);

			ProductDto productDto = new ProductDto();
			productDto.setName(product.getName());
			productDto.setBrand(product.getBrand());
			productDto.setCategory(product.getCategory());
			productDto.setPrice(product.getPrice());
			productDto.setCreatedAt(product.getCreatedAt());
			productDto.setDescription(product.getDescription());

			model.addAttribute("productDto", productDto);
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/products";
		}

		return "admin/products/EditProduct";
	}

	@PostMapping("/edit")
	public String updateProduct(Model model, @RequestParam int id, @Valid @ModelAttribute ProductDto productDto,
			BindingResult result) {

		try {
			Product product = repo.findById(id).get();
			model.addAttribute("product", product);

			if (result.hasErrors()) {
				System.out.println("Có lỗi:");
				result.getAllErrors().forEach(error -> {
					System.out.println(error.getDefaultMessage());
				});
				result.getAllErrors().forEach(error -> {
					System.out.println("Lỗi tại trường: " + error.getObjectName());
					System.out.println("Thông điệp lỗi: " + error.getDefaultMessage());
				});
				model.addAttribute("product", product); // Thêm lại sản phẩm để hiển thị
				return "admin/products/EditProduct";
			}

			if (!productDto.getImageFile().isEmpty()) {
				// delete old image
				String uploadDir = "/public/images/";
				Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());
				// Kiểm tra xem tệp có tồn tại trước khi xóa
				if (Files.exists(oldImagePath)) {
					try {
						Files.delete(oldImagePath);
					} catch (Exception ex) {
						System.out.println("Exception: " + ex.getMessage());
					}
				}

				// save new image file
				MultipartFile image = productDto.getImageFile();
				Date updatedAt = new Date();
				String storageFileName = updatedAt.getTime() + "_" + image.getOriginalFilename();

				try (InputStream inputStream = image.getInputStream()) {
					Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
							StandardCopyOption.REPLACE_EXISTING);
				}

				product.setImageFileName(storageFileName);

			}

			product.setName(productDto.getName());
			product.setBrand(productDto.getBrand());
			product.setCategory(productDto.getCategory());
			product.setPrice(productDto.getPrice());
			product.setDescription(productDto.getDescription());
			product.preUpdate();

			repo.save(product);
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}

		return "redirect:/products";
	}

	@GetMapping("/delete")
	public String deleteProduct(@RequestParam int id) {

		try {
			Product product = repo.findById(id).get();

			// delete products image
			Path imagePath = Paths.get("public/images/" + product.getImageFileName());

			try {
				Files.delete(imagePath);
			} catch (Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
			}

			// delete the product
			repo.delete(product);
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}

		return "redirect:/products";
	}
}