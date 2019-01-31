package com.group06fall17.banksix.exception;
public class IllegalTransactionException extends Exception {
	private static final long serialVersionUID = -6898566779271468872L;
	public IllegalTransactionException() {
	}
	public IllegalTransactionException(Throwable causeFactor) {
		super(causeFactor);
	}
	public IllegalTransactionException(String msg) {
		super(msg);
	}
	public IllegalTransactionException(String msg, Throwable causeFactor) {
		super(msg, causeFactor);
	}
	public IllegalTransactionException(String msg, Throwable causeFactor, boolean suppresnenabled,
			boolean stackTracewritable) {
		super(msg, causeFactor, suppresnenabled, stackTracewritable);
	}
}