/**
 * 
 */
package com.group06fall17.banksix.dao;

import com.group06fall17.banksix.model.ExternalUser;
import com.group06fall17.banksix.model.PII;

/**
 * @author Abhilash
 *
 */
public interface PIIDAO {
	public void add(PII pii);

	public void update(PII pii);

	public void persist(PII pii);

	public void delete(PII pii);

	public PII findBySSN(String ssn);
	
	public PII findBySSN(ExternalUser externaluser);

}
