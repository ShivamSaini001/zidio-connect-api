package com.zidio.connect.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.zidio.connect.entities.CloudinaryFile;
import com.zidio.connect.service.CloudinaryService;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Map<String, Object> uploadImage(MultipartFile file, String folderPath, String fileName) throws IOException {
        return this.uploadFile(file, folderPath, fileName, "auto", false);
    }

    @Override
    public Map<String, Object> uploadImage(MultipartFile file, String folderPath, String fileName, boolean overwrite)
            throws IOException {
        return this.uploadFile(file, folderPath, fileName, "auto", overwrite);
    }

    public Map<String, Object> uploadFile(MultipartFile file, String folderPath, String fileName, String resourceType, boolean overwrite) throws IOException {
        if (file != null) {
            Map<String, Object> config = new HashMap<>();
            config.put("asset_folder", folderPath);
            config.put("public_id", fileName);
            config.put("overwrite", overwrite);
            config.put("resource_type", resourceType);

            Map result = cloudinary.uploader().upload(file.getBytes(), config);

            return result;
        }
        return null;
    }

//    private String generatePdfUrl(String publicId, boolean signed) {
//        return cloudinary.url()
//                .resourceType("raw")
//                .signed(signed)
//                .publicId(publicId)
//                .generate();
//    }

    @Override
    public void deleteFile(CloudinaryFile file) throws IOException {
        if (file != null) {
            String filePath = file.getPublicId();
            if (!filePath.isBlank()) {
                cloudinary.uploader().destroy(filePath, ObjectUtils.emptyMap());
            }
        }
    }

    @Override
    public Map<String, Object> uploadPdf(MultipartFile file, String folderPath, String fileName, boolean overwrite) throws IOException {
        return this.uploadFile(file, folderPath, fileName, "auto", overwrite);
    }

    @Override
    public Map<String, Object> uploadPdf(MultipartFile file, String folderPath, String fileName) throws IOException {
        return this.uploadFile(file, folderPath, fileName, "auto", false);
    }
}
