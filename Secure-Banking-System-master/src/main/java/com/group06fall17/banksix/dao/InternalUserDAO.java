/**
 * 
 */
package com.group06fall17.banksix.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.group06fall17.banksix.model.InternalUser;

/**
 * @author Abhilash
 *
 */

public interface InternalUserDAO {
	public void add(InternalUser externaluser);

	public void update(InternalUser externaluser);

	public void persist(InternalUser externaluser);

	public void delete(InternalUser externaluser);

	public InternalUser searchUsrByEmail(String email);
	
	public InternalUser findUserById(int id);

	public List<InternalUser> findAllRegEmployees();

	public List<InternalUser> findAllSystemManagers();

	public InternalUser findSysAdmin();

}
