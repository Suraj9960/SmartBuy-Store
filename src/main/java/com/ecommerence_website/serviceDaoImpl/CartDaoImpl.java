package com.ecommerence_website.serviceDaoImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerence_website.entity.Cart;
import com.ecommerence_website.entity.Product;
import com.ecommerence_website.entity.User;
import com.ecommerence_website.jpaRepository.CartRepo;
import com.ecommerence_website.jpaRepository.ProductRepo;
import com.ecommerence_website.serviceDao.CartService;

@Service
public class CartDaoImpl implements CartService {

	@Autowired
	private CartRepo repo;

	@Autowired
	private ProductRepo repo2;

	@Override

	public Cart addToCart(Cart cart) {
		return repo.save(cart);
	}

	public boolean addToCart(User user, Product product) {
		// Retrieve the existing cart for the user
		Cart cart = user.getCart();

		if (cart == null) {
			cart = new Cart();
			cart.setUser(user);
		}

		// Retrieve the list of products in the cart
		List<Product> products = cart.getProducts();
		if (products == null) {
			products = new ArrayList<>();
		}

		// Check if the product is already present in the cart

		for (Product pro : products) {
			if (product.equals(pro)) {

				return false;
			}
		}

		// If the product is not already in the cart, add it
		products.add(product);
		cart.setProducts(products);

		// Save or update the cart
		addToCart(cart);

		return true;
	}

	@Override
	public void deleteFromCart(Integer cart_id, Integer product_id) {
		Cart cart = repo.findById(cart_id).get();

		Product product = repo2.findById(product_id).get();
		Integer quant = product.getAvailableQuantity();
		Integer ordered = product.getOrderedQuantity();

		product.setOrderedQuantity(0);
		Integer total = quant + ordered;
		product.setAvailableQuantity(total);

		if (total > 0) {
			product.setAvailability("In Stock");
		} else {
			product.setAvailability("Out Of Stock");
		}

		repo2.save(product);

		Integer productCost = 0;

		if (cart.getProducts().contains(product)) {
			productCost = product.getOrderedQuantity() * product.getDiscountedPrice();

			cart.removeProduct(product);

			Integer totalPay = cart.getTotalToPay();
			totalPay -= productCost;
			cart.setSubtotal(cart.getSubtotal() - productCost);
			if (cart.getSubtotal().equals(0)) {
				cart.setTotalOrderQuantity(0);
			}
			cart.setTotalToPay(totalPay);

			repo.save(cart);
		}
		
		if(cart.getProducts().isEmpty()) {
			cart.setSubtotal(0);
			cart.setTotalToPay(0);
			repo.save(cart);
		}
		
		repo.delete(cart);

	}

	@Override
	public List<Cart> getAllCarts() {

		return repo.findAll();
	}

	public Cart findCartBCartBYId(Integer cart_id) {
		return repo.findById(cart_id).get();
	}

	public List<Cart> getAllCartByUser(User user) {

		Cart cart = user.getCart();

		List<Cart> carts = new ArrayList<>();

		carts.add(cart);

		return carts;

	}

	public Cart updateCart(int productId, User user, int quantity) {

		int total = 0;

		Cart cart2 = user.getCart(); // get the cart

		List<Product> products = cart2.getProducts(); // get the list of products

		Product p = repo2.findById(productId).get();// find the product by pid

		for (Product product : products) {
			if (p.equals(product)) {
				Integer quant = p.getAvailableQuantity() - quantity;
				p.setOrderedQuantity(quantity);
				p.setAvailableQuantity(quant);
			}

			if (p.getAvailableQuantity() > 0) {
				p.setAvailability("In Stock");
			} else {
				p.setAvailability("Out Of Stock");
			}

			repo2.save(p);

			total += product.getOrderedQuantity();
		}

		cart2.setTotalOrderQuantity(total);

		Integer totalPrice = 0;

		for (Product pro : products) {
			totalPrice += pro.getDiscountedPrice() * pro.getOrderedQuantity();
		}
		cart2.setSubtotal(totalPrice);
		cart2.setShipping(20);
		cart2.setTotalToPay(totalPrice + 20);

		return repo.save(cart2);
	}

}
