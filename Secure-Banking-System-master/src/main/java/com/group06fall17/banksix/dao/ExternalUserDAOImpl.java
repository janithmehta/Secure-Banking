package com.group06fall17.banksix.dao;

import java.util.Date;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.group06fall17.banksix.interceptor.ILogs;
import com.group06fall17.banksix.model.ExternalUser;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.group06fall17.banksix.model.Logs;

/**
 * @author Saurabh
 *
 */

@Repository
public class ExternalUserDAOImpl implements ExternalUserDAO {
	private SessionFactory sessnFactry;
	@Autowired
	private LogsDAO logsDao;
	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sessnFactry = sf;
	}
	@Override
	@Transactional
	public void persistextusr(ExternalUser extusr) {
		sessnFactry.getCurrentSession().persist(extusr);
		logIt("persist - ", extusr);
	}
	@Override
	@Transactional
	public void updateextusr(ExternalUser extusr) {
		sessnFactry.getCurrentSession().merge(extusr);
		logIt("update - ", extusr);
	}
	@Override
	@Transactional
	public void deleteextusr(ExternalUser extusr) {
		logIt("delete - ", extusr);
		Query query = sessnFactry.getCurrentSession().createQuery("delete ExternalUser where usrid = :ID");
		query.setParameter("ID", extusr.getUsrid());
		query.executeUpdate();
	}
	@Override
	@Transactional
	public void addextuser(ExternalUser extusr) {
		sessnFactry.getCurrentSession().save(extusr);
		logIt("add - ", extusr);
	}
	@Override
	@Transactional(readOnly = true)
	public ExternalUser srchUsrusingEmail(String email) {
		Session session = this.sessnFactry.getCurrentSession();
		ExternalUser user = (ExternalUser) session.createQuery("from ExternalUser where email.username = :email")
				.setString("email", email).uniqueResult();
		return user;
	}
	@Override
	@Transactional(readOnly = true)
	public ExternalUser searchUserusingUsrId(int id) {
		Session session = this.sessnFactry.getCurrentSession();
		ExternalUser user = (ExternalUser) session.createQuery("from ExternalUser where usrid = :id")
				.setInteger("id", id).uniqueResult();
		return user;
	}
	@Override
	@Transactional(readOnly = true)
	public List<ExternalUser> searhUserusngUserType(String userType) {
		Session session = this.sessnFactry.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<ExternalUser> user = session.createQuery("from ExternalUser where userType = :userType")
				.setString("userType", userType).list();

		return user;
	}
	@Override
	@Transactional(readOnly = true)
	public ExternalUser searchUserusngBname(String orgName) {
		Session sessn = this.sessnFactry.getCurrentSession();
		ExternalUser usr = (ExternalUser) sessn.createQuery("from ExternalUser where organisationName = :organisationName")
				.setString("organisationName", orgName).uniqueResult();
		return usr;
	}
	public void logIt(String actn, ILogs ilogs) {
		Logs logs = new Logs();
		Date dateobject = new Date();
		logs.setLogentrydate(dateobject);
		logs.setLoginfo(actn + ilogs.getLogDetail());

		logsDao.add(logs);
	}
	@Override
	@Transactional(readOnly = true)
	public ExternalUser srchUserUsngSSN(String ssn) {
		Session sessn = this.sessnFactry.getCurrentSession();
		ExternalUser usr = (ExternalUser) sessn.createQuery("from ExternalUser where ssn = :ssn")
				.setString("ssn", ssn).uniqueResult();
		return usr;		
	}
}
