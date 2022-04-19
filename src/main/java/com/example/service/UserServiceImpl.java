package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepository userRepository;
	
	public void authenticateUser(User user) throws Exception {
		
		User u = userRepository.findByEmailAddressAndPhone(user.getEmailAddress(), user.getPhoneNumber());
		if(u!=null) {
			throw new Exception("User Already Present");
		}
		
		userRepository.save(user);
	}
	
	
}
