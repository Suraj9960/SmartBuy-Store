package com.ecommerence_website.jpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerence_website.entity.PaymentDetails;

public interface PaymentRepo extends JpaRepository<PaymentDetails, Integer> {
	
	public PaymentDetails findByOrderId(String orderId);

}
