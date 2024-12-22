package com.boostmytool.model.person;

import java.sql.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee extends Persons{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String position;
    private float salary;
    private String employeeStatus;
    
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
//    public void setEmployee(String employeeName, String gender, Date dateOfBirth, String employeeAddress,
//                            String employeePhoneNumber, String employeeEmail, String position, float salary, String employeeStatus) {
//        this. = employeeName;
//        this.gender = gender;
//        this.dateOfBirth = dateOfBirth;
//        this.employeeAddress = employeeAddress;
//        this.employeePhoneNumber = employeePhoneNumber;
//        this.employeeEmail = employeeEmail;
//        this.position = position;
//        this.salary = salary;
//        this.employeeStatus = employeeStatus;
//    }

    // Phương thức trả về thông tin nhân viên
//    public String getEmployeeDetails() {
//        return "Employee ID: " + employeeID + "\nName: " + employeeName + "\nGender: " + gender +
//               "\nDate of Birth: " + dateOfBirth + "\nAddress: " + employeeAddress + 
//               "\nPhone: " + employeePhoneNumber + "\nEmail: " + employeeEmail + 
//               "\nPosition: " + position + "\nSalary: " + salary + "\nStatus: " + employeeStatus;
//    }
}
