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
import com.boostmytool.service.suppliers.SupplierService;
import com.boostmytool.service.suppliers.SuppliersRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/suppliers")
public class SuppliersController {
	
	

    @Autowired
    private SuppliersRepository repo;
    @Autowired
	private SupplierService supplierService;
    @GetMapping({"", "/"})
    public String showSupplierList(Model model) {
        List<Supplier> suppliers = repo.findAll();
        model.addAttribute("suppliers", suppliers);
        return "admin/suppliers/index";
    }

    // Tìm kiếm nhà cung cấp
    @GetMapping("/search")
    public String searchSuppliers(@RequestParam("keyword") String keyword, Model model) {
        return supplierService.searchByKeyword(keyword, model);
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        SupplierDto supplierDto = new SupplierDto();
        model.addAttribute("supplierDto", supplierDto);
        return "admin/suppliers/CreateSupplier";
    }

    @PostMapping("/create")
    public String createSupplier(@Valid @ModelAttribute SupplierDto supplierDto,BindingResult result) {
        if (supplierDto.getImageLogo().isEmpty()) {
            result.addError(new FieldError("supplierDto", "imageLogo", "The Logo file is required"));
        }
        if (result.hasErrors()) {
            return "admin/suppliers/CreateSupplier";
        }
        return supplierService.createSupplier(supplierDto);
    }


    // Edit Supplier
    @GetMapping("/edit")
    public String showEditPage(
            Model model,
            @RequestParam(value = "id", required = true) int id) {

        try {
            Supplier supplier = repo.findById(id).get();
            model.addAttribute("supplier", supplier);

            SupplierDto supplierDto = new SupplierDto();
            supplierDto.setName(supplier.getName());
            supplierDto.setEmail(supplier.getEmail());
            supplierDto.setPhone(supplier.getPhone());
            supplierDto.setAddress(supplier.getAddress());
            supplierDto.setDescription(supplier.getDescription());
            model.addAttribute("supplierDto", supplierDto);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/suppliers";
        }

        return "admin/suppliers/EditSupplier";
    }

    @PostMapping("/edit")
    public String updateSupplier(
            Model model,
            @RequestParam(value = "id", required = true) int id,
            @Valid @ModelAttribute SupplierDto supplierDto,
            BindingResult result) {
            if (result.hasErrors()) {
                return "admin/suppliers/EditSupplier";
            }
            return supplierService.updateSupplier(supplierDto, id, model);
    }


    @GetMapping("/delete")
    public String deleteSupplier(
            @RequestParam(value = "id", required = true) int id) {
    	return supplierService.deleteSupplier(id);
    }
}
