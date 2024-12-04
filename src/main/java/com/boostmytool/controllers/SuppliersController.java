package com.boostmytool.controllers;

import java.io.InputStream;
import java.nio.file.*;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.boostmytool.model.suppliers.Supplier;
import com.boostmytool.model.suppliers.SupplierDto;
import com.boostmytool.services.suppliers.SuppliersRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/suppliers")
public class SuppliersController {
	
    @Autowired
    private SuppliersRepository repo;

    @GetMapping({"", "/"})
    public String showSupplierList(Model model) {
    	List<Supplier> suppliers = repo.findAll();
        model.addAttribute("suppliers", suppliers);
        return "suppliers/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        SupplierDto supplierDto = new SupplierDto();
        model.addAttribute("supplierDto", supplierDto);
        return "suppliers/CreateSupplier";
    }

    @PostMapping("/create")
    public String createSupplier(
            @Valid @ModelAttribute SupplierDto supplierDto,
            BindingResult result) {

        if (supplierDto.getImageLogo().isEmpty()) {
            result.addError(new FieldError("supplierDto", "imageLogo", "The Logo file is required"));
        }

        // Kiểm tra ID tồn tại
        String newID = supplierDto.getId();
        if (repo.existsById(newID)) {
            result.addError(new FieldError("supplierDto", "id", "ID này đã tồn tại làm ơn chọn 1 ID khác."));
        }

        if (result.hasErrors()) {
            return "suppliers/CreateSupplier";
        }

        // Save image file
        MultipartFile image = supplierDto.getImageLogo();
        Date createAt = new Date();
        String storageFileName = createAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/imageLogo/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, uploadPath.resolve(storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        // Tạo và lưu đối tượng Supplier
        Supplier supplier = new Supplier();
        supplier.setId(supplierDto.getId());
        supplier.setName(supplierDto.getName());
        supplier.setAddress(supplierDto.getAddress());
        supplier.setDescription(supplierDto.getDescription());
        supplier.setCreatedAt(createAt);
        supplier.setUpdatedAt(createAt);
        supplier.setImageLogo(storageFileName);
        repo.save(supplier);

        return "redirect:/suppliers";
    }


    // Edit Supplier
    @GetMapping("/edit")
    public String showEditPage(
            Model model,
            @RequestParam(value = "id", required = true) String id) {

        try {
            Supplier supplier = repo.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));
            model.addAttribute("supplier", supplier);

            SupplierDto supplierDto = new SupplierDto();
            supplierDto.setName(supplier.getName());
            supplierDto.setAddress(supplier.getAddress());
            supplierDto.setDescription(supplier.getDescription());
            model.addAttribute("supplierDto", supplierDto);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/suppliers";
        }

        return "suppliers/EditSupplier";
    }

    @PostMapping("/edit")
    public String updateSupplier(
            Model model,
            @RequestParam(value = "id", required = true) String id,
            @Valid @ModelAttribute SupplierDto supplierDto,
            BindingResult result) {

        try {
            Supplier supplier = repo.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));
            model.addAttribute("supplier", supplier);

            if (result.hasErrors()) {
                return "suppliers/EditSupplier";
            }
            supplier.setUpdatedAt(new Date());
            if (!supplierDto.getImageLogo().isEmpty()) {
                // Delete old image if exists
                String uploadDir = "public/imageLogo/";
                Path oldImagePath = Paths.get(uploadDir + supplier.getImageLogo());

                try {
                    Files.deleteIfExists(oldImagePath);
                } catch (Exception ex) {
                    System.out.println("Exception deleting old image: " + ex.getMessage());
                }

                // Save new image
                MultipartFile image = supplierDto.getImageLogo();
                Date createAt = new Date();
                String storageFileName = createAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                }

                supplier.setImageLogo(storageFileName);
            }

            supplier.setName(supplierDto.getName());
            supplier.setAddress(supplierDto.getAddress());
            supplier.setDescription(supplierDto.getDescription());

            supplier.setUpdatedAt(new Date()); // Update the 'updatedAt' field
            repo.save(supplier);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/suppliers";
    }

    @GetMapping("/delete")
    public String deleteSupplier(
            @RequestParam(value = "id", required = true) String id) {
        try {
            Supplier supplier = repo.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));
            // delete supplier images
            Path imagePath = Paths.get("public/imageLogo/" + supplier.getImageLogo());
            try {
                Files.deleteIfExists(imagePath);
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
            }

            repo.delete(supplier);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/suppliers";
    }
}
