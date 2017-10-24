
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
import com.group06fall17.banksix.dao.UserDAO;
import com.group06fall17.banksix.exception.AuthorizationException;
import com.group06fall17.banksix.exception.IllegalTransactionException;
import com.group06fall17.banksix.model.ExternalUser;
import com.group06fall17.banksix.model.InternalUser;
import com.group06fall17.banksix.model.Task;
import com.group06fall17.banksix.model.Transaction;
import com.group06fall17.banksix.model.User;

/**
 * @author Abhilash
 *
 */
@Service
@Scope("session")
public class SystemManagerImpl implements SystemManagerService {
	@Autowired
	private TransactionDAO transactionDao;

	@Autowired
	private ExternalUserDAO externalUserDao;

	@Autowired
	private InternalUserDAO internalUserDao;
	
	@Autowired
	private UserDAO usersDao;
	
	@Autowired
	private TransactionManagerService transactionManagerService;

	@Autowired
	private TaskDAO taskDao;

	private InternalUser user;
	private List<Task> tasksAssigned;

	@Override
	public void setUser(String email) {
		if (this.user == null)
			this.user = internalUserDao.findUserByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Transaction> viewTransactions(String accountnumber) {
		if(user!= null && user.getAccessprivilege().equals("SM"))
			return transactionDao.findTransactionsOfAccount(accountnumber);
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Transaction viewTransaction(int transid) {
		if(user!= null && user.getAccessprivilege().equals("SM"))
			return transactionDao.findTransactionById(transid);
		return null;
	}

	@Override
	@Transactional
	public void authorizeTransaction(Transaction transaction) throws IllegalTransactionException, AuthorizationException {
		if(user!= null && user.getAccessprivilege().equals("SM"))
			transactionManagerService.performTransaction(transaction);
		else throw new AuthorizationException("Insufficient privileges to perform the action");
	}

	@Override
	@Transactional(readOnly = true)
	public ExternalUser viewExternalUserAcct(String email) {
		if(user!= null && user.getAccessprivilege().equals("SM")){
			return externalUserDao.findUserByEmail(email);
		}
		return null;
	}
	
	@Override
	@Transactional
	public void modifyExternalUserAcct(ExternalUser account) throws AuthorizationException {
		if(user!= null && user.getAccessprivilege().equals("SM")){
			externalUserDao.update(account);
		}
		else throw new AuthorizationException("Insufficient privileges to perform the action");
	}


	@Override
	@Transactional
	public void deleteExternalUserAcct(ExternalUser externalUser) throws AuthorizationException {
		if(user!= null && user.getAccessprivilege().equals("SM")){
			externalUserDao.delete(externalUser);
		}
		else throw new AuthorizationException("Insufficient privileges to perform the action");

	}
	
	@Override
	@Transactional
	public void requestPrivileges(String message) {
		Task task = new Task();

		task.setMessage(message);
		task.setTransid(null);
		task.setStatus("notcompleted");
		task.setTaskassignee_id(internalUserDao.findSysAdmin().getUsrid());
					
		taskDao.add(task);	
	}

	@Transactional
	public void completeTask(int task_id){
		Task task = taskDao.findTaskById(task_id);
		
		task.setStatus("completed");
		
		taskDao.update(task);
	}
	
	@Transactional(readOnly = true)
	public void updateTasks() {
		tasksAssigned = taskDao.findNewTasksAssignedToUser(user.getUsrid());
	}

	public List<Task> getTasks() {
		return tasksAssigned;
	}
	
	@Override
	public void updateInfo(InternalUser user) {
		internalUserDao.update(user);
	}
	
	@Override
	public void updatePasswd(User user) {
		usersDao.update(user);
	}

}
