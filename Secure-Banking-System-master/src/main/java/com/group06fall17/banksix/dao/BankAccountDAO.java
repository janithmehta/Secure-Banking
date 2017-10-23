/**
 * 
 */
package com.group06fall17.banksix.dao;

import java.util.List;

import com.group06fall17.banksix.model.BankAccount;

/**
 * @author Abhilash
 *
 */
public interface BankAccountDAO {
	public void add(BankAccount bankaccount);

	public void update(BankAccount bankaccount);

	public void persist(BankAccount bankaccount);

	public void delete(BankAccount bankaccount);

	public List<BankAccount> findAccountsOfUser(int userid);

	public BankAccount getBankAccountWithAccno(String accountnumber);

	public BankAccount getBankAccountWithAccno(int userid, String accounttype);

}
