package com.zidio.connect.controller;

import com.zidio.connect.config.security.jwt.JwtHelper;
import com.zidio.connect.dto.SkillDto;
import com.zidio.connect.service.SkillService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/all/user/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/create")
    public ResponseEntity<Set<SkillDto>> addSkill(@RequestBody SkillDto skillDto, HttpServletRequest request){
        String email = jwtHelper.getUsernameFromJwtToken(jwtHelper.getJwtTokenFromHeader(request));
        // Adding skill
        skillService.addSkill(email, skillDto);
        return ResponseEntity.ok(skillService.getAllSkills(email));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Set<SkillDto>> removeSkill(@RequestBody SkillDto skillDto, HttpServletRequest request){
        String email = jwtHelper.getUsernameFromJwtToken(jwtHelper.getJwtTokenFromHeader(request));
        // Remove skill
        skillService.deleteSkill(email, skillDto);
        return ResponseEntity.ok(skillService.getAllSkills(email));
    }

    @GetMapping("/get")
    public ResponseEntity<Set<SkillDto>> getAllSkills(HttpServletRequest request){
        String email = jwtHelper.getUsernameFromJwtToken(jwtHelper.getJwtTokenFromHeader(request));
        return ResponseEntity.ok(skillService.getAllSkills(email));
    }

}
