package com.boostmytool.model.employee;

import java.sql.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    private String employeeID;
    private String employeeName;
    private String gender;
    private Date dateOfBirth;
    private String employeeAddress;
    private String employeePhoneNumber;
    private String employeeEmail;
    private String position;
    private float salary;
    private String employeeStatus;

    // Constructor mặc định
    public Employee() {}

    // Constructor đầy đủ
    public Employee(String employeeID, String employeeName, String gender, Date dateOfBirth, String employeeAddress,
                    String employeePhoneNumber, String employeeEmail, String position, float salary, String employeeStatus) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.employeeAddress = employeeAddress;
        this.employeePhoneNumber = employeePhoneNumber;
        this.employeeEmail = employeeEmail;
        this.position = position;
        this.salary = salary;
        this.employeeStatus = employeeStatus;
    }

    // Getter và Setter cho tất cả thuộc tính
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

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    // Phương thức cập nhật thông tin nhân viên
    public void setEmployee(String employeeName, String gender, Date dateOfBirth, String employeeAddress,
                            String employeePhoneNumber, String employeeEmail, String position, float salary, String employeeStatus) {
        this.employeeName = employeeName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.employeeAddress = employeeAddress;
        this.employeePhoneNumber = employeePhoneNumber;
        this.employeeEmail = employeeEmail;
        this.position = position;
        this.salary = salary;
        this.employeeStatus = employeeStatus;
    }

    // Phương thức trả về thông tin nhân viên
    public String getEmployeeDetails() {
        return "Employee ID: " + employeeID + "\nName: " + employeeName + "\nGender: " + gender +
               "\nDate of Birth: " + dateOfBirth + "\nAddress: " + employeeAddress + 
               "\nPhone: " + employeePhoneNumber + "\nEmail: " + employeeEmail + 
               "\nPosition: " + position + "\nSalary: " + salary + "\nStatus: " + employeeStatus;
    }
}
