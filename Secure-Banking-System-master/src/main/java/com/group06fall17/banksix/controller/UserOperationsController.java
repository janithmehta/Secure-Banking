package com.group06fall17.banksix.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.group06fall17.banksix.component.UserSessionInfo;
import com.group06fall17.banksix.dao.BankAccountDAO;
import com.group06fall17.banksix.dao.ExternalUserDAO;
import com.group06fall17.banksix.dao.TransactionDAO;
import com.group06fall17.banksix.dao.UserDAO;
import com.group06fall17.banksix.model.BankAccount;
import com.group06fall17.banksix.model.ExternalUser;
import com.group06fall17.banksix.model.Transaction;
import com.group06fall17.banksix.model.User;
import com.group06fall17.banksix.service.TransacMngrService;
import com.group06fall17.banksix.service.UsrFuncService;
import com.group06fall17.banksix.exception.IllegalTransactionException;

@Controller
@Scope("request")
public class UserOperationsController {
	@Autowired
	UserSessionInfo userSession;
	
	@Autowired
	ExternalUserDAO extUsrDao;
	
	@Autowired
	private UserDAO usrDAO;
	
	@Autowired
	BankAccountDAO bankAccntDao;
	
	@Autowired
	TransactionDAO transacDao;
	
	@Autowired
	UsrFuncService userOperationsService;
	
	@Autowired
	TransacMngrService transacMngrService;
			
	@RequestMapping("/customer")
	public ModelAndView ExternalUserDashboard(){
		// HttpSession session= request.getSession(true);
		// UserSessionInfo user = (UserSessionInfo) session.getAttribute("BOAUser");		
		if (!userLoggedIn()) {
			return new ModelAndView("redirect:/login");
		}

		// user is logged in display user dashboard
		ExternalUser extUser = extUsrDao.searchUsrByEmail(userSession.getUsername());
		List<BankAccount> bankAccounts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("firstName", extUser.getName());
//		map.put("lastName", extUser.getLastname());
		map.put("bankAccounts", bankAccounts);
		
		return new ModelAndView("customer", map);		
	}
	
	// Account Selected from Accounts Selection Page:	
	@RequestMapping(value="account", method=RequestMethod.POST)
	public ModelAndView externalUserAccountDashboardPost(HttpServletRequest request){
		if (!userLoggedIn()) {
			return new ModelAndView("redirect:/login");
		}
		
		// user is logged in display account dashboard
		ExternalUser extUser = extUsrDao.searchUsrByEmail(userSession.getUsername());
		List<BankAccount> bankAccounts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("firstName", extUser.getName());
//		map.put("lastName", extUser.getLastname());
		map.put("bankAccounts", bankAccounts);
		
		// no account info requested
		if (request == null || request.getParameter("accountnumber") == null) {
			map.put("message", "No record of account exists!");
			return new ModelAndView("customer", map);	
		}
		
		String accountnumber = request.getParameter("accountnumber").toString();
		BankAccount bankAccount = bankAccntDao.getBankAccountWithAccno(accountnumber);
		
		// account info does not exist, or does not belong to the user
		if (bankAccount == null || bankAccount.getUsrid().getUsrid() != extUser.getUsrid()) {
			map.put("message", "No record of account with account number " + accountnumber + " exists!");
			return new ModelAndView("customer", map);	
		}
		
		// get transactions
		
		map.put("accountnumber", bankAccount.getAccountnumber());
		map.put("accountType", bankAccount.getAccounttype());
		map.put("balance", bankAccount.getBalance());
		map.put("transactions", transacDao.findTransactionsOfAccount(bankAccount));
		
		userSession.setSelectedUsrAccount(bankAccount.getAccountnumber());
		return new ModelAndView("account", map);		
	}
	
	// Account Page refreshed:
	@RequestMapping(value="account", method=RequestMethod.GET)
	public ModelAndView externalUserAccountDashboardGet(HttpServletRequest request){
		if (!userLoggedIn()) {
			return new ModelAndView("redirect:/login");
		}
		
		ExternalUser extUser = extUsrDao.searchUsrByEmail(userSession.getUsername());
		List<BankAccount> bankAccounts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("firstName", extUser.getName());
//		map.put("lastName", extUser.getLastname());
		map.put("bankAccounts", bankAccounts);
		
		// no account selected
		if (userSession.getSelectedUsrAccount() == null || userSession.getSelectedUsrAccount().isEmpty()) {
			map.put("message", "Please Select an account!");
			return new ModelAndView("customer", map);
		}
		
		String accountnumber = userSession.getSelectedUsrAccount();
		BankAccount bankAccount = bankAccntDao.getBankAccountWithAccno(accountnumber);
		
		// account info does not exist, or does not belong to the user
		if (bankAccount == null || bankAccount.getUsrid().getUsrid() != extUser.getUsrid()) {
			map.put("message", "No record of account with account number " + accountnumber + " exists!");
			return new ModelAndView("customer", map);	
		}
		
		// get transactions		
		map.put("accountnumber", bankAccount.getAccountnumber());
		map.put("accountType", bankAccount.getAccounttype());
		map.put("balance", bankAccount.getBalance());
		map.put("transactions", transacDao.findTransactionsOfAccount(bankAccount));
		
		userSession.setSelectedUsrAccount(bankAccount.getAccountnumber());
		return new ModelAndView("account", map);		
	}
	
	// Debit Renderer
	@RequestMapping(value="debit", method=RequestMethod.GET)
	public ModelAndView debitGet(HttpServletRequest request){
		if (!userLoggedIn()) {
			return new ModelAndView("redirect:/login");
		}
		
		ExternalUser extUser = extUsrDao.searchUsrByEmail(userSession.getUsername());
		List<BankAccount> bankAccounts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("firstName", extUser.getName());
//		map.put("lastName", extUser.getLastname());
		map.put("bankAccounts", bankAccounts);
		
		// no account selected
		if (userSession.getSelectedUsrAccount() == null || userSession.getSelectedUsrAccount().isEmpty()) {
			map.put("message", "Please Select an account!");
			return new ModelAndView("customer", map);
		}
		
		String accountnumber = userSession.getSelectedUsrAccount();
		BankAccount bankAccount = bankAccntDao.getBankAccountWithAccno(accountnumber);
		
		// account info does not exist, or does not belong to the user
		if (bankAccount == null || bankAccount.getUsrid().getUsrid() != extUser.getUsrid()) {
			map.put("message", "No record of account with account number " + accountnumber + " exists!");
			return new ModelAndView("customer", map);	
		}
		
		Map<String, Object> debitMap = new HashMap<String, Object>();
		debitMap.put("firstName", extUser.getName());
//		debitMap.put("lastName", extUser.getLastname());
		debitMap.put("displayOperation", "Debit");
		debitMap.put("operation", "debit");
		debitMap.put("accountNo", accountnumber);
		
		
		return new ModelAndView("debitCredit", debitMap);
	}
	
	// Debit Actuator
	@RequestMapping(value="dodebit", method=RequestMethod.POST)
	public ModelAndView doDebitPost(HttpServletRequest request){
		if (!userLoggedIn()) {
			return new ModelAndView("redirect:/login");
		}
		
		ExternalUser extUser = extUsrDao.searchUsrByEmail(userSession.getUsername());
		List<BankAccount> bankAccounts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("firstName", extUser.getName());
//		map.put("lastName", extUser.getLastname());
		map.put("bankAccounts", bankAccounts);
		
		// no account selected
		if (userSession.getSelectedUsrAccount() == null || userSession.getSelectedUsrAccount().isEmpty()) {
			map.put("message", "Please Select an account!");
			return new ModelAndView("customer", map);
		}
		
		String accountnumber = userSession.getSelectedUsrAccount();
		BankAccount bankAccount = bankAccntDao.getBankAccountWithAccno(accountnumber);
		
		// account info does not exist, or does not belong to the user
		if (bankAccount == null || bankAccount.getUsrid().getUsrid() != extUser.getUsrid()) {
			map.put("message", "No record of account with account number " + accountnumber + " exists!");
			return new ModelAndView("customer", map);	
		}
		
		
		
		Map<String, Object> debitMap = new HashMap<String, Object>();
		debitMap.put("firstName", extUser.getName());
//		debitMap.put("lastName", extUser.getLastname());
		debitMap.put("displayOperation", "Debit");
		debitMap.put("operation", "debit");
		debitMap.put("accountNo", accountnumber);
		
		// check if required form parameter values are present, and are valid
		if (request == null) {
			return new ModelAndView("debitCredit", debitMap);
		} 
		String operation_param = request.getParameter("operation").toString();
		String accno_param = request.getParameter("accountnumber").toString();		
		String amt_param = request.getParameter("Amount").toString();
		String desc_param = request.getParameter("Description").toString();
		
		if (!operation_param.equals("debit")) {
			debitMap.put("errors", "Invalid operation");
			return new ModelAndView("debitCredit", debitMap);
		}
		
		if (!accno_param.equals(accountnumber)) {
			debitMap.put("errors", "Account to debit is not valid");
			return new ModelAndView("debitCredit", debitMap);
		}
		
		if (!isNumeric(amt_param) || !(Float.parseFloat(amt_param) > 0)) {
			debitMap.put("errors", "Amount is not valid. Amount should be a valid number greater than 0.");
			return new ModelAndView("debitCredit", debitMap);
		}
		
		if (bankAccount.getBalance() < Float.parseFloat(amt_param)) {
			debitMap.put("errors", "Not sufficient funds to debit account with $" + Float.parseFloat(amt_param));
			return new ModelAndView("debitCredit", debitMap);
		}
		
		if (desc_param.length() > 45) {
			debitMap.put("errors", "Description of Transaction cannot be more than 45 characters.");
			return new ModelAndView("debitCredit", debitMap);
		}
		
		// passed validations do the debit
		Transaction debitTransaction = new Transaction();
		debitTransaction.setAmt(Float.parseFloat(amt_param));
		debitTransaction.setTransDate(new Date());
		debitTransaction.setTransDesc(desc_param);
		debitTransaction.setFromacc(bankAccount);
		debitTransaction.setTransStatus("cleared");
		debitTransaction.setToacc(bankAccount);
		debitTransaction.setTransType("debit");
		transacDao.update(debitTransaction);
		bankAccount.setBalance(bankAccount.getBalance() - Float.parseFloat(amt_param));
		bankAccntDao.update(bankAccount);
				
		// render message and go to accounts page
		map.put("accountnumber", bankAccount.getAccountnumber());
		map.put("accountType", bankAccount.getAccounttype());
		map.put("balance", bankAccount.getBalance());
		map.put("transactions", transacDao.findTransactionsOfAccount(bankAccount));
		map.put("message", "Debit of $" + amt_param + " successful from account " + bankAccount.getAccountnumber());
		
		//return new ModelAndView("account", map);
		return new ModelAndView("redirect:/account");
	}
	
	// Credit Renderer
	@RequestMapping(value="credit", method=RequestMethod.GET)
	public ModelAndView creditGet(HttpServletRequest request){
		if (!userLoggedIn()) {
			return new ModelAndView("redirect:/login");
		}
		
		ExternalUser extUser = extUsrDao.searchUsrByEmail(userSession.getUsername());
		List<BankAccount> bankAccounts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("firstName", extUser.getName());
//		map.put("lastName", extUser.getLastname());
		map.put("bankAccounts", bankAccounts);
		
		// no account selected
		if (userSession.getSelectedUsrAccount() == null || userSession.getSelectedUsrAccount().isEmpty()) {
			map.put("message", "Please Select an account!");
			return new ModelAndView("customer", map);
		}
		
		String accountnumber = userSession.getSelectedUsrAccount();
		BankAccount bankAccount = bankAccntDao.getBankAccountWithAccno(accountnumber);
		
		// account info does not exist, or does not belong to the user
		if (bankAccount == null || bankAccount.getUsrid().getUsrid() != extUser.getUsrid()) {
			map.put("message", "No record of account with account number " + accountnumber + " exists!");
			return new ModelAndView("customer", map);	
		}
		
		Map<String, Object> creditMap = new HashMap<String, Object>();
		creditMap.put("firstName", extUser.getName());
//		creditMap.put("lastName", extUser.getLastname());
		creditMap.put("displayOperation", "Credit");
		creditMap.put("operation", "credit");
		creditMap.put("accountNo", accountnumber);
		
		
		return new ModelAndView("debitCredit", creditMap);
	}
	
	// Credit Actuator
	@RequestMapping(value="docredit", method=RequestMethod.POST)
	public ModelAndView doCreditPost(HttpServletRequest request){
		if (!userLoggedIn()) {
			return new ModelAndView("redirect:/login");
		}
		
		ExternalUser extUser = extUsrDao.searchUsrByEmail(userSession.getUsername());
		List<BankAccount> bankAccounts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("firstName", extUser.getName());
//		map.put("lastName", extUser.getLastname());
		map.put("bankAccounts", bankAccounts);
		
		// no account selected
		if (userSession.getSelectedUsrAccount() == null || userSession.getSelectedUsrAccount().isEmpty()) {
			map.put("message", "Please Select an account!");
			return new ModelAndView("customer", map);
		}
		
		String accountnumber = userSession.getSelectedUsrAccount();
		BankAccount bankAccount = bankAccntDao.getBankAccountWithAccno(accountnumber);
		
		// account info does not exist, or does not belong to the user
		if (bankAccount == null || bankAccount.getUsrid().getUsrid() != extUser.getUsrid()) {
			map.put("message", "No record of account with account number " + accountnumber + " exists!");
			return new ModelAndView("customer", map);	
		}
		
		
		
		Map<String, Object>creditMap = new HashMap<String, Object>();
		creditMap.put("firstName", extUser.getName());
//		creditMap.put("lastName", extUser.getLastname());
		creditMap.put("displayOperation", "Credit");
		creditMap.put("operation", "credit");
		creditMap.put("accountNo", accountnumber);
		
		// check if required form parameter values are present, and are valid
		if (request == null) {
			return new ModelAndView("debitCredit", creditMap);
		} 
		String operation_param = request.getParameter("operation").toString();
		String accno_param = request.getParameter("accountnumber").toString();		
		String amt_param = request.getParameter("Amount").toString();
		String desc_param = request.getParameter("Description").toString();
		
		if (!operation_param.equals("credit")) {
			creditMap.put("errors", "Invalid operation");
			return new ModelAndView("debitCredit", creditMap);
		}
		
		if (!accno_param.equals(accountnumber)) {
			creditMap.put("errors", "Account to credit is not valid");
			return new ModelAndView("debitCredit", creditMap);
		}
		
		if (!isNumeric(amt_param) || !(Float.parseFloat(amt_param) > 0)) {
			creditMap.put("errors", "Amount is not valid. Amount should be a valid number greater than 0.");
			return new ModelAndView("debitCredit", creditMap);
		}
		
		if (desc_param.length() > 45) {
			creditMap.put("errors", "Description of Transaction cannot be more than 45 characters.");
			return new ModelAndView("debitCredit", creditMap);
		}
		
		// passed validations do the credit
		Transaction creditTransaction = new Transaction();
		creditTransaction.setAmt(Float.parseFloat(amt_param));
		creditTransaction.setTransDate(new Date());
		creditTransaction.setTransDesc(desc_param);
		creditTransaction.setFromacc(bankAccount);
		creditTransaction.setTransStatus("cleared");
		creditTransaction.setToacc(bankAccount);
		creditTransaction.setTransType("credit");
		transacDao.update(creditTransaction);
		bankAccount.setBalance(bankAccount.getBalance() + Float.parseFloat(amt_param));
		bankAccntDao.update(bankAccount);
				
		// render message and go to accounts page
		map.put("accountnumber", bankAccount.getAccountnumber());
		map.put("accountType", bankAccount.getAccounttype());
		map.put("balance", bankAccount.getBalance());
		map.put("transactions", transacDao.findTransactionsOfAccount(bankAccount));
		map.put("message", "Credit of $" + amt_param + " successful to account " + bankAccount.getAccountnumber());
		
		//return new ModelAndView("account", map);
		return new ModelAndView("redirect:/account");
	}
	
	// Account Transfer Renderer
	@RequestMapping(value="transfer", method=RequestMethod.GET)
	public ModelAndView transferGet(HttpServletRequest request){
		if (!userLoggedIn()) {
			return new ModelAndView("redirect:/login");
		}
		
		ExternalUser extUser = extUsrDao.searchUsrByEmail(userSession.getUsername());
		List<BankAccount> bankAccounts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("firstName", extUser.getName());
//		map.put("lastName", extUser.getLastname());
		map.put("bankAccounts", bankAccounts);
		
		// no account selected
		if (userSession.getSelectedUsrAccount() == null || userSession.getSelectedUsrAccount().isEmpty()) {
			map.put("message", "Please Select an account!");
			return new ModelAndView("customer", map);
		}
		
		String accountnumber = userSession.getSelectedUsrAccount();
		BankAccount bankAccount = bankAccntDao.getBankAccountWithAccno(accountnumber);
		
		// account info does not exist, or does not belong to the user
		if (bankAccount == null || bankAccount.getUsrid().getUsrid() != extUser.getUsrid()) {
			map.put("message", "No record of account with account number " + accountnumber + " exists!");
			return new ModelAndView("customer", map);	
		}
		
		Map<String, Object> transferMap = new HashMap<String, Object>();
		transferMap.put("firstName", extUser.getName());
//		transferMap.put("lastName", extUser.getLastname());
		transferMap.put("accountNo", accountnumber);
		
		return new ModelAndView("accountTransfer", transferMap);
	}
	
	// Account Transfer Actuator
	@RequestMapping(value="dotransfer", method=RequestMethod.POST)
	public ModelAndView doTransferPost(
			@RequestParam("FromAccount") String from_accno_param,
			@RequestParam("ToAccount") String to_accno_param,
			@RequestParam("Amount") String amt_param,
			@RequestParam("Description") String desc_param,	
			@RequestParam("PrivateKeyFileLoc") MultipartFile privateKeyFile) {	    
		if (!userLoggedIn()) {
			return new ModelAndView("redirect:/login");
		}
		
		ExternalUser extUser = extUsrDao.searchUsrByEmail(userSession.getUsername());
		List<BankAccount> bankAccounts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("firstName", extUser.getName());
//		map.put("lastName", extUser.getLastname());
		map.put("bankAccounts", bankAccounts);
		
		// no account selected
		if (userSession.getSelectedUsrAccount() == null || userSession.getSelectedUsrAccount().isEmpty()) {
			map.put("message", "Please Select an account!");
			return new ModelAndView("customer", map);
		}
		
		String accountnumber = userSession.getSelectedUsrAccount();
		BankAccount fromBankAccount = bankAccntDao.getBankAccountWithAccno(accountnumber);
		
		// account info does not exist, or does not belong to the user
		if (fromBankAccount == null || fromBankAccount.getUsrid().getUsrid() != extUser.getUsrid()) {
			map.put("message", "No record of account with account number " + accountnumber + " exists!");
			return new ModelAndView("customer", map);	
		}
				
		Map<String, Object> transferMap = new HashMap<String, Object>();
		transferMap.put("firstName", extUser.getName());
//		transferMap.put("lastName", extUser.getLastname());		
		transferMap.put("accountNo", accountnumber);
		transferMap.put("description", desc_param);
		transferMap.put("amount", amt_param);
		transferMap.put("toaccount", to_accno_param);
				
		if (!from_accno_param.equals(accountnumber)) {
			transferMap.put("errors", "Account to debit is not valid");
			return new ModelAndView("accountTransfer", transferMap);
		}
		
		// check if ToAccount is a valid account
		BankAccount toBankAccount = bankAccntDao.getBankAccountWithAccno(to_accno_param);
		if (toBankAccount == null) {
			transferMap.put("errors", "Account to credit transfer funds is not valid");
			return new ModelAndView("accountTransfer", transferMap);
		}
		
		if (fromBankAccount.getAccountnumber().equals(toBankAccount.getAccountnumber())) {
			transferMap.put("errors", "From and to accounts cannot be the same account");
			return new ModelAndView("accountTransfer", transferMap);
		}
		
		// if user is transferring to one of his own accounts. Update object reference 
		// to point to unique object reference and avoid the org.hibernate.NonUniqueObjectException exception
		for (BankAccount bank : bankAccounts) {
			if (bank.getAccountnumber().equals(toBankAccount)) {
				toBankAccount = bank;
				break;
			}
		}
			
		
		if (!isNumeric(amt_param) || !(Float.parseFloat(amt_param) > 0)) {
			transferMap.put("errors", "Amount is not valid. Amount should be a valid number greater than 0.");
			return new ModelAndView("accountTransfer", transferMap);
		}
		
		if (fromBankAccount.getBalance() < Float.parseFloat(amt_param)) {
			transferMap.put("errors", "Not sufficient funds to debit account with $" + Float.parseFloat(amt_param));
			return new ModelAndView("accountTransfer", transferMap);
		}
		
		if (desc_param.length() > 45) {
			transferMap.put("errors", "Description of Transaction cannot be more than 45 characters.");
			return new ModelAndView("accountTransfer", transferMap);
		}
		
		// PKI check		
		if (Float.parseFloat(amt_param) > 500) {
			if (privateKeyFile.isEmpty()) {
				transferMap.put("errors", "Private Key must be provided for transactions more than $500");
				return new ModelAndView("accountTransfer", transferMap);
			}			
			else {
				String privateKeyFileLocation = userOperationsService.uploadFileLoc();
				
				// check if file can be uploaded, if yes upload, if no return
				if (!userOperationsService.toUploadFile(privateKeyFileLocation, privateKeyFile)) {
					transferMap.put("errors", "Private Key could not be uploaded. Private Key file must be readable at the given location and be not more than 50 KB");
					return new ModelAndView("accountTransfer", transferMap);
				}
				
				// check if private key is valid 
				if (!userOperationsService.diffKeys(extUser, privateKeyFileLocation)) {		
					// not valid
					map.put("accountnumber", fromBankAccount.getAccountnumber());
					map.put("accountType", fromBankAccount.getAccounttype());
					map.put("balance", fromBankAccount.getBalance());
					map.put("transactions", transacDao.findTransactionsOfAccount(fromBankAccount));
					map.put("message", "<font color=\"red\">Private key authentication failed!</font>. Your fund transfer request cannot be processed.");
					return new ModelAndView("account", map);		
				}
			}
		}
		
		// passed validations do the fund transfer
		Transaction transferTransaction = new Transaction();
		transferTransaction.setAmt(Float.parseFloat(amt_param));
		transferTransaction.setTransDate(new Date());
		transferTransaction.setFromacc(fromBankAccount);		
		transferTransaction.setToacc(toBankAccount);
		transferTransaction.setTransType("transfer");
		
		// Added by Abhilash
		if(fromBankAccount.getUsrid().getUsrid() != toBankAccount.getUsrid().getUsrid())
			transferTransaction.setTransDesc("external");
		else
			transferTransaction.setTransDesc("internal");
			
		if (Float.parseFloat(amt_param) > 500) {
			transferTransaction.setTransStatus("processing");			
			try {
				transacMngrService.submitTransac(transferTransaction);
				map.put("message", "Private Key authentication is sucssessful. Debit of $" + amt_param + " scheduled from account " + fromBankAccount.getAccountnumber() + " to account " + toBankAccount.getAccountnumber());
			} catch (IllegalTransactionException e) {				
				map.put("message", "Private Key authentication is sucssessful but the fund transfer request could not be processed.");
			}
		} 
		else {
			// amount less than $500
			transferTransaction.setTransStatus("cleared");
			transacDao.update(transferTransaction);
			fromBankAccount.setBalance(fromBankAccount.getBalance() - Float.parseFloat(amt_param));
			toBankAccount.setBalance(toBankAccount.getBalance() + Float.parseFloat(amt_param));
			bankAccntDao.update(fromBankAccount);
			bankAccntDao.update(toBankAccount);
			map.put("message", "Debit of $" + amt_param + " successful from account " + fromBankAccount.getAccountnumber() + " to account " + toBankAccount.getAccountnumber());
		}
				
				
		// render message and go to accounts page
		map.put("accountnumber", fromBankAccount.getAccountnumber());
		map.put("accountType", fromBankAccount.getAccounttype());
		map.put("balance", fromBankAccount.getBalance());
		map.put("transactions", transacDao.findTransactionsOfAccount(fromBankAccount));
				
		//return new ModelAndView("account", map);
		
		return new ModelAndView("redirect:/account");
	}
	
	// HELPER METHODS
	
	public boolean userLoggedIn() {
		if (userSession == null || userSession.getUserActive() != 1)
			return false;		
		else
			return true;
	}
	
	public boolean isNumeric(String str) {
		if (str == null)
			return false;
		try {
			double d = Float.parseFloat(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public void setUserSession(UserSessionInfo userSession) {
		this.userSession = userSession;
	}

	public void setUsr(ExternalUserDAO user) {
		this.extUsrDao = user;
	}

	public void setBankAccountService(BankAccountDAO bankAccountService) {
		this.bankAccntDao = bankAccountService;
	}

	public void setTransactionService(TransactionDAO transactionService) {
		this.transacDao = transactionService;
	}



	@RequestMapping("/downloadpage")
	public ModelAndView downloadPage(){
		if (!userLoggedIn()) {
			return new ModelAndView("redirect:/login");
		}
		
		ExternalUser user = extUsrDao.searchUsrByEmail(userSession.getUsername());
		List<BankAccount> bankAccounts = bankAccntDao.findAccountsOfUser(user.getUsrid());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("firstName", user.getName());
//		map.put("lastName", user.getLastname());
		map.put("bankAccounts", bankAccounts);
		
		// no account selected
		if (userSession.getSelectedUsrAccount() == null || userSession.getSelectedUsrAccount().isEmpty()) {
			map.put("message", "Please Select an account!");
			return new ModelAndView("customer", map);
		}
		
		BankAccount account=bankAccntDao.getBankAccountWithAccno(userSession.getSelectedUsrAccount());
		
		if (account == null || account.getUsrid().getUsrid() != user.getUsrid()) {
			map.put("message", "No record of account with account number " + userSession.getSelectedUsrAccount() + " exists!");
			return new ModelAndView("customer", map);	
		}
		
		//ExternalUser user = extUsrDao.searchUsrByEmail(userSession.getUsername());
		//map.put("firstName",user.getName());
		//map.put("lastName",user.getLastname());
		map.put("accountnumber",account.getAccountnumber());
		map.put("accountType", account.getAccounttype());
		map.put("balance",account.getBalance());
		map.put("acctcreatedate",account.getAcctcreatedate());
		map.put("usrid",account.getUsrid().getUsrid());
		map.put("accountstatus",account.getAccountstatus());
	    return new ModelAndView("downloadpage",map);
	}
	
	
	@RequestMapping("/download")
	public ModelAndView downloadStatement(HttpServletResponse response) throws IOException{
		if (!userLoggedIn()) {
			return new ModelAndView("redirect:/login");
		}
		
		ExternalUser user = extUsrDao.searchUsrByEmail(userSession.getUsername());
		List<BankAccount> bankAccounts = bankAccntDao.findAccountsOfUser(user.getUsrid());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("firstName", user.getName());
//		map.put("lastName", user.getLastname());
		map.put("bankAccounts", bankAccounts);
		
		// no account selected
		if (userSession.getSelectedUsrAccount() == null || userSession.getSelectedUsrAccount().isEmpty()) {
			map.put("message", "Please Select an account!");
			return new ModelAndView("customer", map);
		}
		
		BankAccount account=bankAccntDao.getBankAccountWithAccno(userSession.getSelectedUsrAccount());
		
		if (account == null || account.getUsrid().getUsrid() != user.getUsrid()) {
			map.put("message", "No record of account with account number " + userSession.getSelectedUsrAccount() + " exists!");
			return new ModelAndView("customer", map);	
		}
		
		
	   	String filename="Statement.csv";
	   	String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                filename);
        response.setHeader(headerKey, headerValue);
	   	List<Transaction> trans=transacDao.findTransactionsOfAccount(account);
	   	System.out.println("Size of trans : "+trans.size());
	   	ICsvBeanWriter csvWriter= new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
	   	String[] header ={"transdate","transdesc","transtype","amt", "transstatus"};	
	   	csvWriter.writeHeader(header);
		for (Transaction t :trans){
			csvWriter.write(t, header);
			System.out.println("size of record : "+t.getTransid());
		}
		csvWriter.close();
		return new ModelAndView("downloadpage");
	}
	
	// Make Payment Renderer
	@RequestMapping(value="/payment",method=RequestMethod.GET)
	public ModelAndView makePayment(HttpServletRequest request){
		if (!userLoggedIn()) {
			return new ModelAndView("redirect:/login");
		}
		
		ExternalUser extUser = extUsrDao.searchUsrByEmail(userSession.getUsername());
		List<BankAccount> bankAccounts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("firstName", extUser.getName());
//		map.put("lastName", extUser.getLastname());
		map.put("bankAccounts", bankAccounts);
		
		// no account selected
		if (userSession.getSelectedUsrAccount() == null || userSession.getSelectedUsrAccount().isEmpty()) {
			map.put("message", "Please Select an account!");
			return new ModelAndView("customer", map);
		}
		
		String accountnumber = userSession.getSelectedUsrAccount();
		BankAccount bankAccount = bankAccntDao.getBankAccountWithAccno(accountnumber);
		
		// account info does not exist, or does not belong to the user
		if (bankAccount == null || bankAccount.getUsrid().getUsrid() != extUser.getUsrid()) {
			map.put("message", "No record of account with account number " + accountnumber + " exists!");
			return new ModelAndView("customer", map);	
		}
		
		Map<String, Object> paymentMap = new HashMap<String, Object>();
		paymentMap.put("firstName", extUser.getName());
//		paymentMap.put("lastName", extUser.getLastname());
		paymentMap.put("accountNo", accountnumber);		
		//ExternalUser externaluser = new ExternalUser();		
		List<ExternalUser> merchants = extUsrDao.findUserByUserType("merchant");
		paymentMap.put("merchants", merchants);
				
		return new ModelAndView("payment", paymentMap);
	}
	
	// Make Payment Actuator 
	@RequestMapping(value="/dopayment",method=RequestMethod.POST)
	public ModelAndView payToOrganization(@RequestParam("PrivateKeyFileLoc") MultipartFile privateKeyFile,HttpServletRequest request){
		if (!userLoggedIn()) {
			return new ModelAndView("redirect:/login");
		}
		
		ExternalUser extUser = extUsrDao.searchUsrByEmail(userSession.getUsername());
		List<BankAccount> bankAccounts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("firstName", extUser.getName());
//		map.put("lastName", extUser.getLastname());
		map.put("bankAccounts", bankAccounts);
		
		// no account selected
		if (userSession.getSelectedUsrAccount() == null || userSession.getSelectedUsrAccount().isEmpty()) {
			map.put("message", "Please Select an account!");
			return new ModelAndView("customer", map);
		}
		
		String accountnumber = userSession.getSelectedUsrAccount();
		BankAccount bankAccount = bankAccntDao.getBankAccountWithAccno(accountnumber);
		
		// account info does not exist, or does not belong to the user
		if (bankAccount == null || bankAccount.getUsrid().getUsrid() != extUser.getUsrid()) {
			map.put("message", "No record of account with account number " + accountnumber + " exists!");
			return new ModelAndView("customer", map);	
		}
		
		Map<String, Object> paymentMap = new HashMap<String, Object>();
		paymentMap.put("firstName", extUser.getName());
//		paymentMap.put("lastName", extUser.getLastname());
		paymentMap.put("accountNo", accountnumber);		
		//ExternalUser externaluser = new ExternalUser();		
		List<ExternalUser> merchants = extUsrDao.findUserByUserType("merchant");
		paymentMap.put("merchants", merchants);
		
		
		// check if required form parameter values are present, and are valid
		if (request == null) {
			return new ModelAndView("payment", paymentMap);
		} 
		String amount=request.getParameter("amount").toString();
		String accno_param = request.getParameter("accountnumber").toString();		
		String description=request.getParameter("description").toString();
		String payto= request.getParameter("organization").toString();
		
		if (!accno_param.equals(accountnumber)) {
			paymentMap.put("errors", "Account to Make Payment from is not valid");
			return new ModelAndView("payment", paymentMap);
		}
		
		if (!isNumeric(amount) || !(Float.parseFloat(amount) > 0)) {
			paymentMap.put("errors", "Amount is not valid. Amount should be a valid number greater than 0.");
			return new ModelAndView("payment", paymentMap);
		}
		
		if (bankAccount.getBalance() < Float.parseFloat(amount)) {
			paymentMap.put("errors", "Not sufficient funds to make payment of $" + Float.parseFloat(amount));
			return new ModelAndView("payment", paymentMap);
		}
		
		if (description.length() > 45) {
			paymentMap.put("errors", "Description of Transaction cannot be more than 45 characters.");
			return new ModelAndView("payment", paymentMap);
		}
		
		ExternalUser business=extUsrDao.findUserByBname(payto);
		if (business==null || !business.getUserType().equals("merchant")) {
			paymentMap.put("errors", "Valid Pay To organization not selected.");
			return new ModelAndView("payment", paymentMap);
		}
		BankAccount payee=bankAccntDao.getBankAccountWithAccno(business.getUsrid(),"checking");
		if( payee == null )
			payee=bankAccntDao.getBankAccountWithAccno(business.getUsrid(),"savings");
		if (payee == null) {
			paymentMap.put("errors", "Organization selected does not have a valid checing or savings account");
			return new ModelAndView("payment", paymentMap);
		}
		
		// PKI check		
		if (Float.parseFloat(amount) > 500) {
			if (privateKeyFile.isEmpty()) {
				paymentMap.put("errors", "Private Key must be provided for transactions more than $500");
				return new ModelAndView("payment", paymentMap);
			}			
			else {
				String privateKeyFileLocation = userOperationsService.uploadFileLoc();
				
				// check if file can be uploaded, if yes upload, if no return
				if (!userOperationsService.toUploadFile(privateKeyFileLocation, privateKeyFile)) {
					paymentMap.put("errors", "Private Key could not be uploaded. Private Key file must be readable at the given location and be not more than 50 KB");
					return new ModelAndView("payment", paymentMap);
				}
				
				// check if private key is valid 
				if (!userOperationsService.diffKeys(extUser, privateKeyFileLocation)) {		
					// not valid
					map.put("accountnumber", bankAccount.getAccountnumber());
					map.put("accountType", bankAccount.getAccounttype());
					map.put("balance", bankAccount.getBalance());
					map.put("transactions", transacDao.findTransactionsOfAccount(bankAccount));
					map.put("message", "<font color=\"red\">Private key authentication failed!</font>. Your payment request cannot be processed.");
					return new ModelAndView("account", map);
				}
			}
		}
		// passed validations, initiate Make Payment
		Transaction payment = new Transaction();
		payment.setTransDate(new Date());
		payment.setTransType("payment");
		payment.setAmt(Float.parseFloat(amount));
		payment.setFromacc(bankAccount);
		payment.setToacc(payee);
		payment.setTransDesc(payee.getUsrid().getOrganisationName());
		
		if (Float.parseFloat(amount) > 500) {
			payment.setTransStatus("processing");			
			try {
				transacMngrService.submitTransac(payment);
				map.put("message", "Private Key authentication is sucssessful. Payment of $" + amount + " scheduled from account " + bankAccount.getAccountnumber() + " to merchant " + payee.getUsrid().getOrganisationName());
			} catch (IllegalTransactionException e) {				
				map.put("message", "Private Key authentication is sucssessful but the payment request could not be processed.");
			}
		} 
		else {
			// amount less than $500
			payment.setTransStatus("cleared");
			transacDao.update(payment);
			
			
			payee.setBalance(payee.getBalance()+Float.parseFloat(amount));			
			bankAccount.setBalance(bankAccount.getBalance() - Float.parseFloat(amount));
			payee.setBalance(payee.getBalance() + Float.parseFloat(amount));
			bankAccntDao.update(bankAccount);
			bankAccntDao.update(payee);
			map.put("message", "Payment of $" + amount + " successful from account " + bankAccount.getAccountnumber() + " to merchant " + payee.getUsrid().getOrganisationName());
		}
				
				
		// render message and go to accounts page
		map.put("accountnumber", bankAccount.getAccountnumber());
		map.put("accountType", bankAccount.getAccounttype());
		map.put("balance", bankAccount.getBalance());
		map.put("transactions", transacDao.findTransactionsOfAccount(bankAccount));
				
		//return new ModelAndView("account", map);
		
		return new ModelAndView("redirect:/account");
		
		/*
		if (!userLoggedIn()) {
			return new ModelAndView("redirect:/login");
		}
		
		String amount=request.getParameter("amount").toString();
		String description=request.getParameter("description").toString();
		String payto= request.getParameter("organization").toString();
		String account_no=userSession.getSelectedUsrAccount().toString();
		
		Map<String, Object> paymentMap = new HashMap<String, Object>();
		ExternalUser business=extUsrDao.findUserByBname(payto);
		ExternalUser customer=extUsrDao.searchUsrByEmail(userSession.getUsername());
		BankAccount payer=bankAccntDao.getBankAccountWithAccno(account_no); 
		BankAccount payee=bankAccntDao.getBankAccountWithAccno(business.getUsrid(),"checking");
		List<ExternalUser> merchants =extUsrDao.findUserByUserType("merchant");
		paymentMap.put("user", merchants);
		
		if( payee == null )
			payee=bankAccntDao.getBankAccountWithAccno(business.getUsrid(),"savings");
		Transaction payment = new Transaction();
		
		if(payer.getAccountnumber().equals(payee.getAccountnumber())){
			paymentMap.put("message", "Account no of payer and payee cannot be the same");
			return new ModelAndView("payment", paymentMap);
		}
		
		if(amount.isEmpty()){
			paymentMap.put("message", "Please choose any one payee from the list");
			return new ModelAndView("payment", paymentMap);
		}
		if (!isNumeric(amount) || !(Float.parseFloat(amount) > 0)) {
			paymentMap.put("message", "Amount is not valid. Amount should be a valid number greater than 0.");
			return new ModelAndView("payment", paymentMap);
		}
		
		if (description.length() > 45) {
			paymentMap.put("message", "Description of Transaction cannot be more than 45 characters.");
			return new ModelAndView("payment", paymentMap);
		}
		
		if (Float.parseFloat(amount) > 500) {
			if (privateKeyFile.isEmpty()) {
				paymentMap.put("errors", "Private Key must be provided for transactions more than $500");
				return new ModelAndView("payment", paymentMap);
			}			
			else {
				String privateKeyFileLocation = userOperationsService.uploadFileLoc();
				
				// check if file can be uploaded, if yes upload, if no return
				if (!userOperationsService.toUploadFile(privateKeyFileLocation, privateKeyFile)) {
					paymentMap.put("errors", "Private Key could not be uploaded. Private Key file must be readable at the given location and be not more than 50 KB");
					return new ModelAndView("accountTransfer",paymentMap);
				}
				
				// check if private key is valid 
				if (!userOperationsService.diffKeys(customer, privateKeyFileLocation)) {		
					
					paymentMap.put("errors", "<font color=\"red\">Private key authentication failed!</font>. Your fund transfer request cannot be processed.");
					return new ModelAndView("account",paymentMap);		
				}
			}
		}
		payment.setTransDate(new Date());
		payment.setTransType("payment");
		
		if(Float.parseFloat(amount)> payer.getBalance()){
			paymentMap.put("message", "Amount exceeds your balance.");
			return new ModelAndView("payment", paymentMap);
		}
			
		payment.setAmt(Float.parseFloat(amount));
		payment.setFromacc(payer);
		payment.setToacc(payee);
		payment.setTransDesc(payee.getUsrid().getBName());
		
		if (Float.parseFloat(amount) > 500) {
			payment.setTransStatus("processing");			
			try {
				transacMngrService.submitTransac(payment);
				paymentMap.put("message", "Private Key authentication is sucssessful. Submitted the payment request for approval");
				return new ModelAndView("payment",paymentMap);
			} catch (IllegalTransactionException e) {				
				paymentMap.put("message", "Private Key authentication is sucssessful but the fund transfer request could not be processed.");
				return new ModelAndView("payment",paymentMap);
			}
		} 
		else{
		//do transfer
		payment.setTransStatus("cleared");
		transacDao.update(payment);
		
		payer.setBalance(payer.getBalance() - Float.parseFloat(amount));
		payee.setBalance(payee.getBalance()+Float.parseFloat(amount));
		bankAccntDao.update(payee);
		bankAccntDao.update(payer);
		return new ModelAndView("payment","message","Paid successfully");
		}*/
		
	}
	@RequestMapping("/customerPersonalInfo")
	public ModelAndView personalInformation(Model model){
		String username=userSession.getUsername();
		ExternalUser user=extUsrDao.searchUsrByEmail(username);
		Map<String, Object> fieldMap = new HashMap<String, Object>();
		fieldMap.put("name", user.getName());
		/*fieldMap.put("lastname", user.getLastname());
		fieldMap.put("middlename", user.getMiddlename());*/
		fieldMap.put("email", user.getEmail().getUsername());
		fieldMap.put("address", user.getAddress());
		/*fieldMap.put("addressline2", user.getAddressline2());
		fieldMap.put("city", user.getCity());
		fieldMap.put("state", user.getState());
		fieldMap.put("zipcode", user.getZipcode());*/
		fieldMap.put("ssn",user.getSsn());
		return new ModelAndView("PersonalInformation", fieldMap);
	}
	
	@RequestMapping("/edit")
	public ModelAndView editPersonalInfo(HttpServletRequest request){
		
		String email=userSession.getUsername();
		ExternalUser update=new ExternalUser();
		update=extUsrDao.searchUsrByEmail(email);
		String address1=request.getParameter("address1");
		String address2=request.getParameter("address2");
		String city=request.getParameter("city");
		String state=request.getParameter("state");
		String zipcode=request.getParameter("zip");
		String ssn=update.getSsn();
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder errors = new StringBuilder();
		if (!validateField(address1, 1, 30, true)) {
			errors.append("<li>Address Line 1 must not be empty, be between 1-30 characters and not have special characters</li>");
		}
		if (!validateField(address2, 1, 30, true)) {
			errors.append("<li>Address Line 2 must not be empty, be between 1-30 characters and not have special characters</li>");
		}
		if (!validateField(city, 1, 16, true)) {
			errors.append("<li>City must not be empty, be between 1-16 characters and not have spaces or special characters</li>");
		}
		if (!validateField(state, 1, 16, false)) {

			errors.append("<li>State must not be empty, be between 1-16 characters and not have spaces or special characters<</li>");
		}
		if (!validateField(zipcode, 1, 5, false)) {
	
			errors.append("<li>Zipcode must not be empty, be between 1-5 characters and not have spaces or special characters<</li>");
		}
		/*if (!validateField(ssn, 9, 9, false)) {
			errors.append("<li>SSN must not be empty, be 9 characters long and not have spaces</li>");
		}*/
	
		result.put("name", request.getParameter("name"));
//		result.put("lastname", request.getParameter("lastname"));
//		result.put("middlename",request.getParameter("middlename"));
		result.put("email", email);
		result.put("address",address1);
//		result.put("addressline2",address2);
//		result.put("city", city);
//		result.put("state", state);
//		result.put("zipcode", zipcode);
		result.put("ssn", ssn);
		
		if (errors.length() != 0) {			
			result.put("errors", errors.toString());
			return new ModelAndView("PersonalInformation", result);
		}
		update.setAddress(address1);
		/*if(address2!=null)
		update.setAddressline2(address2);
		update.setCity(city);
		update.setState(state);
		update.setZipcode(zipcode);*/
		//update.setSsn(ssn);
		
		result.put("message","paid successfully");
		extUsrDao.update(update);
		return new ModelAndView("PersonalInformation",result);
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
