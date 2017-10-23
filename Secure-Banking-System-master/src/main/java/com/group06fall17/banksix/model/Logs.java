package com.group06fall17.banksix.model;

import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TemporalType;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Temporal;

//@author Abhilash

@Entity
@Table(name = "logs")
public class Logs {
	
	public Date getLogentrydate() {
		return logentrydate;
	}

	public void setLogentrydate(Date logentrydate) {
		this.logentrydate = logentrydate;
	}

	public Long getSyslogid() {
		return syslogid;
	}

	public void setSyslogid(Long syslogid) {
		this.syslogid = syslogid;
	}
	
	public String getLoginfo() {
		return loginfo;
	}

	public void setLoginfo(String loginfo) {
		this.loginfo = loginfo;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "syslogid", nullable = false)
	private Long syslogid;
	
	@Column(name = "logentrydate", columnDefinition="DATETIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date logentrydate;
	
	@Column(name ="loginfo")
	private String loginfo;
}