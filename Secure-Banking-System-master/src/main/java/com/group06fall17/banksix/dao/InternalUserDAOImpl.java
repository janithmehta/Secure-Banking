package com.group06fall17.banksix.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.Date;
import java.util.List;
import com.group06fall17.banksix.interceptor.ILogs;
import com.group06fall17.banksix.model.InternalUser;
import com.group06fall17.banksix.model.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class InternalUserDAOImpl implements InternalUserDAO {
	private SessionFactory sessnFactry;
	
	@Autowired
	private LogsDAO logsDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sessnFactry = sf;
	}
	
	
	@Override
	@Transactional
	public void update(InternalUser intusr) {
		this.sessnFactry.getCurrentSession().merge(intusr);
		logIt("update - ",intusr);
	}

	@Override
	@Transactional
	public void add(InternalUser intusr) {
		this.sessnFactry.getCurrentSession().save(intusr);
		logIt("add - ",intusr);
	}

	
	@Override
	@Transactional
	public void delete(InternalUser intusr) {
		logIt("delete - ",intusr);
		Query qry = sessnFactry.getCurrentSession().createQuery("delete InternalUser where usrid = :ID");
		qry.setParameter("ID", intusr.getUsrid());
		qry.executeUpdate();		
	}
	@Override
	@Transactional
	public void persist(InternalUser intusr) {
		this.sessnFactry.getCurrentSession().persist(intusr);
		logIt("persist - ",intusr);
	}

	@Override
	@Transactional(readOnly = true)
	public InternalUser searchUsrByEmail(String email) {
		Session sessn = this.sessnFactry.getCurrentSession();
		InternalUser intUser = (InternalUser) sessn.createQuery("from InternalUser where email.username = :email")
				.setString("email", email)
				.uniqueResult();
		return intUser;
	}

	@Override
	@Transactional(readOnly = true)
	public List<InternalUser> findAllRegEmployees() {
		Session sessn = this.sessnFactry.getCurrentSession();		
			List<InternalUser> usrsList = sessn.createQuery("from InternalUser where accessprivilege = 'RE1' or accessprivilege = 'RE2'").list();
		return usrsList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<InternalUser> findAllSystemManagers() {
		Session sessn = this.sessnFactry.getCurrentSession();		
		List<InternalUser> usrsList = sessn.createQuery("from InternalUser where accessprivilege = 'SM'").list();
		return usrsList;
	}

	@Override
	@Transactional(readOnly = true)
	public InternalUser findSysAdmin() {
		Session sessn = this.sessnFactry.getCurrentSession();		
		InternalUser iUsr = (InternalUser) sessn.createQuery("from InternalUser where accessprivilege = 'SA'").uniqueResult();
		return iUsr;
	}

	@Override
	@Transactional(readOnly = true)
	public InternalUser findUserById(int id) {
		Session sessn = this.sessnFactry.getCurrentSession();      
		InternalUser iUsr = (InternalUser) sessn.createQuery("from InternalUser where usrid = :id")
				.setInteger("id", id)
				.uniqueResult();
		return iUsr;
	}
	
	public void logIt(String actn, ILogs  ilogs){
		Logs logs = new Logs();
		Date dateobject = new Date();
		logs.setLogentrydate(dateobject);
		logs.setLoginfo(actn + ilogs.getLogDetail());
		
		logsDao.add(logs);
	}

}
