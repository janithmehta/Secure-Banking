/**
 * @author Abhilash
 */
package com.group06fall17.banksix.service;

import com.group06fall17.banksix.model.InternalUser;
import org.springframework.beans.factory.annotation.Autowired;
import com.group06fall17.banksix.model.User;
import org.springframework.stereotype.Service;
import java.util.List;
import com.group06fall17.banksix.dao.UserDAO;
import com.group06fall17.banksix.dao.InternalUserDAO;
import com.group06fall17.banksix.dao.LogsDAO;
import org.springframework.transaction.annotation.Transactional;
import com.group06fall17.banksix.dao.TaskDAO;
import com.group06fall17.banksix.exception.AuthorizationException;
import com.group06fall17.banksix.model.Logs;
import org.springframework.context.annotation.Scope;
import com.group06fall17.banksix.model.Task;

@Service
@Scope("session")
public class SysAdminImplementation implements SysAdminService {

	private InternalUser user;
	private List<Task> tasksAllocated;
	
	@Autowired
	private TaskDAO taskDao;
	
	@Autowired
	private InternalUserDAO intUsrDao;

	@Autowired
	private UserDAO usrDAO;
	
	@Autowired
	private LogsDAO logsDao;

	@Override
	public void addIntUsrAccnt(InternalUser intUsr) throws AuthorizationException {
		if(user!= null && user.getAccessprivilege().equals("SA"))
		{
			intUsrDao.add(intUsr);
		}
		else throw new AuthorizationException("Privileges are insufficient to perform the desired action");
	}
	
	
	@Override
	public void setUsr(String email) {
		if (this.user == null)
			this.user = intUsrDao.searchUsrByEmail(email);
	}
	
	@Override
	public void removeIntUsrAccnt(InternalUser intUsr) throws AuthorizationException {
		if(user!= null && user.getAccessprivilege().equals("SA"))
		{
			intUsrDao.update(intUsr);
		}
		else throw new AuthorizationException("Privileges are insufficient to perform the desired action");
	}
	
	@Override
	public void changeIntUsrAccnt(InternalUser intUsr) throws AuthorizationException {
		if(user!= null && user.getAccessprivilege().equals("SA"))
		{
			intUsrDao.update(intUsr);
		}
		else throw new AuthorizationException("Privileges are insufficient to perform the desired action");
	}

	@Override
	public void upgradeInfo(InternalUser user) {
		intUsrDao.update(user);
	}
	
	@Override
	public List<Logs> chkSysLogs() {
		return logsDao.findLogs();
	}
	
	@Transactional(readOnly = true)
	public void upgradeTasks() {
		tasksAllocated = taskDao.findNewTasksAssignedToUser(user.getUsrid());
	}
	
	@Override
	public void upgradePasswd(User user) {
		usrDAO.update(user);
	}
	
	@Transactional
	public void finishTask(int task_id){
		Task task = taskDao.findTaskById(task_id);
		task.setStatus("completed");
		taskDao.update(task);
	}
	
	public List<Task> obtainTasks() {
		return tasksAllocated;
	}

}