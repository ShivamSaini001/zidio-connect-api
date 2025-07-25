package com.zidio.connect.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.zidio.connect.entities.CloudinaryFile;

public interface CloudinaryService {

	Map<String, Object> uploadImage(MultipartFile file, String folderPath, String fileName) throws IOException;

	Map<String, Object> uploadImage(MultipartFile file, String folderPath, String fileName, boolean overwrite)
			throws IOException;

	Map<String, Object> uploadPdf(MultipartFile file, String folderPath, String fileName) throws IOException;

	Map<String, Object> uploadPdf(MultipartFile file, String folderPath, String fileName, boolean overwrite) throws IOException;

	void deleteFile(CloudinaryFile file) throws IOException;
}
