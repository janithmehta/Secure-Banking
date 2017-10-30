package com.group06fall17.banksix.dao;

import java.util.Date;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.Query;
import com.group06fall17.banksix.model.Logs;
import org.springframework.stereotype.Repository;
import com.group06fall17.banksix.interceptor.ILogs;
import com.group06fall17.banksix.model.BankAccount;
@Repository
public class BankAccountDAOImpl implements BankAccountDAO {
	private SessionFactory sessnFactry;
	@Autowired
	private LogsDAO logsDao;
	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sessnFactry = sf;
	}

	

	@Override
	@Transactional
	public void persistacct(BankAccount bankacct) {
		sessnFactry.getCurrentSession().persist(bankacct);
		logIt("persist - ", bankacct);
	}
	
	@Override
	@Transactional
	public void addacct(BankAccount bankacct) {
		sessnFactry.getCurrentSession().save(bankacct);
		logIt("add - ", bankacct);
	}

	@Override
	@Transactional
	public void updateacct(BankAccount bankacct) {
		sessnFactry.getCurrentSession().merge(bankacct);
		logIt("update - ", bankacct);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<BankAccount> findAccountsOfUser(int usrid) {
		Session session = this.sessnFactry.getCurrentSession();
		List<BankAccount> acctList = session.createQuery("from BankAccount where usrid = :usrid")
				.setInteger("usrid", usrid).list();

		return acctList;
	}

	@Override
	@Transactional
	public void deleteacct(BankAccount bankacct) {
		logIt("delete - ", bankacct);
		Query query = sessnFactry.getCurrentSession().createQuery("delete BankAccount where accountnumber = :ID");
		query.setParameter("ID", bankacct.getAccountnumber());
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = true)
	public BankAccount getBankAccountWithAccno(int usrid, String accounttype) {
		Session sessn = this.sessnFactry.getCurrentSession();
		BankAccount bankAcct = (BankAccount) sessn
				.createQuery("from BankAccount where usrid = :usrid and accounttype =:accounttype")
				.setInteger("usrid", usrid).setString("accounttype", accounttype).uniqueResult();
		return bankAcct;
	}
	
	@Override
	@Transactional(readOnly = true)
	public BankAccount getBankAccountWithAccno(String acctnmbr) {
		Session session = this.sessnFactry.getCurrentSession();
		BankAccount bankAcct = (BankAccount) session.get(BankAccount.class, new String(acctnmbr));
		return bankAcct;
	}


	public void logIt(String actn, ILogs ilogs) {
		Logs logss = new Logs();
		Date dateobject = new Date();
		logss.setLogentrydate(dateobject);
		logss.setLoginfo(actn + ilogs.getLogDetail());

		logsDao.add(logss);
	}
}