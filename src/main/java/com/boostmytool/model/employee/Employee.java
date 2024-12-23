package com.boostmytool.model.employee;

import com.boostmytool.model.person.Persons;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee extends Persons{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private float salary;
    private String status;
    private String position;

    // Constructor mặc định
    public Employee() {}

    // Getter và Setter cho tất cả thuộc tính

    public float getSalary() {
        return salary;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSalary(float salary) {
        this.salary = salary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

    
}
