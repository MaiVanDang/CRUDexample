package com.boostmytool.model.customers;

import java.sql.Date;

import jakarta.persistence.*;


@Entity
@Table(name = "Customer")
public class Customer {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int customerID;
	
	private String customerName;
	private Date customerDOB;
	private String customerGender;
	private String customerAddress;
	private String customerPhone;
	private String customerEmail;
	private Date customerDateCreated;
	private Date customerDateUpdated;
	private double customerPaidAmount;
	private double customerSumDebt;
	private String customerType;
	public int getCustomerID() {
		return customerID;
	}
	public void setCustomerID(int customerID) {
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
	public double getCustomerPaidAmount() {
		return customerPaidAmount;
	}
	public void setCustomerPaidAmount(double customerPaidAmount) {
		this.customerPaidAmount = customerPaidAmount;
	}
	public double getCustomerSumDebt() {
		return customerSumDebt;
	}
	public void setCustomerSumDebt(double customerSumDebt) {
		this.customerSumDebt = customerSumDebt;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	
//	@Column(columnDefinition = "TEXT")
	
	
}
