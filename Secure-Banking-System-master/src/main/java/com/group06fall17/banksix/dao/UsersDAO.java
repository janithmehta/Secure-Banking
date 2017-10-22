/**
 * 
 */
package com.group06fall17.banksix.dao;

import com.group06fall17.banksix.model.Users;

/**
 * @author Abhilash
 *
 */

public interface UsersDAO {
	public void add(Users users);

	public void update(Users users);

	public void persist(Users users);

	public void delete(Users users);

	public Users findUsersByEmail(String email);
}
