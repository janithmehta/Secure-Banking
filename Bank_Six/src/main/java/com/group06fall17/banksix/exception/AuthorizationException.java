package com.group06fall17.banksix.exception;
public class AuthorizationException extends Exception {
	private static final long serialVersionUID = -921468449528158485L;
	public AuthorizationException() {
	}
	public AuthorizationException(Throwable causeFactor) {
		super(causeFactor);
	}
	public AuthorizationException(String msg) {
		super(msg);
	}
	public AuthorizationException(String msg, Throwable causeFactor) {
		super(msg, causeFactor);
	}
	public AuthorizationException(String msg, Throwable causeFactor, boolean enbleSuppresin,
			boolean stackTracewritable) {
		super(msg, causeFactor, enbleSuppresin, stackTracewritable);
	}
}