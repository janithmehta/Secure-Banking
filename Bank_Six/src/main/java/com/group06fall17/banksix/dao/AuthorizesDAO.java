package com.group06fall17.banksix.dao;
import com.group06fall17.banksix.model.Authorizes;
import com.group06fall17.banksix.model.Transaction;
import com.group06fall17.banksix.model.ExternalUser;
import com.group06fall17.banksix.model.InternalUser;

public interface AuthorizesDAO {
	public void updateAuth(Authorizes authorize);
	public void deleteAuth(Authorizes authorize);
	public void addAuth(Authorizes authorize);
	public void persistAuth(Authorizes authorize);
	public Authorizes findByIds(InternalUser empid, ExternalUser usrid, Transaction transid);
}
