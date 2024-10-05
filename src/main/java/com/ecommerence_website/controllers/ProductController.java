package com.ecommerence_website.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerence_website.entity.Category;
import com.ecommerence_website.entity.Product;
import com.ecommerence_website.entity.BrandDetails;
import com.ecommerence_website.entity.User;
import com.ecommerence_website.serviceDaoImpl.BrandDaoImpl;
import com.ecommerence_website.serviceDaoImpl.CategoryDaoImpl;
import com.ecommerence_website.serviceDaoImpl.ProductDaoImpl;
import com.ecommerence_website.serviceDaoImpl.UserDaoImpl;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/ecommerence/admin/")
public class ProductController {
	
	@Autowired
	private ProductDaoImpl productDaoImpl;
	
	@Autowired
	private CategoryDaoImpl categoryDaoImpl;
	
	@Autowired
	private BrandDaoImpl daoImpl;
	
	
	@Autowired
	private UserDaoImpl userDaoImpl;
	
	public void commonData(Model model, Principal principal) {
		String email = principal.getName();

		User user = userDaoImpl.getByEmail(email);
		model.addAttribute("userobj", user);
	}
	
	
	@GetMapping("all-products")
	public String all_products(Principal principal, Model model) {
		commonData(model, principal);

		List<Product> products = productDaoImpl.getAllProducts();
		model.addAttribute("products", products);

		return "Admin/All-Product";
	}

	@GetMapping("add-products")
	public String add_products(Principal principal, Model model) {
		commonData(model, principal);

		List<Category> categories = categoryDaoImpl.getAllCategories();
		model.addAttribute("categories", categories);

		return "Admin/Add-Product";
	}

	@PostMapping("add_product")
	public String addProduct(@ModelAttribute Product product, Model model,
			@RequestParam("choose") Integer selectedCategory, @RequestParam("product_image") MultipartFile file,
			HttpServletRequest request) throws IOException {

		if (!file.isEmpty()) {

			String uploadDir = "static/Product_image";

			// Create the directory if it doesn't exist
			File directory = new File(uploadDir);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			Path path = Paths.get(uploadDir).resolve(file.getOriginalFilename());

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		}

		product.setProductsImage(file.getOriginalFilename());

		product.setCategories(categoryDaoImpl.findById(selectedCategory));

		Integer discount = product.getCategories().getDiscount();

		Integer price = (int) ((discount / 100.0) * product.getPrice());

		Integer discountedPrice = product.getPrice() - price;
		product.setDiscountedPrice(discountedPrice);

		productDaoImpl.addProduct(product);
		return "redirect:/ecommerence/admin/all-products";
	}
	
	@GetMapping("update-product")
	public String update(Principal principal , Model model , @RequestParam("product_id") Integer id) {
		
		model.addAttribute("id", id);
		
		
		commonData(model, principal);
		
		
		return "Admin/Update-Product";
	}
	
	@PostMapping("add-details")
	public String update_products(@ModelAttribute BrandDetails productDetails , @RequestParam("id") Integer id) {	

		daoImpl.getDetail(id ,productDetails);
		
		
		return "redirect:/ecommerence/admin/all-products";
		
	}

	@RequestMapping("delete-product")
	public String deleteProduct(@RequestParam("product_id") Integer product_id) {
		productDaoImpl.deleteProduct(product_id);

		return "redirect:/ecommerence/admin/all-products";
	}

	
	
	


}
