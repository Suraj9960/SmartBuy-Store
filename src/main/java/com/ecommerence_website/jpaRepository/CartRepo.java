package com.ecommerence_website.jpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerence_website.entity.Cart;
import com.ecommerence_website.entity.Product;

public interface CartRepo extends JpaRepository<Cart, Integer> {
	
	List<Cart> findAllByProductsContaining(Product product);

}
