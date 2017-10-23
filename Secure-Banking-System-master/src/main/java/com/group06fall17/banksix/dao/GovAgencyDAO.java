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
	public void add(GovAgency fedofficers);

	public void update(GovAgency fedofficers);

	public void persist(GovAgency fedofficers);

	public void delete(GovAgency fedofficers);

	public GovAgency findByUsername(String username);
}
