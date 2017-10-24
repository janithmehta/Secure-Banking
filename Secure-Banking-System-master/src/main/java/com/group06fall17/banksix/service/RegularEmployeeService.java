
package com.group06fall17.banksix.service;

import java.util.List;

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

public interface RegularEmployeeService {
	public void createTransaction(Transaction transaction) throws AuthorizationException, IllegalTransactionException;

	public List<Transaction> showAllTransac(String accountnumber);
	
	public Transaction showTransac(int transid);

	public void updateTransaction(Transaction transaction) throws AuthorizationException;

	public void cancelTransaction(Transaction transaction) throws AuthorizationException, IllegalTransactionException;

	public void approveTransac(Transaction transaction) throws IllegalTransactionException, AuthorizationException;

	public ExternalUser viewExternalUsr(String email);

	public void changeExternalUsr(ExternalUser account) throws AuthorizationException;

	public void askPermission(String message);
	
	public void setUsr(String username);
	
	public void upgradeInfo(InternalUser user);
	
	public void finishTask(int task_id);
	
	public void upgradeTasks();
	
	public List<Task> obtainTasks();  
	
	public void upgradePasswd(User user);
	
}
