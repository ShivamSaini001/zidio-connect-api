package com.zidio.connect.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zidio.connect.entities.Role;
import com.zidio.connect.service.RoleService;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    RoleService roleService;
    
    @PostMapping("/add/{name}")
    public ResponseEntity<Role> addRole(@PathVariable String name){
        Role role = roleService.createRole(name);
        return new ResponseEntity<Role>(role, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id){
        Role role = roleService.getRoleById(id);
        return new ResponseEntity<Role>(role, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<List<Role>> getAllRoles(){
        List<Role> roles = roleService.getAllRoles();
        return new ResponseEntity<List<Role>>(roles, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Role> deleteRoleById(@PathVariable Long id){
        Role role = roleService.deleteRoleById(id);
        return new ResponseEntity<Role>(role, HttpStatus.OK);
    }

    @PutMapping("/update/id/{roleId}/name/{roleName}")
    public ResponseEntity<Role> updateRole(@PathVariable("roleId") Long id,
                                           @PathVariable("roleName") String name){
        Role role = roleService.updateRole(id, name);
        return new ResponseEntity<Role>(role, HttpStatus.OK);
    }
}
