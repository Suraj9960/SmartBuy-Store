package com.ecommerence_website.controllers;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ecommerence_website.entity.Cart;
import com.ecommerence_website.entity.PaymentDetails;
import com.ecommerence_website.entity.Product;
import com.ecommerence_website.entity.User;
import com.ecommerence_website.jpaRepository.PaymentRepo;
import com.ecommerence_website.jpaRepository.ProductRepo;
import com.ecommerence_website.serviceDaoImpl.CartDaoImpl;
import com.ecommerence_website.serviceDaoImpl.PaymentDaoImpl;
import com.ecommerence_website.serviceDaoImpl.UserDaoImpl;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/ecommerence/users/")
public class OrderController {

	@Autowired
	private UserDaoImpl userDaoImpl;

	@Autowired
	private CartDaoImpl cartDaoImpl;

	@Autowired
	private PaymentRepo orderRepository;

	@Autowired
	private PaymentDaoImpl paymentDaoImpl;

	@Autowired
	private PaymentRepo repo;

	@Autowired
	private ProductRepo repo2;


	public void commonData(Model model, Principal principal) {
		String email = principal.getName();

		User user = userDaoImpl.getByEmail(email);
		model.addAttribute("userobj", user);
	}

	@GetMapping("checkout")
	public String checkout(Model model, Principal principal, HttpServletRequest request) {

		commonData(model, principal);

		User user = userDaoImpl.getByEmail(principal.getName());
		
		model.addAttribute("userobj", user);

		List<Cart> getCarts = cartDaoImpl.getAllCartByUser(user);

		if (getCarts.isEmpty()) {
			getCarts = new ArrayList<>();
		}

		Cart cart = user.getCart();

		if (cart == null) {
			cart = new Cart();
		}
		model.addAttribute("cart", cart);

		return "User/Checkout";
	}

	// Order Creation steps
	@PostMapping("create-order")
	public String orderCreation(@ModelAttribute @Valid PaymentDetails paymentDetails, BindingResult result, Model model,
			HttpServletRequest request, @RequestParam("id") Integer cart_id, Principal principal) {

		if (result.hasErrors()) {
			FieldError f = result.getFieldError("first_name");
			FieldError l = result.getFieldError("last_name");
			FieldError e = result.getFieldError("email");
			FieldError c = result.getFieldError("city");
			FieldError s = result.getFieldError("state");

			if (f != null) {
				model.addAttribute("first_nameError", f.getDefaultMessage());
			}
			if (l != null) {
				model.addAttribute("last_nameError", l.getDefaultMessage());
			}
			if (e != null) {
				model.addAttribute("emailError", e.getDefaultMessage());
			}
			if (c != null) {
				model.addAttribute("cityError", c.getDefaultMessage());
			}
			if (s != null) {
				model.addAttribute("stateError", s.getDefaultMessage());
			}
		}

		Cart cart = cartDaoImpl.findCartBCartBYId(cart_id);

		User user = userDaoImpl.getByEmail(principal.getName());
		paymentDetails.setModeOfPayment(request.getParameter("cars"));

		PaymentDetails details = paymentDaoImpl.create_payment(paymentDetails, cart, user);

		List<Product> ordered = cart.getProducts();

		List<Product> orderedProducts = new ArrayList<>();

		for (Product product : ordered) {

			product.setOrdered(true);
			product.setConfirmed(true);

			repo2.save(product);

			if (product.isConfirmed()) {
				orderedProducts.add(product);
			}
		}

		details.setOrderedProducts(orderedProducts);
		repo.save(details);

		if ("Online Payment".equals(request.getParameter("cars"))) {
			request.getSession().setAttribute("details", details);
			return "redirect:/ecommerence/users/Online_Payment";
		} else {
			request.getSession().setAttribute("details", details);
			return "redirect:/ecommerence/users/Success";
		}
	}

	@PostMapping("create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> data, Principal principal, HttpServletRequest request,
			Model model) throws RazorpayException {

		int amt = Integer.parseInt(data.get("amount").toString());

		String ticketId = data.get("ticketId").toString();
		model.addAttribute("ticketId", ticketId);
		var client = new RazorpayClient("rzp_test_CN2otsWww3Ooa2", "MGvy27O3RvRXQN6Ba9NdJGTk");

		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", amt * 100);
		orderRequest.put("currency", "INR");
		orderRequest.put("receipt", "receipt#1");

		PaymentDetails details = (PaymentDetails) request.getSession().getAttribute("details");

		// creating new order

		Order order = client.orders.create(orderRequest);

		// save the order into database

		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDateTime = dateTime.format(formatter);

		details.setOrderId(order.get("id"));
		details.setReceipt(order.get("receipt"));
		details.setDateTime(formattedDateTime);
		details.setUserName(details.getCart().getUser().getEmailAddress());
		details.setStatusOfPayment("Created");

		// Save the orders

		orderRepository.save(details);

		return order.toString();
	}

	@PostMapping("update_order")
	public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> data, HttpServletRequest request,
			Model model) {
		String id = data.get("order_id").toString();

		PaymentDetails myOrders = orderRepository.findByOrderId(id);

		myOrders.setStatusOfPayment(data.get("status").toString());
		orderRepository.save(myOrders);

		return ResponseEntity.ok(Map.of("msg", "Data Updated"));

	}

	@GetMapping("Online_Payment")
	public String Online_Payment(Model model, HttpServletRequest request, Principal principal) {
		commonData(model, principal);

		PaymentDetails details = (PaymentDetails) request.getSession().getAttribute("details");
		model.addAttribute("details", details);

		Integer totalPrice = details.getCart().getTotalToPay();
		model.addAttribute("price", totalPrice);
		Integer paymentId = details.getPayment_id();
		model.addAttribute("paymentId", paymentId);

		return "User/Online_Payment";

	}

	@GetMapping("Success")
	public String Ofline_Payment(Model model, HttpServletRequest request, Principal principal) {
		commonData(model, principal);

		PaymentDetails details = (PaymentDetails) request.getSession().getAttribute("details");

		model.addAttribute("id", details.getPayment_id());

		return "User/Success";

	}

	@GetMapping("view-order")
	public String view_order(Model model, HttpServletRequest request, Principal principal,
			@RequestParam("id") Integer id) {
		commonData(model, principal);

		PaymentDetails details = paymentDaoImpl.findById(id);

		model.addAttribute("order_id", details.getOrderId());
		model.addAttribute("details", details);

		return "User/View_Order";
	}

	@RequestMapping("delete_order")
	public String deleteOrder(@RequestParam("id") Integer id, Model model, Principal principal) {

		commonData(model, principal);

		paymentDaoImpl.delete_order(id);

		model.addAttribute("added", "Your Order is Cancel Successfully !! ");

		return "redirect:/ecommerence/users/user-dashboard";
	}

}
