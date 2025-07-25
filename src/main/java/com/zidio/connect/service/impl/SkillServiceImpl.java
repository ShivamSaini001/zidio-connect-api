package com.zidio.connect.service.impl;

import com.zidio.connect.dto.SkillDto;
import com.zidio.connect.entities.Skill;
import com.zidio.connect.entities.User;
import com.zidio.connect.repository.UserRepository;
import com.zidio.connect.service.SkillService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private UserRepository userRopo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SkillDto addSkill(String email, SkillDto skillDto) {
        User user = userRopo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
        // If skill already exists.
        user.getSkills().stream().forEach((skill) -> {
            if (skillDto.getName().compareToIgnoreCase(skill.getName()) == 0){
                throw new EntityExistsException("Skill already exists!!");
            }
        });

        // If skill does not exist.
        Skill newSkill = modelMapper.map(skillDto, Skill.class);
        newSkill.setUser(user);
        user.getSkills().add(newSkill);

        // Save user
        User updatedUser = userRopo.save(user);

        Skill updatedSkill = updatedUser.getSkills().stream()
                .filter((skill) -> skillDto.getName().compareToIgnoreCase(skill.getName()) == 0)
                .collect(Collectors.toSet())
                .stream().findFirst().orElse(newSkill);
        return modelMapper.map(updatedSkill, SkillDto.class);
    }

    @Override
    public Set<SkillDto> getAllSkills(String email) {
        User user = userRopo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
        return user.getSkills().stream().map((skill) -> modelMapper.map(skill, SkillDto.class)).collect(Collectors.toSet());
    }

    @Override
    public void deleteSkill(String email, SkillDto skillDto) {
        User user = userRopo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));

        Set<Skill> newSkills = user.getSkills().stream().filter((skill) -> skill.getId() != skillDto.getId()).collect(Collectors.toSet());
        user.getSkills().clear();
        user.getSkills().addAll(newSkills);

        userRopo.save(user);
    }
}
