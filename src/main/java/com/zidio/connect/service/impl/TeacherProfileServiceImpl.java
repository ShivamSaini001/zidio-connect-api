package com.zidio.connect.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zidio.connect.dto.TeacherProfileDto;
import com.zidio.connect.dto.UserDto;
import com.zidio.connect.entities.CloudinaryFile;
import com.zidio.connect.entities.TeacherProfile;
import com.zidio.connect.entities.User;
import com.zidio.connect.repository.TeacherProfileRepository;
import com.zidio.connect.repository.UserRepository;
import com.zidio.connect.service.CloudinaryFileService;
import com.zidio.connect.service.TeacherProfileService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TeacherProfileServiceImpl implements TeacherProfileService {

    private TeacherProfileRepository teacherProfileRepo;
    private UserRepository userRepo;
    private ModelMapper modelMapper;

    @Autowired
    private CloudinaryFileService cloudinaryFileService;

    // Constructor
    public TeacherProfileServiceImpl(TeacherProfileRepository teacherProfileRepo, UserRepository userRepo,
                                     ModelMapper modelMapper) {
        super();
        this.teacherProfileRepo = teacherProfileRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public TeacherProfileDto createTeacherProfile(TeacherProfileDto profileDto, String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));

        if (profileDto == null) {
            throw new RuntimeException("Please provide profile details!!");
        }

        if (user.getTeacherProfile() == null) {
            TeacherProfile teacherProfile = modelMapper.map(profileDto, TeacherProfile.class);
            // Creating Teacher profile
            teacherProfile.setUser(user);
            TeacherProfile savedTeacherProfile = teacherProfileRepo.save(teacherProfile);

            // updating User entity into database.
            user.setTeacherProfile(savedTeacherProfile);
            User savedUser = userRepo.save(user);

            TeacherProfileDto savedTeacherProfileDto = modelMapper.map(savedTeacherProfile, TeacherProfileDto.class);
            savedTeacherProfileDto.setUserDto(modelMapper.map(savedUser, UserDto.class));
            return savedTeacherProfileDto;
        } else {
            throw new EntityExistsException("Profile already exists!!");
        }
    }

    @Override
    public TeacherProfileDto updateTeacherProfile(TeacherProfileDto profileDto, String email) {
        // fetching teacher profile from database.
        TeacherProfile teacherProfile = this.getTeacherProfile(email);

        if (teacherProfile == null) {
            this.createTeacherProfile(profileDto, email);
            teacherProfile = this.getTeacherProfile(email);
        }

        // updating TeacherProfile.
        TeacherProfile updatedProfile = modelMapper.map(profileDto, TeacherProfile.class);
        updatedProfile.setUserId(teacherProfile.getUserId());
        updatedProfile.setUser(teacherProfile.getUser());

        // update into database.
        TeacherProfile savedTeacherProfile = teacherProfileRepo.save(updatedProfile);
        TeacherProfileDto teacherProfileDto = modelMapper.map(savedTeacherProfile, TeacherProfileDto.class);
        teacherProfileDto.setUserDto(modelMapper.map(savedTeacherProfile.getUser(), UserDto.class));
        return teacherProfileDto;
    }

    @Transactional
    @Override
    public void deleteTeacherProfileByEmail(String email) {
        // fetching teacher profile form db.
        TeacherProfile teacherProfile = this.getTeacherProfile(email);

        // If teacher profile does not exists.
        if (teacherProfile == null) {
            throw new EntityNotFoundException("Teacher profile does not exists.");
        }

        // extracting user entity.
        User user = teacherProfile.getUser();

        // updating user entity.
        user.setTeacherProfile(null);
        userRepo.save(user);

        // updating teacher profile.
        teacherProfile.setUser(null);
        teacherProfileRepo.save(teacherProfile);

        // Deleting Student profile.
        teacherProfileRepo.delete(teacherProfile);
    }

    @Override
    public TeacherProfileDto getTeacherProfileByEmail(String email) {
        // Get user from db
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
        // Get teacher profile
        TeacherProfile teacherProfile = user.getTeacherProfile();

        if (teacherProfile == null) {
            throw new EntityNotFoundException("Profile does not exists!!");
        }

        // Convert teacher profile.
        TeacherProfileDto teacherProfileDto = modelMapper.map(teacherProfile, TeacherProfileDto.class);
        teacherProfileDto.setUserDto(modelMapper.map(user, UserDto.class));
        return teacherProfileDto;
    }

    @Override
    public TeacherProfileDto getTeacherProfileById(Long profileId) {
        // get teacher profile from db
        TeacherProfile teacherProfile = teacherProfileRepo.findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Profile does not exists..."));

        // Get user
        User user = teacherProfile.getUser();
        // Convert Entities into DTO.
        TeacherProfileDto teacherProfileDto = modelMapper.map(teacherProfile, TeacherProfileDto.class);
        UserDto userDto = modelMapper.map(user, UserDto.class);
        teacherProfileDto.setUserDto(userDto);
        return teacherProfileDto;
    }

    @Override
    public List<TeacherProfileDto> getAllTeacherProfiles() {
        return teacherProfileRepo.findAll().stream().map((teacherProfile) -> {
            TeacherProfileDto teacherProfileDto = modelMapper.map(teacherProfile, TeacherProfileDto.class);
            UserDto userDto = modelMapper.map(teacherProfile.getUser(), UserDto.class);
            teacherProfileDto.setUserDto(userDto);
            return teacherProfileDto;
        }).toList();
    }

    private TeacherProfile getTeacherProfile(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User does not exists!!"));
        return user.getTeacherProfile();
    }

    @Override
    public CloudinaryFile updateProfileImageDetails(String email, CloudinaryFile profileDetails) {

        TeacherProfile teacherProfile = this.getTeacherProfile(email);
        CloudinaryFile profileImage = teacherProfile.getProfileImage();

        if (profileImage != null) {
            profileDetails.setId(profileImage.getId());
            profileDetails.setEntityId(profileImage.getEntityId());
        }
        teacherProfile.setProfileImage(profileDetails);
        teacherProfileRepo.save(teacherProfile);
        return profileDetails;
    }

    @Override
    public Map<String, Object> getProfileImage(String email) {
        TeacherProfile teacherProfile = this.getTeacherProfile(email);
        CloudinaryFile profileImage = teacherProfile.getProfileImage();
        Map<String, Object> imageData = new HashMap<>();

        if (profileImage != null) {
            imageData.put("id", profileImage.getId());
            imageData.put("url", profileImage.getSecureUrl());
            imageData.put("size", profileImage.getFileSizeInBytes());
            imageData.put("updated", profileImage.getUploadedAt());
        }
        return imageData;
    }

    @Override
    public void uploadCertificate(String email, CloudinaryFile certificateDetails) {
        User user = this.getTeacherProfile(email).getUser();
        List<CloudinaryFile> certificates = user.getCertificates();

        if (certificates == null) {
            certificates = new ArrayList<>();
        }

        certificates.add(certificateDetails);
        userRepo.save(user);
    }

    @Override
    public List<CloudinaryFile> getAllCertificates(String email) {
        return this.getTeacherProfile(email).getUser().getCertificates();
    }

    @Override
    public CloudinaryFile deleteCertificateById(String email, Long certificateId) {
        User user = this.getTeacherProfile(email).getUser();
        List<CloudinaryFile> certificates = user.getCertificates();

        for (int index = 0; index < certificates.size(); index++) {
            CloudinaryFile certificate = certificates.get(index);
            if (certificate.getId() == certificateId) {
                certificates.remove(index);
                userRepo.save(user);
                cloudinaryFileService.deleteById(certificateId);
                return certificate;
            }
        }
        return null;
    }

}
