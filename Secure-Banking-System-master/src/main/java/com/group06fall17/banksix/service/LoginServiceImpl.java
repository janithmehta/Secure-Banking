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

import com.group06fall17.banksix.dao.UserOtpDAO;
import com.group06fall17.banksix.dao.UserOtpDAOImpl;
import com.group06fall17.banksix.dao.UserDAO;
import com.group06fall17.banksix.model.UserOtp;
import com.group06fall17.banksix.model.User;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.KeyRepresentation;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder;

import com.group06fall17.banksix.component.BOASendMail;

/**
 * @authors Reshma Venkat
 *
 */

@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private BOASendMail boaSendEmail;

	@Autowired
	private UserDAO usersDao;

	UserOtpDAO userOtpDao;
	UserOtp userOtp;
	GoogleAuthenticatorConfigBuilder configBuilder;
	GoogleAuthenticatorConfig config;
	GoogleAuthenticator gAuth;

	@Override
	public boolean validateOtp(String username, int verificationCode) {
		userOtp = userOtpDao.get(username);
		boolean isCodeValid = false;

		if (userOtp != null) {
			isCodeValid = (userOtp.getOtpcode() == verificationCode)
					&& (new Date().getTime() <= userOtp.getOtpvalidity()) ? true : false;
		}
		return isCodeValid;
	}

	@Override
	public int generateOTP(String username) {
		final GoogleAuthenticatorKey key = gAuth.createCredentials(username);
		return key.getVerificationCode();
	}

	@Override
	public void sendEmail(String emailId, String message, String subject) {
		boaSendEmail.SendMailToCustomer(emailId, subject, message);
	}

	@PostConstruct
	public void initIt() throws Exception {
		configBuilder = new GoogleAuthenticatorConfigBuilder().setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(30))
				.setWindowSize(50).setKeyRepresentation(KeyRepresentation.BASE64);
		config = configBuilder.build();
		gAuth = new GoogleAuthenticator(config);
		userOtpDao = new UserOtpDAOImpl();
	}

	@Override
	public void updateLoginInfo(User users) {
		usersDao.update(users);
	}
	
	@Override
	public String generatePassword() {
		return RandomStringUtils.randomAlphanumeric(10);

	}
}
