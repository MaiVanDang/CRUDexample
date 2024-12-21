package com.boostmytool.service.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.boostmytool.model.employee.Employee;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    
    // Tìm nhân viên theo tên (chứa một chuỗi ký tự bất kỳ)
    List<Employee> findByEmployeeNameContaining(String name);
    
    // Tìm nhân viên theo trạng thái công việc
    List<Employee> findByEmployeeStatus(String status);

    // Tìm nhân viên có mức lương lớn hơn hoặc bằng một giá trị
    @Query("SELECT e FROM Employee e WHERE e.salary >= :minSalary")
    List<Employee> findEmployeesWithSalaryGreaterThanEqual(float minSalary);
}
