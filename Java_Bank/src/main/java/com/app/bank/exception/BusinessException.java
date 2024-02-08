package com.app.bank.exception;

@SuppressWarnings("serial")
public class BusinessException extends Exception {

	public BusinessException() {
		super();

	}

	public BusinessException(final String message) {
		super(message);

	}
}
