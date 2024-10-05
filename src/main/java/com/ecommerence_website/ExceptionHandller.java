package com.ecommerence_website;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandller {
	
	@ExceptionHandler(NullPointerException.class)
	public String nullpointerException(NullPointerException ex) {
		
	System.out.println(ex.getMessage());
		
		return "User/Error-Page";
		
	}
	

}
