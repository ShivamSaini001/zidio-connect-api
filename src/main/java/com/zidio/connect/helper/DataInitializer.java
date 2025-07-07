package com.zidio.connect.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.zidio.connect.entities.UserAuthority;
import com.zidio.connect.entities.Skill;
import com.zidio.connect.enums.AuthorityTypeEnum;
import com.zidio.connect.enums.UserSkillsEnum;
import com.zidio.connect.repository.UserAuthorityRepository;
import com.zidio.connect.repository.SkillRepository;

@Component
public class DataInitializer implements CommandLineRunner {

	@Autowired
	private UserAuthorityRepository userAuthorityRepository;

	@Autowired
	private SkillRepository skillRepository;

	@Override
	public void run(String... args) {
		this.initilizeRoles();
		this.initializeSkills();
	}

	public void initilizeRoles() {
		for (AuthorityTypeEnum roleType : AuthorityTypeEnum.values()) {
			String authority = "ROLE_" + roleType.toString().toUpperCase();
			userAuthorityRepository.findByAuthority(authority).orElseGet(() -> {
				return userAuthorityRepository.save(new UserAuthority(authority));
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
