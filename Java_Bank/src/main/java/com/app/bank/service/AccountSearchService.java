package com.app.bank.service;

import java.util.List;

import com.app.bank.exception.BusinessException;
import com.app.bank.model.Account;
import com.app.bank.model.Transactions;

public interface AccountSearchService {

	public Account EmployeeAccessAccountDetails(int account) throws BusinessException;
	public Account CustomerAccessAccountDetails(int accountnumber, String loginEmail) throws BusinessException;
	public List<Account> getAllAccounts() throws BusinessException;
	public int getAccountByEmail(String string) throws BusinessException;
	public List<Account> getAllPendingAccounts() throws BusinessException;	
	public List<Transactions> getAllTransactions() throws BusinessException;
	public List<Transactions> getTransfersByAccountNumber(int accountnumber) throws BusinessException;	
	public int verifyCredentials(String email, String psswd) throws BusinessException;	
	
}
