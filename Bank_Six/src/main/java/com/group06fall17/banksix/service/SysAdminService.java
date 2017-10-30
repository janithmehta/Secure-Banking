/**
 * @author Abhilash
 */
package com.group06fall17.banksix.service;

import com.group06fall17.banksix.model.Logs;
import com.group06fall17.banksix.model.InternalUser;
import java.util.List;
import com.group06fall17.banksix.model.User;
import com.group06fall17.banksix.exception.AuthorizationException;
import java.util.Date;
import com.group06fall17.banksix.model.Task;

public interface SysAdminService {
	public List<Task> obtainTasks();  
	
	public void removeIntUsrAccnt(InternalUser intUsr) throws AuthorizationException;
	
	public void finishTask(int task_id);
	
	public void upgradePasswd(User user);
	
	public void addIntUsrAccnt(InternalUser intUsr) throws AuthorizationException;
	
	public void upgradeInfo(InternalUser user);

	public void changeIntUsrAccnt(InternalUser intUsr) throws AuthorizationException;

	public List<Logs> chkSysLogs();
	
	public void upgradeTasks();
	
	public void setUsr(String email);
}
