package com.ecommerence_website.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cart_id;

	private Integer subtotal;

	private Integer shipping;

	private Integer totalToPay;
	
	private Integer totalOrderQuantity;
	

	@ManyToMany(fetch = FetchType.EAGER)
	private List<Product> products;

	@OneToOne
	private User user;

	public void removeProduct(Product product) {
		this.products.remove(product);
	}
}
