// Author : Abhilash

package com.group06fall17.banksix.service;

import com.group06fall17.banksix.model.ExternalUser;
import org.springframework.web.multipart.MultipartFile;

public interface UsrFuncService {
	public boolean toUploadFile(String location, MultipartFile file);
	public String uploadFileLoc();
	public boolean diffKeys(ExternalUser user, String privateKeyFileLocation);
}
