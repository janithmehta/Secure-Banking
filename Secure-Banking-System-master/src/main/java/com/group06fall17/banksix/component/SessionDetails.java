package com.group06fall17.banksix.component;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionDetails {
	private String username;
	private String firstname;
	private String lastname;
	private int userDown;
	private int userActive;
	private int anothersession;
	private String accountSelected;

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

	public String getAccountSelected() {
		return accountSelected;
	}

	public void setAccountSelected(String accountSelected) {
		this.accountSelected = accountSelected;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getAnothersession() {
		return anothersession;
	}

	public void setAnothersession(int anothersession) {
		this.anothersession = anothersession;
	}
}