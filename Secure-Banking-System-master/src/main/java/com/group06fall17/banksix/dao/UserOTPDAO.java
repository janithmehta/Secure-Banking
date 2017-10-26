/**
 * @author Abhilash
 */
package com.group06fall17.banksix.dao;

import com.group06fall17.banksix.model.UserOTP;

public interface UserOTPDAO {
	
	public UserOTP get(String email);
	public void add(UserOTP userotp); 
	
}
