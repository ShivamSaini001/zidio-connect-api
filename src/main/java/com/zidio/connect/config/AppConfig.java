package com.zidio.connect.config;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class AppConfig {
	
	@Value("${cloudinary.name}")
	private String cloudName;
	
	@Value("${cloudinary.api_key}")
	private String apiKey;
	
	@Value("${cloudinary.api_secret}")
	private String apiSecret;

	@Bean
	ModelMapper getModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper;
	}

	@Bean
	Cloudinary getCloudinary() {
		Map<String, Object> config = new HashMap<>();
		config.put("cloud_name", cloudName);
		config.put("api_key", apiKey);
		config.put("api_secret", apiSecret);
		config.put("secure", true);

		return new Cloudinary(config);
	}

}
