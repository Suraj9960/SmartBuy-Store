package com.ecommerence_website.controllers;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ecommerence_website.entity.User;
import com.ecommerence_website.serviceDaoImpl.UserDaoImpl;


@Controller
@RequestMapping("/ecommerence/admin/")
public class AdminController {

	@Autowired
	private UserDaoImpl userDaoImpl;


	public void commonData(Model model, Principal principal) {
		String email = principal.getName();

		User user = userDaoImpl.getByEmail(email);
		model.addAttribute("userobj", user);
	}

	@GetMapping("base2")
	public String base() {
		return "navbar";
	}

	@GetMapping("admin-dashboard")
	public String userDashboard(Principal principal, Model model) {

		commonData(model, principal);
		
		String userString = principal.getName();
		User user = userDaoImpl.getByEmail(userString);
		model.addAttribute("name", user.getFullname());

		return "Admin/Admin-Dashboard";
	}
	
	@GetMapping("all-users")
	public String all_users(Principal principal, Model model) {
		commonData(model, principal);

		List<User> onlyUsers = userDaoImpl.onlyUser();

		model.addAttribute("users", onlyUsers);

		return "Admin/All-Users";
	}

	@RequestMapping("delete-user")
	public String deleteUser(@RequestParam("user_id") Integer user_id) {

		userDaoImpl.deleteUser(user_id);
		return "redirect:/ecommerence/admin/all-users";
	}

}
