
package com.group06fall17.banksix.service;

import java.security.KeyPair;
import java.security.PrivateKey;

import com.group06fall17.banksix.model.BankAccount;
import com.group06fall17.banksix.model.ExternalUser;
import com.group06fall17.banksix.model.Pii;
import com.group06fall17.banksix.model.Users;

/**
 * @author Vishnu Priya
 *
 */
public interface RegistrationService {
	public void addLoginInfo(Users user);

	public PrivateKey addExternalUser(ExternalUser externalUser);

	public void addBankAccount(BankAccount bankAccount);
	
	public void addPii(Pii pii);

	public ExternalUser userIfExists(String email);

	public Users userIfExistsFromAllUsers(String email);
	
	public ExternalUser externalUserWithSSNExists(String ssn);

	public String generateTemporaryKeyFile(PrivateKey key);

	public String getPrivateKeyLocation(String randFile);

	public KeyPair generateKeyPair();

	public String getVisaStatus();
}