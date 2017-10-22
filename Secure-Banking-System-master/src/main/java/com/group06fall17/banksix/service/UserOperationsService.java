package com.group06fall17.banksix.service;

import org.springframework.web.multipart.MultipartFile;

import com.group06fall17.banksix.model.ExternalUser;

public interface UserOperationsService {
	public String getUploadFileLocation();

	public boolean uploadFile(String location, MultipartFile file);

	public boolean compareKeys(ExternalUser user, String privateKeyFileLocation);
}
