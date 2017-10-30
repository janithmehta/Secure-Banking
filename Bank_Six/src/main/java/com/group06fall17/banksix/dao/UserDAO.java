/**
 * 
 */
package com.group06fall17.banksix.dao;

import com.group06fall17.banksix.model.User;

/**
 * @author Abhilash
 *
 */

public interface UserDAO {
	public void add(User users);

	public void update(User users);

	public void persist(User users);

	public void delete(User users);

	public User findUsersByEmail(String email);
}
