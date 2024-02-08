package com.app.bank.service;

import org.apache.log4j.Logger;

import com.app.bank.model.Account;
import com.app.bank.model.Transactions;

public class LogService {

	private static Logger Log = Logger.getLogger(LogService.class);

	public void InfoLogService(Account x) {
		Log.info(x);
	}

	public void InfoLogService(Transactions x) {
		Log.info(x);
	}

	public void InfoLogService(String message) {
		Log.info(message);
	}

	public void WarnLogService(String message) {
		Log.warn(message);
	}

	public void InfoLogService(String message, String format) {
		Log.info(message);
	}
}
