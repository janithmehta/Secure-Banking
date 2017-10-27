package com.group06fall17.banksix.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
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

import com.group06fall17.banksix.component.RecaptchaCheck;
import com.group06fall17.banksix.model.BankAccount;
import com.group06fall17.banksix.model.ExternalUser;
import com.group06fall17.banksix.model.PII;
import com.group06fall17.banksix.model.User;
import com.group06fall17.banksix.service.RegistrationService;
import static com.group06fall17.banksix.constants.Constants.EMAIL_PATTERN;

@Controller
public class RegistrationController {
	@Autowired
	RegistrationService registerService;

	/*private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";*/

	private Pattern email_pattern = Pattern.compile(EMAIL_PATTERN);

	@RequestMapping("registration")
	public ModelAndView RegistrationPage() {
		return new ModelAndView("registration");
	}

	@RequestMapping(value = "reg_validate", method = RequestMethod.POST)
	public ModelAndView addUser(HttpServletRequest request) {

		try {
			String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
			boolean verify = RecaptchaCheck.captchaVerification(gRecaptchaResponse);
			//TODO UNCOMMENT AFTER UPDATING CAPTCHA INFO
			/*if (!verify) {
				return new ModelAndView("redirect:/registration");
			}*/
		} catch (IOException e) {
			e.printStackTrace();
		}

		// capture form fields
		System.out.println("Caturing form fields!");
		String name = request.getParameter("name").toString();
//		String middleName = request.getParameter("Middle_Name").toString();
//		String lastName = request.getParameter("Last_Name").toString();
		String emailId = request.getParameter("email").toString();
		String password = request.getParameter("password").toString();
		String repassword = request.getParameter("confirmpassword").toString();
		String accountType = request.getParameter("AccountType").toString();
		String organisationName = request.getParameter("BusinessName").toString();
		String address = request.getParameter("address").toString();
//		String addressLine2 = request.getParameter("Address2").toString();
//		String city = request.getParameter("City").toString();
//		String state = request.getParameter("State").toString();
//		String zipcode = request.getParameter("Pin_Code").toString();
		String ssn = request.getParameter("SSN").toString();

		// validate input
		StringBuilder errors = new StringBuilder();
		if (!validateField(name, 1, 30, false)) {
			errors.append("<li>Name should not be empty. It should be between 1-30 characters and without spaces or special characters</li>");
		}
//		if (!validateField(middleName, 0, 30, true)) {
//			errors.append("<li>Middle Name must not more than 30 characters</li>");
//		}
//		if (!validateField(lastName, 1, 30, false)) {
//			errors.append("<li>Last Name must not be empty, be between 1-30 characters and not have spaces or special characters</li>");
//		}

		// email validations
		if (!validateFieldSpecialCharactersAllowed(emailId, 1, 30, false)) {
			errors.append("<li>Email Id must not be empty, be between 1-30 characters and not have spaces</li>");
		}
		Matcher matcher = email_pattern.matcher(emailId);
		if (!matcher.matches()) {
			errors.append("<li>Email Id must be a proper email address</li>");
		}

		if (registerService.userIfExistsFromAllUsers(emailId) != null) {
			errors.append("<li>An user exists with the given email, please use an alternate email</li>");
		}

		// password validations
		if (!validateFieldSpecialCharactersAllowed(password, 1, 30, false)) {
			errors.append("<li>Password must not be empty, be between 1-30 characters and not have spaces</li>");
		} else {
			if (!password.equals(repassword))
				errors.append("<li>Password and Re-entered password are not the same</li>");
		}

		// Account Type & Bname validations
		if (!accountType.equals("individual") && !accountType.equals("merchant")) {
			errors.append("<li>Invalid account type, allowed account types are 'Individual' or 'Merchant'</li>");
		}

		if (accountType.equals("merchant")) {
			if (!validateField(organisationName, 1, 30, true)) {
				errors.append(
						"<li>For Merchant accounts, Organization Name must not be empty, and be between 1-30 characters and not have special characters</li>");
			}
		}

		if (!validateField(address, 1, 30, true)) {
			errors.append("<li>Address Line 1 must not be empty, be between 1-30 characters and not have special characters</li>");
		}
		/*if (!validateField(addressLine2, 1, 30, true)) {
			errors.append("<li>Address Line 2 must not be empty, be between 1-30 characters and not have special characters</li>");
		}
		if (!validateField(city, 1, 16, true)) {
			errors.append("<li>City must not be empty, be between 1-16 characters and not have spaces or special characters</li>");
		}
		if (!validateField(state, 1, 16, false)) {
			errors.append("<li>State must not be empty, be between 1-16 characters and not have spaces or special characters</li>");
		}
		if (!validateField(zipcode, 1, 5, false)) {
			errors.append("<li>Zipcode must not be empty, be between 1-5 characters and not have spaces or special characters</li>");
		}*/
		if (!validateField(ssn, 9, 9, false)) {
			errors.append("<li>SSN must not be empty, be 9 characters long and not have spaces or special characters</li>");
		}
		if (registerService.externalUserWithSSNExists(ssn) != null) {
			errors.append("<li>An user exists with the given SSN, please use an alternate SSN</li>");
		}
		System.out.println("SAURABH 1");
		if (errors.length() != 0) { // return back with errors and previously
									// inputed values
			Map<String, Object> fieldMap = new HashMap<String, Object>();
			fieldMap.put("name", name);
//			fieldMap.put("lastName", lastName);
//			fieldMap.put("middleName", middleName);
			fieldMap.put("email", emailId);
			fieldMap.put("password", password);
			fieldMap.put("userType", accountType);
			if (organisationName != null)
				fieldMap.put("organisationName", organisationName);
			else
				fieldMap.put("organisationName", "");
			fieldMap.put("addreess", address);
//			fieldMap.put("addreess", "Tempe");
//			fieldMap.put("addressLine2", addressLine2);
//			fieldMap.put("city", city);
//			fieldMap.put("state", state);
//			fieldMap.put("zipcode", zipcode);
			fieldMap.put("ssn", ssn);

			errors.insert(0, "Please fix the following input errors: <br /><ol>");
			errors.append("</ol>");
			fieldMap.put("errors", errors.toString());
			return new ModelAndView("registration", fieldMap);
		}

		System.out.println("SAURABH:Creating a new ExternalUser!");
		// passed validation, register user
		ExternalUser external = new ExternalUser();
		external.setName(name);
		/*if (middleName != null)
			external.setMiddlename(middleName);

		external.setLastname(lastName);
		external.setAddress(addressLine1);
		external.setAddressline2(addressLine2);
		external.setCity(city);
*/		external.setAddress(address);
		external.setSsn(ssn);
//		external.setState(state);
		external.setUserType(accountType);
		if (accountType.equals("merchant"))
			external.setOrganisationName(organisationName);
//		external.setZipcode(zipcode);

		User users = new User();
		users.setUsername(emailId);
		users.setUserActive(1);
		
		PII pii = new PII();
		pii.setSsn(ssn);
		pii.setStateID(registerService.getVisaStatus());
		

		StandardPasswordEncoder encryption = new StandardPasswordEncoder();
		users.setPassword(encryption.encode(request.getParameter("password").toString()));
		// users.setUserType("ROLE_INDIVIDUAL");
		System.out.println("Setting USER TYPE!!");
		if (accountType.equals("individual"))
			users.setUserType("ROLE_INDIVIDUAL");
		else if (accountType.equals("merchant"))
			users.setUserType("ROLE_MERCHANT");

		external.setEmail(users);

		registerService.addLoginInfo(users);
		PrivateKey key = registerService.addExternalUser(external);

		// user is created now create checking and savings bank accounts
		// for that user
		System.out.println("Creating Bank Account");
		BankAccount checking = new BankAccount();
		checking.setAccountnumber(registerService.userIfExists(emailId).getUsrid() + "01");
		checking.setAccounttype("checking");
		checking.setAccountstatus("active");
		checking.setBalance(100);
		checking.setAcctcreatedate(new Date());
		checking.setUsrid(registerService.userIfExists(emailId));

		BankAccount savings = new BankAccount();
		savings.setAccountnumber(registerService.userIfExists(emailId).getUsrid() + "02");
		savings.setAccounttype("savings");
		savings.setAccountstatus("active");
		savings.setBalance(100);
		savings.setAcctcreatedate(new Date());
		savings.setUsrid(registerService.userIfExists(emailId));

		registerService.addBankAccount(checking);
		registerService.addBankAccount(savings);
		
		registerService.addPii(pii);
		
		// prepare to pass data back to registration successful page
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println("Passing data to Successful page");
		map.put("name", external.getName());
//		map.put("lastName", external.getLastname());
		map.put("showEmailId", emailId);
		map.put("checkingAccountNo", checking.getAccountnumber());
		map.put("savingsAccountNo", savings.getAccountnumber());
		// map.put("pvtKey", Arrays.toString(key.getEncoded()));
		map.put("pvtKey", registerService.generateTemporaryKeyFile(key));

		return new ModelAndView("registrationSuccessful", map);
	}

	@RequestMapping(value = "boaprivatekey.key", method = RequestMethod.POST)
	public void getKey(HttpServletRequest request, HttpServletResponse response) {
		// capture form fields
		/*
		 * String pvtKey = request.getParameter("PrivateKey").toString();
		 * response.setContentType("application/octet-stream");
		 * //response.setHeader("Content-Disposition","attachment; filename=\""
		 * + file.getFilename() +"\""); return new
		 * FileSystemResource(registerService.getPrivateKeyLocation(pvtKey))
		 * ;
		 */

		String pvtKey = request.getParameter("PrivateKey").toString();
		try {
			// copy it to response's OutputStream
			InputStream is = new FileInputStream(registerService.getPrivateKeyLocation(pvtKey));
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private boolean validateFieldSpecialCharactersAllowed(String field, int minSize, int maxSize, boolean spacesAllowed) {
		if (field == null)
			return false;
		if (!spacesAllowed && field.indexOf(" ") != -1)
			return false;
		if (field.length() < minSize || field.length() > maxSize)
			return false;

		return true;
	}
	
	private boolean validateField(String field, int minSize, int maxSize, boolean spacesAllowed) {
		if (field == null)
			return false;
		if (spacesAllowed && hasSpecialCharactersWithSpace(field)) 
			return false;
		if (!spacesAllowed && hasSpecialCharactersNoSpace(field))
			return false;
		if (field.length() < minSize || field.length() > maxSize)
			return false;			
		return true;
	}
	
	private boolean hasSpecialCharactersWithSpace(String field) {
		if (!StringUtils.isAlphanumericSpace(field))
			return true;
		
		return false;
	}
	
	private boolean hasSpecialCharactersNoSpace(String field) {
		if (!StringUtils.isAlphanumeric(field))
			return true;
		
		return false;
	}
	
}