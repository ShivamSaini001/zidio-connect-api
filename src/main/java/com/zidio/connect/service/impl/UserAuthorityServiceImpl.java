package com.zidio.connect.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zidio.connect.entities.UserAuthority;
import com.zidio.connect.repository.UserAuthorityRepository;
import com.zidio.connect.service.UserAuthorityService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserAuthorityServiceImpl implements UserAuthorityService {

	private UserAuthorityRepository authorityRepo;
	
	// Constructor
	public UserAuthorityServiceImpl(UserAuthorityRepository authorityRepo) {
		super();
		this.authorityRepo = authorityRepo;
	}

	@Override
	public UserAuthority createAuthority(String name) {
		UserAuthority newAuthority = new UserAuthority(name);
		// If role does not exists.
		authorityRepo.findByAuthority(newAuthority.getAuthority()).orElseGet(() -> {
			return authorityRepo.save(newAuthority);
		});
		// If role already exists.
		throw new EntityExistsException("UserAuthority '" + name + "' already exists!!");
	}

	@Override
	public UserAuthority getAuthorityById(Long id) {
		UserAuthority userAuthority = authorityRepo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("UserAuthority which id " + id + ", does not exist!!"));
		return userAuthority;
	}

	@Override
	public UserAuthority getAuthorityByName(String name) {
		UserAuthority userAuthority = new UserAuthority(name);
		userAuthority = authorityRepo.findByAuthority(userAuthority.getAuthority())
				.orElseThrow(() -> new EntityNotFoundException("UserAuthority which name " + name + ", does not exist!!"));
		return userAuthority;
	}

	@Override
	public List<UserAuthority> getAllAuthorities() {
		return authorityRepo.findAll();
	}

	@Override
	public UserAuthority deleteAuthorityById(Long id) {
		UserAuthority userAuthority = this.getAuthorityById(id);
		authorityRepo.delete(userAuthority);
		return userAuthority;
	}

}
