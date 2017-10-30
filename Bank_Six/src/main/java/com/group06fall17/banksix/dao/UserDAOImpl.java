package com.group06fall17.banksix.dao;

import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.group06fall17.banksix.interceptor.ILogs;
import com.group06fall17.banksix.model.Logs;
import com.group06fall17.banksix.model.User;

@Repository
public class UserDAOImpl implements UserDAO{	
	private SessionFactory sessionFactory;     
	
	@Autowired
	private LogsDAO logsDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	@Override
	@Transactional
	public void add(User user) {
		Session session = this.sessionFactory.getCurrentSession();
        session.save(user);
        logIt("add - ", user);
	}

	@Override
	@Transactional
	public void update(User user) {
		Session session = this.sessionFactory.getCurrentSession();
        session.update(user);
        logIt("update - ", user);
	}
	

	@Override
	@Transactional
	public void delete(User user) {
		logIt("delete - ", user);
		Query query = sessionFactory.getCurrentSession().createQuery("delete User where username = :ID");
		query.setParameter("ID", user.getUsername());
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = true)
	public User findUsersByEmail(String email) {
		Session session = this.sessionFactory.getCurrentSession();      
		User user = (User) session.createQuery("from User where username = :user")
				.setString("user", email)
				.uniqueResult();
        return user;
	}

	@Override
	@Transactional
	public void persist(User users) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(users);
	}
	
	public void logIt(String action, ILogs  ilogs){
		Logs logs = new Logs();
		Date dateobj = new Date();
		logs.setLogentrydate(dateobj);
		logs.setLoginfo(action + ilogs.getLogDetail());
		
		logsDao.add(logs);
	}
}
