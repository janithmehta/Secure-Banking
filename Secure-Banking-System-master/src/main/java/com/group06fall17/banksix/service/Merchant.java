/**
* 
*/
package com.group06fall17.banksix.service;

import java.util.List;

import com.group06fall17.banksix.model.BankAccount;
import com.group06fall17.banksix.model.Transaction;

/**
 * @author Saurabh
 *
 */
public class Merchant {
	private String merchantBusinessName;
	
	private List<BankAccount> Accounts; 
	private boolean AccessPrivilege;

	public int getBalanceOfAcct(BankAccount acct) {
		return 0;
	}

	public List<BankAccount> getAccounts() {
		return null;
	}

	public boolean debit(float amount, BankAccount account) {
		return false;
	}

	public boolean credit(float amount, BankAccount aaccount) {
		return false;
	}

	public boolean internalTransfer(float amount, BankAccount senderAccount, BankAccount receiverAccount) {
		return false;
	}

	public boolean externalTransfer(float amount, BankAccount senderAccount, BankAccount receiverAccount) {
		return false;
	}

	public boolean submitTransReview(String message, BankAccount account) {
		return false;
	}

	public boolean submitTransaction(Transaction transaction) {
		return false;
	}

	/**
	 * @return the merchantBusinessName
	 */
	public String getMerchantBusinessName() {
		return merchantBusinessName;
	}

	/**
	 * @param merchantBusinessName the merchantBusinessName to set
	 */
	public void setMerchantBusinessName(String merchantBusinessName) {
		this.merchantBusinessName = merchantBusinessName;
	}
}
