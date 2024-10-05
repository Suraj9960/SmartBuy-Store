package com.ecommerence_website.serviceDao;

import java.util.List;

import com.ecommerence_website.entity.Cart;
import com.ecommerence_website.entity.PaymentDetails;
import com.ecommerence_website.entity.User;

public interface PaymentService {

	public PaymentDetails create_payment(PaymentDetails paymentDetails , Cart cart , User user);
	
	public List<PaymentDetails> getDetails();
	
	public PaymentDetails findById(Integer payment_id);
	
	public void canclePayment(Integer payment_id);
	
	
}
