package com.boostmytool.model.person;

import java.sql.Date;

import jakarta.persistence.*;


@Entity
@Table(name = "Customer")
public class Customer extends Persons{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private Date customerDateCreated;
	private Date customerDateUpdated;
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
