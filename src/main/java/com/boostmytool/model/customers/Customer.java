package com.boostmytool.model.customers;

import java.sql.Date;

import com.boostmytool.model.person.Persons;

import jakarta.persistence.*;


@Entity
@Table(name = "customers")
public class Customer extends Persons{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int totalOrder;
	private Date customerDateCreated;
	private Date customerDateUpdated;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getTotalOrder() {
		return totalOrder;
	}
	public void setTotalOrder(int totalOrder) {
		this.totalOrder = totalOrder;
	}
}
