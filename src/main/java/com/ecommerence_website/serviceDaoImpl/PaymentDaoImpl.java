package com.ecommerence_website.serviceDaoImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerence_website.entity.Cart;
import com.ecommerence_website.entity.PaymentDetails;
import com.ecommerence_website.entity.Product;
import com.ecommerence_website.entity.User;
import com.ecommerence_website.jpaRepository.PaymentRepo;
import com.ecommerence_website.jpaRepository.ProductRepo;
import com.ecommerence_website.serviceDao.PaymentService;

@Service
public class PaymentDaoImpl implements PaymentService {

	@Autowired
	private PaymentRepo repo;

	@Autowired
	private ProductRepo product;

	@Override
	public List<PaymentDetails> getDetails() {

		return repo.findAll();
	}

	@Override
	public PaymentDetails findById(Integer payment_id) {
		PaymentDetails paymentDetails = repo.findById(payment_id).get();
		return paymentDetails;
	}

	@Override
	public PaymentDetails create_payment(PaymentDetails paymentDetails, Cart cart, User user) {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

		LocalDateTime now = LocalDateTime.now();

		String date = dtf.format(now);

		PaymentDetails newPaymentDetails = new PaymentDetails();
		newPaymentDetails.setFirst_name(paymentDetails.getFirst_name());
		newPaymentDetails.setLast_name(paymentDetails.getLast_name());
		newPaymentDetails.setEmail(paymentDetails.getEmail());
		newPaymentDetails.setPhone_number(paymentDetails.getPhone_number());
		newPaymentDetails.setCity(paymentDetails.getCity());
		newPaymentDetails.setState(paymentDetails.getState());
		newPaymentDetails.setZipCode(paymentDetails.getZipCode());
		newPaymentDetails.setModeOfPayment(paymentDetails.getModeOfPayment());
		newPaymentDetails.setCart(cart);
		newPaymentDetails.setUserName(user.getFullname());
		newPaymentDetails.setDateTime(date);
		newPaymentDetails.setAmount(cart.getTotalToPay());

		if (paymentDetails.getModeOfPayment().equals("COD")) {
			newPaymentDetails.setStatusOfPayment("Ordered");
		} else {
			newPaymentDetails.setStatusOfPayment("Created");
		}

		return repo.save(newPaymentDetails);
	}

	@Override
	public void canclePayment(Integer payment_id) {
		PaymentDetails paymentDetails = repo.findById(payment_id).get();
		

		repo.delete(paymentDetails);
	}

	public void delete_order(Integer payment_id) {
		PaymentDetails paymentDetails = findById(payment_id);

		Cart cart = paymentDetails.getCart();

		List<Product> products = cart.getProducts();

		for (Product pro : products) {
			pro.setConfirmed(false);
			Integer quant = pro.getAvailableQuantity() + pro.getOrderedQuantity();
			pro.setAvailableQuantity(quant);
			pro.setOrderedQuantity(0);
			product.save(pro);
			
			if(pro.getAvailableQuantity() > 0) {
				pro.setAvailability("In Stock");
			}else {
				pro.setAvailability("Out Of Stock");
			}
			product.save(pro);

		}

		repo.delete(paymentDetails);
	}

}
