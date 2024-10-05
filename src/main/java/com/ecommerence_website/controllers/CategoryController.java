package com.ecommerence_website.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerence_website.entity.Category;
import com.ecommerence_website.entity.User;
import com.ecommerence_website.serviceDaoImpl.CategoryDaoImpl;
import com.ecommerence_website.serviceDaoImpl.UserDaoImpl;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/ecommerence/admin/")
public class CategoryController {
	
	@Autowired
	private CategoryDaoImpl categoryDaoImpl;
	
	@Autowired
	private UserDaoImpl userDaoImpl;
	
	public void commonData(Model model, Principal principal) {
		String email = principal.getName();

		User user = userDaoImpl.getByEmail(email);
		model.addAttribute("userobj", user);
	}
	
	
	@GetMapping("add-category")
	public String add_category(Principal principal, Model model) {
		commonData(model, principal);
		return "Admin/Add-Category";
	}

	@PostMapping("add_Category")
	public String addCategory(@Valid @ModelAttribute Category category, BindingResult result, Model model,
			@RequestParam("imageFile") MultipartFile file) {

		try {

			if (result.hasErrors()) {
				FieldError category_name = result.getFieldError("category_name");

				model.addAttribute("category_name", category_name.getDefaultMessage());

				return "Admin/Add-Category";

			}

			if (!file.isEmpty()) {

				String uploadDir = "static/Category_image";

				// Create the directory if it doesn't exist
				File directory = new File(uploadDir);
				if (!directory.exists()) {
					directory.mkdirs();
				}

				Path path = Paths.get(uploadDir).resolve(file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			category.setImage(file.getOriginalFilename());

			categoryDaoImpl.addCategory(category);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/ecommerence/admin/all-category";
	}

	@GetMapping("all-category")
	public String all_category(Principal principal, Model model) {
		commonData(model, principal);
		List<Category> categories = categoryDaoImpl.getAllCategories();

		model.addAttribute("categories", categories);

		return "Admin/All-Category";
	}

	@RequestMapping("delete-category")
	public String delete_category(@RequestParam("category_id") Integer category_id) {

		categoryDaoImpl.deleteCategory(category_id);

		return "redirect:/ecommerence/admin/all-category";
	}


}
