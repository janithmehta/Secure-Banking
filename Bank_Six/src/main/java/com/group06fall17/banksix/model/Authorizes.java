/**
 * @author Abhilash
 *
 */
package com.group06fall17.banksix.model;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.CascadeType;
import javax.persistence.TemporalType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

@Entity
@Table(name = "authorizes")
public class Authorizes implements Serializable {

	private static final long serialVersionUID = -2075078276930609695L;

	@Id
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "usrid", nullable = false)
	private InternalUser empid;
	
	/**
	 * @return the empid
	 */
	public InternalUser getEmpid() {
		return empid;
	}

	/**
	 * @param empid the empid to set
	 */
	public void setEmpid(InternalUser empid) {
		this.empid = empid;
	}

	@Id
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "usrid", nullable = false)
	private ExternalUser usrid;
	
	/**
	 * @return the usrid
	 */
	public ExternalUser getUsrid() {
		return usrid;
	}

	/**
	 * @param usrid the usrid to set
	 */
	public void setUsrid(ExternalUser usrid) {
		this.usrid = usrid;
	}

	@Id
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "transid", nullable = false)
	private Transaction transid;

	/**
	 * @return the transid
	 */
	public Transaction getTransid() {
		return transid;
	}

	/**
	 * @param transid the transid to set
	 */
	public void setTransid(Transaction transid) {
		this.transid = transid;
	}
	
	@Column(name = "start_timestmp", columnDefinition="DATETIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date start_timestmp;

	/**
	 * @return the start_timestmp
	 */
	public Date getStart_timestmp() {
		return start_timestmp;
	}

	/**
	 * @param start_timestmp the start_timestmp to set
	 */
	public void setStart_timestmp(Date start_timestmp) {
		this.start_timestmp = start_timestmp;
	}
	
	@Column(name = "end_timestmp", columnDefinition="DATETIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date end_timestmp;

	/**
	 * @return the end_timestmp
	 */
	public Date getEnd_timestmp() {
		return end_timestmp;
	}

	/**
	 * @param end_timestmp the end_timestmp to set
	 */
	public void setEnd_timestmp(Date end_timestmp) {
		this.end_timestmp = end_timestmp;
	}
}
