package com.zidio.connect.helper;

import java.util.UUID;

import com.zidio.connect.enums.AuthorityTypeEnum;

//@Component
public class CloudinaryPathService {

    public enum FileCategory {
        PROFILE_PHOTO,
        RESUME,
        CERTIFICATE,
        ASSIGNMENT,
        COURSE_VIDEO,
        COURSE_BANNER,
        COURSE_NOTE,
        JOB_BANNER,
        COMPANY_LOGO,
        DASHBOARD_ASSET
    }
    
    public CloudinaryPath buildPath(AuthorityTypeEnum role, FileCategory category, String entityId) {
        String baseFolder = role.name().toLowerCase();
        String typeFolder = getTypeFolder(category, entityId);
        String filePrefix = getFilePrefix(category);

        String folder = baseFolder + "/" + typeFolder;
        String publicId = folder + "/" + filePrefix + UUID.randomUUID();

        return new CloudinaryPath(folder, publicId);
    }
    
    private String getTypeFolder(FileCategory category, String id) {
        return switch (category) {
            case PROFILE_PHOTO -> "profile-photos/" + id;
            case RESUME -> "resumes/" + id;
            case CERTIFICATE -> "certificates/" + id;
            case ASSIGNMENT -> "assignments/" + id;
            case COURSE_VIDEO -> "courses/" + id + "/videos";
            case COURSE_BANNER -> "courses/" + id + "/banner";
            case COURSE_NOTE -> "courses/" + id + "/notes";
            case JOB_BANNER -> "job-banners/" + id;
            case COMPANY_LOGO -> "company-logos/" + id;
            case DASHBOARD_ASSET -> "dashboard-assets/" + id;
        };
    }

    private String getFilePrefix(FileCategory category) {
        return switch (category) {
            case PROFILE_PHOTO -> "profile_";
            case RESUME -> "resume_";
            case CERTIFICATE -> "certificate_";
            case ASSIGNMENT -> "assignment_";
            case COURSE_VIDEO -> "video_";
            case COURSE_BANNER -> "banner_";
            case COURSE_NOTE -> "note_";
            case JOB_BANNER -> "job_";
            case COMPANY_LOGO -> "logo_";
            case DASHBOARD_ASSET -> "asset_";
        };
    }
    
    public static void main(String[] args) {
    	CloudinaryPathService pathService = new CloudinaryPathService();
    	
    	System.out.println(pathService.buildPath(AuthorityTypeEnum.TEACHER, FileCategory.PROFILE_PHOTO, "133"));
	}
    
}
