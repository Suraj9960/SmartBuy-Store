package com.ecommerence_website.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerence_website.entity.Product;
import com.ecommerence_website.entity.User;
import com.ecommerence_website.serviceDaoImpl.ProductDaoImpl;
import com.ecommerence_website.serviceDaoImpl.UserDaoImpl;

@Controller
@RequestMapping("/ecommerence/users/")
public class SearchController {

	@Autowired
	private UserDaoImpl userDaoImpl;

	@Autowired
	private ProductDaoImpl productDaoImpl;

	public void commonData(Model model, Principal principal) {
		String email = principal.getName();

		User user = userDaoImpl.getByEmail(email);
		model.addAttribute("userobj", user);
	}

	@GetMapping("search-product")
	public ResponseEntity<List<Product>> searchProduct(@RequestParam("query") String query, Model model,
			Principal principal) {

		commonData(model, principal);

		List<Product> products = productDaoImpl.searchList(query);

		model.addAttribute("products", products);

		return ResponseEntity.ok(products);
	}

	@GetMapping("product_list")
	public String searchProducts(@RequestParam("query") String query, Model model, Principal principal) {

		commonData(model, principal);

		List<Product> products = productDaoImpl.searchList(query);

		model.addAttribute("products", products);

		return "User/Search-Page";
	}

	/* Filter API */

	@PostMapping("filter")
	public String filter(@RequestParam("price") Integer price, @RequestParam("type") String type ,
			@RequestParam("brand") String brand, Model model, Principal principal) {
				
		if(price == null) {
			price = 0;
		}

		commonData(model, principal);

		List<Product> byprice = productDaoImpl.findByPrice(0,price);

		List<Product> byBrand = productDaoImpl.findByBrand(brand);
		
		List<Product> byType = productDaoImpl.findByType(type);
		
		List<Product> allProducts = new ArrayList<>();
		
		for(Product p1 : byBrand ) {
			
			for(Product p2 : byprice ) {
				
				for(Product p3 : byType) {
					
					if( p1 == p2 && p1 == p3 && p2 == p3) {
						allProducts.add(p1);
					}
				}
			}
		}
		
		System.out.println(allProducts);
	
		
		
		
		model.addAttribute("products", allProducts);


		return "User/shop-page";
	}

}
