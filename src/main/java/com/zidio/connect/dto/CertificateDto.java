package com.zidio.connect.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CertificateDto {

	private Long id;
	private String secureUrl;
	private String resourceType;
	private String fileName;
	private String fileFormat;
	private Long fileSizeInBytes;
	private LocalDateTime uploadedAt;

}
