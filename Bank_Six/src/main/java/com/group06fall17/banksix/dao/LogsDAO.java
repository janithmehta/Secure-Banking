//@author Abhilash
package com.group06fall17.banksix.dao;

import com.group06fall17.banksix.model.Logs;
import java.util.List;
import java.util.Date;

public interface LogsDAO {
	public void add(Logs logs);

	public List<Logs> findLogs();
}
