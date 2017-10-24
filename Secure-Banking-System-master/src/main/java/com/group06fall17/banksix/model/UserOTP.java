package com.group06fall17.banksix.model;

/**
 * @author Abhilash
 *
 */

public class UserOTP {
	
	private int code;
	private long validity;
	private String secretKey;
	
	private String email;
	
	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretkey) {
		this.secretKey = secretkey;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public long getValidity() {
		return validity;
	}

	public void setValidity(long validity) {
		this.validity = validity;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
