/**
 * 
 */
package com.group06fall17.banksix.service;

import com.group06fall17.banksix.exception.IllegalTransactionException;
import com.group06fall17.banksix.model.Transaction;

/**
 * @author Abhilash
 *
 */

public interface TransactionManagerService {
	public void scheduleTask();

	public boolean updateEmployeeList();

	public boolean submitTransaction(Transaction transaction) throws IllegalTransactionException;

	public boolean performTransaction(Transaction transaction) throws IllegalTransactionException;

	public boolean updateTransaction(Transaction transaction);

	public boolean cancelTransaction(Transaction transaction);
}
