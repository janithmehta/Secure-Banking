package com.group06fall17.banksix.dao;
import com.group06fall17.banksix.model.GovAgency;
public interface GovAgencyDAO {
	public void updateGovAgncy(GovAgency fedOffcrs);
	public void persistGovAgncy(GovAgency fedOffcrs);
	public void addGovAgncy(GovAgency fedOffcrs);
	public void deleteGovAgncy(GovAgency fedOffcrs);
	public GovAgency findByUsername(String usrname);
}
