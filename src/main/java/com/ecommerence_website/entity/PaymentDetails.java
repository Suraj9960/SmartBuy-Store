package com.ecommerence_website.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer payment_id;
	
	@NotBlank(message = "It is Mandetory")
	private String first_name;
	@NotBlank(message = "It is Mandetory")
	private String last_name;
	@Email
	@NotBlank(message = "Please Provide Valid Email")
	private String email;
	
	private String phone_number;
	@NotBlank(message = "Please Enter correct city")
	private String city;
	
	private String zipCode;
	@NotBlank(message = "Please Enter correct state")
	private String state;
	
	private String modeOfPayment;
	
	private String statusOfPayment;
	
	private String orderId;

	private Integer amount;

	private String receipt;

	private String dateTime;

	private String userName;

	@OneToOne
	private Cart cart;
	
	//private List<Integer> orderedProducts;
	
	@ManyToMany
    private List<Product> orderedProducts; 
	
	
}
