
package com.group06fall17.banksix.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group06fall17.banksix.dao.InternalUserDAO;
import com.group06fall17.banksix.dao.LogsDAO;
import com.group06fall17.banksix.dao.TaskDAO;
import com.group06fall17.banksix.dao.UserDAO;
import com.group06fall17.banksix.exception.AuthorizationException;
import com.group06fall17.banksix.model.InternalUser;
import com.group06fall17.banksix.model.Logs;
import com.group06fall17.banksix.model.Task;
import com.group06fall17.banksix.model.User;

/**
 * @author Abhilash
 *
 */
@Service
@Scope("session")
public class SystemAdministratorImpl implements SystemAdministratorService {

	@Autowired
	private InternalUserDAO intUsrDao;

	@Autowired
	private TaskDAO taskDao;
	
	@Autowired
	private LogsDAO logsDao;

	private InternalUser user;
	private List<Task> tasksAllocated;
	
	@Autowired
	private UserDAO usrDAO;
	
	@Override
	public void setUsr(String email) {
		if (this.user == null)
			this.user = intUsrDao.findUserByEmail(email);
	}
	
	@Override
	public void addInternalUserAccount(InternalUser internalUser) throws AuthorizationException {
		if(user!= null && user.getAccessprivilege().equals("SA")){
			intUsrDao.add(internalUser);
		}
		else throw new AuthorizationException("Insufficient privileges to perform the action");

	}

	@Override
	public void modifyInternalUserAccount(InternalUser internalUser) throws AuthorizationException {
		if(user!= null && user.getAccessprivilege().equals("SA")){
			intUsrDao.update(internalUser);
		}
		else throw new AuthorizationException("Insufficient privileges to perform the action");
	}

	@Override
	public void deleteInternalUserAccount(InternalUser internalUser) throws AuthorizationException {
		if(user!= null && user.getAccessprivilege().equals("SA")){
			intUsrDao.update(internalUser);
		}
		else throw new AuthorizationException("Insufficient privileges to perform the action");

	}

	@Override
	public List<Logs> viewSystemLogs() {
		return logsDao.findLogs();
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

