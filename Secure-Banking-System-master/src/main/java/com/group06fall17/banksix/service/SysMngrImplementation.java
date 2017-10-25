/**
 * @author Abhilash
 *
 */
package com.group06fall17.banksix.service;

import com.group06fall17.banksix.model.ExternalUser;
import com.group06fall17.banksix.dao.InternalUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.group06fall17.banksix.dao.ExternalUserDAO;
import org.springframework.context.annotation.Scope;
import com.group06fall17.banksix.dao.TaskDAO;
import com.group06fall17.banksix.dao.TransactionDAO;
import com.group06fall17.banksix.dao.UserDAO;
import com.group06fall17.banksix.exception.AuthorizationException;
import java.util.List;
import com.group06fall17.banksix.model.InternalUser;
import com.group06fall17.banksix.model.Task;
import org.springframework.transaction.annotation.Transactional;
import com.group06fall17.banksix.model.Transaction;
import com.group06fall17.banksix.model.User;
import com.group06fall17.banksix.exception.IllegalTransactionException;

@Service
@Scope("session")
public class SysMngrImplementation implements SysMngrService {
	private InternalUser user;
	
	@Autowired
	private UserDAO usrDAO;
	
	@Autowired
	private TransactionDAO transacDao;

	@Autowired
	private ExternalUserDAO extUsrDao;
	
	@Autowired
	private TaskDAO taskDao;

	@Autowired
	private InternalUserDAO intUsrDao;
	
	@Autowired
	private TransacMngrService transacMngrService;

	private List<Task> tasksAllocated;

	@Override
	@Transactional(readOnly = true)
	public List<Transaction> showAllTransac(String accountnumber) {
		if(user!= null && user.getAccessprivilege().equals("SM"))
			return transacDao.findTransactionsOfAccount(accountnumber);
		return null;
	}
	
	@Override
	public void setUsr(String email) {
		if (this.user == null)
			this.user = intUsrDao.searchUsrByEmail(email);
	}

	
	@Override
	@Transactional(readOnly = true)
	public Transaction showTransac(int transid) {
		if(user!= null && user.getAccessprivilege().equals("SM"))
			return transacDao.findTransactionById(transid);
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public ExternalUser viewExternalUsr(String email) {
		if(user!= null && user.getAccessprivilege().equals("SM")){
			return extUsrDao.searchUsrByEmail(email);
		}
		return null;
	}
	
	@Override
	@Transactional
	public void approveTransac(Transaction transaction) throws IllegalTransactionException, AuthorizationException {
		if(user!= null && user.getAccessprivilege().equals("SM"))
			transacMngrService.executeTransac(transaction);
		else throw new AuthorizationException("Insufficient privileges to perform the action");
	}

	
	
	@Override
	@Transactional
	public void changeExternalUsr(ExternalUser account) throws AuthorizationException {
		if(user!= null && user.getAccessprivilege().equals("SM")){
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
	
	@Override
	@Transactional
	public void deleteExternalUsr(ExternalUser externalUser) throws AuthorizationException {
		if(user!= null && user.getAccessprivilege().equals("SM")){
			extUsrDao.delete(externalUser);
		}
		else throw new AuthorizationException("Insufficient privileges to perform the action");

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
