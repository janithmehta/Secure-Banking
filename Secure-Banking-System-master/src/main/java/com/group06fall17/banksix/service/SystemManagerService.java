
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
public interface SystemManagerService {
	public List<Transaction> viewTransactions(String accountnumber);
	
	public Transaction viewTransaction(int transid);

	public void authorizeTransaction(Transaction transaction) throws IllegalTransactionException, AuthorizationException;

	public ExternalUser viewExternalUserAcct(String email);
	
	public void modifyExternalUserAcct(ExternalUser externalUser) throws AuthorizationException;
	
	public void deleteExternalUserAcct(ExternalUser externalUser) throws AuthorizationException;

	public void requestPrivileges(String message);
	
	public void setUser(String email);

	public void completeTask(int task_id);
	
	public void updateInfo(InternalUser user);
	
	public void updateTasks();
	
	public List<Task> getTasks();  
	
	public void updatePasswd(User user);
}

