/**
 * 
 */
package com.group06fall17.banksix.dao;

import com.group06fall17.banksix.model.Authorizes;
import com.group06fall17.banksix.model.ExternalUser;
import com.group06fall17.banksix.model.InternalUser;
import com.group06fall17.banksix.model.Transaction;

/**
 * @author Abhilash
 *
 */

public interface AuthorizesDAO {
	public void add(Authorizes authorizes);

	public void update(Authorizes authorizes);

	public void persist(Authorizes authorizes);

	public void delete(Authorizes authorizes);

	public Authorizes findByIds(InternalUser empid, ExternalUser usrid, Transaction transid);
}
