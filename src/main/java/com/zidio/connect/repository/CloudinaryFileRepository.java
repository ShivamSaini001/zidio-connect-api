package com.zidio.connect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zidio.connect.entities.CloudinaryFile;

public interface CloudinaryFileRepository extends JpaRepository<CloudinaryFile, Long> {

}
