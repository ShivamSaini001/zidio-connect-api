package com.zidio.connect.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.zidio.connect.entities.Role;
import com.zidio.connect.enums.RoleType;
import com.zidio.connect.repository.RoleRepository;

@Component
public class DataInitializer implements CommandLineRunner{

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) {
    	
        for (RoleType roleType : RoleType.values()) {
        	String roleName = "ROLE_" + roleType.toString().toUpperCase();
            roleRepository.findByName(roleName).orElseGet(() -> {
                Role role = new Role();
                role.setName(roleName);
                return roleRepository.save(role);
            });
        }
        System.out.println("Predefined roles initialized...");
    }
}
