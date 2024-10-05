package com.ecommerence_website.controllers;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerence_website.entity.BrandDetails;
import com.ecommerence_website.entity.Category;
import com.ecommerence_website.entity.Product;
import com.ecommerence_website.entity.User;
import com.ecommerence_website.serviceDaoImpl.BrandDaoImpl;
import com.ecommerence_website.serviceDaoImpl.CategoryDaoImpl;
import com.ecommerence_website.serviceDaoImpl.ProductDaoImpl;
import com.ecommerence_website.serviceDaoImpl.UserDaoImpl;

@Controller
@RequestMapping("/ecommerence/users/")
public class ShopController {

	@Autowired
	private UserDaoImpl userDaoImpl;

	@Autowired
	private CategoryDaoImpl categoryDaoImpl;

	@Autowired
	private ProductDaoImpl productDaoImpl;

	@Autowired
	private BrandDaoImpl brandDaoImpl;
	
	
	public void common_method(Model model) {
		List<BrandDetails> brandDetails = brandDaoImpl.allDetails();

		List<String> brand_name = brandDaoImpl.all_brand_name(brandDetails);

		model.addAttribute("brand", brand_name);

		List<String> os = brandDaoImpl.all_operating_system(brandDetails);

		model.addAttribute("os", os);

		List<String> type_of_product = brandDaoImpl.all_type(brandDetails);

		model.addAttribute("type", type_of_product);
	}

	public void commonData(Model model, Principal principal) {
		String email = principal.getName();

		User user = userDaoImpl.getByEmail(email);
		model.addAttribute("userobj", user);
	}

	@GetMapping("shop-page")
	public String shop(Principal principal, Model model) {
		commonData(model, principal);

		List<Product> products = productDaoImpl.getAllProducts();
		model.addAttribute("products", products);
		
		common_method(model);
		

		return "User/Shop-Page";
	}

	/* User Shoping Details */

	@GetMapping("headphones")
	public String headphone(Principal principal, Model model) {
		commonData(model, principal);

		Category category2 = categoryDaoImpl.findByName("Headphones");

		List<Product> products = productDaoImpl.findByCategoryName(category2);

		model.addAttribute("products", products);
		
		common_method(model);

		return "User/Headphone-page";
	}

	@GetMapping("earphones")
	public String earphone(Principal principal, Model model) {
		commonData(model, principal);

		Category category2 = categoryDaoImpl.findByName("Earphones");

		List<Product> products = productDaoImpl.findByCategoryName(category2);

		model.addAttribute("products", products);
		
		common_method(model);

		return "User/Earphone-page";
	}

	@GetMapping("smartwatches")
	public String smartwatches(Model model, Principal principal) {
		commonData(model, principal);

		Category category2 = categoryDaoImpl.findByName("Smartwatch");

		List<Product> products = productDaoImpl.findByCategoryName(category2);

		model.addAttribute("products", products);
		
		common_method(model);

		return "User/Smartwatch-page";
	}

	@GetMapping("smartphones")
	public String smartphones(Model model, Principal principal) {
		commonData(model, principal);

		Category category2 = categoryDaoImpl.findByName("Smartphones");

		List<Product> products = productDaoImpl.findByCategoryName(category2);

		model.addAttribute("products", products);
		
		common_method(model);

		return "User/Smartphone-page";
	}

	@GetMapping("computers")
	public String Laptops(Model model, Principal principal) {
		commonData(model, principal);

		Category category2 = categoryDaoImpl.findByName("Laptops");

		List<Product> products = productDaoImpl.findByCategoryName(category2);

		model.addAttribute("products", products);
		
		common_method(model);

		return "User/Computers";
	}

	@GetMapping("other-accessories")
	public String others(Principal principal, Model model) {

		commonData(model, principal);

		Category category2 = categoryDaoImpl.findByName("Tablets");

		List<Product> products = productDaoImpl.findByCategoryName(category2);

		model.addAttribute("products", products);
		
		common_method(model);

		return "User/Other-acc";
	}

}
