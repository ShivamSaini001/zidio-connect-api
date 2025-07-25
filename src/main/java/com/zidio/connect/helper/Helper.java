package com.zidio.connect.helper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.zidio.connect.entities.CloudinaryFile;

public class Helper {

	public static String extractOriginalFileNameWithoutExtension(MultipartFile file) {
		String originalFileName = file.getOriginalFilename(); // e.g., "resume_shivam.pdf"
		if (originalFileName != null && originalFileName.contains(".")) {
			return originalFileName.substring(0, originalFileName.lastIndexOf('.'));
		}
		return null;
	}

	public static CloudinaryFile convertMapIntoCloudinaryFile(Map<String, Object> result) {
		return convertMapIntoCloudinaryFile(result, null);
	}

	public static CloudinaryFile convertMapIntoCloudinaryFile(Map<String, Object> result, Long entityId) {
		CloudinaryFile profileDetails = new CloudinaryFile();
		profileDetails.setFileSizeInBytes(Long.valueOf(result.get("bytes").toString()));
		profileDetails.setFileName(result.get("display_name").toString());
		profileDetails.setPublicId(result.get("public_id").toString());
		profileDetails.setResourceType(result.get("resource_type").toString());
		profileDetails.setSecureUrl(result.get("secure_url").toString());
		profileDetails.setUploadedAt(OffsetDateTime.parse(result.get("created_at").toString()).toLocalDateTime());

		if (entityId != null) {
			profileDetails.setEntityId(entityId);
		}

		return profileDetails;
	}
}
