package com.group06fall17.banksix.dao;
import java.util.List;
import com.group06fall17.banksix.model.BankAccount;
public interface BankAccountDAO {
	public void updateacct(BankAccount bankaccount);
	public void deleteacct(BankAccount bankaccount);
	public void persistacct(BankAccount bankaccount);
	public void addacct(BankAccount bankaccount);
	public List<BankAccount> findAccountsOfUser(int usrid);
	public BankAccount getBankAccountWithAccno(String acctnmbr);
	public BankAccount getBankAccountWithAccno(int usrid, String accttype);
}
