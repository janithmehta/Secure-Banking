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
import com.group06fall17.banksix.model.Pii;;


@Repository
public class PiiDAOImpl implements PiiDAO{
	private SessionFactory sessionFactory;
	
	@Autowired
	LogsDAO logsDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}
	
	@Override
	@Transactional
	public void add(Pii pii) {
		sessionFactory.getCurrentSession().save(pii);
		logIt("add - ", pii);
	}

	@Override
	@Transactional
	public void update(Pii pii) {
		logIt("update - ", pii);
		sessionFactory.getCurrentSession().merge(pii);
	}

	@Override
	@Transactional
	public void persist(Pii pii) {
		logIt("persist - ", pii);
		sessionFactory.getCurrentSession().persist(pii);
	}

	@Override
	@Transactional
	public void delete(Pii pii) {
		logIt("delete - ", pii);
		Query query = sessionFactory.getCurrentSession().createQuery("delete Pii where ssn = :ID");
		query.setParameter("ID", pii.getSsn());
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = true)
	public Pii findBySSN(ExternalUser externaluser) {
		return (Pii) sessionFactory.getCurrentSession().get(Pii.class, externaluser.getSsn());	
	}
	
	@Override
	@Transactional(readOnly = true)
	public Pii findBySSN(String ssn1) {
		Session session = this.sessionFactory.getCurrentSession();
		Pii pii = (Pii) session.createQuery("from Pii where ssn = :ssn1")
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
