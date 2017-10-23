
package com.group06fall17.banksix.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group06fall17.banksix.dao.ExternalUserDAO;
import com.group06fall17.banksix.dao.InternalUserDAO;
import com.group06fall17.banksix.dao.TaskDAO;
import com.group06fall17.banksix.dao.TransactionDAO;
import com.group06fall17.banksix.dao.UsersDAO;
import com.group06fall17.banksix.exception.AuthorizationException;
import com.group06fall17.banksix.exception.IllegalTransactionException;
import com.group06fall17.banksix.model.ExternalUser;
import com.group06fall17.banksix.model.InternalUser;
import com.group06fall17.banksix.model.Task;
import com.group06fall17.banksix.model.Transaction;
import com.group06fall17.banksix.model.Users;

/**
 * @author Abhilash
 *
 */

@Service
@Scope("session")
public class RegularEmployeeImpl implements RegularEmployeeService {

	@Autowired
	private TransactionDAO transactionDao;

	@Autowired
	private ExternalUserDAO externalUserDao;

	@Autowired
	private InternalUserDAO internalUserDao;
	
	@Autowired
	private UsersDAO usersDao;

	@Autowired
	private TaskDAO taskDao;
	
	@Autowired
	private TransactionManagerService transactionManagerService;

	private InternalUser user;
	private List<Task> tasksAssigned;

	@Override
	public void setUser(String email) {
		if (this.user == null)
			this.user = internalUserDao.findUserByEmail(email);
	}

	@Override
	public void createTransaction(Transaction transaction) throws AuthorizationException, IllegalTransactionException {
		if(user!= null && (user.getAccessprivilege().equals("RE1")) || user.getAccessprivilege().equals("RE2"))
			transactionManagerService.submitTransaction(transaction);
		else throw new AuthorizationException("Insufficient privileges to perform the action");
	}

	@Override
	@Transactional(readOnly = true)
	public List<Transaction> viewTransactions(String accno) {
		if(user!= null && (user.getAccessprivilege().equals("RE1")) || user.getAccessprivilege().equals("RE2"))
			return transactionDao.findTransactionsOfAccount(accno);
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Transaction viewTransaction(int tid) {
		if(user!= null && (user.getAccessprivilege().equals("RE1")) || user.getAccessprivilege().equals("RE2"))
			return transactionDao.findTransactionById(tid);
		return null;
	}

	@Override
	@Transactional
	public void updateTransaction(Transaction transaction) throws AuthorizationException {
		if(user!= null && user.getAccessprivilege().equals("RE2"))
			transactionManagerService.updateTransaction(transaction);
		else throw new AuthorizationException("Insufficient privileges to perform the action");
	}

	@Override
	@Transactional
	public void cancelTransaction(Transaction transaction) throws AuthorizationException, IllegalTransactionException {
		if(user!= null && user.getAccessprivilege().equals("RE2")){
			Task task = taskDao.findNewTaskByTID(transaction.getTid());
			
			if(task == null)
				throw new IllegalTransactionException("Cannot cancel approved transactions");
			taskDao.delete(task);
			
			transactionManagerService.cancelTransaction(transaction);
		}
		else throw new AuthorizationException("Insufficient privileges to perform the action");
	}

	@Override
	@Transactional
	public void authorizeTransaction(Transaction transaction) throws IllegalTransactionException, AuthorizationException {
		if(user!= null && (user.getAccessprivilege().equals("RE1")) || user.getAccessprivilege().equals("RE2")){
			String status = transaction.getTransStatus();
			if(status.equals("pending") )
				transactionManagerService.performTransaction(transaction);
		}
		else throw new AuthorizationException("Insufficient privileges to perform the action");
	}

	@Override
	@Transactional(readOnly = true)
	public ExternalUser viewExternalUserAcct(String email) {
		if(user!= null && (user.getAccessprivilege().equals("RE1")) || user.getAccessprivilege().equals("RE2")){
			return externalUserDao.findUserByEmail(email);
		}
		return null;
	}
	
	@Override
	@Transactional
	public void modifyExternalUserAcct(ExternalUser account) throws AuthorizationException {
		if(user!= null && (user.getAccessprivilege().equals("RE1")) || user.getAccessprivilege().equals("RE2")){
			externalUserDao.update(account);
		}
		else throw new AuthorizationException("Insufficient privileges to perform the action");
	}

	@Override
	@Transactional
	public void requestPrivileges(String message) {
		Task task = new Task();

		task.setMessage(message);
		task.setTid(null);
		task.setStatus("notcompleted");
		task.setAssigneeid(internalUserDao.findSysAdmin().getUserid());
					
		taskDao.add(task);	
	}

	@Transactional
	public void completeTask(int taskid){
		Task task = taskDao.findTaskById(taskid);
		
		task.setStatus("completed");
		
		taskDao.update(task);
	}
	
	@Transactional(readOnly = true)
	public void updateTasks() {
		tasksAssigned = taskDao.findNewTasksAssignedToUser(user.getUserid());
	}

	public List<Task> getTasks() {
		return tasksAssigned;
	}

	@Override
	public void updateInfo(InternalUser user) {
		internalUserDao.update(user);
	}
	
	@Override
	public void updatePasswd(Users user) {
		usersDao.update(user);
	}

}
