package com.ecommerence_website.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer user_id;
	
	@NotBlank(message = "Must be contain 3 characters ")
	private String fullname;
	
	@NotBlank(message = "This field is mandatory ")
	private String emailAddress;
	@NotBlank(message = "Must Be Unique ")
	private String password;
	private String phno;
	private String address;
	private boolean checked;
	private String role;
	private String zipCode;
	private String city;
	private String state;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;
	

}
