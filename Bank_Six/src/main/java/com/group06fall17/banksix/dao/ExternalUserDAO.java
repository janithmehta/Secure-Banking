package com.group06fall17.banksix.dao;
import java.util.List;
import com.group06fall17.banksix.model.ExternalUser;
public interface ExternalUserDAO {
	public void updateextusr(ExternalUser extrnlusr);
	public void persistextusr(ExternalUser extrnlusr);
	public void addextuser(ExternalUser extrnlusr);
	public ExternalUser srchUsrusingEmail(String email);
	public ExternalUser searchUserusingUsrId(int id);
	public void deleteextusr(ExternalUser extrnlusr);
	public List<ExternalUser> searhUserusngUserType(String usrtype);
	public ExternalUser searchUserusngBname(String orgName);
	public ExternalUser srchUserUsngSSN(String ssn);
}
