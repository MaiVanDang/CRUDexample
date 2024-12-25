package com.boostmytool.model.employee;

import com.boostmytool.model.person.Persons;

import jakarta.validation.constraints.*;

public class EmployeeDto extends Persons {

	@Min(value = 0, message = "Salary must be at least 0")
	private double salary;

	@NotEmpty(message = "The employee status is required")
	private String status;

	@NotEmpty(message = "The employee position is required")
	private String position;

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
}
