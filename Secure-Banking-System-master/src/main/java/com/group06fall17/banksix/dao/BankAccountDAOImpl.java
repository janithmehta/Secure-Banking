package com.group06fall17.banksix.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.group06fall17.banksix.interceptor.ILogs;
import com.group06fall17.banksix.model.BankAccount;
import com.group06fall17.banksix.model.Logs;;

@Repository
public class BankAccountDAOImpl implements BankAccountDAO {

	private SessionFactory sessionFactory;

	@Autowired
	private LogsDAO logsDao;

	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	@Override
	@Transactional
	public void add(BankAccount bankaccount) {
		sessionFactory.getCurrentSession().save(bankaccount);
		logIt("add - ", bankaccount);
	}

	@Override
	@Transactional
	public void update(BankAccount bankaccount) {
		sessionFactory.getCurrentSession().merge(bankaccount);
		logIt("update - ", bankaccount);
	}

	@Override
	@Transactional
	public void persist(BankAccount bankaccount) {
		sessionFactory.getCurrentSession().persist(bankaccount);
		logIt("persist - ", bankaccount);
	}

	@Override
	@Transactional
	public void delete(BankAccount bankaccount) {
		logIt("delete - ", bankaccount);
		Query query = sessionFactory.getCurrentSession().createQuery("delete BankAccount where accountnumber = :ID");
		query.setParameter("ID", bankaccount.getAccountnumber());
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<BankAccount> findAccountsOfUser(int userid) {
		Session session = this.sessionFactory.getCurrentSession();
		List<BankAccount> accountList = session.createQuery("from BankAccount where userid = :userid")
				.setInteger("userid", userid).list();

		return accountList;
	}

	@Override
	@Transactional(readOnly = true)
	public BankAccount getBankAccountWithAccno(String accountnumber) {
		Session session = this.sessionFactory.getCurrentSession();
		BankAccount bankAccount = (BankAccount) session.get(BankAccount.class, new String(accountnumber));
		return bankAccount;
	}

	@Override
	@Transactional(readOnly = true)
	public BankAccount getBankAccountWithAccno(int userid, String accounttype) {
		Session session = this.sessionFactory.getCurrentSession();
		BankAccount bankAccount = (BankAccount) session
				.createQuery("from BankAccount where userid = :userid and accounttype =:accounttype")
				.setInteger("userid", userid).setString("accounttype", accounttype).uniqueResult();
		return bankAccount;
	}

	public void logIt(String action, ILogs ilogs) {
		Logs logs = new Logs();
		Date dateobj = new Date();
		logs.setLogentrydate(dateobj);
		logs.setLoginfo(action + ilogs.getLogDetail());

		logsDao.add(logs);
	}
}
