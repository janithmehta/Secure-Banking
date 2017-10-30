/**
 * 
 */
package com.group06fall17.banksix.service;

import com.group06fall17.banksix.model.User;

/**
 * @author Abhilash
 *
 */
public interface LoginManager {
	public boolean validateOneTimePassword(String username, int verificationCode);
	
	public void upgradeInfo(User users);

	public int generateOneTimePassword(String username);

	public void sendEmail(String receiverEmail, String emailMessage, String emailSubject);
	
	public String generatePassword();
}
