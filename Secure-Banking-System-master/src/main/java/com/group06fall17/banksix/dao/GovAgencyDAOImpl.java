package com.group06fall17.banksix.dao;

import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.group06fall17.banksix.interceptor.ILogs;
import com.group06fall17.banksix.model.GovAgency;
import com.group06fall17.banksix.model.Logs;

@Repository
public class GovAgencyDAOImpl implements GovAgencyDAO {
	
	private SessionFactory sessionFactory;
	
	@Autowired
	private LogsDAO logsDao;

	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}
	
	@Override
	@Transactional
	public void add(GovAgency fedofficers) {
		this.sessionFactory.getCurrentSession().save(fedofficers);
		logIt("add - ", fedofficers);
	}

	@Override
	@Transactional
	public void update(GovAgency fedofficers) {
		this.sessionFactory.getCurrentSession().merge(fedofficers);
		logIt(" update - ",fedofficers);
	}

	@Override
	@Transactional
	public void persist(GovAgency fedofficers) {
		this.sessionFactory.getCurrentSession().persist(fedofficers);
		logIt("persist - ",fedofficers);
	}

	@Override
	@Transactional
	public void delete(GovAgency fedofficers) {
		logIt("delete - ",fedofficers);
		Query query = sessionFactory.getCurrentSession().createQuery("delete GovAgency where username = :ID");
		query.setParameter("ID", fedofficers.getUsername());
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = true)
	public GovAgency findByUsername(String username) {
		Session session = this.sessionFactory.getCurrentSession();
		GovAgency gov = (GovAgency) session.createQuery("from GovAgency where username = :user")
				.setString("user", username)
				.uniqueResult();
		return gov;
	}
	
	public void logIt(String action, ILogs  ilogs){
		Logs logs = new Logs();
		Date dateobj = new Date();
		logs.setLogentrydate(dateobj);
		logs.setLoginfo(action + ilogs.getLogDetail());
		
		logsDao.add(logs);
	}
	
}
