/**
 * @author Abhilash
 *
 */
package com.group06fall17.banksix.service;

import com.group06fall17.banksix.model.Task;
import com.group06fall17.banksix.model.User;
import com.group06fall17.banksix.exception.IllegalTransactionException;
import com.group06fall17.banksix.model.Transaction;
import com.group06fall17.banksix.model.ExternalUser;
import com.group06fall17.banksix.exception.AuthorizationException;
import com.group06fall17.banksix.model.InternalUser;
import java.util.List;

public interface SysMngrService {
	public void upgradePasswd(User user);
	
	public void askPermission(String message);
	
	public void approveTransac(Transaction transaction) throws IllegalTransactionException, AuthorizationException;
	
	public void upgradeInfo(InternalUser user);
	
	public List<Transaction> showAllTransac(String accountnumber);
	
	public void changeExternalUsr(ExternalUser externalUser) throws AuthorizationException;
	
	public void finishTask(int task_id);
	
	public Transaction showTransac(int transid);

	public ExternalUser viewExternalUsr(String email);
		
	public void deleteExternalUsr(ExternalUser externalUser) throws AuthorizationException;

	public void upgradeTasks();
	
	public void setUsr(String email);
	
	public List<Task> obtainTasks();  
}