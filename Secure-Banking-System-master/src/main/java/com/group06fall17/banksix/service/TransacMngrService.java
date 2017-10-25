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

public interface TransacMngrService {
	public void planTask();

	public boolean upgradeEmpList();

	public boolean submitTransac(Transaction transaction) throws IllegalTransactionException;

	public boolean executeTransac(Transaction transaction) throws IllegalTransactionException;

	public boolean upgradeTransac(Transaction transaction);

	public boolean dropTransac(Transaction transaction);
}
