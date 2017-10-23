package com.group06fall17.banksix.dao;

import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.group06fall17.banksix.interceptor.ILogs;
import com.group06fall17.banksix.model.ExternalUser;
import com.group06fall17.banksix.model.GovAgency;
import com.group06fall17.banksix.model.Logs;
import com.group06fall17.banksix.model.PII;;


@Repository
public class PIIDAOImpl implements PIIDAO{
	private SessionFactory sessionFactory;
	
	@Autowired
	LogsDAO logsDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}
	
	@Override
	@Transactional
	public void add(PII pii) {
		sessionFactory.getCurrentSession().save(pii);
		logIt("add - ", pii);
	}

	@Override
	@Transactional
	public void update(PII pii) {
		logIt("update - ", pii);
		sessionFactory.getCurrentSession().merge(pii);
	}

	@Override
	@Transactional
	public void persist(PII pii) {
		logIt("persist - ", pii);
		sessionFactory.getCurrentSession().persist(pii);
	}

	@Override
	@Transactional
	public void delete(PII pii) {
		logIt("delete - ", pii);
		Query query = sessionFactory.getCurrentSession().createQuery("delete PII where ssn = :ID");
		query.setParameter("ID", pii.getSsn());
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = true)
	public PII findBySSN(ExternalUser externaluser) {
		return (PII) sessionFactory.getCurrentSession().get(PII.class, externaluser.getSsn());	
	}
	
	@Override
	@Transactional(readOnly = true)
	public PII findBySSN(String ssn1) {
		Session session = this.sessionFactory.getCurrentSession();
		PII pii = (PII) session.createQuery("from PII where ssn = :ssn1")
				.setString("ssn1", ssn1)
				.uniqueResult();
		return pii;
	}
	
	public void logIt(String action, ILogs  ilogs){
		Logs logs = new Logs();
		Date dateobj = new Date();
		logs.setLogentrydate(dateobj);
		logs.setLoginfo(action + ilogs.getLogDetail());
		
		logsDao.add(logs);
	}

}
