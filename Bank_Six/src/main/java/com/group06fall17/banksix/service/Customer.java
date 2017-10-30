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

public class Customer {
	private List<BankAccount> accounts; 
	private boolean AccessPrivilege;
	private List<Task> tasks;
	
	public int getBalanceOfAcct(BankAccount acct) {
		return 0;
	}

	public List<BankAccount> getAccounts() {
		return null;
	}

	public boolean debitFromAcct(float amount, BankAccount acct) {
		return false;
	}

	public boolean creditIntoAcct(float amount, BankAccount acct) {
		return false;
	}

	public boolean internalTransfer(float amount, BankAccount fromacc, BankAccount toacc) {
		return false;
	}

	public boolean externalTransfer(float amount, BankAccount fromacc, BankAccount toacc) {
		return false;
	}

	public boolean submitTransReview(String message, BankAccount acct) {
		return false;
	}

	public boolean submitTransac(Transaction transaction) {
		return false;
	}
}
