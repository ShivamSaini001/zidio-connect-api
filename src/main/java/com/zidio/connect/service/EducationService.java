package com.zidio.connect.service;

import com.zidio.connect.dto.EducationDto;

import java.util.List;

public interface EducationService {

    void addEducation(String email, EducationDto educationDto);
    List<EducationDto> getAllEducations(String email);
    void deleteEducation(String email, EducationDto educationDto);
    EducationDto updateEducation(String email, EducationDto educationDto);
}
