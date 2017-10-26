// Author : Shubham

package com.group06fall17.banksix.component;

import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSessionInfo {
	
	private String selectedUsrAccount;
	/**
	 * @return the selectedUsrAccount
	 */
	public String getSelectedUsrAccount() {
		return selectedUsrAccount;
	}

	/**
	 * @param selectedUsrAccount the selectedUsrAccount to set
	 */
	public void setSelectedUsrAccount(String selectedUsrAccount) {
		this.selectedUsrAccount = selectedUsrAccount;
	}

	
	private String username;
	private String name;
	private String lastname;
	private int userDown;
	private int userothersession;
	/**
	 * @return the userothersession
	 */
	public int getUserothersession() {
		return userothersession;
	}

	/**
	 * @param userothersession the userothersession to set
	 */
	public void setUserothersession(int userothersession) {
		this.userothersession = userothersession;
	}

	private int userActive;
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getUserDownAttempts() {
		return userDown;
	}

	public void setUserDownAttempts(int userDown) {
		this.userDown = userDown;
	}

	public int getUserActive() {
		return userActive;
	}

	public void setUserActive(int userActive) {
		this.userActive = userActive;
	}

	

	public String getName() {
		return name;
	}

	public void setFirstname(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	
}