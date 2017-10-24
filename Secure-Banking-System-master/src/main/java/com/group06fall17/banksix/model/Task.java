/**
 * @author Saurabh
 *
 */
package com.group06fall17.banksix.model;

import javax.persistence.Table;
import javax.persistence.GenerationType;
import java.beans.Transient;
import org.hibernate.annotations.SelectBeforeUpdate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import com.group06fall17.banksix.interceptor.ILogs;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.OneToOne;

@Entity
@Table(name = "task")
@DynamicUpdate
@SelectBeforeUpdate 
public class Task implements ILogs{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_id", nullable = false)
	private int task_id;

	/**
	 * @return the task_id
	 */
	public int getTask_id() {
		return task_id;
	}

	/**
	 * @param task_id the task_id to set
	 */
	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

	
	@Column(name = "message", nullable = false)
	private String message;
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "status")
	private String status;
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	@Transient
	@Override
	public Long getId() {
		return Long.valueOf(this.task_id);
	}
	
	@OneToOne
	@JoinColumn(name = "transid")
	private Transaction transid;
	

	/**
	 * @return the transid
	 */
	public Transaction getTransid() {
		return transid;
	}

	/**
	 * @param transid the transid to set
	 */
	public void setTransid(Transaction transid) {
		this.transid = transid;
	}


	@Column(name = "taskassignee_id")
	private int taskassignee_id;

	/**
	 * @return the taskassignee_id
	 */
	public int getTaskassignee_id() {
		return taskassignee_id;
	}

	/**
	 * @param taskassignee_id the taskassignee_id to set
	 */
	public void setTaskassignee_id(int taskassignee_id) {
		this.taskassignee_id = taskassignee_id;
	}
	
	@Transient
	@Override
	public String getLogDetail() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" task ").append(" task_id :" ).append(task_id)
		.append(" message : ").append(message)
		.append(" status : ").append(status)
		.append(" transid : ").append(transid.getTransid())
		.append(" taskassignee_id : ").append(taskassignee_id);

		return sb.toString();
	}
}
