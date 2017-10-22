/**
 * 
 */
package com.group06fall17.banksix.dao;

import java.util.List;

import com.group06fall17.banksix.model.BankAccount;
import com.group06fall17.banksix.model.Transaction;

/**
 * @author Abhilash
 *
 */
public interface TransactionDAO {
	public void add(Transaction transaction);

	public void update(Transaction transaction);

	public void persist(Transaction transaction);

	public void delete(Transaction transaction);

	public List<Transaction> findTransactionsOfAccount(String accno);

	public Transaction findTransactionById(int id);

	public List<Transaction> findTransactionsOfAccount(BankAccount bankaccount);
}
