/**
 * 
 */
package com.group06fall17.banksix.dao;

import com.group06fall17.banksix.model.ExternalUser;
import com.group06fall17.banksix.model.Pii;

/**
 * @author Abhilash
 *
 */
public interface PiiDAO {
	public void add(Pii pii);

	public void update(Pii pii);

	public void persist(Pii pii);

	public void delete(Pii pii);

	public Pii findBySSN(String ssn);
	
	public Pii findBySSN(ExternalUser externaluser);

}
