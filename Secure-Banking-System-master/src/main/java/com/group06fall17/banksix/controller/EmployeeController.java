/**
 * @author Shubham, Abhilash
 *
 */
package com.group06fall17.banksix.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.group06fall17.banksix.dao.InternalUserDAO;
import com.group06fall17.banksix.dao.PIIDAO;
import com.group06fall17.banksix.dao.TransactionDAO;
import com.group06fall17.banksix.dao.UserDAO;
import com.group06fall17.banksix.exception.AuthorizationException;
import com.group06fall17.banksix.exception.IllegalTransactionException;
import com.group06fall17.banksix.model.ExternalUser;
import com.group06fall17.banksix.model.InternalUser;
import com.group06fall17.banksix.model.Logs;
import com.group06fall17.banksix.model.PII;
import com.group06fall17.banksix.model.Task;
import com.group06fall17.banksix.model.Transaction;
import com.group06fall17.banksix.model.User;
import com.group06fall17.banksix.service.RegularEmployeeService;
import com.group06fall17.banksix.service.SysAdminService;
import com.group06fall17.banksix.service.SysMngrService;
import static com.group06fall17.banksix.constants.Constants.EMAIL_PATTERN;

@Controller
@Scope("session")
public class EmployeeController {
	@Autowired
	InternalUserDAO intUsrDao;

	@Autowired
	TransactionDAO transacDao;

	@Autowired
	PIIDAO piiDao;

	@Autowired
	UserDAO usrDAO;

	@Autowired
	RegularEmployeeService regularEmployeeService;

	@Autowired
	SysMngrService systemManagerService;

	@Autowired
	SysAdminService systemAdministratorService;

	/*private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";*/

	private Pattern email_pattern = Pattern.compile(EMAIL_PATTERN);

	@RequestMapping(value = "/employee", method = RequestMethod.GET)
	public ModelAndView getEmployeeView(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;

		List<Task> tasks = null;
		
		//added if to replace switch : start

		if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2")) {
			modelView = new ModelAndView("RegularEmployee");
			regularEmployeeService.setUsr(username);
			regularEmployeeService.upgradeTasks();
			tasks = regularEmployeeService.obtainTasks();
			modelView.addObject("taskList", tasks);
		}
		
		
		if (user.getAccessprivilege().equals("SM")) {
			modelView = new ModelAndView("SystemManager");
			systemManagerService.setUsr(username);
			systemManagerService.upgradeTasks();
			tasks = systemManagerService.obtainTasks();
			modelView.addObject("taskList", tasks);	
		}
		
		
		if (user.getAccessprivilege().equals("SA")) {
			modelView = new ModelAndView("SystemAdmin");
			systemAdministratorService.setUsr(username);
			systemAdministratorService.upgradeTasks();
			tasks = systemAdministratorService.obtainTasks();
			modelView.addObject("taskList", tasks);	
		}
		
		
		//end
		
//		switch (user.getAccessprivilege()) {
//		case "RE1":
//		case "RE2":
//			modelView = new ModelAndView("RegularEmployee");
//
//			regularEmployeeService.setUsr(username);
//
//			regularEmployeeService.upgradeTasks();
//
//			tasks = regularEmployeeService.obtainTasks();
//
//			modelView.addObject("taskList", tasks);
//			break;
//
//		case "SM":
//			modelView = new ModelAndView("SystemManager");
//
//			systemManagerService.setUsr(username);
//
//			systemManagerService.upgradeTasks();
//
//			tasks = systemManagerService.obtainTasks();
//
//			modelView.addObject("taskList", tasks);
//			break;
//
//		case "SA":
//			modelView = new ModelAndView("SystemAdmin");
//
//			systemAdministratorService.setUsr(username);
//
//			systemAdministratorService.upgradeTasks();
//
//			tasks = systemAdministratorService.obtainTasks();
//
//			modelView.addObject("taskList", tasks);
//			break;
//
//		default:
//			break;
//		}

		return modelView;

	}

	@RequestMapping(value = "/employee", method = RequestMethod.POST)
	public ModelAndView postEmployeeView(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		String taskid_str = request.getParameter("taskselected");

		if (taskid_str.equals("")) {
			return new ModelAndView("redirect:/employee");
		}

		int task_id = Integer.valueOf(taskid_str);

		ModelAndView modelView = null;

		List<Task> tasks = null;

		//added if to replace switch : start

		if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2")) {
				modelView = new ModelAndView("RegularEmployee");
				regularEmployeeService.setUsr(username);
				regularEmployeeService.finishTask(task_id);
				regularEmployeeService.upgradeTasks();
				tasks = regularEmployeeService.obtainTasks();
				modelView.addObject("taskList", tasks);
		}
		
		if (user.getAccessprivilege().equals("SM")) {
			modelView = new ModelAndView("SystemManager");
			systemManagerService.setUsr(username);
			systemManagerService.finishTask(task_id);
			systemManagerService.upgradeTasks();
			tasks = systemManagerService.obtainTasks();
			modelView.addObject("taskList", tasks);
		}
		
		if (user.getAccessprivilege().equals("SA")) {
			modelView = new ModelAndView("SystemAdmin");
			systemAdministratorService.setUsr(username);
			systemAdministratorService.finishTask(task_id);
			systemAdministratorService.upgradeTasks();
			tasks = systemAdministratorService.obtainTasks();
			modelView.addObject("taskList", tasks);
		}
				
		// end
				
//		switch (user.getAccessprivilege()) {
//		case "RE1":
//		case "RE2":
//			modelView = new ModelAndView("RegularEmployee");
//
//			regularEmployeeService.setUsr(username);
//
//			regularEmployeeService.finishTask(task_id);
//
//			regularEmployeeService.upgradeTasks();
//
//			tasks = regularEmployeeService.obtainTasks();
//
//			modelView.addObject("taskList", tasks);
//			break;
//
//		case "SM":
//			modelView = new ModelAndView("SystemManager");
//
//			systemManagerService.setUsr(username);
//
//			systemManagerService.finishTask(task_id);
//
//			systemManagerService.upgradeTasks();
//
//			tasks = systemManagerService.obtainTasks();
//
//			modelView.addObject("taskList", tasks);
//			break;
//
//		case "SA":
//			modelView = new ModelAndView("SystemAdmin");
//
//			systemAdministratorService.setUsr(username);
//
//			systemAdministratorService.finishTask(task_id);
//
//			systemAdministratorService.upgradeTasks();
//
//			tasks = systemAdministratorService.obtainTasks();
//
//			modelView.addObject("taskList", tasks);
//			break;
//
//		default:
//			break;
//		}

		return modelView;
	}

	@RequestMapping(value = "/employee/transactionlookup/authorize", method = RequestMethod.POST)
	public ModelAndView postTransactionwithRequestParameter(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		if (request.getParameter("Tid_").equals("")) {
			return new ModelAndView("TransactionLookup");
		}

		int transid = Integer.valueOf(request.getParameter("Tid_"));

		ModelAndView modelView = null;

		Transaction transaction = null;

		switch (user.getAccessprivilege()) {
		case "SM":

			systemManagerService.setUsr(username);

			transaction = transacDao.findTransactionById(transid);

			try {

				systemManagerService.approveTransac(transaction);

			} catch (IllegalTransactionException | AuthorizationException e) {
				e.printStackTrace();
			}

			modelView = new ModelAndView("TransactionLookup");
			modelView.addObject("transaction", transaction);
			break;
			
		case "RE1":
		case "RE2":

			regularEmployeeService.setUsr(username);

			transaction = transacDao.findTransactionById(transid);

			try {

				regularEmployeeService.approveTransac(transaction);

			} catch (IllegalTransactionException | AuthorizationException e) {
				e.printStackTrace();
			}

			modelView = new ModelAndView("TransactionLookup");
			modelView.addObject("transaction", transaction);
			break;

		case "SA":
		default:
			modelView = new ModelAndView("redirect:/employee");
			break;
		}

		return modelView;

	}

	@RequestMapping(value = "/employee/transactionlookup/cancel", method = RequestMethod.POST)
	public ModelAndView getTransactionwithRequestParameter(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		if (request.getParameter("Tid_").equals("")) {
			return new ModelAndView("TransactionLookup");
		}

		int transid = Integer.valueOf(request.getParameter("Tid_"));

		Transaction transaction = null;
		ModelAndView modelView = null;

		switch (user.getAccessprivilege()) {
		case "RE1":
		case "RE2":

			regularEmployeeService.setUsr(username);

			transaction = transacDao.findTransactionById(transid);

			try {

				regularEmployeeService.dropTransac(transaction);

			} catch (IllegalTransactionException | AuthorizationException e) {
				e.printStackTrace();
			}

			modelView = new ModelAndView("TransactionLookup");
			modelView.addObject("transaction", transaction);
			break;

		case "SM":
		case "SA":
		default:
			modelView = new ModelAndView("redirect:/employee");
			break;
		}

		return modelView;

	}

	@RequestMapping(value = "/employee/transactionlookup/modify", method = RequestMethod.POST)
	public ModelAndView modifyTransactionwithRequestParameter(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		if (request.getParameter("Tid_").equals("")) {
			return new ModelAndView("TransactionLookup");
		}

		int transid = Integer.valueOf(request.getParameter("Tid_"));

		ModelAndView modelView = null;

		Transaction transaction = null;

		switch (user.getAccessprivilege()) {
		case "RE1":
		case "RE2":

			regularEmployeeService.setUsr(username);

			transaction = transacDao.findTransactionById(transid);

			try {

				if (request.getParameter("Amount_") == null || transaction == null) {

				}

				float amount = Float.valueOf(request.getParameter("Amount_"));
				transaction.setAmount(amount);
				regularEmployeeService.upgradeTransac(transaction);

			} catch (AuthorizationException e) {
				e.printStackTrace();
			}

			modelView = new ModelAndView("TransactionLookup");
			modelView.addObject("transaction", transaction);
			break;

		case "SM":
		case "SA":
		default:
			modelView = new ModelAndView("redirect:/employee");
			break;
		}

		return modelView;

	}

	@RequestMapping(value = "/employee/transactionlookup", method = RequestMethod.GET)
	public ModelAndView getTransactionwithRequestParameter(HttpServletRequest request, @RequestParam("transid") int transid) {
			
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;

		Transaction transaction = null;

		switch (user.getAccessprivilege()) {
		case "RE1":
		case "RE2":
			regularEmployeeService.setUsr(username);

			transaction = transacDao.findTransactionById(transid);

			modelView = new ModelAndView("TransactionLookup");
			modelView.addObject("transaction", transaction);
			break;

		case "SM":
			systemManagerService.setUsr(username);

			transaction = transacDao.findTransactionById(transid);

			modelView = new ModelAndView("TransactionLookup");
			modelView.addObject("transaction", transaction);
			break;

		case "SA":
		default:
			modelView = new ModelAndView("redirect:/employee");
			break;
		}

		return modelView;

	}

	@RequestMapping(value = "/employee/transactionlookup", method = RequestMethod.POST)
	public ModelAndView getTransactionLookup(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;

		switch (user.getAccessprivilege()) {
		case "RE1":

		case "RE2":

		case "SM":
			modelView = new ModelAndView("TransactionLookup");

			break;

		default:
			modelView = new ModelAndView("redirect:/employee");
			break;
		}

		return modelView;

	}

	@RequestMapping(value = "/employee/transactioninquiry", method = RequestMethod.POST)
	public ModelAndView getTransactionInquiry(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;

		switch (user.getAccessprivilege()) {
		case "RE1":
		case "RE2":
		case "SM":
			modelView = new ModelAndView("TransactionInquiry");
			break;

		default:
			modelView = new ModelAndView("redirect:/employee");
			break;
		}

		return modelView;
	}

	@RequestMapping(value = "/employee/transactioninquiry", method = RequestMethod.GET)
	public ModelAndView getTransactionswithRequestParameter(HttpServletRequest request,
			@RequestParam("account") String account) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;

		List<Transaction> transactionList = null;

		switch (user.getAccessprivilege()) {
		case "RE1":
		case "RE2":
			regularEmployeeService.setUsr(username);

			transactionList = transacDao.findTransactionsOfAccount(account);

			if (transactionList == null) {
				return null;
			}

			modelView = new ModelAndView("TransactionInquiry");
			modelView.addObject("transactionList", transactionList);
			break;

		case "SM":
			systemManagerService.setUsr(username);

			transactionList = transacDao.findTransactionsOfAccount(account);

			if (transactionList == null) {
				return null;
			}

			modelView = new ModelAndView("TransactionInquiry");
			modelView.addObject("transactionList", transactionList);
			break;

		case "SA":
		default:
			modelView = new ModelAndView("redirect:/employee");
			break;
		}

		return modelView;

	}

	@RequestMapping(value = "/employee/editinfo", method = RequestMethod.POST)
	public ModelAndView getEditInfo(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;

		switch (user.getAccessprivilege()) {
		case "RE1":
		case "RE2":
			modelView = new ModelAndView("EditInfo");

			regularEmployeeService.setUsr(username);

			modelView.addObject("user", user);
			break;

		case "SM":
			modelView = new ModelAndView("EditInfo");

			systemManagerService.setUsr(username);

			modelView.addObject("user", user);
			break;

		case "SA":
			modelView = new ModelAndView("EditInfo");

			systemAdministratorService.setUsr(username);

			modelView.addObject("user", user);
			break;

		default:
			break;
		}

		return modelView;

	}

	@RequestMapping(value = "/employee/editinfo/save", method = RequestMethod.POST)
	public ModelAndView modifyEditInfo(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);
		User users = usrDAO.findUsersByEmail(username);

		String firstName = request.getParameter("FName").toString();
		String middleName = request.getParameter("MName").toString();
		String lastName = request.getParameter("LName").toString();
		String password = request.getParameter("Pass").toString();
		String repassword = request.getParameter("RPass").toString();
		String address = request.getParameter("Address1").toString();
		String addressLine2 = request.getParameter("Address2").toString();
		String city = request.getParameter("City").toString();
		String state = request.getParameter("State").toString();
		String zipcode = request.getParameter("Zipcode").toString();
		String ssn = request.getParameter("SSN").toString();

		StringBuilder errors = new StringBuilder();
		if (!validateField(firstName, 1, 30, false)) {
			errors.append("<li>First Name must not be empty, be between 1-30 characters and not have spaces or special characters</li>");
		}
		if (!validateField(middleName, 0, 30, true)) {
			errors.append("<li>Middle Name must not more than 30 characters</li>");
		}
		if (!validateField(lastName, 1, 30, false)) {
			errors.append("<li>Last Name must not be empty, be between 1-30 characters and not have spaces or special characters</li>");
		}

		// password validations
		if (!password.equals("")) {
			if (!validateFieldSpecialCharactersAllowed(password, 1, 30, false)) {
				errors.append("<li>Password must not be empty, be between 1-30 characters and not have spaces</li>");
			} else {
				if (!password.equals(repassword))
					errors.append("<li>Password and Re-entered password are not the same</li>");
			}
		}
		
		if (!validateField(address, 1, 30, true)) {
			errors.append("<li>Address Line 1 must not be empty, be between 1-30 characters and not have special characters</li>");
		}
		if (!validateField(addressLine2, 1, 30, true)) {
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
		}
		if (!validateField(ssn, 9, 9, false)) {
			errors.append("<li>SSN must not be empty, be 9 characters long and not have spaces or special characters</li>");
		}
		

		if (errors.length() != 0) { // return back with errors and previously
									// inputed values
			Map<String, Object> fieldMap = new HashMap<String, Object>();
			/*fieldMap.put("firstName", firstName);
			fieldMap.put("lastName", lastName);
			fieldMap.put("middleName", middleName);
			fieldMap.put("password", password);
			fieldMap.put("addressLine1", addressLine1);
			fieldMap.put("addressLine2", addressLine2);
			fieldMap.put("city", city);
			fieldMap.put("state", state);
			fieldMap.put("zipcode", zipcode);
			fieldMap.put("ssn", ssn);*/
			
			fieldMap.put("user", user);

			errors.insert(0, "Please fix the following input errors: <br /><ol>");
			errors.append("</ol>");
			fieldMap.put("errors", errors.toString());
			return new ModelAndView("EditInfo", fieldMap);
		}

		// passed validation, register user
		InternalUser internal = new InternalUser();
		internal.setUsrid(user.getUsrid());
		internal.setName(firstName);
		/*if (middleName != null)
			internal.setMiddlename(middleName);

		internal.setLastname(lastName);
*/		internal.setAddress(address);
		/*internal.setAddressline2(addressLine2);
		internal.setCity(city);*/
		internal.setSsn(ssn);
		/*internal.setState(state);
		internal.setZipcode(zipcode);*/
		internal.setAccessprivilege(user.getAccessprivilege());

		StandardPasswordEncoder encryption = new StandardPasswordEncoder();

		if (!request.getParameter("Pass").toString().equals(""))
			users.setPassword(encryption.encode(request.getParameter("Pass").toString()));

		internal.setEmail(users);

		ModelAndView modelView = null;

		switch (user.getAccessprivilege()) {
		case "RE1":
		case "RE2":
			modelView = new ModelAndView("EditInfo");

			regularEmployeeService.setUsr(username);

			regularEmployeeService.upgradeInfo(internal);

			if (!request.getParameter("Pass").toString().equals(""))
				regularEmployeeService.upgradePasswd(users);

			modelView.addObject("user", internal);
			break;

		case "SM":
			modelView = new ModelAndView("EditInfo");

			systemManagerService.setUsr(username);

			systemManagerService.upgradeInfo(internal);

			if (!request.getParameter("Pass").toString().equals(""))
				systemManagerService.upgradePasswd(users);

			modelView.addObject("user", internal);
			break;

		case "SA":
			modelView = new ModelAndView("EditInfo");

			systemAdministratorService.setUsr(username);

			systemAdministratorService.upgradeInfo(internal);

			if (!request.getParameter("Pass").toString().equals(""))
				systemAdministratorService.upgradePasswd(users);

			modelView.addObject("user", internal);
			break;

		default:
			break;
		}

		return modelView;

	}

	/*private boolean validateField(String field, int minSize, int maxSize, boolean spacesAllowed) {
		if (field == null)
			return false;
		if (!spacesAllowed && field.indexOf(" ") != -1)
			return false;
		if (field.length() < minSize || field.length() > maxSize)
			return false;

		return true;
	}*/

	@RequestMapping(value = "/employee/logs", method = RequestMethod.POST)
	public ModelAndView getLogs(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;

		switch (user.getAccessprivilege()) {
		case "RE1":
		case "RE2":
		case "SM":
			modelView = new ModelAndView("redirect:/employee");
			break;

		case "SA":
			modelView = new ModelAndView("logs");

			systemAdministratorService.setUsr(username);

			List<Logs> logsList = systemAdministratorService.chkSysLogs();

			modelView.addObject("logsList", logsList);
			break;

		default:
			break;
		}

		return modelView;

	}

	@RequestMapping(value = "/employee/internaluserlookup", method = RequestMethod.GET)
	public ModelAndView getIUserwithRequestParameter(@RequestParam("email") String email, HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;

		StringBuilder errors = new StringBuilder();

		// email validations
		if (!validateFieldSpecialCharactersAllowed(email, 1, 30, false)) {
			errors.append("<li>Email Id must not be empty, be between 1-30 characters and not have spaces</li>");
		}
		Matcher matcher = email_pattern.matcher(email);
		if (!matcher.matches()) {
			errors.append("<li>Email Id must be a proper email address</li>");
		}
		
		/*// email validations
		if (!validateField(email, 1, 30, false)) {
			errors.append("<li>Email Id must not be empty, be between 1-30 characters and not have spaces</li>");
		}

		Matcher matcher = email_pattern.matcher(email);
		if (!matcher.matches()) {
			errors.append("<li>Email Id must be a proper email address</li>");
		}*/

		if (errors.length() > 0) {
			modelView = new ModelAndView("InternalUsersLookUp");

			modelView.addObject("errors", errors);

			return modelView;
		}

		switch (user.getAccessprivilege()) {
		case "RE1":
		case "RE2":
		case "SM":
			modelView = new ModelAndView("redirect:/employee");
			break;
		case "SA":
			InternalUser user1 = intUsrDao.searchUsrByEmail(email);
			modelView = new ModelAndView("InternalUsersLookUp");
			modelView.addObject("user1", user1);
			modelView.addObject("email", email);

			break;
		default:
			break;
		}

		return modelView;

	}

	@RequestMapping(value = "/employee/pii", method = RequestMethod.GET)
	public ModelAndView getPIIwithRequestParameter(@RequestParam("ssn") String ssn, HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;

		switch (user.getAccessprivilege()) {
		case "RE1":
		case "RE2":
		case "SM":
			modelView = new ModelAndView("redirect:/employee");
			break;

		case "SA":
			PII pii = piiDao.findBySSN(ssn);
			modelView = new ModelAndView("PII");

			if (pii != null) {
				modelView.addObject("ssn", pii.getSsn());
				modelView.addObject("stateID", pii.getStateID());
			} else {
				modelView.addObject("message", "No status found!");
			}
			break;

		default:
			modelView = new ModelAndView("redirect:/employee");
			break;
		}

		return modelView;

	}

	@RequestMapping(value = "/employee/pii", method = RequestMethod.POST)
	public ModelAndView getPIILookup(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;

		switch (user.getAccessprivilege()) {
		case "RE1":
		case "RE2":
		case "SM":
			modelView = new ModelAndView("redirect:/employee");
			break;

		case "SA":
			if (user.getPiiaccess() == 1) {
				modelView = new ModelAndView("PII");
			} else {
				modelView = new ModelAndView("redirect:/employee");
			}
			break;

		default:
			modelView = new ModelAndView("redirect:/employee");
			break;
		}

		return modelView;
	}

	@RequestMapping(value = "/employee/internaluserlookup", method = RequestMethod.POST)
	public ModelAndView getIUserLookup(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;

		switch (user.getAccessprivilege()) {
		case "RE1":

		case "RE2":

		case "SM":
			modelView = new ModelAndView("redirect:/employee");
			break;

		case "SA":
			modelView = new ModelAndView("InternalUsersLookUp");
			break;

		default:
			break;
		}
		
		return modelView;
	}

	@RequestMapping(value = "/employee/internaluserlookup/save", method = RequestMethod.POST)
	public ModelAndView postIUserLookup(HttpServletRequest request) {
		
		HttpSession session = request.getSession(true);
		ModelAndView modelView = null;
		
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		int usrid = Integer.valueOf(request.getParameter("Userid").toString());
		String firstName = request.getParameter("FName").toString();
		String middleName = request.getParameter("MName").toString();
		String lastName = request.getParameter("LName").toString();
		String address = request.getParameter("Address1").toString();
		String addressLine2 = request.getParameter("Address2").toString();
		String city = request.getParameter("City").toString();
		String state = request.getParameter("State").toString();
		String zipcode = request.getParameter("Zipcode").toString();
		String email = request.getParameter("email_hidden").toString();
		String accessprivilege = request.getParameter("AP");
		String ssn = request.getParameter("SSN").toString();
		
		StringBuilder errors = new StringBuilder();
		
		if (!validateField(firstName, 1, 30, false)) {
			errors.append("<li>First Name is required, it should be between 1-30 characters without any space</li>");
		}
		if (!validateField(middleName, 0, 30, true)) {
			errors.append("<li>Middle Name is required , it should not be more than 30 characters</li>");
		}
		if (!validateField(lastName, 1, 30, false)) {
			errors.append("<li>Last Name is required, it should be between 1-30 characters without any space</li>");
		}

		if (!validateField(address, 1, 30, true)) {
			errors.append("<li>Address Line 1 is a required field, should be between 1-30 characters</li>");
		}
		if (!validateField(addressLine2, 1, 30, true)) {
			errors.append("<li>Address Line 2 is a required field, should be between 1-30 characters</li>");
		}
		if (!validateField(city, 1, 16, true)) {
			errors.append("<li>City is a required field, should be between 1-16 characters without any spaces</li>");
		}
		if (!validateField(state, 1, 16, false)) {
			errors.append("<li>State is a required field, should be between 1-16 characters without any spaces</li>");
		}
		if (!validateField(zipcode, 1, 5, false)) {
			errors.append("<li>Zipcode is a required field, should be between 1-5 characters without any spaces</li>");
		}
		if (!validateField(ssn, 9, 9, false)) {
			errors.append("<li>SSN is a required field, should be 9 characters long without any spaces</li>");
		}

		if (accessprivilege.equals("SA") || accessprivilege.equals("SM") || accessprivilege.equals("RE1")
				|| accessprivilege.equals("RE2")) {

		} else {
			errors.append("You dont have access privilege");
		}

		if (errors.length() > 0) {
			modelView = new ModelAndView("InternalUsersLookUp");
			modelView.addObject("errors", errors);
			return modelView;
		}

		switch (user.getAccessprivilege()) {
		case "RE1":

		case "RE2":

		case "SM":
			modelView = new ModelAndView("redirect:/employee");
			break;

		case "SA":

			InternalUser user1 = new InternalUser();

			user1.setUsrid(usrid);
			user1.setName(firstName);
			/*user1.setMiddlename(middleName);
			user1.setLastname(lastName);*/
			user1.setAddress(address);
			/*user1.setAddressline2(addressLine2);
			user1.setCity(city);
			user1.setState(state);
			user1.setZipcode(zipcode);*/
			user1.setSsn(ssn);
			user1.setAccessprivilege(accessprivilege);

			User users = usrDAO.findUsersByEmail(email);
			user1.setEmail(users);

			try {
				systemAdministratorService.changeIntUsrAccnt(user1);
			} catch (AuthorizationException ae) {
				ae.printStackTrace();
			}

			modelView = new ModelAndView("redirect:/employee");
			break;

		default:
			break;
		}

		return modelView;

	}

	private boolean validateFieldSpecialCharactersAllowed(String field, int minSize, int maxSize, boolean spacesAllowed) {
		
		if (field == null) {
			return false;
		}
			
		if (!spacesAllowed && field.indexOf(" ") != -1) {
			return false;
		}
			
		if (field.length() > maxSize || field.length() < minSize) {
			return false;
		}
			
		return true;
	}
	
	private boolean validateField(String field, int minSize, int maxSize, boolean spacesAllowed) {
		
		if (field == null) {
			return false;
		}
			
		if (hasSpecialCharactersWithSpace(field) && spacesAllowed)  {
			return false;
		}
			
		if (hasSpecialCharactersNoSpace(field) && !spacesAllowed) {
			return false;
		}
			
		if (field.length() > maxSize || field.length() < minSize) {
			return false;
		}
						
		return true;
	}
	
	private boolean hasSpecialCharactersNoSpace(String field) {
		if (!StringUtils.isAlphanumeric(field)) {
			return true;
		} else {
			return false;
		}		
	}
	
	private boolean hasSpecialCharactersWithSpace(String field) {
		if (!StringUtils.isAlphanumericSpace(field)) {
			return true;
		} else {
			return false;
		}
	}
}
