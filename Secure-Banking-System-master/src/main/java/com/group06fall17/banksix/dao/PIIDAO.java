/**
 * @author Abhilash
 */
package com.group06fall17.banksix.dao;

import com.group06fall17.banksix.model.PII;
import com.group06fall17.banksix.model.ExternalUser;

public interface PIIDAO {
	public void add(PII pii);

	public void update(PII pii);

	public void persist(PII pii);

	public void delete(PII pii);

	public PII findBySSN(String ssn);
	
	public PII findBySSN(ExternalUser externaluser);

}
