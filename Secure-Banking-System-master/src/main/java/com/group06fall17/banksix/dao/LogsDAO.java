//@author Abhilash
package com.group06fall17.banksix.dao;

import java.util.Date;
import java.util.List;
import com.group06fall17.banksix.model.Logs;

public interface LogsDAO {
	public void add(Logs logs);

	public List<Logs> findLogs();
}