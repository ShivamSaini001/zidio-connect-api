package com.zidio.connect.service.impl;

import com.zidio.connect.entities.Role;
import com.zidio.connect.repository.RoleRepository;
import com.zidio.connect.service.RoleService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepo;

	@Override
	public Role createRole(String name) {
		// If role already exists.
		roleRepo.findAll().stream().forEach((role) -> {
			if (role.getName().equals(name)) {
				throw new EntityExistsException("Role '" + name + "' already exists!!");
			}
		});
		Role role = new Role();
		role.setName(name);
		return roleRepo.save(role);
	}

	@Override
	public Role getRoleById(Long id) {
		Role role = roleRepo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Role which id " + id + ", does not exist!!"));
		return role;
	}

	@Override
	public List<Role> getAllRoles() {
		return roleRepo.findAll();
	}

	@Override
	public Role deleteRoleById(Long id) {
		Role role = this.getRoleById(id);
		roleRepo.delete(role);
		return role;
	}

	@Override
	public Role updateRole(Long id, String name) {
		Role role = this.getRoleById(id);
		role.setName(name);
		return roleRepo.save(role);
	}
}
