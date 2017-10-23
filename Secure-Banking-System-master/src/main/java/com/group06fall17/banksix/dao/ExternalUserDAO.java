/**
 * 
 */
package com.group06fall17.banksix.dao;

import java.util.List;

import com.group06fall17.banksix.model.ExternalUser;

/**
 * @author Abhilash
 *
 */
public interface ExternalUserDAO {
	public void add(ExternalUser externaluser);

	public void update(ExternalUser externaluser);

	public void persist(ExternalUser externaluser);

	public void delete(ExternalUser externaluser);

	public ExternalUser findUserByEmail(String email);

	public ExternalUser findUserById(int id);

	public List<ExternalUser> findUserByUserType(String userType);

	public ExternalUser findUserByBname(String organisationName);
	
	public ExternalUser findUserBySSN(String ssn);
}
