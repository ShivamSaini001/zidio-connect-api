package com.zidio.connect.service.impl;

import org.springframework.stereotype.Service;

import com.zidio.connect.entities.CloudinaryFile;
import com.zidio.connect.repository.CloudinaryFileRepository;
import com.zidio.connect.service.CloudinaryFileService;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CloudinaryFileServiceImpl implements CloudinaryFileService {

	private CloudinaryFileRepository cloudinaryFileRepo;

	public CloudinaryFileServiceImpl(CloudinaryFileRepository cloudinaryFileRepo) {
		super();
		this.cloudinaryFileRepo = cloudinaryFileRepo;
	}

	@Override
	public CloudinaryFile create(CloudinaryFile file) {
		if (file != null) {
			boolean isPresent = cloudinaryFileRepo.findById(file.getId()).isPresent();
			if (isPresent) {
				throw new EntityExistsException("File already exists!!");
			}
			return cloudinaryFileRepo.save(file);
		}
		return null;
	}

	@Override
	public CloudinaryFile update(CloudinaryFile file, Long fileId) {
		CloudinaryFile existingFile = this.getById(fileId);
		file.setId(existingFile.getId());
		return cloudinaryFileRepo.save(file);
	}

	@Override
	public CloudinaryFile getById(Long id) {
		return cloudinaryFileRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("File does not exists!!"));
	}

	@Override
	public void deleteById(Long id) {
		CloudinaryFile file = this.getById(id);
		cloudinaryFileRepo.delete(file);
	}
}
