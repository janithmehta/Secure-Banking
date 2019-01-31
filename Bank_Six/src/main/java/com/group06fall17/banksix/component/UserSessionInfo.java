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
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	
	public void setUsername(String username) {
		this.username = username;
	}

	private String name;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	
//	public void setFirstname(String name) {
//		this.name = name;
//	}
	
	private String lastname;
	
	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	private int userDown;
	
	/**
	 * @return the userDown
	 */
	
//	public int getUserDown() {
//		return userDown;
//	}
//
//	/**
//	 * @param userDown the userDown to set
//	 */
//	
//	public void setUserDown(int userDown) {
//		this.userDown = userDown;
//	}
	

	public int getUserDownAttempts() {
		return userDown;
	}

	public void setUserDownAttempts(int userDown) {
		this.userDown = userDown;
	}

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

	/**
	 * @return the userActive
	 */
	public int getUserActive() {
		return userActive;
	}

	/**
	 * @param userActive the userActive to set
	 */
	public void setUserActive(int userActive) {
		this.userActive = userActive;
	}
		
}