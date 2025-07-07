package com.zidio.connect.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.zidio.connect.service.CloudinaryService;

@Service
public class CloudinaryServiceImpl implements CloudinaryService{

	Cloudinary cloudinary;
	
	public CloudinaryServiceImpl(Cloudinary cloudinary) {
		this.cloudinary = cloudinary;
	}

	@Override
	public String uploadFile(MultipartFile file, String path) {
		System.out.println(cloudinary);
		return null;
	}

}
