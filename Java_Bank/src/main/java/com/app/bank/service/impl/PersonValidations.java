package com.app.bank.service.impl;

public class PersonValidations {

	public static boolean isValidPersonName(String name) {
		if (name != null && name.matches("^[a-zA-Z *.*'*\\-*,*]{1,50}$")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidPersonEmail(String email) {
		if (email != null
				&& email.matches("^[[A-Za-z]?[0-9]*[!@#$%^&\\\\*()-]*]{1,30}@[A-Za-z0-9.-]{1,10}.[A-Za-z]{1,10}$")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidEmployeeEmail(String email) {
		if (email != null && email.matches("^[[A-Za-z]?[0-9]*[!@#$%^&\\*()-]*]{1,41}@bank.com$")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidPersonPassword(String password) {
		if (password != null && password.matches("^[[A-Za-z]*[0-9]*[!@#$%^&\\*()-]*]{1,50}$")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidAccountNumber(int accountnumber) {
		if (accountnumber != 0 && String.valueOf(accountnumber).matches("^[0-9]{10}$")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidTransactionAmount(double amount) {
		if (amount >= 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidAccountType(String type) {
		if (type.equals("checking") || type.equals("savings") || type.equals("post") || type.equals("accept")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidAccountStatus(String status) {
		if (status.equals("approved")) {
			return true;
		} else {
			return false;
		}
	}
}