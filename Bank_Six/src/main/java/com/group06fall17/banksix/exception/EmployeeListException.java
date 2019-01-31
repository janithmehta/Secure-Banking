package com.group06fall17.banksix.exception;
public class EmployeeListException extends Exception {
	private static final long serialVersionUID = -6346072519722470992L;
	public EmployeeListException() {
	}
	public EmployeeListException(Throwable causeFactor) {
		super(causeFactor);
	}
	public EmployeeListException(String msg) {
		super(msg);
	}
	public EmployeeListException(String msg, Throwable causeFactor) {
		super(msg, causeFactor);
	}
	public EmployeeListException(String msg, Throwable causeFactor, boolean suppresnenabled,
			boolean stackTracewritable) {
		super(msg, causeFactor, suppresnenabled, stackTracewritable);
	}
}
