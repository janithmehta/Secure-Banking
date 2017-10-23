package com.group06fall17.banksix.model;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.group06fall17.banksix.interceptor.ILogs;

/**
 * @author 
 *
 */

@Entity
@Table(name = "govagency")
@DynamicUpdate
@SelectBeforeUpdate 
public class GovAgency implements ILogs{
	@Id
	@Column(name = "username", nullable = false)	
	private String username;
	
	@Column(name = "password", nullable = false)
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return password;
	}

	public void setPasswd(String password) {
		this.password = password;
	}

	@Transient
	@Override
	public Long getId() {
		return Long.valueOf(this.username);
	}

	@Transient
	@Override
	public String getLogDetail() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" govagency ").
		append(" username :" ).append(username)
		.append(" password :").append(password);

		return sb.toString();
	}
}
