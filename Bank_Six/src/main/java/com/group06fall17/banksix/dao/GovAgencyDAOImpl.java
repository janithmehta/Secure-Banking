package com.group06fall17.banksix.dao;
import java.util.Date;
import com.group06fall17.banksix.interceptor.ILogs;
import com.group06fall17.banksix.model.GovAgency;
import com.group06fall17.banksix.model.Logs;
import org.springframework.stereotype.Repository;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
@Repository
public class GovAgencyDAOImpl implements GovAgencyDAO {
	private SessionFactory sessnFactry;
	@Autowired
	private LogsDAO logsDao;
	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sessnFactry = sf;
	}
	@Override
	@Transactional
	public void persistGovAgncy(GovAgency fedOfcrs) {
		this.sessnFactry.getCurrentSession().persist(fedOfcrs);
		logIt("persist - ",fedOfcrs);
	}
	@Override
	@Transactional
	public void deleteGovAgncy(GovAgency fedOfcrs) {
		logIt("delete - ",fedOfcrs);
		Query query = sessnFactry.getCurrentSession().createQuery("delete GovAgency where username = :ID");
		query.setParameter("ID", fedOfcrs.getUsername());
		query.executeUpdate();
	}
	@Override
	@Transactional
	public void updateGovAgncy(GovAgency fedOfcrs) {
		this.sessnFactry.getCurrentSession().merge(fedOfcrs);
		logIt(" update - ",fedOfcrs);
	}
	@Override
	@Transactional
	public void addGovAgncy(GovAgency fedOfcrs) {
		this.sessnFactry.getCurrentSession().save(fedOfcrs);
		logIt("add - ", fedOfcrs);
	}
	@Override
	@Transactional(readOnly = true)
	public GovAgency findByUsername(String usrname) {
		Session sessn = this.sessnFactry.getCurrentSession();
		GovAgency govAgncy = (GovAgency) sessn.createQuery("from GovAgency where username = :user")
				.setString("user", usrname)
				.uniqueResult();
		return govAgncy;
	}
	public void logIt(String actn, ILogs  logi){
		Logs log = new Logs();
		Date dateobject = new Date();
		log.setLogentrydate(dateobject);
		log.setLoginfo(actn + logi.getLogDetail());
		logsDao.add(log);
	}
}