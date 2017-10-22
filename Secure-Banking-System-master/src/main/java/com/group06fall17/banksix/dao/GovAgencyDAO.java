/**
 * 
 */
package com.group06fall17.banksix.dao;

import com.group06fall17.banksix.model.GovAgency;

/**
 * @author Abhilash
 *
 */
public interface GovAgencyDAO {
	public void add(GovAgency govagency);

	public void update(GovAgency govagency);

	public void persist(GovAgency govagency);

	public void delete(GovAgency govagency);

	public GovAgency findByUsername(String username);
}
