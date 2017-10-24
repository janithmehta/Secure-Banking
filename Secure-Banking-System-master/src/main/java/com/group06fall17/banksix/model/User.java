/**
 * @author Abhilash
 *
 */

package com.group06fall17.banksix.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.beans.Transient;
import com.group06fall17.banksix.interceptor.ILogs;

@Entity
@Table(name = "users")
public class User implements ILogs{	
	@Id	
    @Column(name = "username")
	private String username;	
	
	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "userType", nullable = false)
	private String userType;

	public int getUserActive() {
		return userActive;
	}

	public void setUserActive(int userActive) {
		this.userActive = userActive;
	}

	public int getUserDown() {
		return userDown;
	}

	public void setUserDown(int userDown) {
		this.userDown = userDown;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Column(name = "userActive", nullable = false)
	private int userActive;
	
	@Column(name = "userDown")
	private int userDown;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserType() {
		return userType;
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
		
		sb.append(" users " ).append(" username :" ).append(username)
		.append(" password : ").append(password)
		.append(" userType : ").append(userType)
		.append(" userActive : ").append(userActive)
		.append(" userDown :").append(userDown);
		
		return sb.toString();
	}
}