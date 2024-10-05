package com.ecommerence_website.serviceDao;

import com.ecommerence_website.entity.User;

public interface UserService {
	
	public User createUser(User user);
	
	public User getByEmail(String email);
	
	public User updateProfile(User user , String username);
	
	public User updateAddress(User user , String username);
}
