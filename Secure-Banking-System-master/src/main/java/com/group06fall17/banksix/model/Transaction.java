package com.group06fall17.banksix.model;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "transaction")
@DynamicUpdate
@SelectBeforeUpdate 
public class Transaction implements ILogs{	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "transid", nullable = false)
	private int transid;
	
	@Column(name = "tdate", columnDefinition="DATETIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tdate;
	
	@Column(name = "ttype", nullable = false)
	private String ttype;
	
	@Column(name = "amount", nullable = false)
	private float amount;
	
	@Column(name = "tstatus")
	private String tstatus;
	
	@OneToOne
    @JoinColumn(name = "fromacc")
	private BankAccount fromacc;
	
	@OneToOne
	@JoinColumn(name = "toacc")
	private BankAccount toacc;
	
	@Column(name = "tdesc")
	private String tdesc;
	
	public int getTransid() {
		return transid;
	}

	public void setTransid(int transid) {
		this.transid = transid;
	}

	public Date getTransDate() {
		return tdate;
	}

	public void setTransDate(Date tdate) {
		this.tdate = tdate;
	}

	public String getTransType() {
		return ttype;
	}

	public void setTransType(String transtype) {
		this.ttype = transtype;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getTransStatus() {
		return tstatus;
	}

	public void setTransStatus(String tstatus) {
		this.tstatus = tstatus;
	}
	
	public BankAccount getFromacc() {
		return fromacc;
	}

	public void setFromacc(BankAccount fromacc) {
		this.fromacc = fromacc;
	}

	public BankAccount getToacc() {
		return toacc;
	}

	public void setToacc(BankAccount toacc) {
		this.toacc = toacc;
	}

	public String getTransDesc() {
		return tdesc;
	}

	public void setTransDesc(String tdesc) {
		this.tdesc = tdesc;
	}
	
	// Added by Saurabh - Default named Getters & Setters
	// Required by JSP page renderings
		
		public Date getTdate() {
			return tdate;
		}

		public void setTdate(Date tdate) {
			this.tdate = tdate;
		}

		public String getTtype() {
			return ttype;
		}

		public void setTtype(String transtype) {
			this.ttype = transtype;
		}

		public String getTstatus() {
			return tstatus;
		}

		public void setTstatus(String tstatus) {
			this.tstatus = tstatus;
		}

		public String getTdesc() {
			return tdesc;
		}

		public void setTdesc(String tdesc) {
			this.tdesc = tdesc;
		}
		
	@Transient
	@Override
	public Long getId() {
		return Long.valueOf(this.transid);
	}

	@Transient
	@Override
	public String getLogDetail() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" transaction " ).append(" transid :" ).append(transid)
		.append(" tdate : ").append(tdate)
		.append(" ttype : ").append(ttype)
		.append(" amount : ").append(amount)
		.append(" tstatus : ").append(tstatus)
		.append(" fromacc :").append(fromacc.getAccountnumber())
		.append(" toacc : ").append(toacc.getAccountnumber())
		.append(" tdesc : ").append(tdesc);

		return sb.toString();
	}
	
}
