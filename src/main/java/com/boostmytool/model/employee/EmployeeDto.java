package com.boostmytool.model.employee;

import java.util.Date;

import jakarta.validation.constraints.*;

public class EmployeeDto {
    
    @NotEmpty(message = "The employee ID is required")
    private String employeeID;

    @NotEmpty(message = "The employee name is required")
    @Size(min = 3, message = "The employee name should have at least 3 characters")
    private String employeeName;

    @NotEmpty(message = "The gender is required")
    @Pattern(regexp = "Male|Female|Other", message = "Gender must be Male, Female, or Other")
    private String gender;

    @NotNull(message = "The date of birth is required")
    private Date dateOfBirth;

    @NotEmpty(message = "The address is required")
    private String employeeAddress;

    @NotEmpty(message = "The phone number is required")
    @Pattern(regexp = "\\d{10,15}", message = "Phone number must be between 10 to 15 digits")
    private String employeePhoneNumber;

    @NotEmpty(message = "The email is required")
    @Email(message = "The email format is invalid")
    private String employeeEmail;

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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public String getEmployeePhoneNumber() {
        return employeePhoneNumber;
    }

    public void setEmployeePhoneNumber(String employeePhoneNumber) {
        this.employeePhoneNumber = employeePhoneNumber;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
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
}
