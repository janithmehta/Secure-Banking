/**
 * @author Saurabh, Shubham, Abhilash
 */
package com.group06fall17.banksix.controller;

import static com.group06fall17.banksix.constants.Constants.EMAIL_REGEX;
import com.group06fall17.banksix.model.User;
import javax.servlet.http.HttpServletRequest;
import com.group06fall17.banksix.service.LoginManager;
import javax.servlet.http.HttpSession;
import com.group06fall17.banksix.utilities.RandStrGen;
import org.springframework.stereotype.Controller;
import java.util.regex.Matcher;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.bind.annotation.RequestParam;
import com.group06fall17.banksix.component.UserSessionInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMethod;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.group06fall17.banksix.component.RecaptchaCheck;
import com.group06fall17.banksix.dao.UserDAO;

@Controller
@Scope("request")
public class LoginController {
	@Autowired
	private UserSessionInfo sessionObj;

	@Autowired
	private LoginManager loginManager;

	@Autowired
	private UserDAO usrDAO;

	/*private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";*/

	private Pattern email_pattern = Pattern.compile(EMAIL_REGEX);

	@RequestMapping("/login")
	public ModelAndView getLoginForm(@RequestParam(required = false) String authfailed, String logout,
			HttpSession session) {
		
		String message = "";

		if (authfailed != null) {
			System.out.println(" Session : " + sessionObj);
			System.out.println(" Authfailed value :" + authfailed);
			System.out.println(" Username : " + sessionObj.getUsername());
			/*if (sessionObj.getUserothersession() == 0)
				message = "Close other browsers ";
			else*/ 
			
			if (sessionObj.getUsername().equals("notfound"))
				message = "Username does not exist";
			else if (sessionObj.getUserDownAttempts() >= 3) {
				System.out.println("Failure Attempts in controller : " + sessionObj.getUserDownAttempts());
				if (sessionObj.getUserDownAttempts() == 3) {
					User updateuser = usrDAO.findUsersByEmail(sessionObj.getUsername());
					String password = generatePassword();
					StandardPasswordEncoder encryption = new StandardPasswordEncoder();
					updateuser.setPassword(encryption.encode(password));
					updateuser.setUserDown(0);
					sessionObj.setUserDownAttempts(0);
					usrDAO.update(updateuser);
					loginManager.sendEmail(sessionObj.getUsername(), password, "password");
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
			sessionObj.setUsername(username);
			sessionObj.setUserActive(1);

			User users = usrDAO.findUsersByEmail(username);

			if(users.getUserType().equals("ROLE_INDIVIDUAL")||users.getUserType().equals("ROLE_MERCHANT")){
				modelView = new ModelAndView("redirect:/customer");
			}
			
			else if(users.getUserType().equals("ROLE_EMPLOYEE")||users.getUserType().equals("ROLE_MANAGER")||users.getUserType().equals("ROLE_ADMIN")) {
				modelView = new ModelAndView("redirect:/employee");
			} else {
				modelView = new ModelAndView("/login");
			}
			
			
			
//			switch (users.getUserType()) {
//			case "ROLE_INDIVIDUAL":
//			case "ROLE_MERCHANT":
//				/*
//				 * ExternalUser extUser =
//				 * extUsrDao.searchUsrByEmail(username); List<BankAccount>
//				 * bankAccounts =
//				 * bankAccntDao.findAccountsOfUser(extUser.getUsrid());
//				 * 
//				 * Map<String, Object> map = new HashMap<String, Object>();
//				 * map.put("firstName", extUser.getName());
//				 * map.put("lastName", extUser.getLastname());
//				 * map.put("bankAccounts", bankAccounts); map.put("message",
//				 * message);
//				 */
//				modelView = new ModelAndView("loginSuccessful");
//				break;
//
//			case "ROLE_EMPLOYEE":
//			case "ROLE_MANAGER":
//			case "ROLE_ADMIN":
//				modelView = new ModelAndView("redirect:/employee");
//				break;
//
//			default:
//				modelView = new ModelAndView("/login");
//				break;
//			}

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
		RandStrGen rsg=new RandStrGen();
		String securePassword=rsg.randomString(20);
		return securePassword;
	}

	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public ModelAndView mailpwd(HttpServletRequest request) {
		String email = request.getParameter("email").toString();
		StringBuilder errors = new StringBuilder();

		if (!isValid(email, 1, 50, false)) {
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

		User user = usrDAO.findUsersByEmail(email);
		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");

		System.out.println("Recaptcha Response:" + gRecaptchaResponse);
		try {
			boolean verify = RecaptchaCheck.captchaVerification(gRecaptchaResponse);
			/**
			 * TODO uncomment captcha verification
			 */
			if (user != null && verify) {
				StandardPasswordEncoder encryption = new StandardPasswordEncoder();
				user.setPassword(encryption.encode(password));
				user.setUserDown(0);				
				usrDAO.update(user);
				loginManager.sendEmail(email, "Your password: " + password, "BankSIX temporary password");
				message = "Your password was reset. Check your registered email for temporary password";				
			} else
				message = "User not registered and Captch not verified";
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ModelAndView("ForgotPassword", "message", message);
	}

	private boolean isValid(String fieldName, int lowerSizeLimit, int upperSizeLimit, boolean spaceFlag) {
		if (fieldName == null)
			return false;
		if (!spaceFlag && fieldName.indexOf(" ") != -1)
			return false;
		if (fieldName.length() < lowerSizeLimit || fieldName.length() > upperSizeLimit)
			return false;

		return true;
	}
	
	public boolean isNumericInteger(String fieldName) {
		if (fieldName == null)
			return false;
		try {
			Integer.parseInt(fieldName);
		} catch (NumberFormatException ex) {
			/**
			 * Ignore exception
			 */
			return false;
		}
		return true;
	}
}
