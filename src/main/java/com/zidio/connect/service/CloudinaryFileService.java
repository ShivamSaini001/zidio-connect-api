package com.zidio.connect.service;

import com.zidio.connect.entities.CloudinaryFile;

public interface CloudinaryFileService {

	CloudinaryFile create(CloudinaryFile file);

	CloudinaryFile update(CloudinaryFile file, Long fileId);

	CloudinaryFile getById(Long id);

	void deleteById(Long id);

}
