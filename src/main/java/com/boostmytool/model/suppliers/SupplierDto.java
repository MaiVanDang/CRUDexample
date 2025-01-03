package com.boostmytool.model.suppliers;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;

public class SupplierDto {
	@NotEmpty(message = "The name is required")
	private String name;
	
	@Size(min = 0, message = "The address should be at lease 10 chareacters")
	@Size(max = 2000, message = "The address cannot exceed 2000 characters")	
	private String address;
	
	@Size(min = 10, message = "The description should be at least 10 characters")
	@Size(max = 2000, message = "The description cannot exceed 2000 characters")
	private String description;
	
	private MultipartFile imageLogo;
    @NotEmpty(message = "The phone is required")
    @Pattern(regexp = "\\d{10,15}", message = "The phone must be a valid number with 10-15 digits")
    private String phone;

    @NotEmpty(message = "The email is required")
    @Email(message = "The email must be a valid email address")
    private String email;

    // Getter và Setter
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
	public String getName() {
		return name;
	}
	

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getImageLogo() {
		return imageLogo;
	}

	public void setImageLogo(MultipartFile imageLogo) {
		this.imageLogo = imageLogo;
	}
}