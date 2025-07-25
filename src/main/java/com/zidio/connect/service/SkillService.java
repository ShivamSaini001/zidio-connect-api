package com.zidio.connect.service;

import com.zidio.connect.dto.SkillDto;

import java.util.Set;

public interface SkillService {

    SkillDto addSkill(String email, SkillDto skillDto);
    Set<SkillDto> getAllSkills(String email);
    void deleteSkill(String email, SkillDto skillDto);

}
