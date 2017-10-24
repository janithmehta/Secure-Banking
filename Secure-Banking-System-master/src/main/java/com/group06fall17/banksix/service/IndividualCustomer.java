/**
 * 
 */
package com.group06fall17.banksix.service;

import java.util.List;

import com.group06fall17.banksix.model.BankAccount;
import com.group06fall17.banksix.model.Task;
import com.group06fall17.banksix.model.Transaction;

/**
 * @author Saurabh
 *
 */

public class IndividualCustomer {
	private List<BankAccount> accounts; 
	private boolean AccessPrivilege;
	private List<Task> tasks;
	
	public int getBalanceOfAcct(BankAccount acct) {
		return 0;
	}

	public List<BankAccount> getAccounts() {
		return null;
	}

	public boolean debitFromAcct(float amt, BankAccount acct) {
		return false;
	}

	public boolean creditIntoAcct(float amt, BankAccount acct) {
		return false;
	}

	public boolean internalTransfer(float amt, BankAccount fromacc, BankAccount toacc) {
		return false;
	}

	public boolean externalTransfer(float amt, BankAccount fromacc, BankAccount toacc) {
		return false;
	}

	public boolean submitTransReview(String message, BankAccount acct) {
		return false;
	}

	public boolean submitTransaction(Transaction transaction) {
		return false;
	}
}
