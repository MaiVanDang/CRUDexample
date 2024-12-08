package com.boostmytool.model.orders;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name = "orders")
public class Order {
	
	@Id
	private String orderID;
	
    private String customerID;
    private String productID;
    private int quantity;
    private float price;
    
    @Column (columnDefinition = "TEXT")
    private String promotion;
    private Date createdAt;
    private Date updatedAt;
    private Date estimatedDeliveryDate;
    private String paymentMethod;
    private String paymentStatus;
    private String orderStatus;
    private String note;
    
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getPromotion() {
		return promotion;
	}
	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Date getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}
	public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) {
		this.estimatedDeliveryDate = estimatedDeliveryDate;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	public Order() {
		
	}
	
	public Order(String orderID, String customerID, String productID, int quantity, float price, String promotion,
			Date estimatedDeliveryDate, String paymentMethod, String paymentStatus, String orderStatus, String note) {
		super();
		this.orderID = orderID;
		this.customerID = customerID;
		this.productID = productID;
		this.quantity = quantity;
		this.price = price;
		this.promotion = promotion;
		
		LocalDate current_date = LocalDate.now();
		Date current = Date.valueOf(current_date);
		this.setCreatedAt(current);
		this.setUpdatedAt(current);
		
		this.estimatedDeliveryDate = estimatedDeliveryDate;
		this.paymentMethod = paymentMethod;
		this.paymentStatus = paymentStatus;
		this.orderStatus = orderStatus;
		this.note = note;
	}
	public Order(String orderID, String customerID, String productID, int quantity, float price, String promotion,
			Date createdAt, Date updatedAt, Date estimatedDeliveryDate, String paymentMethod, String paymentStatus,
			String orderStatus, String note) {
		super();
		this.orderID = orderID;
		this.customerID = customerID;
		this.productID = productID;
		this.quantity = quantity;
		this.price = price;
		this.promotion = promotion;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.estimatedDeliveryDate = estimatedDeliveryDate;
		this.paymentMethod = paymentMethod;
		this.paymentStatus = paymentStatus;
		this.orderStatus = orderStatus;
		this.note = note;
	}
	
	
    
    
}
