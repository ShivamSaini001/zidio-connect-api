package com.zidio.connect.service;

import java.util.List;

import com.zidio.connect.entities.User;

public interface UserService {

	User createUser(User newUser);
	User deleteUser(User user);
	User updateUser(User updatedUser);
	User getUserById(Long id);
	User getUserByEmail(String email);
	List<User> getAllUsers();
}
