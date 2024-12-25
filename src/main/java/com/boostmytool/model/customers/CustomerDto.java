package com.boostmytool.model.customers;

import java.sql.Date;

import com.boostmytool.model.person.Persons;

import jakarta.validation.constraints.NotEmpty;

public class CustomerDto extends Persons {

	private Date customerDateCreated;

	private Date customerDateUpdated;

	private String customerGender;

	// No-argument constructor
	public CustomerDto() {
	}

	// Ghi đè getter và setter cho các thuộc tính kế thừa
	@NotEmpty(message = "The Customer Name is required")
	@Override
	public String getName() {
		return super.getName();
	}

	@Override
	public void setName(String name) {
		super.setName(name);
	}

	@NotEmpty(message = "The Customer Address is required")
	@Override
	public String getAddress() {
		return super.getAddress();
	}

	@Override
	public void setAddress(String address) {
		super.setAddress(address);
	}

	@NotEmpty(message = "The Customer Phone Number is required")
	@Override
	public String getPhone() {
		return super.getPhone();
	}

	@Override
	public void setPhone(String phone) {
		super.setPhone(phone);
	}

	@NotEmpty(message = "The Customer Email Address is required")
	@Override
	public String getEmail() {
		return super.getEmail();
	}

	@Override
	public void setEmail(String email) {
		super.setEmail(email);
	}

	public Date getCustomerDateCreated() {
		return customerDateCreated;
	}

	public void setCustomerDateCreated(Date customerDateCreated) {
		this.customerDateCreated = customerDateCreated;
	}

	public Date getCustomerDateUpdated() {
		return customerDateUpdated;
	}

	public void setCustomerDateUpdated(Date customerDateUpdated) {
		this.customerDateUpdated = customerDateUpdated;
	}

	public String getCustomerGender() {
		return customerGender;
	}

	public void setCustomerGender(String customerGender) {
		this.customerGender = customerGender;
	}

}
