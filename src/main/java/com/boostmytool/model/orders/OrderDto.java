package com.boostmytool.model.orders;
import java.sql.Date;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
public class OrderDto {
	@NotEmpty(message = "The quantity is required")
	private String customerID;
	
	@NotEmpty(message = "The quantity is required")
    private String productID;
    
	@Min(0)
	 private int quantity;
	 
	 @Min(0)
	 private float price;
	 
	 @Min(0)
	 private float cost;
	 
	 @NotEmpty(message = "The quantity is required")
	 private String promotion;
	 
	 private Date createdAt;
	 
	 private Date updatedAt;
	 
	 private Date estimatedDeliveryDate;
	 
	 @NotEmpty(message = "The quantity is required")
	 private String paymentMethod;//check lai
	 
	 @NotEmpty(message = "The quantity is required")
	 private String paymentStatus;
	 
	 @NotEmpty(message = "The quantity is required")
	 private String orderStatus;
	 
	 @Size(max = 2000, message = "The note cannot exceed 2000 characters")
	 private String note;
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
	public float getCost() {
		return cost;
	}
	public void setCost(float cost) {
		this.cost = cost;
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
	 
}