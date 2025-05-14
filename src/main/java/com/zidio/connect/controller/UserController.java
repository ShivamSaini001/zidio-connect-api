package com.zidio.connect.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zidio.connect.entities.User;
import com.zidio.connect.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	UserService userService;

	@PostMapping("/create")
	public ResponseEntity<User> createNewUser(User user) {
		User savedUser = userService.createUser(user);
		return new ResponseEntity<User>(savedUser, HttpStatus.OK);
	}

	@DeleteMapping("/delete/id/{userId}")
	public ResponseEntity<User> deleteUser(@PathVariable Long userId) {
		User user = userService.getUserById(userId);
		User deletedUser = userService.deleteUser(user);
		return new ResponseEntity<User>(deletedUser, HttpStatus.OK);
	}

	@DeleteMapping("/delete/email/{emailId}")
	public ResponseEntity<User> deleteUser(@PathVariable("emailId") String email) {
		User user = userService.getUserByEmail(email);
		User deletedUser = userService.deleteUser(user);
		return new ResponseEntity<User>(deletedUser, HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<User> updateUser(User user) {
		User updatedUser = userService.updateUser(user);
		return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
	}

	@GetMapping("/get/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable("userId") Long id) {
		User user = userService.getUserById(id);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@GetMapping("/get/email/{emailId}")
	public ResponseEntity<User> getUserByEmail(@PathVariable("emailId") String email) {
		User user = userService.getUserByEmail(email);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@GetMapping("/get")
	ResponseEntity<List<User>> getAllUsers() {
		List<User> allUsers = userService.getAllUsers();
		return new ResponseEntity<List<User>>(allUsers, HttpStatus.OK);
	}
}
