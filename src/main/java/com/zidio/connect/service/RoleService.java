package com.zidio.connect.service;

import com.zidio.connect.entities.Role;

import java.util.List;

public interface RoleService {

    public Role createRole(String name);
    public Role getRoleById(Long id);
    public List<Role> getAllRoles();
    public Role deleteRoleById(Long id);
    public Role updateRole(Long id, String name);

}
