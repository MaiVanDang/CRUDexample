package com.boostmytool.service.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boostmytool.model.person.Employee;
import com.boostmytool.model.person.EmployeeDto;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Lấy tất cả nhân viên
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Lấy thông tin nhân viên theo ID
    public Employee getEmployeeById(String employeeID) throws Exception {
        return employeeRepository.findById(employeeID)
                .orElseThrow(() -> new Exception("Employee not found with ID: " + employeeID));
    }

    // Tìm nhân viên theo tên
    public List<Employee> searchEmployeesByName(String name) {
        return employeeRepository.findByEmployeeNameContaining(name);
    }

    // Lấy danh sách nhân viên theo trạng thái
    public List<Employee> getEmployeesByStatus(String status) {
        return employeeRepository.findByEmployeeStatus(status);
    }

    // Lấy danh sách nhân viên có mức lương tối thiểu
    public List<Employee> getEmployeesWithMinSalary(float minSalary) {
        return employeeRepository.findEmployeesWithSalaryGreaterThanEqual(minSalary);
    }

    // Thêm nhân viên mới
    public Employee createEmployee(EmployeeDto employeeDto) {
        Employee employee = mapDtoToEntity(employeeDto);
        return employeeRepository.save(employee);
    }

    // Cập nhật thông tin nhân viên
    public Employee updateEmployee(String employeeID, EmployeeDto employeeDto) throws Exception {
        Employee existingEmployee = getEmployeeById(employeeID);
        updateEntityWithDto(existingEmployee, employeeDto);
        return employeeRepository.save(existingEmployee);
    }

    // Xóa nhân viên
    public void deleteEmployee(String employeeID) throws Exception {
        Employee existingEmployee = getEmployeeById(employeeID);
        employeeRepository.delete(existingEmployee);
    }

    // Chuyển đổi từ DTO sang Entity
    private Employee mapDtoToEntity(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setId(employeeDto.getEmployeeID());
        employee.setName(employeeDto.getEmployeeName());
        employee.setGender(employeeDto.getGender());
        employee.setDOB(employeeDto.getDateOfBirth());
        employee.setAddress(employeeDto.getEmployeeAddress());
        employee.setPhone(employeeDto.getEmployeePhoneNumber());
        employee.setEmail(employeeDto.getEmployeeEmail());
        employee.setPosition(employeeDto.getPosition());
        employee.setSalary((float) employeeDto.getSalary());
        employee.setEmployeeStatus(employeeDto.getEmployeeStatus());
        return employee;
    }

    // Cập nhật Entity bằng DTO
    private void updateEntityWithDto(Employee employee, EmployeeDto employeeDto) {
        employee.setName(employeeDto.getEmployeeName());
        employee.setGender(employeeDto.getGender());
        employee.setDOB(employeeDto.getDateOfBirth());
        employee.setAddress(employeeDto.getEmployeeAddress());
        employee.setPhone(employeeDto.getEmployeePhoneNumber());
        employee.setEmail(employeeDto.getEmployeeEmail());
        employee.setPosition(employeeDto.getPosition());
        employee.setSalary((float) employeeDto.getSalary());
        employee.setEmployeeStatus(employeeDto.getEmployeeStatus());
    }
}
