/**
 * @author Abhilash
 *
 */

package com.group06fall17.banksix.service;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group06fall17.banksix.dao.BankAccountDAO;
import com.group06fall17.banksix.dao.InternalUserDAO;
import com.group06fall17.banksix.dao.TaskDAO;
import com.group06fall17.banksix.dao.TransactionDAO;
import com.group06fall17.banksix.exception.EmployeeListException;
import com.group06fall17.banksix.exception.IllegalTransactionException;
import com.group06fall17.banksix.model.BankAccount;
import com.group06fall17.banksix.model.InternalUser;
import com.group06fall17.banksix.model.Task;
import com.group06fall17.banksix.model.Transaction;

@Service
@Scope("singleton")
public class TransacMngrImplementation implements Runnable, TransacMngrService{
	
	@Autowired
	private TaskDAO taskDao;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private BankAccountDAO bankAccntDao;
	
	@Autowired
	private InternalUserDAO intUsrDao;	

	@Autowired
	private TransactionDAO transacDao;
	
	private List<Integer> sysMngrList;
	private Deque<Task> processingTaskQueue;
	private List<Integer> regEmpList;
	private Random rand = new Random();
	private final static float thresholdAmnt = 500.00f;
	
	
/*  Pop a task from the queue and assign the task to an employee based on following parameter
 *	1) Transaction type
 *		i) If the transaction is transfer type,
 *			x) non - critical: assign to any regular employee
 *			y) critical : assign to system manager
 *		ii) If the transaction is a payment, assign to that merchant ID based on the merchant's account number
 *	   iii) If the transaction is review, assign to any regular employee
 *		iv) If the transaction is openacc/delacc, assign to system manager	 	
 *	2) On Success, update the transaction status from processing to pending
 *  3) On Failure, update the transaction status from processing to declined
 *  4) on exception return false else true
 */	
	
	@Override
	@Transactional
	public void planTask(){
		
		if(processingTaskQueue.size() == 0){
			return;
		}
		
		Task task = processingTaskQueue.pollFirst();
		Transaction transaction = task.getTransid();
		InternalUser intUsr = null;
		//ExternalUser externalUser;
		
		String type = transaction.getTransType();
		switch (type) {

		case "transfer":
		//	if (transaction.getAmt() > thresholdAmnt) {
		//	All request go to regular employees
		//		intUsr = intUsrDao.findUserById(sysMngrList.get(rand.nextInt(sysMngrList.size())));
		//	} else {
				intUsr = intUsrDao.findUserById(regEmpList.get( rand.nextInt(regEmpList.size())));
		//	}

			task.setTaskassignee_id(intUsr.getUsrid());
			transaction.setTransStatus("pending");

			taskDao.update(task);
			transacDao.update(transaction);
			
			break;

		case "payment":
			// Payment transferred directly to internal employee
			/*if(task.getTaskassignee_id() == 0){
				externalUser = transaction.getToacc().getUsrid();
				
				task.setTaskassignee_id(externalUser.getUsrid());
				transaction.setTransStatus("processing");
	
			}else{
			*/
				intUsr = intUsrDao.findUserById(regEmpList.get(rand.nextInt(regEmpList.size())));
				
				task.setTaskassignee_id(intUsr.getUsrid());
				transaction.setTransStatus("pending");				
		//	}
			
			taskDao.update(task);
			transacDao.update(transaction);
			
			break;

		case "review":
			intUsr = intUsrDao.findUserById(regEmpList.get(rand.nextInt(regEmpList.size())));
			
			task.setTaskassignee_id(intUsr.getUsrid());
			transaction.setTransStatus("pending");

			taskDao.update(task);
			transacDao.update(transaction);			
			break;

		case "openacc":
		case "delacc":
			intUsr = intUsrDao.findUserById(sysMngrList.get(rand.nextInt(sysMngrList.size())));

			task.setTaskassignee_id(intUsr.getUsrid());
			transaction.setTransStatus("pending");

			taskDao.update(task);
			transacDao.update(transaction);

			break;
		}

	}
	
/*
 * 1) Get all the internal employees from the database and categorize the	m into different types of employees
 * 2) On exception return false else true
 */	
	@Override
	@Transactional(readOnly = true)
	public boolean upgradeEmpList(){		
		try {
			if (processingTaskQueue == null) {
				processingTaskQueue = new ArrayDeque<Task>();
			}

			// Regular Employees List
			if (regEmpList == null) {
				regEmpList = new ArrayList<Integer>();
			} else {
				regEmpList.clear();
			}
			List<InternalUser> list = intUsrDao.findAllRegEmployees();

			if (list == null){
				throw new EmployeeListException("Found Error while fetching regular employees list");
			}

			for (InternalUser user : list) {
				if(regEmpList.contains(user.getUsrid()))
					continue;
				regEmpList.add(user.getUsrid());
			}

			// System Managers List
			if (sysMngrList == null) {
				sysMngrList = new ArrayList<Integer>();
			} else {
				sysMngrList.clear();
			}

			list = intUsrDao.findAllSystemManagers();
			if (list == null)
				throw new EmployeeListException("Found Error while fetching system managers list");

			for (InternalUser user : list) {
				if(sysMngrList.contains(user.getUsrid()))
					continue;
				sysMngrList.add(user.getUsrid());
			}

			return true;
		} catch (EmployeeListException e) {
			e.printStackTrace();
		}
		return false;
	}
	
/* 1) Save the transaction into database
 * 2) Create a task with status not completed and associate it to that transaction
 * 3) Push into the processing queue
 */
	@Override
	@Transactional
	public boolean submitTransac(Transaction transaction) throws IllegalTransactionException{
		if(!isValidTransaction(transaction)){
			throw new IllegalTransactionException("Transaction not allowed");
		}
		
		transacDao.add(transaction);
		
		if(transaction.getTransType().equals("credit") || transaction.getTransType().equals("debit")){
			return executeTransac(transaction);
		}
		
		Task newTask = new Task();
		
		newTask.setTransid(transaction);
		newTask.setMessage("general");
		newTask.setStatus("notcompleted");
		
		taskDao.add(newTask);
		processingTaskQueue.addLast(newTask);
		
		return true;
	}
	

	private boolean isValidTransaction(Transaction transaction) {
		boolean isValid = false;

		switch (transaction.getTransType()) {
		case "debit":
		case "credit":
		case "payment":
		case "review":
			isValid = true;
			break;

		case "transfer":
			switch (transaction.getTransDesc()) {
			case "internal":
			case "external":
				isValid = true;
				break;
			default:
				isValid = false;
				break;

			}
			break;
		default:
			break;
		}

		float amount = transaction.getAmt();

		if (amount < 0 || Float.isNaN(amount))
			isValid = false;

		return isValid;

	}

	/* perform the transaction based on each transaction type
 * 1) Transaction Type
 *    a) credit/debit
 *    b) transfer
 *    		i) external 
 *    		ii) internal
 *    c) payment
 *    d) review
 *    e) openacc
 *    f) delacc
 * 2) On Success, update the transaction status from pending to approved
 * 3) On Failure, update the transaction status from pending to declined
 * 4) on exception return false else true  
 */
	@Override
	@Transactional
	public boolean executeTransac(Transaction transaction) throws IllegalTransactionException {
			String transType = transaction.getTransType();

			BankAccount fromAccount = transaction.getFromacc();
			BankAccount toAccount = transaction.getToacc();

			if (!toAccount.getAccountstatus().equals("active") || !fromAccount.getAccountstatus().equals("active")) {

				transaction.setTransStatus("declined");
				transacDao.update(transaction);

				return false;
			}

			float amount;
			float balance;

			switch (transType) {
			case "credit":
				amount = transaction.getAmt();
				balance = fromAccount.getBalance();

				balance = amount + balance;

				fromAccount.setBalance(balance);
				bankAccntDao.update(fromAccount);

				transaction.setTransStatus("approved");
				transacDao.update(transaction);
				break;

			case "debit":
				amount = transaction.getAmt();
				balance = fromAccount.getBalance();

				if (amount <= balance) {
					balance = balance - amount;

					fromAccount.setBalance(balance);
					bankAccntDao.update(fromAccount);

					transaction.setTransStatus("approved");
				} else {
					transaction.setTransStatus("declined");
				}

				transacDao.update(transaction);
				break;

			case "transfer":
				switch (transaction.getTransDesc()) {
				case "internal":
					if (fromAccount.getUsrid().getUsrid() == toAccount.getUsrid().getUsrid()) {
						if (fromAccount.getAccountnumber() != toAccount.getAccountnumber()) {
							amount = transaction.getAmt();

							float bal1 = fromAccount.getBalance();
							float bal2 = toAccount.getBalance();

							if (amount <= bal1) {
								bal1 = bal1 - amount;
								bal2 = bal2 + amount;

								fromAccount.setBalance(bal1);
								toAccount.setBalance(bal2);

								bankAccntDao.update(fromAccount);
								bankAccntDao.update(toAccount);

								transaction.setTransStatus("approved");
							} else {
								transaction.setTransStatus("declined");
							}
							transacDao.update(transaction);
						} else {
							transaction.setTransStatus("declined");
							transacDao.update(transaction);

							throw new IllegalTransactionException("Invalid transaction");
						}
					} else {
						transaction.setTransStatus("declined");
						transacDao.update(transaction);

						throw new IllegalTransactionException("Invalid transaction");
					}
					break;

				case "external":
					if (fromAccount.getUsrid().getUsrid() != toAccount.getUsrid().getUsrid()) {
	//					if (fromAccount.getAccounttype().equals("checking") && toAccount.getAccounttype().equals("checking")) {
							amount = transaction.getAmt();

							float bal1 = fromAccount.getBalance();
							float bal2 = toAccount.getBalance();

							if (amount <= bal1) {
								bal1 = bal1 - amount;
								bal2 = bal2 + amount;

								fromAccount.setBalance(bal1);
								toAccount.setBalance(bal2);

								bankAccntDao.update(fromAccount);
								bankAccntDao.update(toAccount);

								transaction.setTransStatus("approved");
							} else {
								transaction.setTransStatus("declined");
							}
							transacDao.update(transaction);
						/*} else {
							transaction.setTransStatus("declined");
							transacDao.update(transaction);

							throw new IllegalTransactionException("Not valid transaction");
						}
*/					} else {
						transaction.setTransStatus("declined");
						transacDao.update(transaction);

						throw new IllegalTransactionException("Invalid transaction");
					}
					break;

				}
				break;

			case "payment":
				if (fromAccount.getUsrid().getUsrid() != toAccount.getUsrid().getUsrid()) {
//					if (fromAccount.getAccounttype().equals("checking") && toAccount.getAccounttype().equals("checking")) {
						amount = transaction.getAmt();

						float bal1 = fromAccount.getBalance();
						float bal2 = toAccount.getBalance();

						if (amount <= bal1) {
							bal1 = bal1 - amount;
							bal2 = bal2 + amount;

							fromAccount.setBalance(bal1);
							toAccount.setBalance(bal2);

							bankAccntDao.update(fromAccount);
							bankAccntDao.update(toAccount);

							transaction.setTransStatus("approved");
						} else {
							transaction.setTransStatus("declined");
						}
						transacDao.update(transaction);
	//				} else {
	//					transaction.setTransStatus("declined");
	//					transacDao.update(transaction);

	//					throw new IllegalTransactionException("Not valid transaction");
	//				}
				} else {
					transaction.setTransStatus("declined");
					transacDao.update(transaction);

					throw new IllegalTransactionException("Invalid transaction");
				}
				break;

			case "review":
				transaction.setTransStatus("approved");
				transacDao.update(transaction);
				break;

			case "openacc":
				fromAccount.setAccountstatus("active");
				bankAccntDao.update(fromAccount);

				transaction.setTransStatus("approved");
				transacDao.update(transaction);
				break;

			case "delacc":
				if (fromAccount.getBalance() == 0) {
					fromAccount.setAccountstatus("inactive");
					bankAccntDao.update(fromAccount);
					transaction.setTransStatus("approved");
				} else {
					transaction.setTransStatus("declined");
				}

				transacDao.update(transaction);
				break;

			default:
				transaction.setTransStatus("declined");
				transacDao.update(transaction);

				throw new IllegalTransactionException("Invalid transaction");
			}
		return true;
	}
	
/* 1) Modify old transaction with new transaction input in the parameter with same id
 * 2) Reflect the balances in the account as well
 * 3) on exception return false else true  
 */
	@Override
	@Transactional
	public boolean upgradeTransac(Transaction transaction){
		if (transaction.getTransStatus().equals("approved") || transaction.getTransStatus().equals("declined")) {
			return false;
		} else {
				transacDao.update(transaction);
		}
		return true;
	}
	
/* 1) Cancel the transaction if it is still either in pending or processing stage
 	WARNING: delete all tasks relating the transaction when executing this method
 */
	@Override
	@Transactional
	public boolean dropTransac(Transaction transaction)
	{
		if (transaction.getTransStatus().equals("pending") || transaction.getTransStatus().equals("processing")) {			
					transacDao.delete(transaction);
		}else{
			return false;
		}
		
		return true;
	}
	
/* 1) Periodically invokes planTask() 
 * 2) Periodically invokes updateEmployees() method - different time from 1)
 */
	@Override
	public void run() {
		int counter = 0;
		try{
			while(true){
				if(counter == 0){
					upgradeEmpList();
				}
				planTask();
				Thread.sleep(1000);
				planTask();
				counter = (counter + 1) % 1000;
			}
		}catch(InterruptedException e){
			e.printStackTrace();
			counter = 0;
		}
	}
	
	@PostConstruct
	public void initIt() throws Exception {
	  new Thread((Runnable) context.getBean("transacMngrService")).start();;
	}
}
