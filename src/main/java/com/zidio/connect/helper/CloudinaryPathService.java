package com.zidio.connect.helper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.zidio.connect.enums.AuthorityTypeEnum;

@Component
public class CloudinaryPathService {

	public CloudinaryPath buildPath(AuthorityTypeEnum role, FileCategoryEnum category, String fileName,Long userId) {
		return this.buildPath(role, category, fileName, userId, 0l);
	}

	public CloudinaryPath buildPath(AuthorityTypeEnum role, FileCategoryEnum category, Long userId) {
		return this.buildPath(role, category, null, userId, 0l);
	}

	public CloudinaryPath buildPath(AuthorityTypeEnum role, FileCategoryEnum category, Long userId, Long entityId) {
		return this.buildPath(role, category, null, userId, entityId);
	}

	public CloudinaryPath buildPath(AuthorityTypeEnum role, FileCategoryEnum category, String fileName, Long userId,
			Long entityId) {
		String baseFolder = role.name().toLowerCase() + "/" + "userId_" + userId;
		String typeFolder = getTypeFolder(category, entityId);
		String filePrefix = getFilePrefix(category);

		String folder = baseFolder + "/" + typeFolder;
		String publicId;
		if (fileName != null && !fileName.isBlank()) {
			publicId = folder + "/" + filePrefix + fileName;
		} else {
			publicId = folder + "/" + filePrefix + UUID.randomUUID();
		}

		return new CloudinaryPath(folder, publicId);
	}

	private String getTypeFolder(FileCategoryEnum category, Long entityId) {
		return switch (category) {
		case PROFILE_PHOTO -> "profile-photos";
		case RESUME -> "resumes";
		case CERTIFICATE -> "certificates";
		// Teacher Cources.
		case COURSE_ASSIGNMENT -> "courses" + "/" + "courseId_" + entityId + "/" + "assignments";
		case COURSE_VIDEO -> "courses" + "/" + "courseId_" + entityId + "/" + "videos";
		case COURSE_BANNER -> "courses" + "/" + "courseId_" + entityId + "/" + "banner";
		case COURSE_NOTES -> "courses" + "/" + "courseId_" + entityId + "/" + "notes";

		// Recruiter Jobs.
		case JOB_BANNER -> "jobs" + "/" + "jobId_" + entityId + "/" + "banners";
		case COMPANY_LOGO -> "companies" + "/" + "companyId_" + entityId + "/" + "logo";
		case DASHBOARD_ASSET -> "dashboard-assets" + "/" + entityId;
		};
	}

	private String getFilePrefix(FileCategoryEnum category) {
		return switch (category) {
		case PROFILE_PHOTO -> "profile_";
		case RESUME -> "resume_";
		case CERTIFICATE -> "certificate_";
		case COURSE_ASSIGNMENT -> "assignment_";
		case COURSE_VIDEO -> "video_";
		case COURSE_BANNER -> "banner_";
		case COURSE_NOTES -> "note_";
		case JOB_BANNER -> "job_";
		case COMPANY_LOGO -> "logo_";
		case DASHBOARD_ASSET -> "asset_";
		};
	}

//	public static void main(String[] args) {
//		CloudinaryPathService pathService = new CloudinaryPathService();
//
//		System.out.println(pathService.buildPath(AuthorityTypeEnum.TEACHER, FileCategoryEnum.COURSE_ASSIGNMENT, 15, 6));
//	}

}
