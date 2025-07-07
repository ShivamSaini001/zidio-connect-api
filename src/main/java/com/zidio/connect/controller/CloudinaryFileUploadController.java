package com.zidio.connect.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Controller
@RequestMapping("/api/v1/user")
public class CloudinaryFileUploadController {

	@Autowired
	private Cloudinary cloudinary;

	@PostMapping("/profile-photo")
	public ResponseEntity<Map> uploadProfilePhoto(@RequestParam("file") MultipartFile file) throws IOException {
		Map result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
		System.out.println(result);
		return ResponseEntity.ok(result);
	}
}
