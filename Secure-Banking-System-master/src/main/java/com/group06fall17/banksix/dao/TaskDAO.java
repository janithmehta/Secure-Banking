/**
 * 
 */
package com.group06fall17.banksix.dao;

import java.util.List;

import com.group06fall17.banksix.model.Task;

/**
 * @author Abhilash
 *
 */
public interface TaskDAO {
	public void add(Task task);

	public void update(Task task);

	public void persist(Task task);

	public void delete(Task task);

	public Task findTaskById(int taskid);
	
	public List<Task> findNewTasksAssignedToUser(int usrid);
	
	public Task findNewTaskByTID(int transid);
}
