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
 * @author Saurabh
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
	
	@Column(name = "transdate", columnDefinition="DATETIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date transdate;
	
	@Column(name = "transtype", nullable = false)
	private String transtype;
	
	@Column(name = "amt", nullable = false)
	private float amt;
	
	@Column(name = "transstatus")
	private String transstatus;
	
	@OneToOne
    @JoinColumn(name = "fromacc")
	private BankAccount fromacc;
	
	@OneToOne
	@JoinColumn(name = "toacc")
	private BankAccount toacc;
	
	@Column(name = "transdesc")
	private String transdesc;
	
	public int getTransid() {
		return transid;
	}

	public void setTransid(int transid) {
		this.transid = transid;
	}

	public Date getTransDate() {
		return transdate;
	}

	public void setTransDate(Date transdate) {
		this.transdate = transdate;
	}

	public String getTransType() {
		return transtype;
	}

	public void setTransType(String transtype) {
		this.transtype = transtype;
	}

	public float getAmt() {
		return amt;
	}

	public void setAmt(float amt) {
		this.amt = amt;
	}

	public String getTransStatus() {
		return transstatus;
	}

	public void setTransStatus(String transstatus) {
		this.transstatus = transstatus;
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
		return transdesc;
	}

	public void setTransDesc(String transdesc) {
		this.transdesc = transdesc;
	}
	
	// Added by Saurabh - Default named Getters & Setters
	// Required by JSP page renderings
		
		public Date getTransdate() {
			return transdate;
		}

		public void setTransdate(Date transdate) {
			this.transdate = transdate;
		}

		public String getTranstype() {
			return transtype;
		}

		public void setTranstype(String transtype) {
			this.transtype = transtype;
		}

		public String getTransstatus() {
			return transstatus;
		}

		public void setTransstatus(String transstatus) {
			this.transstatus = transstatus;
		}

		public String getTransdesc() {
			return transdesc;
		}

		public void setTransdesc(String transdesc) {
			this.transdesc = transdesc;
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
		.append(" transdate : ").append(transdate)
		.append(" transtype : ").append(transtype)
		.append(" amt : ").append(amt)
		.append(" transstatus : ").append(transstatus)
		.append(" fromacc :").append(fromacc.getAccountnumber())
		.append(" toacc : ").append(toacc.getAccountnumber())
		.append(" transdesc : ").append(transdesc);

		return sb.toString();
	}
	
}