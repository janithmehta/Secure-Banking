/**
 * 
 */
package com.group06fall17.banksix.service;

import com.group06fall17.banksix.model.Users;

/**
 * @author Abhilash
 *
 */
public interface LoginService {
	public boolean validateOtp(String username, int verificationCode);
	
	public void updateLoginInfo(Users users);

	public int generateOTP(String username);

	public void sendEmail(String email, String message, String subject);
	
	public String generatePassword();
}
