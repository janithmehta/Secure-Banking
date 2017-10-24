/**
 * 
 */
package com.group06fall17.banksix.dao;

import com.group06fall17.banksix.model.UserOTP;

/**
 * @author Abhilash
 *
 */
public interface UserOTPDAO {
	public void add(UserOTP userotp); // 

	public UserOTP get(String email);
}
