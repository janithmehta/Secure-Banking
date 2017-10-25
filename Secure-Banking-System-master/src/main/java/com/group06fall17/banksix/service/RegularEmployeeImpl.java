
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
public class RegularEmployeeImpl implements RegularEmployeeService {

	@Autowired
	private TransactionDAO transacDao;

	@Autowired
	private ExternalUserDAO extUsrDao;

	@Autowired
	private InternalUserDAO intUsrDao;
	
	@Autowired
	private UserDAO usrDAO;

	@Autowired
	private TaskDAO taskDao;
	
	@Autowired
	private TransacMngrService transacMngrService;

	private InternalUser user;
	private List<Task> tasksAllocated;

	@Override
	public void setUsr(String email) {
		if (this.user == null)
			this.user = intUsrDao.searchUsrByEmail(email);
	}

	@Override
	public void createTransaction(Transaction transaction) throws AuthorizationException, IllegalTransactionException {
		if(user!= null && (user.getAccessprivilege().equals("RE1")) || user.getAccessprivilege().equals("RE2"))
			transacMngrService.submitTransac(transaction);
		else throw new AuthorizationException("Insufficient privileges to perform the action");
	}

	@Override
	@Transactional(readOnly = true)
	public List<Transaction> showAllTransac(String accountnumber) {
		if(user!= null && (user.getAccessprivilege().equals("RE1")) || user.getAccessprivilege().equals("RE2"))
			return transacDao.findTransactionsOfAccount(accountnumber);
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Transaction showTransac(int transid) {
		if(user!= null && (user.getAccessprivilege().equals("RE1")) || user.getAccessprivilege().equals("RE2"))
			return transacDao.findTransactionById(transid);
		return null;
	}

	@Override
	@Transactional
	public void upgradeTransac(Transaction transaction) throws AuthorizationException {
		if(user!= null && user.getAccessprivilege().equals("RE2"))
			transacMngrService.upgradeTransac(transaction);
		else throw new AuthorizationException("Insufficient privileges to perform the action");
	}

	@Override
	@Transactional
	public void dropTransac(Transaction transaction) throws AuthorizationException, IllegalTransactionException {
		if(user!= null && user.getAccessprivilege().equals("RE2")){
			Task task = taskDao.findNewTaskByTID(transaction.getTransid());
			
			if(task == null)
				throw new IllegalTransactionException("Cannot cancel approved transactions");
			taskDao.delete(task);
			
			transacMngrService.dropTransac(transaction);
		}
		else throw new AuthorizationException("Insufficient privileges to perform the action");
	}

	@Override
	@Transactional
	public void approveTransac(Transaction transaction) throws IllegalTransactionException, AuthorizationException {
		if(user!= null && (user.getAccessprivilege().equals("RE1")) || user.getAccessprivilege().equals("RE2")){
			String status = transaction.getTransStatus();
			if(status.equals("pending") )
				transacMngrService.executeTransac(transaction);
		}
		else throw new AuthorizationException("Insufficient privileges to perform the action");
	}

	@Override
	@Transactional(readOnly = true)
	public ExternalUser viewExternalUsr(String email) {
		if(user!= null && (user.getAccessprivilege().equals("RE1")) || user.getAccessprivilege().equals("RE2")){
			return extUsrDao.searchUsrByEmail(email);
		}
		return null;
	}
	
	@Override
	@Transactional
	public void changeExternalUsr(ExternalUser account) throws AuthorizationException {
		if(user!= null && (user.getAccessprivilege().equals("RE1")) || user.getAccessprivilege().equals("RE2")){
			extUsrDao.update(account);
		}
		else throw new AuthorizationException("Insufficient privileges to perform the action");
	}

	@Override
	@Transactional
	public void askPermission(String message) {
		Task task = new Task();

		task.setMessage(message);
		task.setTransid(null);
		task.setStatus("notcompleted");
		task.setTaskassignee_id(intUsrDao.findSysAdmin().getUsrid());
					
		taskDao.add(task);	
	}

	@Transactional
	public void finishTask(int task_id){
		Task task = taskDao.findTaskById(task_id);
		
		task.setStatus("completed");
		
		taskDao.update(task);
	}
	
	@Transactional(readOnly = true)
	public void upgradeTasks() {
		tasksAllocated = taskDao.findNewTasksAssignedToUser(user.getUsrid());
	}

	public List<Task> obtainTasks() {
		return tasksAllocated;
	}

	@Override
	public void upgradeInfo(InternalUser user) {
		intUsrDao.update(user);
	}
	
	@Override
	public void upgradePasswd(User user) {
		usrDAO.update(user);
	}

}
