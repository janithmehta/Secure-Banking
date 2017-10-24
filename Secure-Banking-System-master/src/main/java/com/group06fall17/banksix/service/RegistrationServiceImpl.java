/**
 * 
 */
package com.group06fall17.banksix.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group06fall17.banksix.dao.BankAccountDAO;
import com.group06fall17.banksix.dao.ExternalUserDAO;
import com.group06fall17.banksix.dao.PIIDAO;
import com.group06fall17.banksix.dao.UserDAO;
import com.group06fall17.banksix.model.BankAccount;
import com.group06fall17.banksix.model.ExternalUser;
import com.group06fall17.banksix.model.PII;
import com.group06fall17.banksix.model.User;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder;

/**
 * @author Abhilash
 *
 */

@Service
public class RegistrationServiceImpl implements RegistrationService {
	@Autowired
	private UserDAO usrDAO;

	@Autowired
	private ExternalUserDAO extUsrDao;

	@Autowired
	private BankAccountDAO bankAccountDao;
	
	@Autowired
	private PIIDAO piiDao;

	GoogleAuthenticatorConfigBuilder configBuilder;
	GoogleAuthenticatorConfig config;
	GoogleAuthenticator gAuth;	
	
	@Override
	public void addLoginInfo(User users) {
		usrDAO.add(users);
	}

	@Override
	@Transactional
	public PrivateKey addExternalUser(ExternalUser externalUser) {
		// Public private key generation
		KeyPair keyPair = generateKeyPair();
		try {
			externalUser.setPublickey(new SerialBlob(keyPair.getPublic().getEncoded()));
			extUsrDao.add(externalUser);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return keyPair.getPrivate();
	}
	
	@Override
	@Transactional
	public void addBankAccount(BankAccount bankAccount) {
			bankAccountDao.add(bankAccount);
	}

	@Override
	@Transactional(readOnly = true)
	public ExternalUser userIfExists(String email) {
		return extUsrDao.findUserByEmail(email);
	}
	
	// Added by Saurabh, all users emails should be checked
	// not only from the external user table
	@Override
	@Transactional(readOnly = true)
	public User userIfExistsFromAllUsers(String email) {
		return usrDAO.findUsersByEmail(email);
	}

	public KeyPair generateKeyPair() {
		KeyPair keyPair = null;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(1024);
			keyPair = keyPairGenerator.genKeyPair();
		} catch (Exception e) {
			System.out.println("Error generating RSA keys");
		}
		return keyPair;
	}

	@Override
	public String generateTemporaryKeyFile(PrivateKey key) {
		try {
			Random randomGenerator = new Random();
			int rand = randomGenerator.nextInt();			
			if (rand<0)
				rand *= -1;
			
			File temp = File.createTempFile(rand + "", ".tmp");
			byte[] encoded = key.getEncoded();
			Files.write(Paths.get(temp.getAbsolutePath()), encoded);
			return temp.getName();
		} catch(IOException e){    		
    	    e.printStackTrace();
    	}
		return "";
	}
	
	@Override
	public String getPrivateKeyLocation(String randFile) {
		String tempDir = System.getProperty("java.io.tmpdir");
		return tempDir + "/" + randFile;			
	}

	@Override
	public String getVisaStatus() {
		// this should be external service
		String[] visaStatus = {
							"F1",
							"H1B",
							"B1",
							"B2",
							"F2",
							"L1",
							"L2",
							"L4"
		}; 		
		return visaStatus[ThreadLocalRandom.current().nextInt(0, visaStatus.length)];		
	}

	@Override
	@Transactional(readOnly = true)
	public ExternalUser externalUserWithSSNExists(String ssn) {
		return extUsrDao.findUserBySSN(ssn);
	}

	@Override
	@Transactional
	public void addPii(PII pii) {
		piiDao.add(pii);
	}	
	
}