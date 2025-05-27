package com.zidio.connect.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.zidio.connect.entities.Role;
import com.zidio.connect.entities.Skill;
import com.zidio.connect.enums.RoleTypeEnum;
import com.zidio.connect.enums.UserSkillsEnum;
import com.zidio.connect.repository.RoleRepository;
import com.zidio.connect.repository.SkillRepository;

@Component
public class DataInitializer implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private SkillRepository skillRepository;

	@Override
	public void run(String... args) {
		this.initilizeRoles();
		this.initializeSkills();
	}

	public void initilizeRoles() {
		for (RoleTypeEnum roleType : RoleTypeEnum.values()) {
			String roleName = "ROLE_" + roleType.toString().toUpperCase();
			roleRepository.findByName(roleName).orElseGet(() -> {
				Role role = new Role();
				role.setName(roleName);
				return roleRepository.save(role);
			});
		}
		System.out.println("Predefined roles initialized...");
	}

	public void initializeSkills() {
		for (UserSkillsEnum skill : UserSkillsEnum.values()) {
			String name = skill.toString().trim();
			// Capitalize word here
			// ............

			skillRepository.findByName(name).orElseGet(() -> {
				Skill newSkill = new Skill();
				newSkill.setName(name);
				return skillRepository.save(newSkill);
			});
		}
		System.out.println("Predefined skills initialized...");
	}
}
