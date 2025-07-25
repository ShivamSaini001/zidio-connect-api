package com.zidio.connect.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.zidio.connect.enums.AuthorityTypeEnum;
import com.zidio.connect.helper.CloudinaryPath;
import com.zidio.connect.helper.CloudinaryPathService;
import com.zidio.connect.helper.FileCategoryEnum;
import com.zidio.connect.helper.Helper;
import com.zidio.connect.service.CloudinaryService;

@Controller
@RequestMapping("/api/v1/cloud")
public class CloudinaryFileUploadController {

	private CloudinaryPathService cloudinaryPathService;
	private CloudinaryService cloudinaryService;
	
	@Autowired
	Cloudinary cloudinary;

	public CloudinaryFileUploadController(CloudinaryPathService cloudinaryPathService,
			CloudinaryService cloudinaryService) {
		super();
		this.cloudinaryPathService = cloudinaryPathService;
		this.cloudinaryService = cloudinaryService;
	}

	@PostMapping("/profile-photo")
	public ResponseEntity<Map<String, Object>> uploadProfilePhoto(@RequestParam("file") MultipartFile file)
			throws IOException {

		System.out.println(file.getSize() / 1024 + " KB");

		// --------- Extracting File name
		String nameWithoutExtension = Helper.extractOriginalFileNameWithoutExtension(file);

		// Create file path
		CloudinaryPath path = cloudinaryPathService.buildPath(AuthorityTypeEnum.TEACHER, FileCategoryEnum.COURSE_NOTES,
				nameWithoutExtension, 10l, 2l);

		// upload file
		Map<String, Object> result = cloudinaryService.uploadImage(file, path.getFolder(), path.getPublicId());
		return ResponseEntity.ok(result);
	}

	@DeleteMapping("/delete-profile")
	public ResponseEntity<Map<String, Object>> deleteProfilePhoto() throws IOException {
		
		Map<String, Object> result = cloudinary.uploader().destroy("teacher/userId_10/courses/courseId_2/notes/note_cast certificate shivam12", ObjectUtils.emptyMap());
		
		return ResponseEntity.ok(result);
	}

}
