package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.User;
import com.example.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/user")
	public ResponseEntity<String> addUser(@RequestBody User user) {

		try {
			System.out.println(user.getEmailAddress());
			userService.authenticateUser(user);
		} catch (Exception e) {
			return ResponseEntity.ok().body("User Already Present");
		}

		return ResponseEntity.ok().body("User Added");
	}

}
