/**
 * 
 */
package com.group06fall17.banksix.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group06fall17.banksix.dao.UserOTPDAO;
import com.group06fall17.banksix.dao.UserOTPDAOImplementation;
import com.group06fall17.banksix.dao.UserDAO;
import com.group06fall17.banksix.model.UserOTP;
import com.group06fall17.banksix.model.User;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.KeyRepresentation;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder;

import com.group06fall17.banksix.component.Email;

/**
 * @authors Saurabh
 *
 */

@Service
public class LoginManagerImpl implements LoginManager {
	@Autowired
	private Email email;

	@Autowired
	private UserDAO usrDAO;

	UserOTPDAO userOTPDAO;
	UserOTP userOTP;
	GoogleAuthenticatorConfigBuilder configurationBuilder;
	GoogleAuthenticatorConfig configuration;
	GoogleAuthenticator googleAuthenticator;

	@Override
	public boolean validateOneTimePassword(String username, int verificationCode) {
		userOTP = userOTPDAO.get(username);
		boolean isCodeValid = false;

		if (userOTP != null) {
			isCodeValid = (userOTP.getCode() == verificationCode)
					&& (new Date().getTime() <= userOTP.getValidity()) ? true : false;
		}
		return isCodeValid;
	}

	@Override
	public int generateOneTimePassword(String username) {
		final GoogleAuthenticatorKey key = googleAuthenticator.createCredentials(username);
		return key.getVerificationCode();
	}

	@Override
	public void sendEmail(String receiverEmail, String emailMessage, String emailSubject) {
		email.sendEmail(receiverEmail, emailMessage, emailSubject);
	}

	@PostConstruct
	public void initIt() throws Exception {
		configurationBuilder = new GoogleAuthenticatorConfigBuilder().setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(30))
				.setWindowSize(50).setKeyRepresentation(KeyRepresentation.BASE64);
		configuration = configurationBuilder.build();
		googleAuthenticator = new GoogleAuthenticator(configuration);
		userOTPDAO = new UserOTPDAOImplementation();
	}

	@Override
	public void upgradeInfo(User users) {
		usrDAO.update(users);
	}
	
	@Override
	public String generatePassword() {
		return RandomStringUtils.randomAlphanumeric(10);

	}
}
