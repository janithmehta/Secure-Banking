
package com.group06fall17.banksix.service;

import java.util.Date;
import java.util.List;

import com.group06fall17.banksix.exception.AuthorizationException;
import com.group06fall17.banksix.model.InternalUser;
import com.group06fall17.banksix.model.Logs;
import com.group06fall17.banksix.model.Task;
import com.group06fall17.banksix.model.User;

/**
 * @author Abhilash
 *
 */
public interface SystemAdministratorService {
	public void addInternalUserAccount(InternalUser internalUser) throws AuthorizationException;

	public void modifyInternalUserAccount(InternalUser internalUser) throws AuthorizationException;

	public void deleteInternalUserAccount(InternalUser internalUser) throws AuthorizationException;

	public List<Logs> viewSystemLogs();
	
	public void setUsr(String email);

	public void finishTask(int task_id);
	
	public void upgradeInfo(InternalUser user);
	
	public void upgradeTasks();
	
	public List<Task> obtainTasks();  
	
	public void upgradePasswd(User user);

}
