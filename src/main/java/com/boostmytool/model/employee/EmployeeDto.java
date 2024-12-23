package com.boostmytool.model.employee;

import com.boostmytool.model.person.Persons;

import jakarta.validation.constraints.*;

public class EmployeeDto extends Persons{
    
    @NotEmpty(message = "The employee ID is required")
    private String employeeID;

    @NotEmpty(message = "The position is required")
    private String position;

    @Min(value = 0, message = "Salary must be at least 0")
    private double salary;

    @NotEmpty(message = "The employee status is required")
    private String employeeStatus;

    // Getter and Setter methods
    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }
    
 // Ghi đè getter và setter cho các thuộc tính kế thừa
    @NotEmpty(message = "The Employee Name is required")
    @Override
    public String getName() {
        return super.getName();
    }
    
    @Override
    public void setName(String name) {
        super.setName(name);
    }
    
    @NotEmpty(message = "The Employee Address is required")
    @Override
    public String getAddress() {
        return super.getAddress();
    }

    @Override
    public void setAddress(String address) {
        super.setAddress(address);
    }
    
    @NotEmpty(message = "The Employee Phone Number is required")
    @Override
    public String getPhone() {
        return super.getPhone();
    }

    @Override
    public void setPhone(String phone) {
        super.setPhone(phone);
    }
    
    @NotEmpty(message = "The Employee Email Address is required")
    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }
}
