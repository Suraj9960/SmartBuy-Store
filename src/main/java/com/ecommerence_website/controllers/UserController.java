package com.ecommerence_website.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ecommerence_website.entity.Cart;
import com.ecommerence_website.entity.User;
import com.ecommerence_website.serviceDaoImpl.CartDaoImpl;
import com.ecommerence_website.serviceDaoImpl.UserDaoImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDaoImpl userDaoImpl;

	@Autowired
	private CartDaoImpl cartDaoImpl;
	
	/* Index Page */

	@GetMapping("/")
	public String homePage() {
		return "Home-Page";
	}

	@GetMapping("navbar")
	public String navbar(Model model, Principal principal) {
		/* Add To Cart */

		User user = userDaoImpl.getByEmail(principal.getName());

		List<Cart> carts = cartDaoImpl.getAllCartByUser(user);

		if (carts.isEmpty()) {
			carts = new ArrayList<>();
		} 
		
		Cart cart = user.getCart();
		
		if( cart == null) {
			cart = new Cart();
		}
		model.addAttribute("cart", cart);
		
		return "Navbar";
	}

	@GetMapping("footer")
	public String footer() {
		return "Footer";
	}

	@GetMapping("/signup")
	public String login(@RequestParam(value = "error", required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("errorMsg", "Signup failed. Please try again.");
		}
		return "Login-Page";
	}

	@GetMapping("register-page")
	public String register(Model model, User us) {
		return "Register-Page";
	}

	@PostMapping("do-register")
	public String register_page(@Valid @ModelAttribute User user, BindingResult result, Model model,
			@RequestParam(value = "checked", defaultValue = "false") boolean checked, HttpSession session) throws UnsupportedEncodingException {

		if (result.hasErrors()) {
			FieldError fullname = result.getFieldError("fullname");
			FieldError email = result.getFieldError("emailAddress");
			FieldError password = result.getFieldError("password");

			model.addAttribute("password", password.getDefaultMessage());
			model.addAttribute("name", fullname.getDefaultMessage());
			model.addAttribute("email", email.getDefaultMessage());

			return "Register-Page";
		}

		if (user.getEmailAddress().equals("admin@gmail.com")) {
			user.setRole("ROLE_ADMIN");
		} else {
			user.setRole("ROLE_USER");
		}

		User user2 = userDaoImpl.getByEmail(user.getEmailAddress());

		if (user2 != null) {
			model.addAttribute("msg", "User is already register with this email address !!");
		} else {
			if (checked) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				userDaoImpl.createUser(user);
				model.addAttribute("msg", "User Registered Successfully !! ");
			}
		}
		return "Register-Page";
	}

}
