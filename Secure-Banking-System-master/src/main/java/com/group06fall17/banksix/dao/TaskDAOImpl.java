package com.group06fall17.banksix.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.group06fall17.banksix.interceptor.ILogs;
import com.group06fall17.banksix.model.Logs;
import com.group06fall17.banksix.model.Task;
import com.group06fall17.banksix.model.Transaction;

@Repository
public class TaskDAOImpl implements TaskDAO {
	private SessionFactory sessionFactory;

	@Autowired
	LogsDAO logsDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	@Override
	@Transactional
	public void add(Task task) {
		sessionFactory.getCurrentSession().save(task);
		logIt("add - ", task);
	}

	@Override
	@Transactional
	public void update(Task task) {
		sessionFactory.getCurrentSession().merge(task);
		logIt("update - ", task);
	}

	@Override
	@Transactional
	public void persist(Task task) {
		sessionFactory.getCurrentSession().persist(task);
		logIt("persist - ", task);
	}
	
	@Override
	@Transactional
	public void delete(Task task) {
		logIt("delete   -", task);
		Query query = sessionFactory.getCurrentSession().createQuery("delete Task where task_id = :ID");
		query.setParameter("ID", task.getTask_id());
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = true)
	public Task findTaskById(int task_id) {
		Task task = (Task) sessionFactory.getCurrentSession()
				.createQuery("from Task where task_id = " + task_id + " and status = 'notcompleted'").uniqueResult();
		return task;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Task> findNewTasksAssignedToUser(int id) {
		List<Task> list = sessionFactory.getCurrentSession()
				.createQuery("from Task where taskassignee_id = " + id + " and status = 'notcompleted'").list();
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public Task findNewTaskByTID(int transid) {
		Task task = (Task) sessionFactory.getCurrentSession()
				.createQuery("from Task where transid = " + transid + " and status = 'notcompleted'").uniqueResult();
		return task;
	}

	public void logIt(String action, ILogs  ilogs){
		Logs logs = new Logs();
		Date dateobj = new Date();
		logs.setLogentrydate(dateobj);
		logs.setLoginfo(action + ilogs.getLogDetail());
		
		logsDao.add(logs);
	}
}
