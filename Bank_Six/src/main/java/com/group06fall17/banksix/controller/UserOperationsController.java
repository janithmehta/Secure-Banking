package com.group06fall17.banksix.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.regex.Pattern;
import java.util.regex.Matcher;
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
import com.group06fall17.banksix.dao.InternalUserDAO;
import com.group06fall17.banksix.dao.TransactionDAO;
import com.group06fall17.banksix.dao.UserDAO;
import com.group06fall17.banksix.model.BankAccount;
import com.group06fall17.banksix.model.ExternalUser;
import com.group06fall17.banksix.model.InternalUser;
import com.group06fall17.banksix.model.Transaction;
import com.group06fall17.banksix.model.User;
import com.group06fall17.banksix.service.TransacMngrService;
import com.group06fall17.banksix.service.UsrFuncService;
import com.group06fall17.banksix.exception.IllegalTransactionException;
import static com.group06fall17.banksix.constants.Constants.EMAIL_REGEX;

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
	
	@Autowired
	InternalUserDAO intUsrDao;
			
	@RequestMapping("/customer")
	public ModelAndView extUsrDashboard(){		
		if (!usrLoggedIn()) {
			return new ModelAndView("redirect:/login");
		}
		ExternalUser extUser = extUsrDao.srchUsrusingEmail(userSession.getUsername());
		List<BankAccount> bnkaccts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		Map<String, Object> mapper = new HashMap<String, Object>();
		mapper.put("firstName", extUser.getName());
		mapper.put("bankAccounts", bnkaccts);
		return new ModelAndView("customer", mapper);		
	}
	
	
	public int reps(HttpServletRequest rqst,ExternalUser extUser,BankAccount bankacct ) {
		if (!usrLoggedIn()) { return 1; }
		if (rqst == null || rqst.getParameter("accountnumber") == null) { return 2; }
		if (bankacct == null || bankacct.getUsrid().getUsrid() != extUser.getUsrid()) {	return 3; }
		return 0;
	}
	
	public int reps1(HttpServletRequest rqst,ExternalUser extUser,BankAccount bankacct ) {
		if (!usrLoggedIn()) { return 1; }
		if (userSession.getSelectedUsrAccount() == null || userSession.getSelectedUsrAccount().isEmpty()) { return 2; }
		if (bankacct == null || bankacct.getUsrid().getUsrid() != extUser.getUsrid()) {	return 3; }
		return 0;
	}
	
	@RequestMapping(value="account", method=RequestMethod.POST)
	public ModelAndView extUsrAcctDashbrdPost(HttpServletRequest rqst){
		ExternalUser extUser = extUsrDao.srchUsrusingEmail(userSession.getUsername());
		List<BankAccount> bnkaccts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		Map<String, Object> mapper = new HashMap<String, Object>();
		mapper.put("firstName", extUser.getName());
		mapper.put("bankAccounts", bnkaccts);
		String acctnumb = rqst.getParameter("accountnumber").toString();
		BankAccount bankacct = bankAccntDao.getBankAccountWithAccno(acctnumb);
		switch(reps(rqst,extUser,bankacct)){
		case 1: return new ModelAndView("redirect:/login");
		case 2: mapper.put("message", "No record of this account exists!");return new ModelAndView("customer", mapper);
		case 3: mapper.put("message", "No information of account with account number " + acctnumb + " exists in database!");return new ModelAndView("customer", mapper);
		case 0: break;
		}
		
		mapper.put("accountnumber", bankacct.getAccountnumber());
		mapper.put("accountType", bankacct.getAccounttype());
		mapper.put("balance", bankacct.getBalance());
		mapper.put("transactions", transacDao.findTransactionsOfAccount(bankacct));
		userSession.setSelectedUsrAccount(bankacct.getAccountnumber());
		return new ModelAndView("account", mapper);		
	}
	
	@RequestMapping(value="account", method=RequestMethod.GET)
	public ModelAndView extUsrAcctDashbrdGet(HttpServletRequest rqst){	
		ExternalUser extUser = extUsrDao.srchUsrusingEmail(userSession.getUsername());
		List<BankAccount> bnkaccts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		Map<String, Object> mapper = new HashMap<String, Object>();
		mapper.put("firstName", extUser.getName());
		mapper.put("bankAccounts", bnkaccts);
		String acctnumb = userSession.getSelectedUsrAccount();
		BankAccount bankacct = bankAccntDao.getBankAccountWithAccno(acctnumb);
		switch(reps1(rqst,extUser,bankacct)){
		case 1: return new ModelAndView("redirect:/login");
		case 2: mapper.put("message", "No record of this account exists!");return new ModelAndView("customer", mapper);
		case 3: mapper.put("message", "No information of account with account number " + acctnumb + " exists in database!");return new ModelAndView("customer", mapper);
		case 0: break;
		}
		mapper.put("accountnumber", bankacct.getAccountnumber());
		mapper.put("accountType", bankacct.getAccounttype());
		mapper.put("balance", bankacct.getBalance());
		mapper.put("transactions", transacDao.findTransactionsOfAccount(bankacct));
		userSession.setSelectedUsrAccount(bankacct.getAccountnumber());
		return new ModelAndView("account", mapper);		
	}
	
	@RequestMapping(value="debit", method=RequestMethod.GET)
	public ModelAndView getDebit(HttpServletRequest rqst){
		ExternalUser extUser = extUsrDao.srchUsrusingEmail(userSession.getUsername());
		List<BankAccount> bnkaccts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());	
		Map<String, Object> mapper = new HashMap<String, Object>();
		mapper.put("firstName", extUser.getName());
		mapper.put("bankAccounts", bnkaccts);
		String acctnumb = userSession.getSelectedUsrAccount();
		BankAccount bankacct = bankAccntDao.getBankAccountWithAccno(acctnumb);
		switch(reps1(rqst,extUser,bankacct)){
		case 1: return new ModelAndView("redirect:/login");
		case 2: mapper.put("message", "Please Select an account!");return new ModelAndView("customer", mapper);
		case 3: mapper.put("message", "No information of account with account number " + acctnumb + " exists in database!");return new ModelAndView("customer", mapper);
		case 0: break;
		}
		Map<String, Object> debitMap = new HashMap<String, Object>();
		debitMap.put("firstName", extUser.getName());
		debitMap.put("displayOperation", "Debit");
		debitMap.put("operation", "debit");
		debitMap.put("accountNo", acctnumb);
		return new ModelAndView("debitCredit", debitMap);
	}
	
	@RequestMapping(value="dodebit", method=RequestMethod.POST)
	public ModelAndView doDebitPost(HttpServletRequest rqst){	
		ExternalUser extUser = extUsrDao.srchUsrusingEmail(userSession.getUsername());
		List<BankAccount> bnkaccts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		Map<String, Object> mapper = new HashMap<String, Object>();
		mapper.put("firstName", extUser.getName());
		mapper.put("bankAccounts", bnkaccts);
		String acctnumb = userSession.getSelectedUsrAccount();
		BankAccount bankacct = bankAccntDao.getBankAccountWithAccno(acctnumb);
		switch(reps1(rqst,extUser,bankacct)){
		case 1: return new ModelAndView("redirect:/login");
		case 2: mapper.put("message", "Please Select an account!");return new ModelAndView("customer", mapper);
		case 3: mapper.put("message", "No information of account with account number " + acctnumb + " exists in database!");return new ModelAndView("customer", mapper);
		case 0: break;
		}
		Map<String, Object> debitMapper = new HashMap<String, Object>();
		debitMapper.put("firstName", extUser.getName());
		debitMapper.put("displayOperation", "Debit");
		debitMapper.put("operation", "debit");
		debitMapper.put("accountNo", acctnumb);		
		if (rqst == null) {
			return new ModelAndView("debitCredit", debitMapper);
		} 
		String oprtn_parameter = rqst.getParameter("operation").toString();
		String acctno_parameter = rqst.getParameter("accountnumber").toString();		
		String amt_param = rqst.getParameter("Amount").toString();
		String description_param = rqst.getParameter("Description").toString();
		if (!acctno_parameter.equals(acctnumb)) {
			debitMapper.put("errors", "Account to debit is not valid");
			return new ModelAndView("debitCredit", debitMapper);
		}
		if (!oprtn_parameter.equals("debit")) {
			debitMapper.put("errors", "Invalid oper");
			return new ModelAndView("debitCredit", debitMapper);
		}
		if (bankacct.getBalance() < Float.parseFloat(amt_param)) {
			debitMapper.put("errors", "Cannot debit $" + Float.parseFloat(amt_param)+ " from the account due to insufficient funds");
			return new ModelAndView("debitCredit", debitMapper);
		}
		if (!isitNumeric(amt_param) || !(Float.parseFloat(amt_param) > 0)) {
			debitMapper.put("errors", "Please enter valid number greater than 0 for the amount.");
			return new ModelAndView("debitCredit", debitMapper);
		}
		if (description_param.length() > 45) {
			debitMapper.put("errors", "Maximum length of Description of Transaction is 45 characters.");
			return new ModelAndView("debitCredit", debitMapper);
		}
		Transaction debittrnasac = new Transaction();
		debittrnasac.setAmount(Float.parseFloat(amt_param));
		debittrnasac.setTransDate(new Date());
		debittrnasac.setTransDesc(description_param);
		debittrnasac.setFromacc(bankacct);
		debittrnasac.setTransStatus("cleared");
		debittrnasac.setToacc(bankacct);
		debittrnasac.setTransType("debit");
		transacDao.update(debittrnasac);
		bankacct.setBalance(bankacct.getBalance() - Float.parseFloat(amt_param));
		bankAccntDao.updateacct(bankacct);
		mapper.put("accountnumber", bankacct.getAccountnumber());
		mapper.put("accountType", bankacct.getAccounttype());
		mapper.put("balance", bankacct.getBalance());
		mapper.put("transactions", transacDao.findTransactionsOfAccount(bankacct));
		mapper.put("message", "Debit of amount $" + amt_param + " is performed successfully from the account " + bankacct.getAccountnumber());
		return new ModelAndView("redirect:/account");
	}
	
	@RequestMapping(value="credit", method=RequestMethod.GET)
	public ModelAndView creditGet(HttpServletRequest rqst){
		ExternalUser extUser = extUsrDao.srchUsrusingEmail(userSession.getUsername());
		List<BankAccount> bnkaccts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());		
		Map<String, Object> mapper = new HashMap<String, Object>();
		mapper.put("firstName", extUser.getName());
		mapper.put("bankAccounts", bnkaccts);
		String acctnumb = userSession.getSelectedUsrAccount();
		BankAccount bankacct = bankAccntDao.getBankAccountWithAccno(acctnumb);
		switch(reps1(rqst,extUser,bankacct)){
		case 1: return new ModelAndView("redirect:/login");
		case 2: mapper.put("message", "Please Select an account!");return new ModelAndView("customer", mapper);
		case 3: mapper.put("message", "No information of account with account number " + acctnumb + " exists in database!");return new ModelAndView("customer", mapper);
		case 0: break;
		}		
		Map<String, Object> creditMap = new HashMap<String, Object>();
		creditMap.put("firstName", extUser.getName());
		creditMap.put("displayOperation", "Credit");
		creditMap.put("operation", "credit");
		creditMap.put("accountNo", acctnumb);
		return new ModelAndView("debitCredit", creditMap);
	}
	
	// Credit Actuator
	@RequestMapping(value="docredit", method=RequestMethod.POST)
	public ModelAndView doCreditPost(HttpServletRequest rqst){
		ExternalUser extUser = extUsrDao.srchUsrusingEmail(userSession.getUsername());
		List<BankAccount> bnkaccts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());		
		Map<String, Object> mapper = new HashMap<String, Object>();
		mapper.put("firstName", extUser.getName());
		mapper.put("bankAccounts", bnkaccts);
		String acctnumb = userSession.getSelectedUsrAccount();
		BankAccount bankacct = bankAccntDao.getBankAccountWithAccno(acctnumb);
		switch(reps1(rqst,extUser,bankacct)){
		case 1: return new ModelAndView("redirect:/login");
		case 2: mapper.put("message", "No record of this account exists!");return new ModelAndView("customer", mapper);
		case 3: mapper.put("message", "No information of account with account number " + acctnumb + " exists in database!");return new ModelAndView("customer", mapper);
		case 0: break;
		}
		Map<String, Object>creditMap = new HashMap<String, Object>();
		creditMap.put("firstName", extUser.getName());
		creditMap.put("displayOperation", "Credit");
		creditMap.put("operation", "credit");
		creditMap.put("accountNo", acctnumb);
		
		// check if required form parameter values are present, and are valid
		if (rqst == null) {
			return new ModelAndView("debitCredit", creditMap);
		} 
		String operation_param = rqst.getParameter("operation").toString();
		String accno_param = rqst.getParameter("accountnumber").toString();		
		String amount_param = rqst.getParameter("Amount").toString();
		String desc_param = rqst.getParameter("Description").toString();
		
		if (!operation_param.equals("credit")) {
			creditMap.put("errors", "Invalid operation");
			return new ModelAndView("debitCredit", creditMap);
		}
		
		if (!accno_param.equals(acctnumb)) {
			creditMap.put("errors", "Account to credit is not valid");
			return new ModelAndView("debitCredit", creditMap);
		}
		
		if (!isitNumeric(amount_param) || !(Float.parseFloat(amount_param) > 0)) {
			creditMap.put("errors", "Amount is not valid. Amount should be a valid number greater than 0.");
			return new ModelAndView("debitCredit", creditMap);
		}
		
		if (desc_param.length() > 45) {
			creditMap.put("errors", "Description of Transaction cannot be more than 45 characters.");
			return new ModelAndView("debitCredit", creditMap);
		}
		
		// passed validations do the credit
		Transaction creditTransaction = new Transaction();
		creditTransaction.setAmount(Float.parseFloat(amount_param));
		creditTransaction.setTransDate(new Date());
		creditTransaction.setTransDesc(desc_param);
		creditTransaction.setFromacc(bankacct);
		creditTransaction.setTransStatus("cleared");
		creditTransaction.setToacc(bankacct);
		creditTransaction.setTransType("credit");
		transacDao.update(creditTransaction);
		bankacct.setBalance(bankacct.getBalance() + Float.parseFloat(amount_param));
		bankAccntDao.updateacct(bankacct);
				
		// render message and go to accounts page
		mapper.put("accountnumber", bankacct.getAccountnumber());
		mapper.put("accountType", bankacct.getAccounttype());
		mapper.put("balance", bankacct.getBalance());
		mapper.put("transactions", transacDao.findTransactionsOfAccount(bankacct));
		mapper.put("message", "Credit of $" + amount_param + " successful to account " + bankacct.getAccountnumber());
		return new ModelAndView("redirect:/account");
	}
	
	// Account Transfer Renderer
	@RequestMapping(value="transfer", method=RequestMethod.GET)
	public ModelAndView transferGet(HttpServletRequest rqst){
		ExternalUser extUser = extUsrDao.srchUsrusingEmail(userSession.getUsername());
		List<BankAccount> bnkaccts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		Map<String, Object> mapper = new HashMap<String, Object>();
		mapper.put("firstName", extUser.getName());
		mapper.put("bankAccounts", bnkaccts);
		String acctnumb = userSession.getSelectedUsrAccount();
		BankAccount bankacct = bankAccntDao.getBankAccountWithAccno(acctnumb);
		switch(reps1(rqst,extUser,bankacct)){
		case 1: return new ModelAndView("redirect:/login");
		case 2: mapper.put("message", "No record of this account exists!");return new ModelAndView("customer", mapper);
		case 3: mapper.put("message", "No information of account with account number " + acctnumb + " exists in database!");return new ModelAndView("customer", mapper);
		case 0: break;
		}
		Map<String, Object> transferMap = new HashMap<String, Object>();
		transferMap.put("firstName", extUser.getName());
		transferMap.put("accountNo", acctnumb);
		
		return new ModelAndView("accountTransfer", transferMap);
	}
	
	// Account Transfer Actuator
	@RequestMapping(value="dotransfer", method=RequestMethod.POST)
	public ModelAndView doTransferPost(
			@RequestParam("FromAccount") String from_accno_param,
			@RequestParam("ToAccount") String to_accno_param,
			@RequestParam("Amount") String amount_param,
			@RequestParam("Description") String desc_param,	
			@RequestParam("PrivateKeyFileLoc") MultipartFile privateKeyFile) {	    
		if (!usrLoggedIn()) {
			return new ModelAndView("redirect:/login");
		}
		
		ExternalUser extUser = extUsrDao.srchUsrusingEmail(userSession.getUsername());
		List<BankAccount> bnkaccts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		
		Map<String, Object> mapper = new HashMap<String, Object>();
		mapper.put("firstName", extUser.getName());
		mapper.put("bankAccounts", bnkaccts);
		if (userSession.getSelectedUsrAccount() == null || userSession.getSelectedUsrAccount().isEmpty()) {
			mapper.put("message", "Please Select an account!");
			return new ModelAndView("customer", mapper);
		}
		
		String acctnumb = userSession.getSelectedUsrAccount();
		BankAccount bankacct = bankAccntDao.getBankAccountWithAccno(acctnumb);
		if (bankacct == null || bankacct.getUsrid().getUsrid() != extUser.getUsrid()) {
			mapper.put("message", "No account with account number " + acctnumb + " found!");
			return new ModelAndView("customer", mapper);	
		}
		Map<String, Object> transferMapper = new HashMap<String, Object>();
		transferMapper.put("firstName", extUser.getName());		
		transferMapper.put("accountNo", acctnumb);
		transferMapper.put("description", desc_param);
		transferMapper.put("amount", amount_param);
		transferMapper.put("toaccount", to_accno_param);
				
		if (!from_accno_param.equals(acctnumb)) {
			transferMapper.put("errors", "Account mentioned to debit from is not valid");
			return new ModelAndView("accountTransfer", transferMapper);
		}
		
		// check if ToAccount is a valid account
		Pattern emailRegex = Pattern.compile(EMAIL_REGEX);
		BankAccount toBankAcct = null;
		User user = null;
			Matcher matcher = emailRegex.matcher(to_accno_param);
			System.out.println("SAURABH::START");
			if (matcher.matches()) {
				System.out.println("SAURABH::EMAIL MATCHED 1");
				if(!to_accno_param.equals(userSession.getUsername())) {
				user = usrDAO.findUsersByEmail(to_accno_param);
				System.out.println("SAURABH::EMAIL MATCHED 2 ");
				if(user!=null && (user.getUserType().contains("ROLE_INDIVIDUAL") || user.getUserType().contains("ROLE_MERCHANT"))) {
					System.out.println("SAURABH::EMAIL MATCHED 3");
					ExternalUser externalUser = extUsrDao.srchUsrusingEmail(user.getUsername());
					if(externalUser!=null) {
						System.out.println("SAURABH::EMAIL MATCHED 4");
						int userid = externalUser.getUsrid();
							toBankAcct = bankAccntDao.getBankAccountWithEmail(userid,"checking");
					}
				} else if (user!=null && (user.getUserType().contains("ROLE_EMPLOYEE") || user.getUserType().contains("ROLE_MANAGER") || user.getUserType().contains("ROLE_ADMIN"))) {
					System.out.println("SAURABH::EMAIL MATCHED 5");
					InternalUser internalUser = intUsrDao.searchUsrByEmail(user.getUsername());
					if(internalUser!=null) {
						System.out.println("SAURABH::EMAIL MATCHED 6");
						int userid = internalUser.getUsrid();
						toBankAcct = bankAccntDao.getBankAccountWithEmail(userid,"checking");
					}
				}
				} else {
					transferMapper.put("errors", "Funds cannot be transfered to the same account");
					return new ModelAndView("accountTransfer", transferMapper);
				}
			} else {
				System.out.println("SAURABH::EMAIL NOT MATCHED");
				toBankAcct = bankAccntDao.getBankAccountWithAccno(to_accno_param);
			}
		if (toBankAcct == null) {
			transferMapper.put("errors", "Account mentioned to transfer credit funds to is not valid");
			return new ModelAndView("accountTransfer", transferMapper);
		}
		
		if (bankacct.getAccountnumber().equals(toBankAcct.getAccountnumber())) {
			transferMapper.put("errors", "To and From account fields cannot be the same account");
			return new ModelAndView("accountTransfer", transferMapper);
		}
		for (BankAccount bnk : bnkaccts) {
			if (bnk.getAccountnumber().equals(toBankAcct)) {
				toBankAcct = bnk;
				break;
			}
		}
			
		
		if (!isitNumeric(amount_param) || !(Float.parseFloat(amount_param) > 0)) {
			transferMapper.put("errors", "Amount should be a valid number greater than 0.");
			return new ModelAndView("accountTransfer", transferMapper);
		}
		System.out.println("SAURABH:bankacct.getAccounttype():"+bankacct.getAccounttype());
		if (!bankacct.getAccounttype().contains("credit") && bankacct.getBalance() < Float.parseFloat(amount_param)) {
			transferMapper.put("errors", "Insufficient funds to debit account with $" + Float.parseFloat(amount_param));
			return new ModelAndView("accountTransfer", transferMapper);
		}
		
		if (desc_param.length() > 45) {
			transferMapper.put("errors", "Description of Transaction can have maximum of 45 characters.");
			return new ModelAndView("accountTransfer", transferMapper);
		}
		if (Float.parseFloat(amount_param) > 500) {
			if (privateKeyFile.isEmpty()) {
				transferMapper.put("errors", "For transactions more than $500, Private Key must be provided");
				return new ModelAndView("accountTransfer", transferMapper);
			}			
			else {
				String privateKeyFileLocation = userOperationsService.uploadFileLoc();
				if (!userOperationsService.toUploadFile(privateKeyFileLocation, privateKeyFile)) {
					transferMapper.put("errors", "Upload of Private Key failed. Private Key file must be readable at the given location and be not more than 50 KB");
					return new ModelAndView("accountTransfer", transferMapper);
				}
				if (!userOperationsService.diffKeys(extUser, privateKeyFileLocation)) {		
					// not valid
					mapper.put("accountnumber", bankacct.getAccountnumber());
					mapper.put("accountType", bankacct.getAccounttype());
					mapper.put("balance", bankacct.getBalance());
					mapper.put("transactions", transacDao.findTransactionsOfAccount(bankacct));
					mapper.put("message", "<font color=\"red\">Authentication of Private key failed!</font>. Your fund transfer request cannot be processed.");
					return new ModelAndView("account", mapper);		
				}
			}
		}
		Transaction transferTransaction = new Transaction();
		transferTransaction.setAmount(Float.parseFloat(amount_param));
		transferTransaction.setTransDate(new Date());
		transferTransaction.setFromacc(bankacct);		
		transferTransaction.setToacc(toBankAcct);
		transferTransaction.setTransType("transfer");
		
		if(bankacct.getUsrid().getUsrid() != toBankAcct.getUsrid().getUsrid())
			transferTransaction.setTransDesc("external");
		else
			transferTransaction.setTransDesc("internal");
		if(Float.parseFloat(amount_param) < 500) {
			transferTransaction.setTransStatus("cleared");
			transacDao.update(transferTransaction);
			bankacct.setBalance(bankacct.getBalance() - Float.parseFloat(amount_param));
			toBankAcct.setBalance(toBankAcct.getBalance() + Float.parseFloat(amount_param));
			bankAccntDao.updateacct(bankacct);
			bankAccntDao.updateacct(toBankAcct);
			mapper.put("message", "Debit of amount $" + amount_param + "is successful from the account " + bankacct.getAccountnumber() + " to the account " + toBankAcct.getAccountnumber());	
		}
		else {
			transferTransaction.setTransStatus("processing");			
			try {
				transacMngrService.submitTransac(transferTransaction);
				mapper.put("message", "Authentication of Private Key is sucssessful. Debit amount of $" + amount_param + " is scheduled from account " + bankacct.getAccountnumber() + " to the account " + toBankAcct.getAccountnumber());
			} catch (IllegalTransactionException e) {				
				mapper.put("message", "Authentication of Private Key is sucssessful but, the fund transfer request could not be processed.");
			}
		}
		// render message and go to accounts page
		mapper.put("accountnumber", bankacct.getAccountnumber());
		mapper.put("accountType", bankacct.getAccounttype());
		mapper.put("balance", bankacct.getBalance());
		mapper.put("transactions", transacDao.findTransactionsOfAccount(bankacct));
		return new ModelAndView("redirect:/account");
	}
	
	// HELPER METHODS
	
	public boolean usrLoggedIn() {
		if (userSession == null || userSession.getUserActive() != 1)
			return false;		
		else
			return true;
	}
	
	public boolean isitNumeric(String val) {
		if (val == null)
			return false;
		try {
			double d = Float.parseFloat(val);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public void setTransactionService(TransactionDAO trsxSrvce) {
		this.transacDao = trsxSrvce;
	}

	public void setUsr(ExternalUserDAO usr) {
		this.extUsrDao = usr;
	}

	public void setBankAccountService(BankAccountDAO bankAcctSrvce) {
		this.bankAccntDao = bankAcctSrvce;
	}

	public void setUserSession(UserSessionInfo userSessn) {
		this.userSession = userSessn;
	}

	public int reps2(ExternalUser extUser,BankAccount bankacct ) {
		if (!usrLoggedIn()) { return 1; }
		if (userSession.getSelectedUsrAccount() == null || userSession.getSelectedUsrAccount().isEmpty()) { return 2; }
		// account info does not exist, or does not belong to the user
		if (bankacct == null || bankacct.getUsrid().getUsrid() != extUser.getUsrid()) {	return 3; }
		return 0;
	}
	
	@RequestMapping("/downloadpage")
	public ModelAndView downloadPage(){
		ExternalUser extUser = extUsrDao.srchUsrusingEmail(userSession.getUsername());
		List<BankAccount> bnkaccts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		Map<String, Object> mapper = new HashMap<String, Object>();
		mapper.put("firstName", extUser.getName());
		mapper.put("bankAccounts", bnkaccts);
		String acctnumb=userSession.getSelectedUsrAccount();
		BankAccount bankacct=bankAccntDao.getBankAccountWithAccno(userSession.getSelectedUsrAccount());
		switch(reps2(extUser,bankacct)){
		case 1: return new ModelAndView("redirect:/login");
		case 2: mapper.put("message", "No record of this account exists!");return new ModelAndView("customer", mapper);
		case 3: mapper.put("message", "No information of account with account number " + acctnumb + " exists in database!");return new ModelAndView("customer", mapper);
		case 0: break;
		}
		mapper.put("accountnumber",bankacct.getAccountnumber());
		mapper.put("accountType", bankacct.getAccounttype());
		mapper.put("balance",bankacct.getBalance());
		mapper.put("acctcreatedate",bankacct.getAcctcreatedate());
		mapper.put("usrid",bankacct.getUsrid().getUsrid());
		mapper.put("accountstatus",bankacct.getAccountstatus());
	    return new ModelAndView("downloadpage",mapper);
	}
	
	
	@RequestMapping("/download")
	public ModelAndView downloadStatement(HttpServletResponse response) throws IOException{
		ExternalUser extUser = extUsrDao.srchUsrusingEmail(userSession.getUsername());
		List<BankAccount> bnkaccts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		Map<String, Object> mapper = new HashMap<String, Object>();
		mapper.put("firstName", extUser.getName());
		mapper.put("bankAccounts", bnkaccts);
		String acctnumb=userSession.getSelectedUsrAccount();
		BankAccount bankacct=bankAccntDao.getBankAccountWithAccno(userSession.getSelectedUsrAccount());
		switch(reps2(extUser,bankacct)){
		case 1: return new ModelAndView("redirect:/login");
		case 2: mapper.put("message", "No record of this account exists!");return new ModelAndView("customer", mapper);
		case 3: mapper.put("message", "No information of account with account number " + acctnumb + " exists in database!");return new ModelAndView("customer", mapper);
		case 0: break;
		}
		
	   	String filename="Statement.csv";
	   	String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                filename);
        response.setHeader(headerKey, headerValue);
	   	List<Transaction> trans=transacDao.findTransactionsOfAccount(bankacct);
	   	System.out.println("Size of trans : "+trans.size());
	   	ICsvBeanWriter csvWriter= new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
	   	String[] header ={"tdate","tdesc","ttype","amount", "tstatus"};	
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
	public ModelAndView makePayment(HttpServletRequest rqst){
		ExternalUser extUser = extUsrDao.srchUsrusingEmail(userSession.getUsername());
		List<BankAccount> bnkaccts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		Map<String, Object> mapper = new HashMap<String, Object>();
		mapper.put("firstName", extUser.getName());
		mapper.put("bankAccounts", bnkaccts);
		String acctnumb = userSession.getSelectedUsrAccount();
		BankAccount bankacct = bankAccntDao.getBankAccountWithAccno(acctnumb);
		switch(reps2(extUser,bankacct)){
		case 1: return new ModelAndView("redirect:/login");
		case 2: mapper.put("message", "No record of this account exists!");return new ModelAndView("customer", mapper);
		case 3: mapper.put("message", "No information of account with account number " + acctnumb + " exists in database!");return new ModelAndView("customer", mapper);
		case 0: break;
		}
		Map<String, Object> paymentMapper = new HashMap<String, Object>();
		paymentMapper.put("firstName", extUser.getName());
		paymentMapper.put("accountNo", acctnumb);				
		List<ExternalUser> merchants = extUsrDao.searhUserusngUserType("merchant");
		paymentMapper.put("merchants", merchants);		
		return new ModelAndView("payment", paymentMapper);
	}
	
	// Make Payment Actuator 
	@RequestMapping(value="/dopayment",method=RequestMethod.POST)
	public ModelAndView payToOrganization(@RequestParam("PrivateKeyFileLoc") MultipartFile privateKeyFile,HttpServletRequest rqst){
		ExternalUser extUser = extUsrDao.srchUsrusingEmail(userSession.getUsername());
		List<BankAccount> bnkaccts = bankAccntDao.findAccountsOfUser(extUser.getUsrid());
		Map<String, Object> mapper = new HashMap<String, Object>();
		mapper.put("firstName", extUser.getName());
		mapper.put("bankAccounts", bnkaccts);
		String acctnumb = userSession.getSelectedUsrAccount();
		BankAccount bankacct = bankAccntDao.getBankAccountWithAccno(acctnumb);
		switch(reps2(extUser,bankacct)){
		case 1: return new ModelAndView("redirect:/login");
		case 2: mapper.put("message", "No record of this account exists!");return new ModelAndView("customer", mapper);
		case 3: mapper.put("message", "No information of account with account number " + acctnumb + " exists in database!");return new ModelAndView("customer", mapper);
		case 0: break;
		}
		
		Map<String, Object> paymentMapper = new HashMap<String, Object>();
		paymentMapper.put("firstName", extUser.getName());
		paymentMapper.put("accountNo", acctnumb);				
		List<ExternalUser> merchants = extUsrDao.searhUserusngUserType("merchant");
		paymentMapper.put("merchants", merchants);
		
		
		// check if required form parameter values are present, and are valid
		if (rqst == null) {
			return new ModelAndView("payment", paymentMapper);
		} 
		String amount=rqst.getParameter("amount").toString();
		String accno_param = rqst.getParameter("accountnumber").toString();		
		String description=rqst.getParameter("description").toString();
		String payto = null;
		if(rqst!=null && !rqst.getParameter("organization").toString().isEmpty())
			payto = rqst.getParameter("organization").toString();
		
		if (!accno_param.equals(acctnumb)) {
			paymentMapper.put("errors", "Account to Make Payment from is not valid");
			return new ModelAndView("payment", paymentMapper);
		}
		
		if (!isitNumeric(amount) || !(Float.parseFloat(amount) > 0)) {
			paymentMapper.put("errors", "Amount is not valid. Amount should be a valid number greater than 0.");
			return new ModelAndView("payment", paymentMapper);
		}
		
		if (!bankacct.getAccounttype().contains("credit") && bankacct.getBalance() < Float.parseFloat(amount)) {
			paymentMapper.put("errors", "Not sufficient funds to make payment of $" + Float.parseFloat(amount));
			return new ModelAndView("payment", paymentMapper);
		}
		
		if (description.length() > 45) {
			paymentMapper.put("errors", "Description of Transaction cannot be more than 45 characters.");
			return new ModelAndView("payment", paymentMapper);
		}
		
		ExternalUser business=extUsrDao.searchUserusngBname(payto);
		if (business==null || !business.getUserType().equals("merchant")) {
			paymentMapper.put("errors", "Valid Pay To organization not selected.");
			return new ModelAndView("payment", paymentMapper);
		}
		BankAccount payee=bankAccntDao.getBankAccountWithAccno(business.getUsrid(),"checking");
		if( payee == null )
			payee=bankAccntDao.getBankAccountWithAccno(business.getUsrid(),"savings");
		if (payee == null) {
			paymentMapper.put("errors", "Organization selected does not have a valid checing or savings account");
			return new ModelAndView("payment", paymentMapper);
		}
		
		// PKI check		
		if (Float.parseFloat(amount) > 500) {
			if (privateKeyFile.isEmpty()) {
				paymentMapper.put("errors", "For transactions more than $500, Private Key must be provided");
				return new ModelAndView("payment", paymentMapper);
			}			
			else {
				String privateKeyFileLocation = userOperationsService.uploadFileLoc();
				
				// check if file can be uploaded, if yes upload, if no return
				if (!userOperationsService.toUploadFile(privateKeyFileLocation, privateKeyFile)) {
					paymentMapper.put("errors", "Private Key could not be uploaded. Private Key file must be readable at the given location and be not more than 50 KB");
					return new ModelAndView("payment", paymentMapper);
				}
				
				// check if private key is valid 
				if (!userOperationsService.diffKeys(extUser, privateKeyFileLocation)) {		
					// not valid
					mapper.put("accountnumber", bankacct.getAccountnumber());
					mapper.put("accountType", bankacct.getAccounttype());
					mapper.put("balance", bankacct.getBalance());
					mapper.put("transactions", transacDao.findTransactionsOfAccount(bankacct));
					mapper.put("message", "<font color=\"red\">Private key authentication failed!</font>. Your payment request cannot be processed.");
					return new ModelAndView("account", mapper);
				}
			}
		}
		// passed validations, initiate Make Payment
		Transaction payment = new Transaction();
		payment.setTransDate(new Date());
		payment.setTransType("payment");
		payment.setAmount(Float.parseFloat(amount));
		payment.setFromacc(bankacct);
		payment.setToacc(payee);
		payment.setTransDesc(payee.getUsrid().getOrganisationName());
		
		if (Float.parseFloat(amount) > 500) {
			payment.setTransStatus("processing");			
			try {
				transacMngrService.submitTransac(payment);
				mapper.put("message", "Private Key authentication is sucssessful. Payment of $" + amount + " scheduled from account " + bankacct.getAccountnumber() + " to merchant " + payee.getUsrid().getOrganisationName());
			} catch (IllegalTransactionException e) {				
				mapper.put("message", "Private Key authentication is sucssessful but the payment request could not be processed.");
			}
		} 
		else {
			// amount less than $500
			payment.setTransStatus("cleared");
			transacDao.update(payment);
			
			
			payee.setBalance(payee.getBalance()+Float.parseFloat(amount));			
			bankacct.setBalance(bankacct.getBalance() - Float.parseFloat(amount));
			bankAccntDao.updateacct(bankacct);
			bankAccntDao.updateacct(payee);
			mapper.put("message", "Payment of $" + amount + " successful from account " + bankacct.getAccountnumber() + " to merchant " + payee.getUsrid().getOrganisationName());
		}
		mapper.put("accountnumber", bankacct.getAccountnumber());
		mapper.put("accountType", bankacct.getAccounttype());
		mapper.put("balance", bankacct.getBalance());
		mapper.put("transactions", transacDao.findTransactionsOfAccount(bankacct));
		return new ModelAndView("redirect:/account");
		
		
	}
	@RequestMapping("/customerPersonalInfo")
	public ModelAndView personalInformation(Model model){
		String username=userSession.getUsername();
		ExternalUser user=extUsrDao.srchUsrusingEmail(username);
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
	public ModelAndView editPersonalInfo(HttpServletRequest rqst){
		
		String email=userSession.getUsername();
		ExternalUser update=new ExternalUser();
		update=extUsrDao.srchUsrusingEmail(email);
		String address=rqst.getParameter("address");
		String name = rqst.getParameter("name");
		String ssn=update.getSsn();
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuilder errors = new StringBuilder();
		if (!validateField(address, 1, 30, true)) {
			errors.append("<li>Address Line 1 must not be empty, be between 1-30 characters and not have special characters</li>");
		}
		result.put("name", name);
		result.put("email", email);
		result.put("address",address);

		result.put("ssn", ssn);
		
		if (errors.length() != 0) {			
			result.put("errors", errors.toString());
			return new ModelAndView("PersonalInformation", result);
		}
		update.setAddress(address);
		update.setName(name);
		result.put("message","Updated successfully");
		extUsrDao.updateextusr(update);
		return new ModelAndView("PersonalInformation",result);
	}

	private boolean validateField(String fld, int minisize, int maxisize, boolean spaceAllowed) {
		if (fld == null)
			return false;
		if (spaceAllowed && spclcharswithsapce(fld)) 
			return false;
		if (!spaceAllowed && withspclcharsnospace(fld))
			return false;
		if (fld.length() < minisize || fld.length() > maxisize)
			return false;			
		return true;
	}
	
	private boolean spclcharswithsapce(String fld) {
		if (!StringUtils.isAlphanumericSpace(fld))
			return true;
		
		return false;
	}
	private boolean withspclcharsnospace(String field) {
		if (!StringUtils.isAlphanumeric(field))
			return true;
		
		return false;
	}
	
	
}
