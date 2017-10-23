package com.group06fall17.banksix.model;

/**
 * @author Abhilash
 *
 */

public class UserOtp {
	
	private int otpcode;
	private long otpvalidity;
	private String secretkey;
	
	private String email;
	
	public String getSecretKey() {
		return secretkey;
	}

	public void setSecretKey(String secretkey) {
		this.secretkey = secretkey;
	}

	public int getOtpcode() {
		return otpcode;
	}

	public void setOtpcode(int otpcode) {
		this.otpcode = otpcode;
	}

	public long getOtpvalidity() {
		return otpvalidity;
	}

	public void setOtpvalidity(long otpvalidity) {
		this.otpvalidity = otpvalidity;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
