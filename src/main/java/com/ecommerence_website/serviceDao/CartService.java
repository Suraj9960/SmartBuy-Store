package com.ecommerence_website.serviceDao;

import java.util.List;

import com.ecommerence_website.entity.Cart;

public interface CartService {
	
	public Cart addToCart(Cart cart);
	
	public void deleteFromCart(Integer cart_id , Integer product_id);
	
	public List<Cart> getAllCarts();
}
