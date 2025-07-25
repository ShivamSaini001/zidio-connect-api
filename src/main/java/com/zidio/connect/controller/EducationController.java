package com.zidio.connect.controller;

import com.zidio.connect.config.security.jwt.JwtHelper;
import com.zidio.connect.dto.EducationDto;
import com.zidio.connect.service.EducationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/all/user/educations")
public class EducationController {

    @Autowired
    private EducationService educationService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/create")
    public ResponseEntity<List<EducationDto>> addEducation(@RequestBody EducationDto educationDto, HttpServletRequest request){
        String email = jwtHelper.getUsernameFromJwtToken(jwtHelper.getJwtTokenFromHeader(request));
        educationService.addEducation(email, educationDto);
        return ResponseEntity.ok(educationService.getAllEducations(email));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateEducation(@RequestBody EducationDto educationDto, HttpServletRequest request){
        String email = jwtHelper.getUsernameFromJwtToken(jwtHelper.getJwtTokenFromHeader(request));
        educationService.updateEducation(email, educationDto);
        return ResponseEntity.ok(educationService.getAllEducations(email));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> removeEducation(@RequestBody EducationDto educationDto, HttpServletRequest request){
        String email = jwtHelper.getUsernameFromJwtToken(jwtHelper.getJwtTokenFromHeader(request));
        educationService.deleteEducation(email, educationDto);
        return ResponseEntity.ok(educationService.getAllEducations(email));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllEducation(HttpServletRequest request){
        String email = jwtHelper.getUsernameFromJwtToken(jwtHelper.getJwtTokenFromHeader(request));
        return ResponseEntity.ok(educationService.getAllEducations(email));
    }
}
