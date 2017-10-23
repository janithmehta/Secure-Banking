package com.group06fall17.banksix.model;

import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.group06fall17.banksix.interceptor.ILogs;
import org.hibernate.annotations.SelectBeforeUpdate;
import java.beans.Transient;
import javax.persistence.Column;

//@author Abhilash

@Entity
@Table(name = "fedofficers")
@DynamicUpdate
@SelectBeforeUpdate 
public class GovAgency implements ILogs{
	@Id
	@Column(name = "username", nullable = false)	
	private String username;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", nullable = false)
	private String password;
	
	public String getPasswd() {
		return password;
	}

	public void setPasswd(String password) {
		this.password = password;
	}

	@Transient
	@Override
	public String getLogDetail() {
		StringBuilder strb = new StringBuilder();
		strb.append(" fedofficers ").append(" username :" ).append(username).append(" password :").append(password);
		return strb.toString();
	}
	
	@Transient
	@Override
	public Long getId() {
		return Long.valueOf(this.username);
	}
}