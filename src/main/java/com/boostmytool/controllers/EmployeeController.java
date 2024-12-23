package com.boostmytool.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.boostmytool.model.employee.Employee;
import com.boostmytool.model.employee.EmployeeDto;
import com.boostmytool.service.employee.EmployeeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Hiển thị danh sách nhân viên
    @GetMapping({ "", "/" })
    public String showEmployeeList(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "admin/employees/index";
    }

    // Tìm kiếm nhân viên theo ID
    @GetMapping("/ID/{employeeID}")
    public String searchEmployeeById(Model model, @PathVariable int employeeID) {
        try {
            Employee employee = employeeService.getEmployeeById(employeeID);
            model.addAttribute("employee", employee);
            return "admin/employees/showEmployee";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Employee not found with ID: " + employeeID);
            return "admin/employees/index";
        }
    }

    // Hiển thị trang tạo mới nhân viên
    @GetMapping("/create")
    public String showCreatePage(Model model) {
        EmployeeDto employeeDto = new EmployeeDto();
        model.addAttribute("employeeDto", employeeDto);
        return "admin/employees/CreateEmployee";
    }

    // Tạo mới nhân viên
    @PostMapping("/create")
    public String createEmployee(@Valid @ModelAttribute EmployeeDto employeeDto, BindingResult result) {
        // Kiểm tra nếu có lỗi validation
        if (result.hasErrors()) {
            return "admin/employees/CreateEmployee";
        }

        try {
            // Gọi service để tạo nhân viên
            employeeService.createEmployee(employeeDto);
            return "redirect:/employees";
        } catch (Exception ex) {
            result.addError(new FieldError("employeeDto", "", "Error creating employee: " + ex.getMessage()));
            return "admin/employees/CreateEmployee";
        }
    }

    // Hiển thị trang chỉnh sửa nhân viên
    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int employeeID) {
        try {
            Employee employee = employeeService.getEmployeeById(employeeID);
            model.addAttribute("employee", employee);

            EmployeeDto employeeDto = new EmployeeDto();
            employeeDto.setName(employee.getName());
            employeeDto.setGender(employee.getGender());
            employeeDto.setDob(employee.getDob());
            employeeDto.setAddress(employee.getAddress());
            employeeDto.setPhone(employee.getPhone());
            employeeDto.setEmail(employee.getEmail());
            employeeDto.setSalary(employee.getSalary());
            employeeDto.setStatus(employee.getStatus());
            employeeDto.setPosition(employee.getPosition());

            model.addAttribute("employeeDto", employeeDto);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Error fetching employee: " + ex.getMessage());
            return "redirect:/employees";
        }
        return "admin/employees/EditEmployee";
    }

    // Cập nhật thông tin nhân viên
    @PostMapping("/edit")
    public String updateEmployee(Model model, @RequestParam int employeeID, @Valid @ModelAttribute EmployeeDto employeeDto, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("employeeDto", employeeDto); // Thêm lại dữ liệu để hiển thị
            return "admin/employees/EditEmployee";
        }

        try {
            employeeService.updateEmployee(employeeID, employeeDto);
            return "redirect:/employees";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Error updating employee: " + ex.getMessage());
            return "admin/employees/EditEmployee";
        }
    }

    // Xóa nhân viên
    @GetMapping("/delete")
    public String deleteEmployee(@RequestParam int employeeID, Model model) {
        try {
            employeeService.deleteEmployee(employeeID);
            return "redirect:/employees";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Error deleting employee: " + ex.getMessage());
            return "redirect:/employees";
        }
    }

}
