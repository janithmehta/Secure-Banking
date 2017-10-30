package com.group06fall17.banksix.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.group06fall17.banksix.interceptor.ILogs;

/**
 * @author Shubham
 *
 */

@Entity
@Table(name = "pii")
@DynamicUpdate
@SelectBeforeUpdate 
public class PII implements Serializable, ILogs{
	
	private static final long serialVersionUID = 310779046388655840L;

	@Id
    @Column(name = "ssn", nullable = false)
	private String ssn;
	
	@Column(name = "stateID", nullable = false)
	private String stateID;

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getStateID() {
		return stateID;
	}

	public void setStateID(String stateID) {
		this.stateID = stateID;
	}

	
	@Override
	public Long getId() {
		return Long.valueOf(ssn);
	}

	
	@Override
	public String getLogDetail() {
	StringBuilder getLog = new StringBuilder();
		
		getLog.append(" pii ").append(" ssn :" ).append(ssn)
		.append(" stateID :").append(stateID);

		return getLog.toString();
	}
}
