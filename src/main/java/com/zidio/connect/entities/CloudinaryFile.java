package com.zidio.connect.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cloudinary_files")
public class CloudinaryFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// ID of the entity it is linked with (courseId, userId, etc.)
	@Column(name = "entity_id")
	private Long entityId;

	// The full path like: "teacher/userId_10/courses/courseId_2/notes/note_abc123"
	@Column(name = "public_id", nullable = false, unique = true)
	private String publicId;

	// Cloudinary secure access URL
	@Column(name = "secure_url", nullable = false)
	private String secureUrl;

	// Cloudinary resource type: "image", "video", "raw"
	@Column(name = "resource_type", nullable = false)
	private String resourceType;

	// Original name from upload (without extension)
	@Column(name = "filename")
	private String fileName;

	// File size in bytes
	@Column(name = "size_in_bytes")
	private Long fileSizeInBytes;

	@Column(name = "uploaded_at")
	private LocalDateTime uploadedAt;

	@PrePersist
	protected void onCreate() {
		this.uploadedAt = LocalDateTime.now();
	}
}
