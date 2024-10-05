package com.ecommerence_website.serviceDaoImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerence_website.entity.User;
import com.ecommerence_website.jpaRepository.UserRepo;
import com.ecommerence_website.serviceDao.UserService;

@Service
public class UserDaoImpl implements UserService {

	@Autowired
	private UserRepo repo;

	@Override
	public User createUser(User user) {
		return repo.save(user);
	}

	@Override
	public User getByEmail(String email) {

		return repo.findByEmailAddress(email);
	}
	
	
	public List<User> findAllUsers(){
		return repo.findAll();
	}
	
	public List<User> onlyUser(){
		List<User> users = findAllUsers();
		
		List<User> onlyUsers = new ArrayList<>();
		
		for( User us : users) {
			if(us.getRole().equals("ROLE_USER")) {
				onlyUsers.add(us);
			}
		}
		
		return onlyUsers;
	}
	
	public void deleteUser(Integer user_id) {
		User user = repo.findById(user_id).get();
		
		repo.delete(user);
	}

	@Override
	public User updateProfile(User user , String username) {
		
		User newUser = getByEmail(username);
		newUser.setFullname(user.getFullname());
		newUser.setEmailAddress(user.getEmailAddress());
		newUser.setAddress(user.getAddress());
		
		return repo.save(newUser);
	}

	@Override
	public User updateAddress(User user , String username) {
		
		User newUser = getByEmail(username);
		
		newUser.setAddress(user.getAddress());
		newUser.setCity(user.getCity());
		newUser.setState(user.getState());
		newUser.setZipCode(user.getZipCode());
		
		
		return repo.save(newUser);
	}

}
