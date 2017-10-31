/**
 * @author Saurabh, Abhilash, Shubham
 */
package com.group06fall17.banksix.controller;

import static com.group06fall17.banksix.constants.Constants.EMAIL_REGEX;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.supercsv.cellprocessor.ParseInt;

import com.group06fall17.banksix.component.RecaptchaCheck;
import com.group06fall17.banksix.model.BankAccount;
import com.group06fall17.banksix.model.ExternalUser;
import com.group06fall17.banksix.model.PII;
import com.group06fall17.banksix.model.User;
import com.group06fall17.banksix.service.RegistrationService;
import com.group06fall17.banksix.utilities.RandStrGen;

@Controller
public class RegistrationController {
	@Autowired
	RegistrationService registrationService;

	private Pattern emailRegex = Pattern.compile(EMAIL_REGEX);

	@RequestMapping("registration")
	public ModelAndView RegistrationPage() {
		return new ModelAndView("registration");
	}

	@RequestMapping(value = "validation", method = RequestMethod.POST)
	public ModelAndView addUser(HttpServletRequest httpRequest) {

		try {
			String gRecaptchaResponse = httpRequest.getParameter("g-recaptcha-response");
			boolean verify = RecaptchaCheck.captchaVerification(gRecaptchaResponse);
			//TODO UNCOMMENT AFTER UPDATING CAPTCHA INFO
			/*if (!verify) {
				return new ModelAndView("redirect:/registration");
			}*/
		} catch (IOException e) {
			e.printStackTrace();
		}

		/**
		 * Capture the WEB form fields
		 */
		System.out.println("Processing WEB form fields!");
		String name = httpRequest.getParameter("name").toString();
		String email = httpRequest.getParameter("email").toString();
		String password = httpRequest.getParameter("password").toString();
		String confirmPassword = httpRequest.getParameter("confirmpassword").toString();
		String accountType = httpRequest.getParameter("AccountType").toString();
		String organisationName = httpRequest.getParameter("organisationName").toString();
		String address = httpRequest.getParameter("address").toString();
		String SSN = httpRequest.getParameter("SSN").toString();

		/**
		 * Validations
		 */
		StringBuilder errorString = new StringBuilder();
		if (!isValid(name, 1, 30, true)) {
			errorString.append("<li>Field Name shouldn't be empty. Enter characters between 1-30 with NO special characters </li>");
		}
		if (!isValidWithSpecialCharacters(email, 1, 30, false)) {
			errorString.append("<li>Field Email shouldn't be empty. Enter characters between 1-30 with NO spaces</li>");
		}
		Matcher matcher = emailRegex.matcher(email);
		if (!matcher.matches()) {
			errorString.append("<li>Wrong Email format</li>");
		}

		if (registrationService.userIfExistsFromAllUsers(email) != null) {
			errorString.append("<li>Email already in use.</li>");
		}

		if (!isValidWithSpecialCharacters(password, 1, 30, false)) {
			errorString.append("<li>Field passwrod shouldn't be empty. Enter characters between 1-30 with NO spaces</li>");
		} else {
			if (!password.equals(confirmPassword))
				errorString.append("<li>The entered and re-entered password fields don't match</li>");
		}

		if (!accountType.equals("individual") && !accountType.equals("merchant")) {
			errorString.append("<li>Account type shoukd be either 'Individual' or 'Merchant'</li>");
		}

		if (accountType.equals("merchant")) {
			if (!isValid(organisationName, 1, 30, true)) {
				errorString.append(
						"<li>Name of the organization shouldn't be empty. Enter characters between 1-30 with NO special characters</li>");
			}
		}

		if (!isValid(address, 1, 30, true)) {
			errorString.append("<li>Field Address shouldn't be empty. Enter characters between 1-30 with NO special characters</li>");
		}
		if (!isValid(SSN, 9, 9, false)) {
			errorString.append("<li>Field SSN shouldn't be empty. Enter characters between 0-9 with NO spaces or special characters</li>");
		}
		if (registrationService.externalUserWithSSNExists(SSN) != null) {
			errorString.append("<li>User already exists with the provided SSN</li>");
		}
		System.out.println("Return found ERRORS back to WEB form");
		if (errorString.length() != 0) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("email", email);
			map.put("password", password);
			map.put("accountType", accountType);
			if (organisationName != null)
				map.put("organisationName", organisationName);
			else
				map.put("organisationName", "");
			map.put("address", address);
			map.put("SSN", SSN);

			errorString.insert(0, "Please fix the input validation errors: <br /><ol>");
			errorString.append("</ol>");
			map.put("errors", errorString.toString());
			return new ModelAndView("registration", map);
		}

		System.out.println("Validations done, registering user.");
		ExternalUser external = new ExternalUser();
		external.setName(name);
		external.setAddress(address);
		external.setSsn(SSN);
		external.setUserType(accountType);
		if (accountType.equals("merchant"))
			external.setOrganisationName(organisationName);

		User users = new User();
		users.setUsername(email);
		users.setUserActive(1);
		
		PII pii = new PII();
		pii.setSsn(SSN);
		pii.setStateID(registrationService.getVisaStatus());
		

		StandardPasswordEncoder encryption = new StandardPasswordEncoder();
		users.setPassword(encryption.encode(httpRequest.getParameter("password").toString()));
		System.out.println("Categorizing EXTERNAL user");
		if (accountType.equals("individual"))
			users.setUserType("ROLE_INDIVIDUAL");
		else if (accountType.equals("merchant"))
			users.setUserType("ROLE_MERCHANT");

		external.setEmail(users);

		registrationService.addLoginInfo(users);
		PrivateKey key = registrationService.addExternalUser(external);

		System.out.println("Creating a CHECKING account");
		BankAccount checking = new BankAccount();
		checking.setAccountnumber(registrationService.userIfExists(email).getUsrid() + "91");
		checking.setAccounttype("checking");
		checking.setAccountstatus("active");
		checking.setBalance(100);
		checking.setAcctcreatedate(new Date());
		checking.setUsrid(registrationService.userIfExists(email));

		System.out.println("Creating a SAVINGS account");
		BankAccount savings = new BankAccount();
		savings.setAccountnumber(registrationService.userIfExists(email).getUsrid() + "92");
		savings.setAccounttype("savings");
		savings.setAccountstatus("active");
		savings.setBalance(100);
		savings.setAcctcreatedate(new Date());
		savings.setUsrid(registrationService.userIfExists(email));

		System.out.println("Creating a CREDIT account");
		BankAccount credit = new BankAccount();
		credit.setAccountnumber(registrationService.userIfExists(email).getUsrid() + "93");
		credit.setAccounttype("credit");
		RandStrGen cvvGen=new RandStrGen();
//		int cvv=Integer.parseInt(cvvGen.randomCVV());
		credit.setCvv(cvvGen.randomCVV());
		credit.setAccountstatus("active");
		credit.setBalance(0);
		credit.setAcctcreatedate(new Date());
		credit.setUsrid(registrationService.userIfExists(email));
		
		RandStrGen cardGen=new RandStrGen();
		String card=cardGen.randomCard();
		credit.setCardNo(card);
		
		
		registrationService.addBankAccount(checking);
		registrationService.addBankAccount(savings);
		registrationService.addBankAccount(credit);
		registrationService.addPii(pii);
		
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println("Passing data to Successful page");
		map.put("name", external.getName());
		map.put("email", email);
		map.put("checkingAccountNo", checking.getAccountnumber());
		map.put("savingsAccountNo", savings.getAccountnumber());
		map.put("creditAccountNo", credit.getAccountnumber());
		map.put("creditCardNo", credit.getCardNo());
		map.put("cvv", credit.getCvv());
		map.put("pvtKey", registrationService.generateTemporaryKeyFile(key));

		return new ModelAndView("registrationSuccessful", map);
	}

	@RequestMapping(value = "boaprivatekey.key", method = RequestMethod.POST)
	public void getKey(HttpServletRequest request, HttpServletResponse response) {

		String pvtKey = request.getParameter("PrivateKey").toString();
		try {
			InputStream is = new FileInputStream(registrationService.getPrivateKeyLocation(pvtKey));
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private boolean isValidWithSpecialCharacters(String fieldName, int lowerSize, int upperSize, boolean spaceFlag) {
		if (fieldName == null)
			return false;
		if (!spaceFlag && fieldName.indexOf(" ") != -1)
			return false;
		if (fieldName.length() < lowerSize || fieldName.length() > upperSize)
			return false;

		return true;
	}
	
	private boolean isValid(String fieldName, int lowerSizeLimit, int upperSizeLimit, boolean spaceFlag) {
		if (fieldName == null)
			return false;
		if (spaceFlag) {
			if(!StringUtils.isAlphanumericSpace(fieldName))
				return false;
		} else {
			if(!StringUtils.isAlphanumeric(fieldName))
				return false;
		}
		if (fieldName.length() < lowerSizeLimit || fieldName.length() > upperSizeLimit)
			return false;			
		return true;
	}
}