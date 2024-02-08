package com.app.bank.service.impl;

import java.util.List;

import com.app.bank.dao.AccountSearchServiceDAO;
import com.app.bank.dao.impl.AccountSearchServiceDAOImpl;
import com.app.bank.exception.BusinessException;
import com.app.bank.model.Account;
import com.app.bank.model.Transactions;
import com.app.bank.service.LogService;
import com.app.bank.service.AccountSearchService;

public class AccountSearchServiceImpl implements AccountSearchService {

	private AccountSearchServiceDAO accountsearchdao = new AccountSearchServiceDAOImpl();
	LogService logservice = new LogService();

	@Override
	public int verifyCredentials(String email, String psswd) throws BusinessException {

		if (!PersonValidations.isValidPersonEmail(email)) {
			logservice.InfoLogService("Entered email " + email + " is Invalid");
			throw new BusinessException();
		}
		if (!PersonValidations.isValidPersonPassword(psswd)) {
			logservice.InfoLogService("Entered password is Invalid");
			throw new BusinessException();
		}

		return accountsearchdao.verifyCredentials(email, psswd);
	}

	@Override
	public Account EmployeeAccessAccountDetails(int accountnumber) throws BusinessException {
		if (!PersonValidations.isValidAccountNumber(accountnumber)) {
			logservice.InfoLogService("Entered account number " + accountnumber + " is Invalid");
			throw new BusinessException();
		}

		return accountsearchdao.EmployeeAccessAccountDetails(accountnumber);
	}

	@Override
	public Account CustomerAccessAccountDetails(int accountnumber, String email) throws BusinessException {
		if (!PersonValidations.isValidPersonEmail(email)) {
			logservice.InfoLogService("Entered email " + email + " is Invalid");
			throw new BusinessException();
		}
		if (!PersonValidations.isValidAccountNumber(accountnumber)) {
			logservice.InfoLogService("Entered account " + accountnumber + " is Invalid");
			throw new BusinessException();
		}

		return accountsearchdao.CustomerAccessAccountDetails(accountnumber, email);
	}

	@Override
	public List<Account> getAllPendingAccounts() throws BusinessException {

		return accountsearchdao.getAllPendingAccounts();
	}

	@Override
	public List<Account> getAllAccounts() throws BusinessException {

		return accountsearchdao.getAllAccounts();
	}

	@Override
	public List<Transactions> getAllTransactions() throws BusinessException {

		return accountsearchdao.getAllTransactions();
	}

	@Override
	public List<Transactions> getTransfersByAccountNumber(int accountnumber) throws BusinessException {

		if (!PersonValidations.isValidAccountNumber(accountnumber)) {
			logservice.InfoLogService("Entered account " + accountnumber + " is Invalid");
			throw new BusinessException();
		}

		return accountsearchdao.getTransfersByAccountNumber(accountnumber);
	}

	@Override
	public int getAccountByEmail(String email) throws BusinessException {

		if (!PersonValidations.isValidPersonEmail(email)) {
			logservice.InfoLogService("Entered email " + email + " is Invalid");
			throw new BusinessException();
		}

		return accountsearchdao.getAccountByEmail(email);
	}
}