/**
 * 
 */
package com.group06fall17.banksix.service;

import com.group06fall17.banksix.model.User;

/**
 * @author Abhilash
 *
 */
public interface LoginService {
	public boolean validateOtp(String username, int verificationCode);
	
	public void updateLoginInfo(User users);

	public int generateOTP(String username);

	public void sendEmail(String email, String message, String subject);
	
	public String generatePassword();
}
