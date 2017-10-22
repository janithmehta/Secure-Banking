
package com.group06fall17.banksix.service;

import java.util.Date;
import java.util.List;

import com.group06fall17.banksix.exception.AuthorizationException;
import com.group06fall17.banksix.model.InternalUser;
import com.group06fall17.banksix.model.Logs;
import com.group06fall17.banksix.model.Task;
import com.group06fall17.banksix.model.Users;

/**
 * @author Abhilash
 *
 */
public interface SystemAdministratorService {
	public void addInternalUserAccount(InternalUser internalUser) throws AuthorizationException;

	public void modifyInternalUserAccount(InternalUser internalUser) throws AuthorizationException;

	public void deleteInternalUserAccount(InternalUser internalUser) throws AuthorizationException;

	public List<Logs> viewSystemLogs();
	
	public void setUser(String email);

	public void completeTask(int taskid);
	
	public void updateInfo(InternalUser user);
	
	public void updateTasks();
	
	public List<Task> getTasks();  
	
	public void updatePasswd(Users user);

}
