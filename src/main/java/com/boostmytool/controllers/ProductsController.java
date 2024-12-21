package com.boostmytool.controllers;

import java.util.Date;
import java.util.List;

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

import com.boostmytool.model.products.Product;
import com.boostmytool.model.products.ProductDto;
import com.boostmytool.services.products.ProductService;
import com.boostmytool.services.products.ProductsRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductsController {

	@Autowired
	private ProductsRepository repo;

	@Autowired
	private ProductService productService;

	// Show
	@GetMapping({ "", "/" })
	public String showProductList(Model model) {
		List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("products", products);
		return "admin/products/index";
	}

	// Search
	@GetMapping("/ID/{id}")
	public String searchProductById(Model model, @PathVariable int id) {
		return productService.searchById(id, model);
	}

	@GetMapping("/search")
	public String searchProducts(@RequestParam("name") String name, Model model) {
		return productService.searchByName(name, model);
	}

	@GetMapping("/create")
	public String showCreatePage(Model model) {
		ProductDto productDto = new ProductDto();
		productDto.setCreatedAt(new Date());
		model.addAttribute("productDto", productDto);
		return "admin/products/CreateProduct";
	}

	@PostMapping("/create")
	public String createProduct(@Valid @ModelAttribute ProductDto productDto, BindingResult result) {
		// Kiểm tra validation
		if (productDto.getImageFile().isEmpty()) {
			result.addError(new FieldError("productDto", "imageFile", "The image file is required"));
		}

		if (result.hasErrors()) {
			return "admin/products/CreateProduct";
		}

		try {
			// Gọi service để tạo sản phẩm
			productService.createProduct(productDto);
			return "redirect:/products";
		} catch (Exception ex) {
			// Xử lý ngoại lệ nếu cần
			result.addError(new FieldError("productDto", "", "Error creating product"));
			return "admin/products/CreateProduct";
		}
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
			productDto.setBase_price(product.getBaseprice());
			productDto.setDiscount(product.getDiscount());
			productDto.setQuantity(product.getQuantity());
			productDto.setSupplierID(product.getSupplierID());
			productDto.setSupplierID(product.getSupplierID());
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

			try {
				// Gọi service để cập nhật sản phẩm
				productService.updateProduct(id, productDto);
				return "redirect:/products";
			} catch (Exception ex) {
				// Xử lý ngoại lệ
				model.addAttribute("errorMessage", "Error updating product: " + ex.getMessage());
				return "admin/products/EditProduct";
			}
	}

	@GetMapping("/delete")
	public String deleteProduct(@RequestParam int id) {

		try {
			productService.deleteProduct(id);
            return "redirect:/products";
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}

		return "redirect:/products";
	}
}