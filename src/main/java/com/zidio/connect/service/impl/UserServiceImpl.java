package com.zidio.connect.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zidio.connect.entities.User;
import com.zidio.connect.repository.UserRepository;
import com.zidio.connect.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepo;

	@Override
	public User createUser(User newUser) {
		if (newUser != null) {
			// If user already exists.
			userRepo.findAll().stream().forEach((user) -> {
				if (user.getEmail().equals(newUser.getEmail())) {
					throw new EntityExistsException("User with email '" + newUser.getEmail() + "' already exists!!");
				}
			});
			// else
			newUser.setCreatedAt(LocalDateTime.now());
			newUser.setUpdatedAt(LocalDateTime.now());
			User createdUser = userRepo.save(newUser);
			return createdUser;
		}
		return null;
	}

	@Override
	public User deleteUser(User user) {
		if (user != null) {
			userRepo.delete(this.getUserById(user.getUserId()));
			return user;
		}
		return null;
	}

	@Override
	public User updateUser(User updatedUser) {
		if (updatedUser != null) {
			// Get user from db.
			User user = this.getUserById(updatedUser.getUserId());
			// Update user details.
			user.setName(updatedUser.getName());
			user.setEmail(updatedUser.getEmail());
			user.setProfileImageUrl(updatedUser.getProfileImageUrl());
			user.setUpdatedAt(LocalDateTime.now());
			userRepo.save(user);
			return user;
		}
		return null;
	}

	@Override
	public User getUserById(Long id) {
		User user = userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
		return user;
	}

	@Override
	public User getUserByEmail(String email) {
		List<User> users = userRepo.findByEmail(email);
		if (users.isEmpty()) {
			throw new EntityNotFoundException("User does not exists!!");
		}
		return users.get(0);
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = userRepo.findAll();
		return users;
	}

}
