package com.boostmytool.model.employee;

import com.boostmytool.model.person.Persons;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee extends Persons{

    @Id
    private String employeeID;
    private String position;
    private float salary;
    private String employeeStatus;

    // Constructor mặc định
    public Employee() {}

    // Getter và Setter cho tất cả thuộc tính
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

    
}
