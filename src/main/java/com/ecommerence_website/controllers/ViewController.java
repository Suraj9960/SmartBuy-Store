package com.ecommerence_website.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ecommerence_website.entity.Banner;
import com.ecommerence_website.entity.Cart;
import com.ecommerence_website.entity.Category;
import com.ecommerence_website.entity.PaymentDetails;
import com.ecommerence_website.entity.Product;
import com.ecommerence_website.entity.User;
import com.ecommerence_website.serviceDaoImpl.BannerDaoImpl;
import com.ecommerence_website.serviceDaoImpl.CartDaoImpl;
import com.ecommerence_website.serviceDaoImpl.CategoryDaoImpl;
import com.ecommerence_website.serviceDaoImpl.PaymentDaoImpl;
import com.ecommerence_website.serviceDaoImpl.ProductDaoImpl;
import com.ecommerence_website.serviceDaoImpl.UserDaoImpl;

@Controller
@RequestMapping("/ecommerence/users/")
public class ViewController {

	@Autowired
	private UserDaoImpl userDaoImpl;

	@Autowired
	private BannerDaoImpl bannerDaoImpl;

	@Autowired
	private CategoryDaoImpl categoryDaoImpl;

	@Autowired
	private ProductDaoImpl productDaoImpl;

	@Autowired
	private CartDaoImpl cartDaoImpl;

	@Autowired
	private PaymentDaoImpl paymentDaoImpl;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void commonData(Model model, Principal principal) {

		if (principal.getName() == null) {
			// handle the exception

			throw new NullPointerException("Something Went Wrong");
		}
		String email = principal.getName();

		User user = userDaoImpl.getByEmail(email);
		model.addAttribute("userobj", user);
	}

	@GetMapping("user-dashboard")
	public String userDashboard(Principal principal, Model model) {

		commonData(model, principal);

		List<Banner> banners = bannerDaoImpl.getAllBanners();

		model.addAttribute("banners", banners);

		List<Category> categories = categoryDaoImpl.getAllCategories();
		model.addAttribute("categories", categories);

		List<Product> products = productDaoImpl.getAllProducts();

		if (products.isEmpty()) {
			products = new ArrayList<>();
		} else {
			model.addAttribute("products", products);
		}

		/* Add To Cart */

		User user = userDaoImpl.getByEmail(principal.getName());

		List<Cart> carts = cartDaoImpl.getAllCartByUser(user);

		if (carts.isEmpty()) {
			carts = new ArrayList<>();
		} else {
			model.addAttribute("carts", carts);
		}

		return "User/User-Dashboard";
	}

	/* View Controller */

	@GetMapping("view-page")
	public String viewProduct(@RequestParam("product_id") Integer id, Model model, Principal principal) {
		commonData(model, principal);

		Product product = productDaoImpl.findById(id);

		model.addAttribute("product", product);

		Category category = product.getCategories();
		model.addAttribute("c_name", category.getCategory_name());
		model.addAttribute("ava", product.getAvailability());

		/* Add To Cart */

		User user = userDaoImpl.getByEmail(principal.getName());

		List<Cart> carts = cartDaoImpl.getAllCartByUser(user);
		if (carts == null) {
			carts = new ArrayList<Cart>();
		}
		model.addAttribute("carts", carts);

		boolean f = false;

		for (Cart cart2 : carts) {
			
			if (cart2 == null  || cart2.getProducts() == null) {
				continue;
			}
			
			List<Product> products = cart2.getProducts();

			for (Product p : products) {
				if (p.getProduct_id().equals(id)) {
					f = true;
					model.addAttribute("added", f);
				}
			}
		}

		return "User/View-Page";
	}

	@RequestMapping("add-to-cart")
	public String addCart(@RequestParam("pid") Integer pid, Model model, Principal principal) {
		commonData(model, principal);

		Product product = productDaoImpl.findById(pid);

		User user = userDaoImpl.getByEmail(principal.getName());

		boolean cart = cartDaoImpl.addToCart(user, product);

		if (cart) {
			model.addAttribute("msg", "Added To Cart !!");
		}

		return "redirect:/ecommerence/users/view-page?product_id=" + pid;
	}

	@RequestMapping("buy-now")
	public String buy_product(@RequestParam("pid") Integer pid, Model model, Principal principal) {
		commonData(model, principal);

		User user = userDaoImpl.getByEmail(principal.getName());

		Product product = productDaoImpl.findById(pid);

		cartDaoImpl.addToCart(user, product);
		System.out.println("added to cart successfully");

		return "redirect:/ecommerence/users/shopping-cart";
	}

	/* Profile API */
	@GetMapping("profile")
	public String porfile(Model model, Principal principal) {

		commonData(model, principal);

		User user = userDaoImpl.getByEmail(principal.getName());

		String userName = user.getFullname();

		model.addAttribute("name", userName);

		return "User/Profile/Profile-Page";
	}

	@GetMapping("all-orders")
	public String orders(Model model, Principal principal) {

		commonData(model, principal);

		List<PaymentDetails> paymentDetails = paymentDaoImpl.getDetails();

		model.addAttribute("details", paymentDetails);

		return "User/Profile/All-Order";
	}

	@GetMapping("account-details")
	public String account_details(Model model, Principal principal) {

		commonData(model, principal);

		User user = userDaoImpl.getByEmail(principal.getName());
		model.addAttribute("userobj", user);

		return "User/Profile/Account-Details";
	}

	@GetMapping("transction-details")
	public String transction_details(Model model, Principal principal) {

		commonData(model, principal);

		List<PaymentDetails> paymentDetails = paymentDaoImpl.getDetails();

		model.addAttribute("details", paymentDetails);

		return "User/Profile/Transction-Details";
	}

	@PostMapping("update-profile")
	public String updateProfile(@ModelAttribute User user, Principal principal, Model model) {

		String username = principal.getName();

		User user2 = userDaoImpl.getByEmail(username);

		user2.setPassword(passwordEncoder.encode(user.getPassword()));

		userDaoImpl.updateProfile(user, username);

		return "redirect:/signup";
	}

	@PostMapping("update-address")
	public String updateAddress(@ModelAttribute User user, Principal principal, Model model) {

		String username = principal.getName();

		userDaoImpl.updateAddress(user, username);

		return "redirect:/ecommerence/users/account-details";
	}

}
