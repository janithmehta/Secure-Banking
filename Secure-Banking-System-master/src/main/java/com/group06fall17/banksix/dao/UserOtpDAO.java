/**
 * 
 */
package com.group06fall17.banksix.dao;

import com.group06fall17.banksix.model.UserOtp;

/**
 * @author Abhilash
 *
 */
public interface UserOtpDAO {
	public void add(UserOtp userotp); // 

	public UserOtp get(String email);
}
