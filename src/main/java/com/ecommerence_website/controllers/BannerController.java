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

import com.ecommerence_website.entity.Banner;
import com.ecommerence_website.entity.User;
import com.ecommerence_website.serviceDaoImpl.BannerDaoImpl;
import com.ecommerence_website.serviceDaoImpl.UserDaoImpl;

@Controller
@RequestMapping("/ecommerence/admin/")
public class BannerController {

	@Autowired
	private BannerDaoImpl bannerDaoImpl;

	@Autowired
	private UserDaoImpl userDaoImpl;

	public void commonData(Model model, Principal principal) {
		String email = principal.getName();

		User user = userDaoImpl.getByEmail(email);
		model.addAttribute("userobj", user);
	}

	@GetMapping("all-banner")
	public String all_banner(Principal principal, Model model) {
		commonData(model, principal);
		List<Banner> banners = bannerDaoImpl.getAllBanners();
		model.addAttribute("banners", banners);

		return "Admin/All-Banner";
	}

	@GetMapping("add-banner")
	public String add_banner(Principal principal, Model model) {
		commonData(model, principal);

		return "Admin/Add-Banner";
	}

	@PostMapping("add_banner")
	public String addBanner(@ModelAttribute Banner banner, @RequestParam("img") MultipartFile file, Model model)
			throws IOException {

		if (!file.isEmpty()) {

			String uploadDir = "static/Banner_image";

			// Create the directory if it doesn't exist
			File directory = new File(uploadDir);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			Path path = Paths.get(uploadDir).resolve(file.getOriginalFilename());

			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		}

		banner.setBanner_image(file.getOriginalFilename());

		bannerDaoImpl.create_banner(banner);

		return "redirect:/ecommerence/admin/all-banner";
	}

	@RequestMapping("delete-banner")
	public String delete_banner(@RequestParam("banner_id") Integer banner_id) {
		bannerDaoImpl.delete_banner(banner_id);

		return "redirect:/ecommerence/admin/all-banner";
	}

}
