package com.group06fall17.banksix.dao;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.group06fall17.banksix.model.InternalUser;
public interface InternalUserDAO {
	public void update(InternalUser intusr);
	public void delete(InternalUser intusr);
	public void add(InternalUser intusr);
	public void persist(InternalUser intusr);
	public InternalUser searchUsrByEmail(String email);
	public InternalUser findUserById(int id);
	public List<InternalUser> findAllRegEmployees();
	public List<InternalUser> findAllSystemManagers();
	public InternalUser findSysAdmin();
}
