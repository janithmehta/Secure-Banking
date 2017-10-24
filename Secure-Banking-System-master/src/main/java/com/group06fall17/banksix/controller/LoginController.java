/**
 * 
 */
package com.group06fall17.banksix.controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.group06fall17.banksix.component.SessionDetails;
import com.group06fall17.banksix.component.VerifyRecaptcha;
import com.group06fall17.banksix.dao.UserDAO;
import com.group06fall17.banksix.model.User;
import com.group06fall17.banksix.service.LoginManager;

/**
 * @author Saurabh
 *
 */

@Controller
@Scope("request")
public class LoginController {
	@Autowired
	private SessionDetails sessionDetails;

	@Autowired
	private LoginManager loginManager;

	@Autowired
	private UserDAO userDAO;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private Pattern email_pattern = Pattern.compile(EMAIL_PATTERN);

	@RequestMapping("/login")
	public ModelAndView getLoginForm(@RequestParam(required = false) String authfailed, String logout,
			HttpSession session) {
		String message = "";
		if (authfailed != null) {
			System.out.println(" Session : " + sessionDetails);
			System.out.println(" Authfailed value :" + authfailed);
			System.out.println(" Username : " + sessionDetails.getUsername());
			/*if (sessionDetails.getAnothersession() == 0)
				message = "Close other browsers ";
			else*/ 
			if (sessionDetails.getUsername().equals("notfound"))
				message = "Username does not exist";
			else if (sessionDetails.getUserDownAttempts() >= 3) {
				System.out.println("Failure Attempts in controller : " + sessionDetails.getUserDownAttempts());
				if (sessionDetails.getUserDownAttempts() == 3) {
					User updateuser = userDAO.findUsersByEmail(sessionDetails.getUsername());
					String password = generatePassword();
					StandardPasswordEncoder encryption = new StandardPasswordEncoder();
					updateuser.setPassword(encryption.encode(password));
					updateuser.setUserDown(0);
					sessionDetails.setUserDownAttempts(0);
					userDAO.update(updateuser);
					loginManager.sendEmail(sessionDetails.getUsername(), password, "password");
				}
				message = "Your password was reset. A temporary password was mailed to your email-id";

			} else
				message = "Invalid username/password";

		} else if (logout != null) {
			System.out.println("Logged successfully");
			message = "Logged out successfully, to login again please restart your browser!";
			session.invalidate();
		}
		return new ModelAndView("login", "message", message);

	}

	@RequestMapping("/otp")
	public ModelAndView geOtpView(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		session.setAttribute("BOAUsername", username);
		int otp = loginManager.generateOneTimePassword(username);
		loginManager.sendEmail(username, "OTP is " + Integer.toString(otp) + "\n\n You cannot recognize this activity? \n PLEASE REPORT TO THE BANK IMMEDIATELY!!", "One Time Password for Login with BANKSIX account");
		return new ModelAndView("otp");

	}

	@RequestMapping(value = "/otpverification", method = RequestMethod.POST)
	public ModelAndView verifyOtpView(HttpServletRequest request) {
		String message = "";
		HttpSession session = request.getSession(true);
		String username = session.getAttribute("BOAUsername").toString();
		String otp_pwd = request.getParameter("password").toString();
		
		if(!isNumericInteger(otp_pwd)) {
			message = "Invalid OTP!";
			try {
				request.logout();
				//session.invalidate();
			} catch (ServletException e) {
				e.printStackTrace();
			}
			return new ModelAndView("/login", "message", message);
		}
		
		boolean isCodeValid = loginManager.validateOneTimePassword(username, Integer.parseInt(otp_pwd));
		

		// validations
		
		
		ModelAndView modelView = null;

		if (isCodeValid) {
			System.out.println("In the if part");
			
			// object
			message = "OTP Validated!";
			sessionDetails.setUsername(username);
			sessionDetails.setUserActive(1);

			User users = userDAO.findUsersByEmail(username);

			switch (users.getUserType()) {
			case "ROLE_INDIVIDUAL":
			case "ROLE_MERCHANT":
				/*
				 * ExternalUser extUser =
				 * externalUserDao.findUserByEmail(username); List<BankAccount>
				 * bankAccounts =
				 * bankAccountDao.findAccountsOfUser(extUser.getUsrid());
				 * 
				 * Map<String, Object> map = new HashMap<String, Object>();
				 * map.put("firstName", extUser.getName());
				 * map.put("lastName", extUser.getLastname());
				 * map.put("bankAccounts", bankAccounts); map.put("message",
				 * message);
				 */
				modelView = new ModelAndView("loginSuccessful");
				break;

			case "ROLE_EMPLOYEE":
			case "ROLE_MANAGER":
			case "ROLE_ADMIN":
				modelView = new ModelAndView("redirect:/employee");
				break;

			default:
				modelView = new ModelAndView("/login");
				break;
			}

		} else {
			message = "Invalid OTP!";
			try {
				request.logout();
				// session.invalidate();
			} catch (ServletException e) {
				e.printStackTrace();
			}
			modelView = new ModelAndView("/login", "message", message);
		}

		return modelView;
	}

	@RequestMapping("403page")
	public String ge403denied() {
		return "redirect:login?denied";
	}

	@RequestMapping("/ForgotPassword")
	public ModelAndView forgotPasswordPage() {
		return new ModelAndView("ForgotPassword");
	}

	public String generatePassword() {
		return RandomStringUtils.randomAlphanumeric(10);

	}

	@RequestMapping(value = "/resetpwd", method = RequestMethod.POST)
	public ModelAndView mailpwd(HttpServletRequest request) {
		String email = request.getParameter("email").toString();
		StringBuilder errors = new StringBuilder();

		if (!validateField(email, 1, 30, false)) {
			errors.append("<li>Email Id must not be empty, be between 1-30 characters and not have spaces</li>");
		}
		Matcher matcher = email_pattern.matcher(email);
		if (!matcher.matches()) {
			errors.append("<li>Email Id must be a proper email address</li>");
		}
		String message = new String();

		if (errors.length() > 0) {
			message = "Invalid email address specified";
			return new ModelAndView("ForgotPassword", "message", message);
		}
		String password = generatePassword();

		User user = userDAO.findUsersByEmail(email);
		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");

		System.out.println("Recaptcha Response:" + gRecaptchaResponse);
		try {
			boolean verify = VerifyRecaptcha.verify(gRecaptchaResponse);

			if (user != null && verify) {
				StandardPasswordEncoder encryption = new StandardPasswordEncoder();
				user.setPassword(encryption.encode(password));
				user.setUserDown(0);				
				userDAO.update(user);
				loginManager.sendEmail(email, "Your password: " + password, "Bank of Arizona Password");
				message = "Your password was reset. A temporary password was mailed to your email-id";				
			} else
				message = "Username does not exist";
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ModelAndView("ForgotPassword", "message", message);
	}

	private boolean validateField(String field, int minSize, int maxSize, boolean spacesAllowed) {
		if (field == null)
			return false;
		if (!spacesAllowed && field.indexOf(" ") != -1)
			return false;
		if (field.length() < minSize || field.length() > maxSize)
			return false;

		return true;
	}
	
	public boolean isNumericInteger(String str) {
		if (str == null)
			return false;
		try {
			int d = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}