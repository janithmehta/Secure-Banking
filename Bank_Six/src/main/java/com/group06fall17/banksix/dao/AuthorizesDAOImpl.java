	package com.group06fall17.banksix.dao;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import com.group06fall17.banksix.model.Authorizes;
import com.group06fall17.banksix.model.ExternalUser;
import org.springframework.transaction.annotation.Transactional;
import com.group06fall17.banksix.model.InternalUser;
import com.group06fall17.banksix.model.Transaction;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class AuthorizesDAOImpl implements AuthorizesDAO {
	private SessionFactory sessnFactry;
	@Autowired
	public void setSessnFactry(SessionFactory sf) {
		this.sessnFactry = sf;
	}
	@Override
	@Transactional
	public void deleteAuth(Authorizes authorize) {
		Session sessn = this.sessnFactry.getCurrentSession();
		sessn.delete(authorize);
	}
	@Override
	@Transactional
	public void updateAuth(Authorizes authorize) {
		Session sessn = this.sessnFactry.getCurrentSession();
		sessn.update(authorize);
	}
	@Override
	@Transactional
	public void persistAuth(Authorizes authorize) {
		Session sessn = this.sessnFactry.getCurrentSession();
		sessn.persist(authorize);
	}
	@Override
	@Transactional
	public void addAuth(Authorizes authorize) {
		Session sessn = this.sessnFactry.getCurrentSession();
        sessn.save(authorize);
	}
	@Override
	@Transactional(readOnly = true)
	public Authorizes findByIds(InternalUser intUser, ExternalUser extUser, Transaction tran) {
		Session sessn = this.sessnFactry.getCurrentSession();
		String hql = "from Authorizes A where A.getEmpid() = :empid and A.getUsrid() = :usrid, and A.getTransid() = :transid";		
		Authorizes query = (Authorizes) sessn.createQuery(hql)
				.setInteger("empid", intUser.getUsrid())
				.setInteger("usrid"	, extUser.getUsrid())
				.setInteger("transid", tran.getTransid())
				.uniqueResult();
		return query;
	}
}