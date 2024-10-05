package com.ecommerence_website.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer product_id;
	private String product_name;
	private String brandName;
	private Integer price;
	private String productsImage;
	private Integer discountedPrice;
	private Integer availableQuantity;
	private Integer orderedQuantity;
	private String availability = "In Stock";
	private boolean isOrdered;
	private boolean isConfirmed;
	
	@ManyToOne
	private Category categories;
	
	@ManyToMany(mappedBy = "orderedProducts")
    private List<PaymentDetails> paymentDetails;
	
	@OneToOne
	private BrandDetails brandDetails;
	

}
