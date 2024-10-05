package com.ecommerence_website.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerence_website.entity.Cart;
import com.ecommerence_website.entity.Product;
import com.ecommerence_website.entity.User;
import com.ecommerence_website.jpaRepository.ProductRepo;
import com.ecommerence_website.serviceDaoImpl.CartDaoImpl;
import com.ecommerence_website.serviceDaoImpl.ProductDaoImpl;
import com.ecommerence_website.serviceDaoImpl.UserDaoImpl;

@Controller
@RequestMapping("/ecommerence/users/")
public class CartController {

	@Autowired
	private UserDaoImpl userDaoImpl;

	@Autowired
	private ProductDaoImpl productDaoImpl;

	@Autowired
	private CartDaoImpl cartDaoImpl;
	
	@Autowired
	private ProductRepo repo;
	

	public void commonData(Model model, Principal principal) {
		String email = principal.getName();

		User user = userDaoImpl.getByEmail(email);
		model.addAttribute("userobj", user);
	}

	@RequestMapping("add-cart")
	public String add_cart(@RequestParam("product_id") Integer id, Principal principal, Model model,
			RedirectAttributes attributes) {

		commonData(model, principal);

		String loggedUsername = principal.getName();

		User user = userDaoImpl.getByEmail(loggedUsername);

		Product product = productDaoImpl.findById(id);

		boolean added = cartDaoImpl.addToCart(user, product);
		if (added) {
			attributes.addFlashAttribute("added", "Added to Cart !!");
			
		} else {
			attributes.addFlashAttribute("added", "Already added to Cart !!");
		}

		return "redirect:/ecommerence/users/user-dashboard";
	}

	@PostMapping("update-cart")
	public String updateCart(Model model, Principal principal, @RequestParam("product_id") int product_id,
			@RequestParam("quantity") int quantity) {
		commonData(model, principal);
		String user = principal.getName();
		User user2 = userDaoImpl.getByEmail(user);
		cartDaoImpl.updateCart(product_id, user2, quantity);

		return "redirect:/ecommerence/users/shopping-cart";

	}

	@GetMapping("shopping-cart")
	public String shopping_cart(Model model, Principal principal) {
		commonData(model, principal);

		User user = userDaoImpl.getByEmail(principal.getName());

		List<Cart> getCarts = cartDaoImpl.getAllCartByUser(user);

		if (getCarts.isEmpty()) {
			getCarts = new ArrayList<>();
		}

		Cart cart = user.getCart();

		if (cart == null) {
			cart = new Cart();
		}

		model.addAttribute("cart", cart);

		List<Product> products = cart.getProducts();

		if (products == null || products.isEmpty()) {
			model.addAttribute("products", null);
		} else {
			model.addAttribute("products", products);
		}

		return "User/Shopping-cart";
	}

	/* Delete Product from cart */

	@RequestMapping("delete-cart")
	public String deleteFromCart(@RequestParam("cart_id") Integer id, @RequestParam("product_id") Integer product_id,
			RedirectAttributes model) {

		cartDaoImpl.deleteFromCart(id, product_id);
		
		Product product = productDaoImpl.findById(product_id);
		
		product.setOrdered(false);
		
		repo.save(product);

		model.addFlashAttribute("remove", "Removed From Cart !!");

		return "redirect:/ecommerence/users/shopping-cart";
	}

}
