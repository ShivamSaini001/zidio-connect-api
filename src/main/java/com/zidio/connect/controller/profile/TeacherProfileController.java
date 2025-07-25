package com.zidio.connect.controller.profile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zidio.connect.config.security.jwt.JwtHelper;
import com.zidio.connect.dto.CertificateDto;
import com.zidio.connect.dto.TeacherProfileDto;
import com.zidio.connect.entities.CloudinaryFile;
import com.zidio.connect.enums.AuthorityTypeEnum;
import com.zidio.connect.exception.CustomException;
import com.zidio.connect.helper.CloudinaryPath;
import com.zidio.connect.helper.CloudinaryPathService;
import com.zidio.connect.helper.FileCategoryEnum;
import com.zidio.connect.helper.Helper;
import com.zidio.connect.service.CloudinaryService;
import com.zidio.connect.service.TeacherProfileService;
import com.zidio.connect.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/teacher/profile")
public class TeacherProfileController {

    @Autowired
    private TeacherProfileService teacherProfileService;

    @Autowired
    private CloudinaryPathService cloudinaryPathService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/create/{email}")
    public ResponseEntity<TeacherProfileDto> createProfile(@RequestBody TeacherProfileDto profileDto,
                                                           @PathVariable("email") String userEmail) {
        // userDto should contains email
        TeacherProfileDto savedProfile = teacherProfileService.createTeacherProfile(profileDto, userEmail);
        return ResponseEntity.ok(savedProfile);
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<TeacherProfileDto> updateProfile(@RequestBody TeacherProfileDto profileDto,
                                                           @PathVariable("email") String userEmail) {
        TeacherProfileDto updatedProfile = teacherProfileService.updateTeacherProfile(profileDto, userEmail);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Map<String, Object>> deleteProfile(@PathVariable String email) {
        teacherProfileService.deleteTeacherProfileByEmail(email);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Response Message: ", "Student profile successfully deleted...");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<TeacherProfileDto> getProfileById(@PathVariable Long id) {
        TeacherProfileDto profile = teacherProfileService.getTeacherProfileById(id);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/get/email/{email}")
    public ResponseEntity<TeacherProfileDto> getProfileByEmail(@PathVariable String email) {
        TeacherProfileDto profile = teacherProfileService.getTeacherProfileByEmail(email);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/get")
    public ResponseEntity<List<TeacherProfileDto>> getAllProfiles() {
        List<TeacherProfileDto> allProfiles = teacherProfileService.getAllTeacherProfiles();
        return ResponseEntity.ok(allProfiles);
    }

//	<------------ Profile Image Upload --------------->

    @PostMapping("/image/upload")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                                       HttpServletRequest request) throws IOException {
        // Check if file is image type.
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new CustomException("Only image files are allowed (jpg, png, webp, etc.)", HttpStatus.BAD_REQUEST);
        }

        // Get email from jwt token
        String email = jwtHelper.getUsernameFromJwtToken(jwtHelper.getJwtTokenFromHeader(request));

        // Gettring user Id
        Long userId = userService.getUserIdByEmail(email);

        // --------- File name
        String nameWithoutExtension = "teacher_image";

        // Generate file path
        CloudinaryPath filePath = cloudinaryPathService.buildPath(AuthorityTypeEnum.TEACHER,
                FileCategoryEnum.PROFILE_PHOTO, nameWithoutExtension, userId);

        // upload file
        Map<String, Object> result = cloudinaryService.uploadImage(file, filePath.getFolder(), filePath.getPublicId(),
                true);

        // Convert map into CloudinaryFile Object
        CloudinaryFile profileDetails = Helper.convertMapIntoCloudinaryFile(result);

        // Update file details into DB.
        teacherProfileService.updateProfileImageDetails(email, profileDetails);
        return ResponseEntity.ok(profileDetails.getSecureUrl());
    }

    @GetMapping("/image/get")
    public ResponseEntity<?> getProfilePicture(HttpServletRequest request) {
        // Get email from jwt token
        String email = jwtHelper.getUsernameFromJwtToken(jwtHelper.getJwtTokenFromHeader(request));
        return ResponseEntity.ok(teacherProfileService.getProfileImage(email));
    }

    @PostMapping("/certificate/upload")
    public ResponseEntity<?> uploadCertificate(@RequestParam List<MultipartFile> files, HttpServletRequest request)
            throws IOException {
        // Get email from jwt token
        String email = jwtHelper.getUsernameFromJwtToken(jwtHelper.getJwtTokenFromHeader(request));

        for (MultipartFile file : files) {
            if (file.isEmpty())
                continue;

            // Validate file content type
            String contentType = file.getContentType();
            if (!"application/pdf".equalsIgnoreCase(contentType)) {
                throw new CustomException("Only PDF files are allowed. Invalid file: " + file.getOriginalFilename(),
                        HttpStatus.BAD_REQUEST);
            }

            String originalFileNameWithoutExtension = Helper.extractOriginalFileNameWithoutExtension(file);
            Long userId = userService.getUserIdByEmail(email);

            // Generate path for file
            CloudinaryPath path = cloudinaryPathService.buildPath(AuthorityTypeEnum.TEACHER,
                    FileCategoryEnum.CERTIFICATE, originalFileNameWithoutExtension + "_" + UUID.randomUUID(), userId);

            // Upload file to the cloud
            Map<String, Object> uploadFile = cloudinaryService.uploadPdf(file, path.getFolder(), path.getPublicId(),
                    true);

            CloudinaryFile certificateDetails = Helper.convertMapIntoCloudinaryFile(uploadFile);

            // save data into db
            teacherProfileService.uploadCertificate(email, certificateDetails);

        }

        // Fetch all certificates from Database.
        List<CloudinaryFile> uploadedCertificates = teacherProfileService.getAllCertificates(email);

        List<CertificateDto> certificateDto = uploadedCertificates.stream()
                .map((certificate) -> modelMapper.map(certificate, CertificateDto.class)).collect(Collectors.toList());

        return ResponseEntity.ok(certificateDto);
    }

    @GetMapping("/certificate/get")
    public ResponseEntity<List<CertificateDto>> getAllCertificates(HttpServletRequest request) {

        // Get email from jwt token
        String email = jwtHelper.getUsernameFromJwtToken(jwtHelper.getJwtTokenFromHeader(request));
        List<CloudinaryFile> allCertificates = teacherProfileService.getAllCertificates(email);

        List<CertificateDto> certificatesDto = allCertificates.stream()
                .map((certificate) -> modelMapper.map(certificate, CertificateDto.class)).collect(Collectors.toList());
        return ResponseEntity.ok(certificatesDto);
    }

    @DeleteMapping("/certificate/delete/{id}")
    public ResponseEntity<String> deleteCertificateById(@PathVariable("id") Long certificateId, HttpServletRequest request)
            throws IOException {
        // Get email from jwt token
        String email = jwtHelper.getUsernameFromJwtToken(jwtHelper.getJwtTokenFromHeader(request));

        // Delete Certificate from Database.
        CloudinaryFile deletedCertificate = teacherProfileService.deleteCertificateById(email, certificateId);

        if (deletedCertificate == null) {
            throw new EntityNotFoundException("Certificate does not exists!!");
        }

        // Delete Certificate from Cloudinary.
        cloudinaryService.deleteFile(deletedCertificate);

        return ResponseEntity.ok("Certificate deleted successfully!!");
    }
}
