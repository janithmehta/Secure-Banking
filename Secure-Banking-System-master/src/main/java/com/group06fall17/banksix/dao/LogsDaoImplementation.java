//@author Sirish

package com.group06fall17.banksix.dao;

import com.group06fall17.banksix.model.Logs;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class LogsDaoImplementation implements LogsDAO {
	
	@Autowired
	LogsDAO logsDao;

	private SessionFactory sessFact;
	
	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sessFact = sf;
	}

	@Override
	@Transactional
	public void add(Logs logs) {
		sessFact.getCurrentSession().save(logs);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Logs> findLogs() {
		List<Logs> var_list = sessFact.getCurrentSession().createQuery("from Logs").list();
		return var_list;
	}
}
