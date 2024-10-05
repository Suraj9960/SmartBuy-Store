package com.ecommerence_website.jpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerence_website.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {
	
	public User findByEmailAddress(String emailAddress);

}
