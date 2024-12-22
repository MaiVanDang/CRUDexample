package com.boostmytool.model.person;

import java.sql.Date;
import java.time.LocalDate;

import jakarta.validation.constraints.*;

public class CustomerDto {
	
	@NotEmpty(message = "The Customer Name is required")
	private String customerName;
	
	@NotEmpty(message = "The Customer Address is required")
	private String customerAddress;
	
	@NotEmpty(message = "The Customer Phone Number is required")
	private String customerPhone;
	
	@NotEmpty(message = "The Customer Email Address is required")
	private String customerEmail;
	
	private LocalDate customerDOB;
	
	private Date customerDateCreated;
	
	private Date customerDateUpdated;
	
	private String customerGender;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public LocalDate getCustomerDOB() {
		return customerDOB;
	}

	public void setCustomerDOB(LocalDate customerDOB) {
		this.customerDOB = customerDOB;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
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
