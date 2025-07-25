package com.zidio.connect.service.impl;

import com.zidio.connect.dto.EducationDto;
import com.zidio.connect.entities.Education;
import com.zidio.connect.entities.User;
import com.zidio.connect.repository.UserRepository;
import com.zidio.connect.service.EducationService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EducationServiceImpl implements EducationService {

    @Autowired
    private UserRepository userRopo;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public void addEducation(String email, EducationDto educationDto) {
        User user = userRopo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
        // If Education already exists.
        Education education = modelMapper.map(educationDto, Education.class);
        education.setUser(user);

        user.getEducations().add(education);
        User updatedUser = userRopo.save(user);
    }

    @Override
    public List<EducationDto> getAllEducations(String email) {
        User user = userRopo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
        return user.getEducations().stream().map((edu) -> modelMapper.map(edu, EducationDto.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteEducation(String email, EducationDto educationDto) {
        User user = userRopo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
        List<Education> newEducations = user.getEducations().stream().filter((edu) -> edu.getEducationId() != educationDto.getEducationId()).collect(Collectors.toList());

        user.getEducations().clear();
        user.getEducations().addAll(newEducations);
        userRopo.save(user);
    }

    @Override
    public EducationDto updateEducation(String email, EducationDto educationDto) {
        User user = userRopo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));

        List<Education> newEducations = user.getEducations().stream().map((education) -> {
            if(education.getEducationId() == educationDto.getEducationId()){
                modelMapper.map(educationDto, education);
            }
            return education;
        }).collect(Collectors.toList());

        user.getEducations().clear();
        user.getEducations().addAll(newEducations);
        userRopo.save(user);
        return educationDto;
    }
}
