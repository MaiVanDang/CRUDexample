package com.boostmytool.model.customers;

import java.sql.Date;

import jakarta.persistence.*;


@Entity
@Table(name = "Customer")
public class Customer {
	
	@Id
	private String customerID;
	private String customerName;
	private Date customerDOB;
	private String customerGender;
	private String customerAddress;
	private String customerPhone;
	private String customerEmail;
	private Date customerDateCreated;
	private Date customerDateUpdated;
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Date getCustomerDOB() {
		return customerDOB;
	}
	public void setCustomerDOB(Date customerDOB) {
		this.customerDOB = customerDOB;
	}
	public String getCustomerGender() {
		return customerGender;
	}
	public void setCustomerGender(String customerGender) {
		this.customerGender = customerGender;
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
	
//	@Column(columnDefinition = "TEXT")
	
	
}
