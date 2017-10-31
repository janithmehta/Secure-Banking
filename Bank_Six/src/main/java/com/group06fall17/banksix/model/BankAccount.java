package com.group06fall17.banksix.model;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.group06fall17.banksix.interceptor.ILogs;

/**
 * @author Shubham
 *
 */

@Entity
@Table(name = "bankaccount")
@DynamicUpdate
@SelectBeforeUpdate 
public class BankAccount implements  ILogs{
	@Id
	@Column(name = "accountnumber", nullable = false)	
	private String accountnumber;
	
	@Column(name = "balance", nullable = false)
	private float balance;
	
	@Column(name = "accounttype", nullable = false)
	private String accounttype;
	
	@Column(name = "acctcreatedate", columnDefinition="DATETIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date acctcreatedate;
	
	@Column(name = "Card")
	String cardNo; 
	
	@Column(name = "CVV")
	String cvv;
	
	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Date getAcctcreatedate() {
		return acctcreatedate;
	}

	public void setAcctcreatedate(Date acctcreatedate) {
		this.acctcreatedate = acctcreatedate;
	}

	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "usrid")
	private ExternalUser usrid;
	
	@Column(name ="accountstatus", nullable = false)
	private String accountstatus;

	public float getBalance() {
		return balance;
	}


	public ExternalUser getUsrid() {
		return usrid;
	}
	
	public void setBalance(float balance) {
		this.balance = balance;
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}


	public String getAccountstatus() {
		return accountstatus;
	}


	public void setAccountstatus(String accountstatus) {
		this.accountstatus = accountstatus;
	}

	
	public void setUsrid(ExternalUser usrid) {
		this.usrid = usrid;
	}		

	@Transient
	@Override
	public Long getId() {
		return Long.valueOf(this.accountnumber);
	}

	@Transient
	@Override
	public String getLogDetail() {
		StringBuilder logDetails = new StringBuilder();
		
		logDetails.append(" bankaccount ")
		.append(" accountnumber : ").append(accountnumber)
		.append(" balance : ").append(balance)
		.append(" accounttype : ").append(accounttype)
		.append(" acctcreatedate : ").append(acctcreatedate)
		.append(" usrid : ").append(usrid.getUsrid())
		.append(" accountstatus :").append(accountstatus)
		.append(" CVV").append(cvv);

		return logDetails.toString();
	}
	
}
