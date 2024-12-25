package com.boostmytool.service.suppliers;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import com.boostmytool.model.suppliers.Supplier;
import com.boostmytool.model.suppliers.SupplierDto;
@Service
public class SupplierService {
	@Autowired
    private SuppliersRepository repo;

    public String searchByKeyword(String keyword, Model model) {
    	try {
            int number = Integer.parseInt(keyword);
            Optional<Supplier> supplierOptional = repo.findById(number);
            if (supplierOptional.isPresent()) {
                List<Supplier> suppliers = List.of(supplierOptional.get());
                model.addAttribute("suppliers", suppliers);
            } else {
                model.addAttribute("error", "Nha cung cap không tồn tại!");
                return "admin/Error";
            }
        } catch (NumberFormatException e) {
            List<Supplier> suppliers = repo.findByKeyword(keyword);
            model.addAttribute("suppliers", suppliers);
        }
        
        model.addAttribute("keyword", keyword);
        return "admin/suppliers/SearchSupplier";
    }
    public String createSupplier(SupplierDto supplierDto) {
    	Supplier supplier = new Supplier();
        supplier.setName(supplierDto.getName());
        supplier.setAddress(supplierDto.getAddress());
        supplier.setDescription(supplierDto.getDescription());
        supplier.setPhone(supplierDto.getPhone());
        supplier.setEmail(supplierDto.getEmail());

        Date createAt = new Date();
        supplier.setCreatedAt(createAt);
        supplier.setUpdatedAt(createAt);

        // Lưu ảnh và thêm thông tin ảnh
        MultipartFile image = supplierDto.getImageLogo();
        String storageFileName = createAt.getTime() + "_" + image.getOriginalFilename();
        String uploadDir = "public/images/";
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, uploadPath.resolve(storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
            supplier.setImageLogo(storageFileName);
        } catch (Exception e) {
            System.out.println("Error uploading image: " + e.getMessage());
        }

        repo.save(supplier);
        return "redirect:/suppliers";
    }
    
    public String updateSupplier(SupplierDto supplierDto,int id,Model model) {
    	try {
            Supplier supplier = repo.findById(id).get();
            model.addAttribute("supplier", supplier);
            supplier.setName(supplierDto.getName());
            supplier.setEmail(supplierDto.getEmail());
            supplier.setPhone(supplierDto.getPhone());
            supplier.setAddress(supplierDto.getAddress());
            supplier.setDescription(supplierDto.getDescription());
            supplier.setPhone(supplierDto.getPhone());
            supplier.setEmail(supplierDto.getEmail());

            // Cập nhật ảnh nếu được tải lên
            if (!supplierDto.getImageLogo().isEmpty()) {
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + supplier.getImageLogo());
                try {
                    Files.deleteIfExists(oldImagePath);
                } catch (Exception ex) {
                    System.out.println("Error deleting old image: " + ex.getMessage());
                }

                MultipartFile image = supplierDto.getImageLogo();
                String storageFileName = new Date().getTime() + "_" + image.getOriginalFilename();
                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                }
                supplier.setImageLogo(storageFileName);
            }

            supplier.setUpdatedAt(new Date());
            repo.save(supplier);

        } catch (Exception e) {
            System.out.println("Error updating supplier: " + e.getMessage());
        }

        return "redirect:/suppliers";
    }
    public String deleteSupplier(int id) {
    	try {
            Supplier supplier = repo.findById(id).get();
            // delete supplier images
            Path imagePath = Paths.get("public/images/" + supplier.getImageLogo());
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
    
    public int totalNumberSupplier() {
    	return repo.totalNumberSupplier();
    }
    
    public Supplier[] topSelling() {
        Pageable pageable = PageRequest.of(0, 3);
        List<Supplier> supplier = repo.findTop3SuppliersByRevenue(pageable);
        return supplier.toArray(new Supplier[0]);
        
    }
}