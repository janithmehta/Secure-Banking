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
import static com.group06fall17.banksix.constants.Constants.EMAIL_REGEX;

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



	private Pattern emailRegex = Pattern.compile(EMAIL_REGEX);

	@RequestMapping(value = "/employee", method = RequestMethod.GET)
	public ModelAndView getEmployeeView(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;

		List<Task> tasks = null;
		
		
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
		
	

		if (user.getAccessprivilege().equals("SM")) {
			systemManagerService.setUsr(username);
			transaction = transacDao.findTransactionById(transid);

			try {

				systemManagerService.approveTransac(transaction);

			} catch (IllegalTransactionException | AuthorizationException e) {
				e.printStackTrace();
			}

			modelView = new ModelAndView("TransactionLookup");
			modelView.addObject("transaction", transaction);

		} else if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2")) {
			
			regularEmployeeService.setUsr(username);
			transaction = transacDao.findTransactionById(transid);

			try {

				regularEmployeeService.approveTransac(transaction);

			} catch (IllegalTransactionException | AuthorizationException e) {
				e.printStackTrace();
			}

			modelView = new ModelAndView("TransactionLookup");
			modelView.addObject("transaction", transaction);
			
		} else if (user.getAccessprivilege().equals("SA")) {
			
			modelView = new ModelAndView("redirect:/employee");

		} else {
			
			modelView = new ModelAndView("redirect:/employee");
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


		if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2")) {
			regularEmployeeService.setUsr(username);

			transaction = transacDao.findTransactionById(transid);

			try {

				regularEmployeeService.dropTransac(transaction);

			} catch (IllegalTransactionException | AuthorizationException e) {
				e.printStackTrace();
			}

			modelView = new ModelAndView("TransactionLookup");
			modelView.addObject("transaction", transaction);

		} else if (user.getAccessprivilege().equals("SM") || user.getAccessprivilege().equals("SA")) {
			modelView = new ModelAndView("redirect:/employee");
		} else {
			modelView = new ModelAndView("redirect:/employee");
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

		
		if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2")) {
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
			
		} else if (user.getAccessprivilege().equals("SM") || user.getAccessprivilege().equals("SA")) {
			modelView = new ModelAndView("redirect:/employee");
		} else {
			modelView = new ModelAndView("redirect:/employee");
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

		
		if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2")) {
			regularEmployeeService.setUsr(username);

			transaction = transacDao.findTransactionById(transid);

			modelView = new ModelAndView("TransactionLookup");
			modelView.addObject("transaction", transaction);
			
		} else if (user.getAccessprivilege().equals("SM")) {
			
			systemManagerService.setUsr(username);
			transaction = transacDao.findTransactionById(transid);
			modelView = new ModelAndView("TransactionLookup");
			modelView.addObject("transaction", transaction);
			
		} else if (user.getAccessprivilege().equals("SA")) {
			
			modelView = new ModelAndView("redirect:/employee");
			
		} else {
			modelView = new ModelAndView("redirect:/employee");
		}
				
		return modelView;

	}

	@RequestMapping(value = "/employee/transactionlookup", method = RequestMethod.POST)
	public ModelAndView getTransactionLookup(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;

		
		if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2") || user.getAccessprivilege().equals("SM")) {
			
			modelView = new ModelAndView("TransactionLookup");
			
		} else {
			
			modelView = new ModelAndView("redirect:/employee");
		}
				
		
		return modelView;

	}

	@RequestMapping(value = "/employee/transactioninquiry", method = RequestMethod.POST)
	public ModelAndView getTransactionInquiry(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;
		
		
		if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2") || user.getAccessprivilege().equals("SM")) {
			modelView = new ModelAndView("TransactionInquiry");
		} else {
			modelView = new ModelAndView("redirect:/employee");
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
		
		
		if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2")) {
			
			regularEmployeeService.setUsr(username);
			transactionList = transacDao.findTransactionsOfAccount(account);

			if (transactionList == null) {
				return null;
			}

			modelView = new ModelAndView("TransactionInquiry");
			modelView.addObject("transactionList", transactionList);
			
		} else if (user.getAccessprivilege().equals("SM")) {
			
			systemManagerService.setUsr(username);
			transactionList = transacDao.findTransactionsOfAccount(account);

			if (transactionList == null) {
				return null;
			}

			modelView = new ModelAndView("TransactionInquiry");
			modelView.addObject("transactionList", transactionList);
			
		} else if (user.getAccessprivilege().equals("SA")) {
			modelView = new ModelAndView("redirect:/employee");
		
		} else {
			modelView = new ModelAndView("redirect:/employee");
		}
				
		

		return modelView;

	}

	@RequestMapping(value = "/employee/editinfo", method = RequestMethod.POST)
	public ModelAndView getEditInfo(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;

				
		if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2")) {
			
			modelView = new ModelAndView("EditInfo");
			regularEmployeeService.setUsr(username);
			modelView.addObject("user", user);
			
		} 
		
		if (user.getAccessprivilege().equals("SM")) {
			
			modelView = new ModelAndView("EditInfo");
			systemManagerService.setUsr(username);
			modelView.addObject("user", user);
		} 
		
		if (user.getAccessprivilege().equals("SA")) {
			
			modelView = new ModelAndView("EditInfo");
			systemAdministratorService.setUsr(username);
			modelView.addObject("user", user);
		}
				
		

		return modelView;

	}

	@RequestMapping(value = "/employee/editinfo/save", method = RequestMethod.POST)
	public ModelAndView modifyEditInfo(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);
		User users = usrDAO.findUsersByEmail(username);

		String firstName = request.getParameter("name").toString();
		String password = request.getParameter("Pass").toString();
		String repassword = request.getParameter("RPass").toString();
		String address = request.getParameter("address").toString();
		String ssn = request.getParameter("SSN").toString();

		StringBuilder errors = new StringBuilder();
		if (!validateField(firstName, 1, 30, false)) {
			errors.append("<li>First Name must not be empty, be between 1-30 characters and not have spaces or special characters</li>");
		}

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

		if (!validateField(ssn, 9, 9, false)) {
			errors.append("<li>SSN must not be empty, be 9 characters long and not have spaces or special characters</li>");
		}
		

		if (errors.length() != 0) {
			Map<String, Object> fieldMap = new HashMap<String, Object>();
		
			
			fieldMap.put("user", user);

			errors.insert(0, "Please fix the following input errors: <br /><ol>");
			errors.append("</ol>");
			fieldMap.put("errors", errors.toString());
			return new ModelAndView("EditInfo", fieldMap);
		}

		InternalUser internal = new InternalUser();
		internal.setUsrid(user.getUsrid());
		internal.setName(firstName);

		internal.setAddress(address);
	
		internal.setSsn(ssn);
	
		internal.setAccessprivilege(user.getAccessprivilege());

		StandardPasswordEncoder encryption = new StandardPasswordEncoder();

		if (!request.getParameter("Pass").toString().equals(""))
			users.setPassword(encryption.encode(request.getParameter("Pass").toString()));

		internal.setEmail(users);

		ModelAndView modelView = null;
		
		
		if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2")) {
			
			modelView = new ModelAndView("EditInfo");
			regularEmployeeService.setUsr(username);
			regularEmployeeService.upgradeInfo(internal);

			if (!request.getParameter("Pass").toString().equals(""))
				regularEmployeeService.upgradePasswd(users);

			modelView.addObject("user", internal);
		}

		if (user.getAccessprivilege().equals("SM")) {
			
			modelView = new ModelAndView("EditInfo");
			systemManagerService.setUsr(username);
			systemManagerService.upgradeInfo(internal);

			if (!request.getParameter("Pass").toString().equals(""))
				systemManagerService.upgradePasswd(users);

			modelView.addObject("user", internal);
		}	
		
		if (user.getAccessprivilege().equals("SA")) {
			
			modelView = new ModelAndView("EditInfo");
			systemAdministratorService.setUsr(username);
			systemAdministratorService.upgradeInfo(internal);

			if (!request.getParameter("Pass").toString().equals(""))
				systemAdministratorService.upgradePasswd(users);

			modelView.addObject("user", internal);
		}
				

		return modelView;

	}



	@RequestMapping(value = "/employee/logs", method = RequestMethod.POST)
	public ModelAndView getLogs(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;


		
		if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2") || user.getAccessprivilege().equals("SM")) {
			
			modelView = new ModelAndView("redirect:/employee");
		}

		if (user.getAccessprivilege().equals("SA")) {
			
			modelView = new ModelAndView("logs");
			systemAdministratorService.setUsr(username);
			List<Logs> logsList = systemAdministratorService.chkSysLogs();
			modelView.addObject("logsList", logsList);
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
		System.out.println("SAURABH:Inside employee/internaluserlookup : GET 1!");
		// email validations
		if (!validateFieldSpecialCharactersAllowed(email, 1, 50, false)) {
			System.out.println("SAURABH:Inside employee/internaluserlookup : GET 2!");
			errors.append("<li>Email Id must not be empty, be between 1-50 characters and not have spaces</li>");
		}
		Matcher matcher = emailRegex.matcher(email);
		if (!matcher.matches()) {
			System.out.println("SAURABH:Inside employee/internaluserlookup : GET 3!");
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
			System.out.println("SAURABH:Inside employee/internaluserlookup : GET 4!");
			modelView = new ModelAndView("InternalUsersLookUp");

			modelView.addObject("errors", errors);

			return modelView;
		}

		
		if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2") || user.getAccessprivilege().equals("SM")) {
			System.out.println("SAURABH:Inside employee/internaluserlookup : GET 5!");
			modelView = new ModelAndView("redirect:/employee");
		}
		
		if (user.getAccessprivilege().equals("SA")) {
			System.out.println("SAURABH:Inside employee/internaluserlookup : GET 5!");
			InternalUser user1 = intUsrDao.searchUsrByEmail(email);
			modelView = new ModelAndView("InternalUsersLookUp");
			modelView.addObject("user1", user1);
			modelView.addObject("email", email);
		}
		

		return modelView;

	}

	@RequestMapping(value = "/employee/pii", method = RequestMethod.GET)
	public ModelAndView getPIIwithRequestParameter(@RequestParam("ssn") String ssn, HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;
		
		
		if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2") || user.getAccessprivilege().equals("SM")) {
			
			modelView = new ModelAndView("redirect:/employee");
		
		} else if (user.getAccessprivilege().equals("SA")) {
			
			PII pii = piiDao.findBySSN(ssn);
			modelView = new ModelAndView("PII");

			if (pii != null) {
				modelView.addObject("ssn", pii.getSsn());
				modelView.addObject("stateID", pii.getStateID());
			} else {
				modelView.addObject("message", "No status found!");
			}
		} else {
			
			modelView = new ModelAndView("redirect:/employee");
		}
		


		return modelView;

	}

	@RequestMapping(value = "/employee/pii", method = RequestMethod.POST)
	public ModelAndView getPIILookup(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;

		
		if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2") || user.getAccessprivilege().equals("SM")) {
			
			modelView = new ModelAndView("redirect:/employee");
		
		} else if (user.getAccessprivilege().equals("SA")) {
			
			if (user.getPiiaccess() == 1) {
				modelView = new ModelAndView("PII");
			} else {
				modelView = new ModelAndView("redirect:/employee");
			}
		
		} else {
			
			modelView = new ModelAndView("redirect:/employee");
		}
		

		return modelView;
	}

	@RequestMapping(value = "/employee/internaluserlookup", method = RequestMethod.POST)
	public ModelAndView getIUserLookup(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String username = (String) session.getAttribute("BOAUsername");

		InternalUser user = intUsrDao.searchUsrByEmail(username);

		ModelAndView modelView = null;
		
		
		if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2") || user.getAccessprivilege().equals("SM")) {
					
			modelView = new ModelAndView("redirect:/employee");
				
		}
		
		if (user.getAccessprivilege().equals("SA")) {
			
			modelView = new ModelAndView("InternalUsersLookUp");
				
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
		String name = request.getParameter("name").toString();
		String address = request.getParameter("address").toString();

		String email = request.getParameter("email_internalUser").toString();
		String accessprivilege = request.getParameter("AP");
		String ssn = request.getParameter("SSN").toString();
		
		StringBuilder errors = new StringBuilder();
		
		if (!validateField(name, 1, 30, false)) {
			errors.append("<li>First Name is required, it should be between 1-30 characters without any space</li>");
		}


		if (!validateField(address, 1, 30, true)) {
			errors.append("<li>Address Line 1 is a required field, should be between 1-30 characters</li>");
		}

		if (!validateField(ssn, 9, 9, false)) {
			errors.append("<li>SSN is a required field, should be 9 characters long without any spaces</li>");
		}

		if (accessprivilege.equals("SA") || accessprivilege.equals("SM") || accessprivilege.equals("RE1")
				|| accessprivilege.equals("RE2")) {

		} else {
			errors.append("You dont have access privilege");
		}
		System.out.println("SAURABH::accessprivilege:"+accessprivilege);
		System.out.println("SAURABH::email:"+email);
		InternalUser user2 = intUsrDao.searchUsrByEmail(email);
		System.out.println("SAURABH::user2.getAccessprivilege():"+user2.getAccessprivilege());
		if(user.getAccessprivilege().equals("SA") && user2.getAccessprivilege().equals("SA")) {
			errors.append("You can't modify admin account's access privileges");
		}
		if (errors.length() > 0) {
			modelView = new ModelAndView("InternalUsersLookUp");
			modelView.addObject("errors", errors);
			return modelView;
		}

		
		if (user.getAccessprivilege().equals("RE1") || user.getAccessprivilege().equals("RE2") || user.getAccessprivilege().equals("SM")) {
							
			modelView = new ModelAndView("redirect:/employee");
					
		}
		
		if (user.getAccessprivilege().equals("SA") && !user2.getAccessprivilege().equals("SA")) {
			
			InternalUser user1 = new InternalUser();
			
			user1.setUsrid(usrid);
			user1.setName(name);
		
			user1.setAddress(address);
	
			user1.setSsn(ssn);
			user1.setAccessprivilege(accessprivilege);

			User users = usrDAO.findUsersByEmail(email);
			user1.setEmail(users);

			try {
				systemAdministratorService.changeIntUsrAccnt(user1);
			} catch (AuthorizationException ae) {
				ae.printStackTrace();
			}

			modelView = new ModelAndView("redirect:/employee");modelView = new ModelAndView("redirect:/employee");
					
		} else 
			modelView = new ModelAndView("redirect:/employee");
			
				
				

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
